package com.hakan.core.configuration.annotations;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ConfigValue annotation to
 * define config value and get
 * connected filed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigValue {

    /**
     * Path of the value.
     *
     * @return Path of the value.
     */
    @Nonnull
    String value();
}
