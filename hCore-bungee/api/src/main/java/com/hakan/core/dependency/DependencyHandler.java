package com.hakan.core.dependency;

import com.hakan.core.dependency.annotations.Dependency;
import com.hakan.core.dependency.annotations.DependencyList;
import com.hakan.core.dependency.utils.DependencyUtils;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;

/**
 * DependencyHandler class to load
 * dependencies at runtime.
 */
public final class DependencyHandler {

    /**
     * Loads the dependencies of
     * the class to given path.
     *
     * @param object The object.
     */
    public static void load(@Nonnull Object object) {
        Validate.notNull(object, "object cannot be null!");


        DependencyList dependencyList = object.getClass().getAnnotation(DependencyList.class);
        Validate.notNull(dependencyList, "class must have @DependencyList annotation!");

        for (Dependency dependency : dependencyList.value()) {
            DependencyAttribute dependencyAttribute = new DependencyAttribute(dependency);

            DependencyUtils.downloadJars(dependencyAttribute);
            DependencyUtils.loadJars(object.getClass(), dependencyAttribute);
        }
    }
}
