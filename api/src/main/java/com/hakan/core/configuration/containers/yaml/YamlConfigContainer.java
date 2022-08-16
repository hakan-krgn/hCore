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
import java.io.File;
import java.lang.reflect.Field;

/**
 * {@inheritDoc}
 */
public class YamlConfigContainer extends ConfigContainer {

    private final File file;

    /**
     * {@inheritDoc}
     */
    public YamlConfigContainer(@Nonnull ConfigFile configFile) {
        super(configFile);
        this.file = new File(configFile.path());
    }

    /**
     * {@inheritDoc}
     */
    public YamlConfigContainer(@Nonnull String path,
                               @Nonnull Class<? extends JavaPlugin> plugin) {
        super(path, plugin);
        this.file = new File(path);
    }

    /**
     * {@inheritDoc}
     */
    public YamlConfigContainer(@Nonnull String path,
                               @Nonnull ConfigType type,
                               @Nonnull Class<? extends JavaPlugin> plugin) {
        super(path, type, plugin);
        this.file = new File(path);
    }

    /**
     * {@inheritDoc}
     */
    public YamlConfigContainer(@Nonnull String path,
                               @Nonnull String resource,
                               @Nonnull ConfigType type,
                               @Nonnull Class<? extends JavaPlugin> plugin) {
        super(path, resource, type, plugin);
        this.file = new File(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadData(@Nonnull Object configClass) {
        try {
            Validate.notNull(configClass, "config class cannot be null!");
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(this.file);


            boolean save = false;
            for (Field field : configClass.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(ConfigValue.class))
                    continue;

                ConfigValue configValue = field.getAnnotation(ConfigValue.class);

                Object value = configuration.get(configValue.path());
                Object defaultValue = ReflectionUtils.getField(configClass, field.getName());

                if (defaultValue != null && value != null) {
                    ReflectionUtils.setField(configClass, field.getName(), value);
                } else if (defaultValue == null && value != null) {
                    ReflectionUtils.setField(configClass, field.getName(), value);
                } else if (defaultValue != null) {
                    configuration.set(configValue.path(), defaultValue);
                    save = true;
                } else {
                    throw new IllegalArgumentException("config value cannot be null!");
                }
            }

            if (save) configuration.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}