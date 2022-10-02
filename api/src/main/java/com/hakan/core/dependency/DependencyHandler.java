package com.hakan.core.dependency;

import com.hakan.core.dependency.annotations.Dependency;
import com.hakan.core.dependency.annotations.DependencyList;
import com.hakan.core.dependency.utils.DependencyUtils;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * DependencyHandler class to load
 * dependencies at runtime.
 */
public final class DependencyHandler {

    /**
     * Loads the dependencies of
     * the class to given path.
     *
     * @param object   The object.
     * @param savePath The save path.
     */
    public static void load(@Nonnull Object object,
                            @Nonnull String savePath) {
        Validate.notNull(object, "object cannot be null!");
        Validate.notNull(savePath, "save path cannot be null!");
        DependencyHandler.load(object, new File(savePath));
    }

    /**
     * Loads the dependencies of
     * the class to given path.
     *
     * @param object     The object.
     * @param saveFolder The save folder.
     */
    public static void load(@Nonnull Object object,
                            @Nonnull File saveFolder) {
        Validate.notNull(object, "object cannot be null!");
        Validate.notNull(saveFolder, "save folder cannot be null!");


        DependencyList dependencyList = object.getClass().getAnnotation(DependencyList.class);
        Validate.notNull(dependencyList, "class must have @DependencyList annotation!");

        for (Dependency dependency : dependencyList.value()) {
            DependencyAttribute dependencyAttribute = new DependencyAttribute(dependency);

            String dependencyUrl = dependencyAttribute.asUrl();
            String dependencyPath = dependencyAttribute.asPath(saveFolder);

            DependencyUtils.saveFile(dependencyUrl, dependencyPath);
            DependencyUtils.loadJar(object.getClass(), dependencyPath);
        }
    }
}