package com.hakan.core.configuration;

import com.hakan.core.configuration.annotations.ConfigFile;
import com.hakan.core.configuration.containers.ConfigContainer;
import com.hakan.core.configuration.utils.ConfigurationUtils;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Configuration handler class to load
 * and update configuration files.
 */
public final class ConfigHandler {

    private static final Map<String, ConfigContainer> configurations = new HashMap<>();

    /**
     * Loads configuration container.
     *
     * @param configClass Configuration class.
     * @return Configuration class.
     */
    @Nonnull
    public static ConfigContainer load(@Nonnull Object configClass) {
        Validate.notNull(configClass, "config class cannot be null!");
        ConfigFile configFile = configClass.getClass().getAnnotation(ConfigFile.class);

        if (configFile == null)
            throw new IllegalArgumentException("config class must be annotated with @ConfigFile!");
        else if (configFile.path().isEmpty())
            throw new IllegalArgumentException("config file must have a path!");

        ConfigContainer configContainer = ConfigHandler.load(ConfigContainer.of(configFile));
        configContainer.loadData(configClass);
        return configContainer;
    }

    /**
     * Loads configuration container.
     *
     * @param file Configuration container.
     * @return Configuration container.
     */
    @Nonnull
    public static ConfigContainer load(@Nonnull ConfigContainer file) {
        Validate.notNull(file, "config file cannot be null!");
        ConfigurationUtils.createFile(file);

        configurations.put(file.getPath(), file);
        return file;
    }

    /**
     * Updates configuration container.
     *
     * @param configClass Configuration class.
     */
    public static void update(@Nonnull Object configClass) {
        Validate.notNull(configClass, "config class cannot be null!");
    }

    /**
     * Finds configuration container.
     *
     * @param path Configuration container path.
     * @return Configuration container.
     */
    @Nonnull
    public static Optional<ConfigContainer> findByPath(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return Optional.ofNullable(configurations.get(path));
    }

    /**
     * Gets configuration file.
     *
     * @param path Configuration file container.
     * @return Configuration container.
     */
    @Nonnull
    public static ConfigContainer getByPath(@Nonnull String path) {
        return findByPath(path).orElseThrow((() -> new IllegalArgumentException("config file not found with path(" + path + ")!")));
    }
}