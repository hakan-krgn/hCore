package com.hakan.core.plugin;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a class
 * as a plugin and to create a plugin.yml
 * file automatically at compile time.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Plugin {

    /**
     * The name of the plugin.
     *
     * @return The name of the plugin.
     */
    @Nonnull
    String name();

    /**
     * The version of the plugin.
     *
     * @return The version of the plugin.
     */
    @Nonnull
    String version();

    /**
     * The description of the plugin.
     *
     * @return The description of the plugin.
     */
    @Nonnull
    String description() default "";

    /**
     * The api version of the plugin.
     *
     * @return The api version of the plugin.
     */
    @Nonnull
    String apiVersion() default "";

    /**
     * The load worlds of the plugin.
     *
     * @return The load worlds of the plugin.
     */
    @Nonnull
    String load() default "";

    /**
     * The website of the plugin.
     *
     * @return The website of the plugin.
     */
    @Nonnull
    String website() default "";

    /**
     * The prefix of the plugin.
     *
     * @return The prefix of the plugin.
     */
    @Nonnull
    String prefix() default "";

    /**
     * The authors of the plugin.
     *
     * @return The authors of the plugin.
     */
    @Nonnull
    String[] authors() default {};

    /**
     * The depend list of the plugin.
     *
     * @return The depend list of the plugin.
     */
    @Nonnull
    String[] depends() default {};

    /**
     * The soft depend list of the plugin.
     *
     * @return The soft depend list of the plugin.
     */
    @Nonnull
    String[] softDepends() default {};

    /**
     * The load before list of the plugin.
     *
     * @return The load before list of the plugin.
     */
    @Nonnull
    String[] loadBefore() default {};

    /**
     * The libraries of the plugin.
     *
     * @return The libraries of the plugin.
     */
    @Nonnull
    String[] libraries() default {};
}
