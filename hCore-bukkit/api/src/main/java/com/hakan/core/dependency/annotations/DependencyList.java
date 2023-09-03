package com.hakan.core.dependency.annotations;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DependencyList annotation to load
 * multiple dependencies at runtime.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DependencyList {

    /**
     * The dependencies of the dependency.
     *
     * @return The dependencies.
     */
    @Nonnull
    Dependency[] value() default {};
}
