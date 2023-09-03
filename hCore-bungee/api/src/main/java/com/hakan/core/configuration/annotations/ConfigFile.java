package com.hakan.core.configuration.annotations;

import com.hakan.core.configuration.ConfigType;
import net.md_5.bungee.api.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ConfigFile annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigFile {

    /**
     * Path of the config file.
     *
     * @return Path of the config file.
     */
    @Nonnull
    String path();

    /**
     * Resource to include
     * the config file.
     *
     * @return Resource to include
     * the config file.
     */
    @Nullable
    String resource() default "";

    /**
     * Main class of plugin.
     *
     * @return Main class of plugin.
     */
    @Nonnull
    Class<? extends Plugin> plugin();

    /**
     * Config type of the file.
     *
     * @return config type of the file.
     */
    @Nonnull
    ConfigType type() default ConfigType.YAML;
}
