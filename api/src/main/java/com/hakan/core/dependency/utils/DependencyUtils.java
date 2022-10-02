package com.hakan.core.dependency.utils;

import com.hakan.core.configuration.utils.ConfigUtils;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * DependencyUtils class to save
 * jar from website to folder and
 * load it to classpath.
 */
public final class DependencyUtils {

    /**
     * Save jar from website to folder.
     *
     * @param urlText  The url of jar.
     * @param savePath The save path.
     */
    public static void saveFile(@Nonnull String urlText,
                                @Nonnull String savePath) {
        Validate.notNull(urlText, "url text cannot be null!");
        Validate.notNull(savePath, "save path cannot be null!");
        DependencyUtils.saveFile(urlText, new File(savePath));
    }

    /**
     * Save jar from website to folder.
     *
     * @param urlText  The url of jar.
     * @param saveFile The save file.
     */
    public static void saveFile(@Nonnull String urlText,
                                @Nonnull File saveFile) {
        Validate.notNull(urlText, "url text cannot be null!");
        Validate.notNull(saveFile, "save file cannot be null!");

        try {
            ConfigUtils.createFile(saveFile.getPath());

            URL website = new URL(urlText);
            InputStream in = website.openStream();
            Files.copy(in, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the dependencies from
     * the given url text.
     *
     * @param clazz   The class for loader instance.
     * @param urlText The url text.
     */
    public static void loadJar(@Nonnull Class<?> clazz,
                               @Nonnull String urlText) {
        try {
            Validate.notNull(clazz, "class cannot be null!");
            Validate.notNull(urlText, "url text cannot be null!");

            URL url = new File(urlText).toURI().toURL();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(clazz.getClassLoader(), url);
            method.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}