package com.hakan.core.configuration.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Json utilities for handling json objects
 * and saving them to files.
 */
public final class JsonUtils {

    private static final Gson gson = new Gson();
    private static final JsonParser parser = new JsonParser();

    /**
     * Gets JsonObject from file.
     *
     * @param filePath Path to file.
     * @return JsonObject from file.
     */
    @Nonnull
    public static JsonObject loadFromFile(@Nonnull String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Validate.notNull(filePath, "file path cannot be null!");

            JsonReader jsonReader = new JsonReader(reader);
            Map<?, ?> objectMap = gson.fromJson(jsonReader, Map.class);
            return parser.parse(gson.toJson(objectMap)).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets JsonObject from file.
     *
     * @param object     JsonObject to get.
     * @param filePath   Path to file.
     * @param beautified Beautified json.
     */
    public static void saveToFile(@Nonnull JsonObject object,
                                  @Nonnull String filePath,
                                  boolean beautified) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            Validate.notNull(object, "object cannot be null!");
            Validate.notNull(filePath, "file path cannot be null!");
            if (!beautified) fileWriter.write(object.toString());
            else fileWriter.write(JsonUtils.beautify(object.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves JsonObject to file.
     *
     * @param object   JsonObject to save.
     * @param filePath Path to file.
     */
    public static void saveToFile(@Nonnull JsonObject object,
                                  @Nonnull String filePath) {
        JsonUtils.saveToFile(object, filePath, true);
    }

    /**
     * Gets element from parent json
     * object by given path.
     *
     * @param parent Parent json object.
     * @param key    Key to get.
     * @return Element.
     */
    @Nullable
    public static Object getValue(@Nonnull JsonObject parent,
                                  @Nonnull String key) {
        Validate.notNull(parent, "parent cannot be null!");
        Validate.notNull(key, "key cannot be null!");

        String[] keys = key.split("\\.");
        JsonObject jsonObject = parent;
        for (int i = 0; i < keys.length - 1; i++)
            jsonObject = jsonObject.getAsJsonObject(keys[i]);

        JsonElement element = jsonObject.get(keys[keys.length - 1]);
        if (element instanceof JsonObject || element instanceof JsonArray)
            return element;
        else if (element instanceof JsonNull || element == null)
            return null;
        return ReflectionUtils.getField(element, "value");
    }

    /**
     * Sets element to parent json.
     *
     * @param parent  Parent json object.
     * @param key     Key to set.
     * @param element Element to set.
     */
    public static void setValue(@Nonnull JsonObject parent,
                                @Nonnull String key,
                                @Nonnull JsonElement element) {
        Validate.notNull(parent, "parent cannot be null!");
        Validate.notNull(key, "key cannot be null!");
        Validate.notNull(element, "element cannot be null!");

        String[] keys = key.split("\\.");
        JsonObject jsonObject = parent;
        for (int i = 0; i < keys.length - 1; i++)
            jsonObject = jsonObject.getAsJsonObject(keys[i]);
        jsonObject.add(keys[keys.length - 1], element);
    }

    /**
     * Beautifies the given json string.
     *
     * @param input Input json string.
     * @return Beautified json string.
     * @see <a href="https://stackoverflow.com/questions/5457524/json-beautifier-library-for-java">Json beautifier method</a>
     */
    @Nonnull
    public static String beautify(@Nonnull String input) {
        Validate.notNull(input, "input cannot be null!");

        StringBuilder inputBuilder = new StringBuilder();
        char[] inputChar = input.toCharArray();

        int tabCount = 0;
        for (int i = 0; i < inputChar.length; i++) {
            String charI = String.valueOf(inputChar[i]);
            if (charI.equals("}") || charI.equals("]")) {
                tabCount--;
                if (!String.valueOf(inputChar[i - 1]).equals("[") && !String.valueOf(inputChar[i - 1]).equals("{"))
                    inputBuilder.append(newLine(tabCount));
            }
            inputBuilder.append(charI);

            if (charI.equals("{") || charI.equals("[")) {
                tabCount++;
                if (String.valueOf(inputChar[i + 1]).equals("]") || String.valueOf(inputChar[i + 1]).equals("}"))
                    continue;

                inputBuilder.append(newLine(tabCount));
            }

            if (charI.equals(","))
                inputBuilder.append(newLine(tabCount));
        }

        return inputBuilder.toString();
    }

    /**
     * New line.
     *
     * @param tabCount Tab count.
     * @return New line.
     */
    @Nonnull
    private static String newLine(int tabCount) {
        StringBuilder builder = new StringBuilder();

        builder.append("\n");
        for (int j = 0; j < tabCount; j++)
            builder.append("  ");
        return builder.toString();
    }
}
