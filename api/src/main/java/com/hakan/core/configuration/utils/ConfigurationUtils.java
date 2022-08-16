package com.hakan.core.configuration.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.hakan.core.HCore;
import com.hakan.core.configuration.containers.ConfigContainer;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

/**
 * Configuration utilities for
 * creating files and including resources.
 */
public final class ConfigurationUtils {

    private static final Gson gson = new Gson();
    private static final JsonParser parser = new JsonParser();

    /**
     * Creates a file.
     *
     * @param file File to create.
     * @return Created file.
     */
    @Nonnull
    public static File createFile(@Nonnull ConfigContainer file) {
        Validate.notNull(file, "config file cannot be null!");
        return ConfigurationUtils.createFile(file.getPath(), file.getResource(), file.getPlugin());
    }

    /**
     * Creates a file.
     *
     * @param path     Path to create.
     * @param resource Resource to include.
     * @return Created file.
     */
    @Nonnull
    public static File createFile(@Nonnull String path,
                                  @Nullable String resource) {
        Validate.notNull(path, "path cannot be null!");
        return ConfigurationUtils.createFile(path, resource, HCore.class);
    }

    /**
     * Creates a file.
     *
     * @param path     Path to create.
     * @param resource Resource to include.
     * @param loader   Class loader.
     * @return Created file.
     */
    @Nonnull
    public static File createFile(@Nonnull String path,
                                  @Nullable String resource,
                                  @Nonnull Class<?> loader) {
        Validate.notNull(path, "path cannot be null!");
        Validate.notNull(loader, "loader cannot be null!");

        File file = new File(path);
        if (file.exists()) return file;

        File created = ConfigurationUtils.createFile(path);
        if (resource == null || resource.isEmpty()) return file;

        try (InputStream inputStream = loader.getResourceAsStream("/" + resource);
             OutputStream outputStream = Files.newOutputStream(created.toPath())) {
            Validate.notNull(inputStream, "resource couldn't find(" + resource + ")!");
            Validate.notNull(outputStream, "output stream cannot be null!");

            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = inputStream.read(buffer)) > 0)
                outputStream.write(buffer, 0, readBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * Creates a file.
     *
     * @param path Path to create.
     * @return Created file.
     */
    @Nonnull
    public static File createFile(@Nonnull String path) {
        try {
            Validate.notNull(path, "path cannot be null!");

            File file = new File(path.replace("/", File.separator));
            file.getParentFile().mkdirs();
            file.createNewFile();
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets JsonObject from file.
     *
     * @param filePath Path to file.
     * @return JsonObject from file.
     */
    @Nonnull
    public static JsonObject getJsonObject(@Nonnull String filePath) {
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
    public static void saveJsonObject(@Nonnull JsonObject object,
                                      @Nonnull String filePath,
                                      boolean beautified) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            Validate.notNull(object, "object cannot be null!");
            Validate.notNull(filePath, "file path cannot be null!");
            if (!beautified) fileWriter.write(object.toString());
            else fileWriter.write(JsonBeautifier.of(object.toString()));
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
    public static void saveJsonObject(@Nonnull JsonObject object,
                                      @Nonnull String filePath) {
        ConfigurationUtils.saveJsonObject(object, filePath, true);
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
    public static Object getElement(@Nonnull JsonObject parent,
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
    public static void setElement(@Nonnull JsonObject parent,
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
}