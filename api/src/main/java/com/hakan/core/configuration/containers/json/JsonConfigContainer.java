package com.hakan.core.configuration.containers.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hakan.core.configuration.ConfigType;
import com.hakan.core.configuration.annotations.ConfigFile;
import com.hakan.core.configuration.annotations.ConfigValue;
import com.hakan.core.configuration.containers.ConfigContainer;
import com.hakan.core.configuration.utils.ConfigurationUtils;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

/**
 * {@inheritDoc}
 */
public class JsonConfigContainer extends ConfigContainer {

    private final Gson gson;

    /**
     * {@inheritDoc}
     */
    public JsonConfigContainer(@Nonnull ConfigFile configFile) {
        super(configFile);
        this.gson = new Gson();
    }

    /**
     * {@inheritDoc}
     */
    public JsonConfigContainer(@Nonnull String path,
                               @Nonnull Class<? extends JavaPlugin> plugin) {
        super(path, plugin);
        this.gson = new Gson();
    }

    /**
     * {@inheritDoc}
     */
    public JsonConfigContainer(@Nonnull String path,
                               @Nonnull ConfigType type,
                               @Nonnull Class<? extends JavaPlugin> plugin) {
        super(path, type, plugin);
        this.gson = new Gson();
    }

    /**
     * {@inheritDoc}
     */
    public JsonConfigContainer(@Nonnull String path,
                               @Nonnull String resource,
                               @Nonnull ConfigType type,
                               @Nonnull Class<? extends JavaPlugin> plugin) {
        super(path, resource, type, plugin);
        this.gson = new Gson();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadData(@Nonnull Object configClass) {
        try {
            Validate.notNull(configClass, "config class cannot be null!");
            JsonObject jsonObject = ConfigurationUtils.getJsonObject(super.path);

            boolean save = false;
            for (Field field : configClass.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(ConfigValue.class))
                    continue;

                ConfigValue configValue = field.getAnnotation(ConfigValue.class);

                Object value = ConfigurationUtils.getElement(jsonObject, configValue.path());
                Object defaultValue = ReflectionUtils.getField(configClass, field.getName());

                if (defaultValue != null && value != null) {
                    ReflectionUtils.setField(configClass, field.getName(), value);
                } else if (defaultValue == null && value != null) {
                    ReflectionUtils.setField(configClass, field.getName(), value);
                } else if (defaultValue != null) {
                    ConfigurationUtils.setElement(jsonObject, configValue.path(), this.gson.toJsonTree(defaultValue));
                    save = true;
                } else {
                    throw new IllegalArgumentException("config value cannot be null!");
                }
            }

            if (save) ConfigurationUtils.saveJsonObject(jsonObject, super.path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}