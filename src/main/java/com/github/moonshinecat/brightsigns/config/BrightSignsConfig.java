package com.github.moonshinecat.brightsigns.config;

import com.google.gson.*;

import java.io.*;

import static com.github.moonshinecat.brightsigns.BrightSigns.MODID;
import static com.github.moonshinecat.brightsigns.BrightSigns.LOGGER;

//Very large config for one value.
//I'd link the original source of this base config, but I can't find it.
public class BrightSignsConfig {
    public static final String configPath = "config/" + MODID + ".json";
    private final Gson gson;
    private JsonObject configuration;
    public boolean state;

    public BrightSignsConfig(){
        gson = new GsonBuilder().setPrettyPrinting().create();

        try{
            configuration = new JsonParser().parse(new FileReader(configPath)).getAsJsonObject();
        } catch (FileNotFoundException e){
            LOGGER.warn("Couldn't load configuration. Creating a new one.");

            File file = new File(configPath);
            try {
                file.createNewFile();
                try(FileWriter writer = new FileWriter(file)){
                    writer.write("{}");
                    writer.flush();
                }

                configuration = new JsonParser().parse(new FileReader(configPath)).getAsJsonObject();

            } catch (IOException ex) {
                LOGGER.error("Error while creating configuration.", ex);
            }
        } catch (IllegalStateException e) {
            LOGGER.error("Existing configuration is not formatted properly. Remaking configuration.", e);

            try {
                File file = new File(configPath);
                file.delete();
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("{}");
                    writer.flush();
                }

                configuration = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
            } catch (IOException ex) {
                LOGGER.error("Unable to reset configuration.", ex);
            }
        }

        state = initialize("general", "state", true);
    }

    public boolean initialize(String category, String key, boolean defaultValue) {
        if (hasKey(category, key)) {
            return get(category, key, defaultValue);
        } else {
            write(category, key, defaultValue);
            return defaultValue;
        }
    }

    public boolean get(String category, String key, boolean defaultValue) {
        try {
            if (higherDepth(configuration, category + "." + key) != null) {
                return higherDepth(configuration, category + "." + key).getAsBoolean();
            }
        } catch (Exception e) {
            LOGGER.error("Exception when reading a boolean.", e);
        }

        return defaultValue;
    }

    public void write(String category, String key, boolean value) {
        try {
            JsonObject categoryJson = higherDepth(configuration, category, new JsonObject());
            if (categoryJson.has(key) && categoryJson.get(key).getAsBoolean() == value) {
                return;
            }
            categoryJson.addProperty(key, value);
            configuration.add(category, categoryJson);
            save();
        } catch (Exception e) {
            LOGGER.error("Exception when writing a boolean", e);
        }
    }

    private JsonObject higherDepth(JsonElement element, String path, JsonObject defaultValue) {
        try {
            return higherDepth(element, path).getAsJsonObject();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private JsonElement higherDepth(JsonElement element, String path) {
        String[] paths = path.split("\\.");

        try {
            for (String key : paths) {
                if (key.length() >= 3 && key.startsWith("[") && key.endsWith("]")) {
                    element = element.getAsJsonArray().get(Integer.parseInt(key.substring(1, key.length() - 1)));
                } else {
                    element = element.getAsJsonObject().get(key);
                }
            }
            return element;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean hasKey(String category, String key) {
        try {
            return configuration.has(category) && higherDepth(configuration, category + "." + key) != null;
        } catch (Exception e) {
            LOGGER.error("Exception when reading configuration.", e);
        }
        return false;
    }

    private synchronized void save() throws FileNotFoundException {
        try (FileWriter writer = new FileWriter(configPath)) {
            gson.toJson(configuration, writer);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("Error saving configuration", e);
        }
        configuration = new JsonParser().parse(new FileReader(configPath)).getAsJsonObject();
        state = get("general", "state", true);
    }
}
