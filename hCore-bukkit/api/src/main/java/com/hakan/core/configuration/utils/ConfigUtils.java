package com.hakan.core.configuration.utils;

import com.hakan.core.HCore;
import com.hakan.core.configuration.annotations.ConfigFile;
import com.hakan.core.configuration.containers.ConfigContainer;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Config utilities for creating files
 * and including resources.
 */
public final class ConfigUtils {

    /**
     * Creates a file.
     *
     * @param container Container to create.
     * @return Created file.
     */
    @Nonnull
    public static File createFile(@Nonnull ConfigContainer container) {
        Validate.notNull(container, "config container cannot be null!");
        return ConfigUtils.createFile(container.getPath(), container.getResource(), container.getPlugin());
    }

    /**
     * Creates a file.
     *
     * @param file File to create.
     * @return Created file.
     */
    @Nonnull
    public static File createFile(@Nonnull ConfigFile file) {
        Validate.notNull(file, "config file cannot be null!");
        return ConfigUtils.createFile(file.path(), file.resource(), file.plugin());
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
        return ConfigUtils.createFile(path, resource, HCore.class);
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

        File created = ConfigUtils.createFile(path);
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
}
