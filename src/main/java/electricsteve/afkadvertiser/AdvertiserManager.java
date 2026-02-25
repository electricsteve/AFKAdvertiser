package electricsteve.afkadvertiser;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import electricsteve.afkadvertiser.client.AFKAdvertiserClient;

public class AdvertiserManager {
    public final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private boolean Enabled = false;
    private final Config config;

    public AdvertiserManager() {
        File textsFile = FabricLoader.getInstance().getConfigDir().resolve("afkadvertiser.txt").toFile();
        this.config = Config.fromFile(textsFile);
        if (this.config == null) {
            AFKAdvertiserClient.LOGGER.warn("Failed to load config, mod will be disabled.");
            return;
        }
        Runnable task = () -> {
            AFKAdvertiserClient.LOGGER.debug("Task called");
            if (!Enabled) return;
            int configRandomOffset = this.config.getRandomOffset();
            if (configRandomOffset != 0) {
                AFKAdvertiserClient.LOGGER.debug("Random offset enabled");
                float wait_time = new Random().nextFloat(configRandomOffset);
                AFKAdvertiserClient.LOGGER.debug("Wait time: {}", wait_time);
                try {
                    Thread.sleep((long) (wait_time * 1000));
                } catch (InterruptedException e) {
                    AFKAdvertiserClient.LOGGER.error("Sleep interrupted while waiting for config to be loaded");
                    throw new RuntimeException(e);
                }
            }
            AFKAdvertiserClient.LOGGER.debug("Actually sending message");
            sendRandomMessage();
        };
        executor.scheduleWithFixedDelay(task, 0, this.config.getInterval(), TimeUnit.SECONDS);
    }

    public void sendRandomMessage() {
        try {
            String[] texts = this.config.getTexts();
            int index = new Random().nextInt(texts.length);
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatMessage(texts[index]);
        } catch (Exception e) {
            AFKAdvertiserClient.LOGGER.error("Error sending message. Error: {}", String.valueOf(e));
        }
    }

    public void setEnabled(boolean enabled) {
        this.Enabled = enabled;
        String message = enabled ? "Enabled" : "Disabled";
        AFKAdvertiserClient.LOGGER.info("{} AFK Advertiser", message);
    }

    public boolean toggleEnabled() {
        setEnabled(!Enabled);
        return this.Enabled;
    }
}
