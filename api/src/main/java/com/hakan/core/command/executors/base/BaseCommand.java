package com.hakan.core.command.executors.base;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BaseCommand class to annotate command class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BaseCommand {

    /**
     * Gets command name of annotation.
     *
     * @return Command name of annotation.
     */
    @Nonnull
    String name();

    /**
     * Gets command description of annotation.
     *
     * @return Command description of annotation.
     */
    @Nonnull
    String description() default "";

    /**
     * Gets command usage of annotation.
     *
     * @return Command usage of annotation.
     */
    @Nonnull
    String usage() default "";

    /**
     * Gets command aliases of annotation.
     *
     * @return Command aliases of annotation.
     */
    @Nonnull
    String[] aliases() default "";
}