package com.hakan.core.command.executors.basecommand;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BaseCommand class to annotate com.hakan.core.command class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BaseCommand {

    /**
     * Gets com.hakan.core.command name of annotation.
     *
     * @return Command name of annotation.
     */
    @Nonnull
    String name();

    /**
     * Gets com.hakan.core.command description of annotation.
     *
     * @return Command description of annotation.
     */
    @Nonnull
    String description() default "";

    /**
     * Gets com.hakan.core.command usage of annotation.
     *
     * @return Command usage of annotation.
     */
    @Nonnull
    String usage() default "";

    /**
     * Gets com.hakan.core.command aliases of annotation.
     *
     * @return Command aliases of annotation.
     */
    @Nonnull
    String[] aliases() default "";

    /**
     * Gets auto tab complete of com.hakan.core.command.
     *
     * @return Auto tab complete of com.hakan.core.command.
     */
    boolean tabComplete() default true;
}
