package com.hakan.core.configuration.containers;

import com.hakan.core.configuration.ConfigType;
import com.hakan.core.configuration.annotations.ConfigFile;
import com.hakan.core.configuration.containers.json.JsonConfigContainer;
import com.hakan.core.configuration.containers.yaml.YamlConfigContainer;
import com.hakan.core.utils.Validate;
import org.bukkit.plugin.java.JavaPlugin;

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
     * @param plugin JavaPlugin.
     * @return ConfigContainer.
     */
    @Nonnull
    public static ConfigContainer of(@Nonnull String path,
                                     @Nonnull Class<? extends JavaPlugin> plugin) {
        return of(path, ConfigType.YAML, plugin);
    }

    /**
     * Create a new ConfigContainer.
     *
     * @param path   Config file path.
     * @param type   Config file type.
     * @param plugin JavaPlugin.
     * @return ConfigContainer.
     */
    @Nonnull
    public static ConfigContainer of(@Nonnull String path,
                                     @Nonnull ConfigType type,
                                     @Nonnull Class<? extends JavaPlugin> plugin) {
        return of(path, null, type, plugin);
    }

    /**
     * Create a new ConfigContainer.
     *
     * @param path     Config file path.
     * @param resource Config file resource.
     * @param type     Config file type.
     * @param plugin   JavaPlugin.
     * @return ConfigContainer.
     */
    @Nonnull
    public static ConfigContainer of(@Nonnull String path,
                                     @Nullable String resource,
                                     @Nonnull ConfigType type,
                                     @Nonnull Class<? extends JavaPlugin> plugin) {
        Validate.notNull(path, "path cannot be null!");
        Validate.notNull(type, "type cannot be null!");
        Validate.notNull(plugin, "plugin cannot be null!");
        Validate.notNull(resource, "resource cannot be null!");

        switch (type) {
            case YAML:
                return new YamlConfigContainer(path, resource, type, plugin);
            case JSON:
                return new JsonConfigContainer(path, resource, type, plugin);
            default:
                throw new IllegalArgumentException("unsupported configuration file type: " + type);
        }
    }


    protected final String path;
    protected final String resource;
    protected final ConfigType type;
    protected final Class<? extends JavaPlugin> plugin;

    /**
     * Create a new ConfigContainer.
     *
     * @param configFile ConfigFile annotation.
     */
    public ConfigContainer(@Nonnull ConfigFile configFile) {
        this(configFile.path(), configFile.resource(), configFile.type(), configFile.plugin());
    }

    /**
     * Create a new ConfigContainer.
     *
     * @param path   Config file path.
     * @param plugin JavaPlugin.
     */
    public ConfigContainer(@Nonnull String path,
                           @Nonnull Class<? extends JavaPlugin> plugin) {
        this(path, null, ConfigType.YAML, plugin);
    }

    /**
     * Create a new ConfigContainer.
     *
     * @param path   Config file path.
     * @param type   Config file type.
     * @param plugin JavaPlugin.
     */
    public ConfigContainer(@Nonnull String path,
                           @Nonnull ConfigType type,
                           @Nonnull Class<? extends JavaPlugin> plugin) {
        this(path, null, type, plugin);
    }

    /**
     * Create a new ConfigContainer.
     *
     * @param path     Config file path.
     * @param resource Config file resource.
     * @param type     Config file type.
     * @param plugin   JavaPlugin.
     */
    public ConfigContainer(@Nonnull String path,
                           @Nullable String resource,
                           @Nonnull ConfigType type,
                           @Nonnull Class<? extends JavaPlugin> plugin) {
        this.resource = resource;
        this.path = Validate.notNull(path, "path cannot be null!");
        this.type = Validate.notNull(type, "type cannot be null!");
        this.plugin = Validate.notNull(plugin, "plugin cannot be null!");
    }

    /**
     * Get the config file path.
     *
     * @return Config file path.
     */
    @Nonnull
    public final String getPath() {
        return this.path;
    }

    /**
     * Get the config file resource.
     *
     * @return Config file resource.
     */
    @Nonnull
    public final String getResource() {
        return this.resource;
    }

    /**
     * Get the config file type.
     *
     * @return Config file type.
     */
    @Nonnull
    public final ConfigType getType() {
        return this.type;
    }

    /**
     * Get the JavaPlugin.
     *
     * @return JavaPlugin.
     */
    @Nonnull
    public final Class<? extends JavaPlugin> getPlugin() {
        return this.plugin;
    }


    /**
     * Gets all nodes from config
     * and sets it to the all fields
     * of given object.
     *
     * @param configClass Object to set fields.
     */
    public abstract void loadData(@Nonnull Object configClass);
}