package com.hakan.core.command.executors.placeholder;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Placeholder class to register
 * placeholders for auto tab completion.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Placeholder {

    /**
     * Gets name of annotation.
     *
     * @return Name of annotation.
     */
    @Nonnull
    String name();

    /**
     * Gets the permission for
     * using this placeholder.
     *
     * @return Required permission.
     */
    @Nonnull
    String permission() default "";
}
