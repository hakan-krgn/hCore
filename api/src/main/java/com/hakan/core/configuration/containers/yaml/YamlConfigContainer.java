package com.hakan.core.configuration.containers.yaml;

import com.hakan.core.configuration.ConfigType;
import com.hakan.core.configuration.annotations.ConfigFile;
import com.hakan.core.configuration.annotations.ConfigValue;
import com.hakan.core.configuration.containers.ConfigContainer;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * {@inheritDoc}
 */
@SuppressWarnings({"unchecked"})
public class YamlConfigContainer extends ConfigContainer {

    private final File file;
    private FileConfiguration configuration;

    /**
     * {@inheritDoc}
     */
    public YamlConfigContainer() {
        this.file = new File(super.path);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * {@inheritDoc}
     */
    public YamlConfigContainer(@Nonnull ConfigFile configFile) {
        super(configFile);
        this.file = new File(super.path);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * {@inheritDoc}
     */
    public YamlConfigContainer(@Nonnull String path,
                               @Nonnull Class<? extends JavaPlugin> plugin) {
        super(path, plugin);
        this.file = new File(super.path);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * {@inheritDoc}
     */
    public YamlConfigContainer(@Nonnull String path,
                               @Nonnull ConfigType type,
                               @Nonnull Class<? extends JavaPlugin> plugin) {
        super(path, type, plugin);
        this.file = new File(super.path);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * {@inheritDoc}
     */
    public YamlConfigContainer(@Nonnull String path,
                               @Nonnull String resource,
                               @Nonnull ConfigType type,
                               @Nonnull Class<? extends JavaPlugin> plugin) {
        super(path, resource, type, plugin);
        this.file = new File(super.path);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final ConfigContainer save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final <T> T getValue(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return (T) this.configuration.get(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final <T> T getValue(@Nonnull String path,
                                @Nonnull Class<T> clazz) {
        Validate.notNull(clazz, "clazz cannot be null!");
        Validate.notNull(path, "path cannot be null!");
        return clazz.cast(this.configuration.get(path));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final ConfigContainer setValue(@Nonnull String path,
                                          @Nonnull Object value) {
        return this.setValue(path, value, true);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final ConfigContainer setValue(@Nonnull String path,
                                          @Nonnull Object value,
                                          boolean save) {
        Validate.notNull(path, "path cannot be null!");
        Validate.notNull(value, "value cannot be null!");
        this.configuration.set(path, value);
        if (save) this.save();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final ConfigContainer loadData() {
        return this.loadData(this);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final ConfigContainer loadData(@Nonnull Object configClass) {
        try {
            Validate.notNull(configClass, "config class cannot be null!");
            this.configuration = YamlConfiguration.loadConfiguration(this.file);


            boolean save = false;
            for (Field field : configClass.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(ConfigValue.class))
                    continue;

                ConfigValue configValue = field.getAnnotation(ConfigValue.class);

                Object value = this.getValue(configValue.path());
                Object defaultValue = ReflectionUtils.getField(configClass, field.getName());

                if (defaultValue != null && value != null) {
                    ReflectionUtils.setField(configClass, field.getName(), value);
                } else if (defaultValue == null && value != null) {
                    ReflectionUtils.setField(configClass, field.getName(), value);
                } else if (defaultValue != null) {
                    this.setValue(configValue.path(), defaultValue, false);
                    save = true;
                } else {
                    throw new IllegalArgumentException("config value cannot be null!");
                }
            }

            if (save) this.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }
}