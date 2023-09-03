package com.hakan.core.dependency.annotations;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Dependency annotation to download
 * a dependency at runtime.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Dependency {

    /**
     * The group id of the dependency.
     *
     * @return The group id.
     */
    @Nonnull
    String groupId();

    /**
     * The artifact id of the dependency.
     *
     * @return The artifact id.
     */
    @Nonnull
    String artifactId();

    /**
     * The version of the dependency.
     *
     * @return The version.
     */
    @Nonnull
    String version();

    /**
     * The path of the dependency
     * that you want to download.
     *
     * @return The path.
     */
    @Nonnull
    String savePath();

    /**
     * The website of the dependency
     * that you want to download.
     *
     * @return The website.
     */
    @Nonnull
    String website() default "https://repo1.maven.org/maven2/";
}
