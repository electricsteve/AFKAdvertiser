package electricsteve.afkadvertiser;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import electricsteve.afkadvertiser.client.AFKAdvertiserClient;

@SuppressWarnings("FieldMayBeFinal")
public class Config {
    private String[] texts;
    private int interval;
    private int random_offset;

    private Config(String[] texts, int interval, int random_offset) {
        this.texts = texts;
        this.interval = interval;
        this.random_offset = random_offset;
    }

    public static Config fromFile(File file) {
        String[] texts;
        int interval;
        int random_offset;
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
                return null;
            }
            try {
                if (array.length < 2) throw new NumberFormatException();
                random_offset = Integer.parseInt(array[1]);
            }  catch (NumberFormatException e) {
                AFKAdvertiserClient.LOGGER.error("Invalid random offset in txt file. This may be because you still have an old config from when random offset wasn't implemented, if so, please add a random offset on the second line. Error: {}", String.valueOf(e));
                random_offset = 0;
            }
            if (array.length < 3) {
                AFKAdvertiserClient.LOGGER.warn("Txt file doesn't contain any messages. Mod will be disabled.");
                return null;
            }
            texts = ArrayUtils.subarray(array, 2, array.length);
            stream.close();
        } catch (IOException e) {
            AFKAdvertiserClient.LOGGER.error("Texts File could not be read. Error: {}", String.valueOf(e));
            return null;
        }
        AFKAdvertiserClient.LOGGER.debug("Loaded config with these values: interval: {}, random_offset: {}, texts: {}", interval, random_offset, texts);
        return new Config(texts, interval, random_offset);
    }

    private static String[] addDefaultsToTextsFile(File file) {
        try {
            FileWriter writer = new FileWriter(file);
            String[] lines = new String[]{"60", "10", "First Line", "Second Line"};
            String format = String.join("\n", lines);
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

    public int getRandomOffset() {
        return random_offset;
    }
}
