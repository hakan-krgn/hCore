package com.hakan.core.command.executors.subcommand;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SubCommand class to annotate sub commands.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

    /**
     * Gets args of annotation.
     *
     * @return Args of annotation.
     */
    @Nonnull
    String[] args() default {};

    /**
     * Gets permission of annotation.
     *
     * @return Permission of annotation.
     */
    @Nonnull
    String permission() default "";

    /**
     * Gets the permission message of annotation.
     *
     * @return Permission message of annotation.
     */
    @Nonnull
    String permissionMessage() default "";
}
