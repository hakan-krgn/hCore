package com.hakan.core.configuration.containers.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hakan.core.configuration.ConfigType;
import com.hakan.core.configuration.annotations.ConfigFile;
import com.hakan.core.configuration.annotations.ConfigValue;
import com.hakan.core.configuration.containers.ConfigContainer;
import com.hakan.core.configuration.utils.JsonUtils;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import net.md_5.bungee.api.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * {@inheritDoc}
 */
@SuppressWarnings({"unchecked"})
public class JsonConfigContainer extends ConfigContainer {

    private final Gson gson;
    private JsonObject jsonObject;

    /**
     * {@inheritDoc}
     */
    public JsonConfigContainer() {
        this.gson = new Gson();
        this.jsonObject = JsonUtils.loadFromFile(super.path);
    }

    /**
     * {@inheritDoc}
     */
    public JsonConfigContainer(@Nonnull ConfigFile configFile) {
        super(configFile);
        this.gson = new Gson();
        this.jsonObject = JsonUtils.loadFromFile(super.path);
    }

    /**
     * {@inheritDoc}
     */
    public JsonConfigContainer(@Nonnull String path,
                               @Nonnull Class<? extends Plugin> plugin) {
        super(path, ConfigType.JSON, plugin);
        this.gson = new Gson();
        this.jsonObject = JsonUtils.loadFromFile(super.path);
    }

    /**
     * {@inheritDoc}
     */
    public JsonConfigContainer(@Nonnull String path,
                               @Nullable String resource,
                               @Nonnull Class<? extends Plugin> plugin) {
        super(path, resource, ConfigType.JSON, plugin);
        this.gson = new Gson();
        this.jsonObject = JsonUtils.loadFromFile(super.path);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final ConfigContainer save() {
        JsonUtils.saveToFile(this.jsonObject, super.path);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final <T> T getValue(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return (T) JsonUtils.getValue(this.jsonObject, path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final <T> T getValue(@Nonnull String path,
                                @Nonnull Class<T> clazz) {
        Validate.notNull(path, "path cannot be null!");
        Validate.notNull(clazz, "clazz cannot be null!");
        return clazz.cast(JsonUtils.getValue(this.jsonObject, path));
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

        JsonUtils.setValue(this.jsonObject, path, this.gson.toJsonTree(value));
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
            this.jsonObject = JsonUtils.loadFromFile(super.path);

            boolean save = false;
            for (Field field : configClass.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(ConfigValue.class))
                    continue;

                ConfigValue configValue = field.getAnnotation(ConfigValue.class);

                Object value = this.getValue(configValue.value());
                Object defaultValue = ReflectionUtils.getField(configClass, field.getName());

                if (defaultValue != null && value != null) {
                    ReflectionUtils.setField(configClass, field.getName(), value);
                } else if (defaultValue == null && value != null) {
                    ReflectionUtils.setField(configClass, field.getName(), value);
                } else if (defaultValue != null) {
                    this.setValue(configValue.value(), defaultValue, false);
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
