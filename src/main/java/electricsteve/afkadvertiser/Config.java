package electricsteve.afkadvertiser;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import electricsteve.afkadvertiser.client.AFKAdvertiserClient;

public class Config {
    private String[] texts;
    private int interval;

    private Config(String[] texts, int interval) {
        this.texts = texts;
        this.interval = interval;
    }

    public static Config fromFile(File file) {
        String[] texts;
        int interval = 0;
        try {
            if (file.createNewFile()) {
                addDefaultsToTextsFile(file);
            }
        } catch (IOException e) {
            AFKAdvertiserClient.LOGGER.error("Could not create config text file, this mod will not work!", e);
            return null;
        }
        try {
            Stream<String> stream = Files.lines(file.toPath());
            String[] array = stream.toArray(String[]::new);
            if (array.length == 0) {
                array = addDefaultsToTextsFile(file);
                if (array == null) return null;
            }
            try {
                interval = Integer.parseInt(array[0]);
            } catch (NumberFormatException e) {
                AFKAdvertiserClient.LOGGER.error("Invalid delay in txt file. Error: {}", String.valueOf(e));
            }
            texts = ArrayUtils.remove(array, 0);
            stream.close();
        } catch (IOException e) {
            AFKAdvertiserClient.LOGGER.error("Texts File could not be read. Error: {}", String.valueOf(e));
            return null;
        }
        return new Config(texts, interval);
    }

    private static String[] addDefaultsToTextsFile(File file) {
        try {
            FileWriter writer = new FileWriter(file);
            String[] lines = new String[]{"60", "First Line", "Second Line"};
            String format = String.join("%n", lines);
            writer.write(format);
            writer.close();
            AFKAdvertiserClient.LOGGER.warn("No config file found, created new one.");
            return lines;
        } catch (IOException e) {
            AFKAdvertiserClient.LOGGER.error("Couldn't create config file. Error: {}", String.valueOf(e));
            return null;
        }
    }

    public int getInterval() {
        return interval;
    }

    public String[] getTexts() {
        return texts;
    }
}
