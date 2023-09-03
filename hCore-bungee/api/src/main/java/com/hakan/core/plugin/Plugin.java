package com.hakan.core.plugin;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a class
 * as a com.hakan.core.plugin and to create a com.hakan.core.plugin.yml
 * file automatically at compile time.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Plugin {

    /**
     * The name of the com.hakan.core.plugin.
     *
     * @return The name of the com.hakan.core.plugin.
     */
    @Nonnull
    String name();

    /**
     * The version of the com.hakan.core.plugin.
     *
     * @return The version of the com.hakan.core.plugin.
     */
    @Nonnull
    String version();

    /**
     * The description of the com.hakan.core.plugin.
     *
     * @return The description of the com.hakan.core.plugin.
     */
    @Nonnull
    String description() default "";

    /**
     * The api version of the com.hakan.core.plugin.
     *
     * @return The api version of the com.hakan.core.plugin.
     */
    @Nonnull
    String apiVersion() default "";

    /**
     * The load worlds of the com.hakan.core.plugin.
     *
     * @return The load worlds of the com.hakan.core.plugin.
     */
    @Nonnull
    String load() default "";

    /**
     * The website of the com.hakan.core.plugin.
     *
     * @return The website of the com.hakan.core.plugin.
     */
    @Nonnull
    String website() default "";

    /**
     * The prefix of the com.hakan.core.plugin.
     *
     * @return The prefix of the com.hakan.core.plugin.
     */
    @Nonnull
    String prefix() default "";

    /**
     * The authors of the com.hakan.core.plugin.
     *
     * @return The authors of the com.hakan.core.plugin.
     */
    @Nonnull
    String[] authors() default {};

    /**
     * The depend list of the com.hakan.core.plugin.
     *
     * @return The depend list of the com.hakan.core.plugin.
     */
    @Nonnull
    String[] depends() default {};

    /**
     * The soft depend list of the com.hakan.core.plugin.
     *
     * @return The soft depend list of the com.hakan.core.plugin.
     */
    @Nonnull
    String[] softDepends() default {};

    /**
     * The load before list of the com.hakan.core.plugin.
     *
     * @return The load before list of the com.hakan.core.plugin.
     */
    @Nonnull
    String[] loadBefore() default {};

    /**
     * The libraries of the com.hakan.core.plugin.
     *
     * @return The libraries of the com.hakan.core.plugin.
     */
    @Nonnull
    String[] libraries() default {};
}
