package electricsteve.afkadvertiser;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import electricsteve.afkadvertiser.client.AFKAdvertiserClient;

public class AdvertiserManager {
    private File textsFile = FabricLoader.getInstance().getConfigDir().resolve("afkadvertiser.txt").toFile();
    private String[] texts;
    public final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private boolean Enabled = false;

    public AdvertiserManager() {
        int delay = 60;
        try {
            if (textsFile.createNewFile()) {
                addDefaultsToTextsFile();
            }
        } catch (IOException e) {
            AFKAdvertiserClient.LOGGER.error("Could not create config text file, this mod will not work!", e);
            return;
        }
        try {
            Stream<String> stream = Files.lines(textsFile.toPath());
            String[] array = stream.toArray(String[]::new);
            if (array.length == 0) {
                addDefaultsToTextsFile();
            }
            try {
                delay = Integer.parseInt(array[0]);
            } catch (NumberFormatException e) {
                AFKAdvertiserClient.LOGGER.error("Invalid delay in txt file. Error: {}", String.valueOf(e));
            }
            this.texts = ArrayUtils.remove(array, 0);
            stream.close();
        } catch (IOException e) {
            AFKAdvertiserClient.LOGGER.error("Texts File could not be read. Error: {}", String.valueOf(e));
        }
        Runnable task = () -> {
            if (!Enabled) return;
            sendRandomMessage();
        };
        executor.scheduleAtFixedRate(task, 0, delay, TimeUnit.SECONDS);
    }

    public void addDefaultsToTextsFile() {
        try {
            FileWriter writer = new FileWriter(this.textsFile);
            String format = String.format("60%n" +
                    "First Line%n" +
                    "Second Line");
            writer.write(format);
            writer.close();
            AFKAdvertiserClient.LOGGER.warn("No config file found, created new one.");
        } catch (IOException e) {
            AFKAdvertiserClient.LOGGER.error("Couldn't create config file. Error: {}", String.valueOf(e));
        }
    }

    public void sendRandomMessage() {
        try {
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
