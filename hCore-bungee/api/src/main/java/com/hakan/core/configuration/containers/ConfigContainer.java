package com.hakan.core.configuration.containers;

import com.hakan.core.configuration.ConfigType;
import com.hakan.core.configuration.annotations.ConfigFile;
import com.hakan.core.configuration.containers.json.JsonConfigContainer;
import com.hakan.core.configuration.containers.yaml.YamlConfigContainer;
import com.hakan.core.utils.Validate;
import net.md_5.bungee.api.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * ConfigContainer is an abstract class
 * that is used to load and save config files.
 */
public abstract class ConfigContainer {

    /**
     * Create a new ConfigContainer.
     *
     * @param configFile ConfigFile annotation.
     * @return ConfigContainer.
     */
    @Nonnull
    public static ConfigContainer of(@Nonnull ConfigFile configFile) {
        Validate.notNull(configFile, "config file cannot be null!");
        return of(configFile.path(), configFile.resource(), configFile.type(), configFile.plugin());
    }

    /**
     * Create a new ConfigContainer.
     *
     * @param path   Config file path.
     * @param plugin Plugin.
     * @return ConfigContainer.
     */
    @Nonnull
    public static ConfigContainer of(@Nonnull String path,
                                     @Nonnull Class<? extends Plugin> plugin) {
        return of(path, ConfigType.YAML, plugin);
    }

    /**
     * Create a new ConfigContainer.
     *
     * @param path   Config file path.
     * @param type   Config file type.
     * @param plugin Plugin.
     * @return ConfigContainer.
     */
    @Nonnull
    public static ConfigContainer of(@Nonnull String path,
                                     @Nonnull ConfigType type,
                                     @Nonnull Class<? extends Plugin> plugin) {
        return of(path, null, type, plugin);
    }

    /**
     * Create a new ConfigContainer.
     *
     * @param path     Config file path.
     * @param resource Config file resource.
     * @param type     Config file type.
     * @param plugin   Plugin.
     * @return ConfigContainer.
     */
    @Nonnull
    public static ConfigContainer of(@Nonnull String path,
                                     @Nullable String resource,
                                     @Nonnull ConfigType type,
                                     @Nonnull Class<? extends Plugin> plugin) {
        Validate.notNull(path, "path cannot be null!");
        Validate.notNull(type, "type cannot be null!");
        Validate.notNull(plugin, "plugin cannot be null!");

        switch (type) {
            case YAML:
                return new YamlConfigContainer(path, resource, plugin);
            case JSON:
                return new JsonConfigContainer(path, resource, plugin);
            default:
                throw new IllegalArgumentException("unsupported config file type: " + type);
        }
    }


    protected final String path;
    protected final String resource;
    protected final ConfigType type;
    protected final Class<? extends Plugin> plugin;

    /**
     * Creates a new ConfigContainer.
     */
    public ConfigContainer() {
        ConfigFile configFile = this.getClass().getAnnotation(ConfigFile.class);
        this.path = configFile.path();
        this.resource = configFile.resource();
        this.type = configFile.type();
        this.plugin = configFile.plugin();
    }

    /**
     * Creates a new ConfigContainer.
     *
     * @param configFile ConfigFile annotation.
     */
    public ConfigContainer(@Nonnull ConfigFile configFile) {
        this(configFile.path(), configFile.resource(), configFile.type(), configFile.plugin());
    }

    /**
     * Creates a new ConfigContainer.
     *
     * @param path   Config file path.
     * @param plugin Plugin.
     */
    public ConfigContainer(@Nonnull String path,
                           @Nonnull Class<? extends Plugin> plugin) {
        this(path, null, ConfigType.YAML, plugin);
    }

    /**
     * Creates a new ConfigContainer.
     *
     * @param path   Config file path.
     * @param type   Config file type.
     * @param plugin Plugin.
     */
    public ConfigContainer(@Nonnull String path,
                           @Nonnull ConfigType type,
                           @Nonnull Class<? extends Plugin> plugin) {
        this(path, null, type, plugin);
    }

    /**
     * Creates a new ConfigContainer.
     *
     * @param path     Config file path.
     * @param resource Config file resource.
     * @param type     Config file type.
     * @param plugin   Plugin.
     */
    public ConfigContainer(@Nonnull String path,
                           @Nullable String resource,
                           @Nonnull ConfigType type,
                           @Nonnull Class<? extends Plugin> plugin) {
        this.resource = resource;
        this.path = Validate.notNull(path, "path cannot be null!");
        this.type = Validate.notNull(type, "type cannot be null!");
        this.plugin = Validate.notNull(plugin, "plugin cannot be null!");
    }

    /**
     * Gets the config file path.
     *
     * @return Config file path.
     */
    @Nonnull
    public final String getPath() {
        return this.path;
    }

    /**
     * Gets the config file resource.
     *
     * @return Config file resource.
     */
    @Nullable
    public final String getResource() {
        return this.resource;
    }

    /**
     * Gets the config file type.
     *
     * @return Config file type.
     */
    @Nonnull
    public final ConfigType getType() {
        return this.type;
    }

    /**
     * Gets the Plugin.
     *
     * @return Plugin.
     */
    @Nonnull
    public final Class<? extends Plugin> getPlugin() {
        return this.plugin;
    }


    /**
     * Saves last data to
     * config file as persist.
     *
     * @return ConfigContainer.
     */
    @Nonnull
    public abstract ConfigContainer save();

    /**
     * Gets value from config file
     * with the given path.
     *
     * @param path Value path.
     * @param <T>  Value type.
     * @return Value.
     */
    @Nullable
    public abstract <T> T getValue(@Nonnull String path);

    /**
     * Gets value from config file
     * with the given path.
     *
     * @param path  Value path.
     * @param clazz Value class.
     * @param <T>   Value type.
     * @return Value.
     */
    @Nullable
    public abstract <T> T getValue(@Nonnull String path,
                                   @Nonnull Class<T> clazz);

    /**
     * Sets value to config file
     * with the given path and save
     * it to file.
     *
     * @param path  Value path.
     * @param value Value.
     * @return ConfigContainer.
     */
    @Nonnull
    public abstract ConfigContainer setValue(@Nonnull String path,
                                             @Nonnull Object value);

    /**
     * Sets value to config file
     * with the given path.
     *
     * @param path  Value path.
     * @param value Value.
     * @param save  Save config file after setting value.
     * @return ConfigContainer.
     */
    @Nonnull
    public abstract ConfigContainer setValue(@Nonnull String path,
                                             @Nonnull Object value,
                                             boolean save);

    /**
     * Gets all nodes from config
     * and sets it to the all fields
     * of this class.
     *
     * @return ConfigContainer.
     */
    @Nonnull
    public abstract ConfigContainer loadData();

    /**
     * Gets all nodes from config
     * and sets it to the all fields
     * of given object.
     *
     * @param configClass Object to set fields.
     * @return ConfigContainer.
     */
    @Nonnull
    public abstract ConfigContainer loadData(@Nonnull Object configClass);
}
