package com.hakan.core.configuration;

import com.hakan.core.configuration.annotations.ConfigFile;
import com.hakan.core.configuration.containers.ConfigContainer;
import com.hakan.core.configuration.utils.ConfigUtils;
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

        Validate.notNull(configFile, "config class must have ConfigFile annotation!");
        Validate.isTrue(configFile.path().isEmpty(), "config file must have a path!");
        Validate.isTrue(configurations.containsKey(configFile.path()), "config file already exists!");

        ConfigUtils.createFile(configFile);

        ConfigContainer container = ConfigContainer.of(configFile);
        configurations.put(container.getPath(), container);

        return container.loadData(configClass);
    }

    /**
     * Loads configuration container.
     *
     * @param container Configuration container.
     * @return Configuration container.
     */
    @Nonnull
    public static ConfigContainer load(@Nonnull ConfigContainer container) {
        Validate.notNull(container, "config container cannot be null!");
        Validate.isTrue(container.getPath().isEmpty(), "config file must have a path!");
        Validate.isTrue(configurations.containsKey(container.getPath()), "config file already exists!");

        ConfigUtils.createFile(container);

        configurations.put(container.getPath(), container);
        return container.loadData(container);
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