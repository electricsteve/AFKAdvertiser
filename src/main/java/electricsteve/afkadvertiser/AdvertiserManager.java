package electricsteve.afkadvertiser;

import electricsteve.afkadvertiser.client.AFKAdvertiserClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class AdvertiserManager {
    private final Path textsPath = FabricLoader.getInstance().getConfigDir().resolve("afkadvertiser.txt");
    private String[] texts;
    public ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private boolean Enabled = false;
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (!Enabled) return;
            sendRandomMessage();
        }
    };

    public AdvertiserManager() {
        int delay = 60;
        File textsFile = textsPath.toFile();
        try {
            if (textsFile.createNewFile()) {
                FileWriter writer = new FileWriter(textsFile);
                writer.write(String.format("60%n" +
                        "First Line%n" +
                        "Second Line"));
                writer.close();
                AFKAdvertiserClient.LOGGER.warn("No config file found, created new one.");
            }
        } catch (IOException e) {
            AFKAdvertiserClient.LOGGER.error("Couldn't create config file. Error: " + e);
        }
        try {
            Stream<String> stream = Files.lines(textsPath);
            String[] array = stream.toArray(String[]::new);
            try {
                delay = Integer.parseInt(array[0]);
            } catch (NumberFormatException e) {
                AFKAdvertiserClient.LOGGER.error("Invalid delay in txt file. Error: " + e);
            }
            this.texts = ArrayUtils.remove(array, 0);
            stream.close();
        } catch (IOException e) {
            AFKAdvertiserClient.LOGGER.error("Texts File could not be read. Error: " + e);
        }
        executor.scheduleAtFixedRate(task, 0, delay, TimeUnit.SECONDS);
    }

    public void sendRandomMessage() {
        try {
            int index = new Random().nextInt(texts.length);
            MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(texts[index]);
        } catch (Exception e) {
            AFKAdvertiserClient.LOGGER.error("Error sending message. Error: " + e);
        }
    }

    public void setEnabled(boolean enabled) {
        this.Enabled = enabled;
        String message = enabled ? "Enabled" : "Disabled";
        AFKAdvertiserClient.LOGGER.info(message + " AFK Advertiser");
    }

    public void toggleEnabled() {
        setEnabled(!Enabled);
    }
}
