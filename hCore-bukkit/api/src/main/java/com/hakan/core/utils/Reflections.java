package com.hakan.core.utils;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Reflections utility class.
 * to get classes and instances
 * by type or annotation.
 */
@SuppressWarnings({"unchecked"})
public final class Reflections {

    private final JavaPlugin plugin;

    /**
     * Create a new Reflections instance.
     *
     * @param plugin the plugin instance.
     */
    public Reflections(@Nonnull JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin cannot be null");
    }

    /**
     * Get all instances by type.
     *
     * @param scannedPackage the package to scan.
     * @param assignable     the assignable type.
     * @param <T>            the type.
     * @return the list of instances.
     */
    @Nonnull
    public <T> List<T> getInstancesByType(@Nonnull String scannedPackage,
                                          @Nonnull Class<T> assignable) {
        Validate.notNull(assignable, "assignable cannot be null");
        Validate.notNull(scannedPackage, "scanned package cannot be null");

        return this.getClassesByType(scannedPackage, assignable).stream()
                .map(this::newInstance)
                .collect(Collectors.toList());
    }

    /**
     * Get a new instance of the annotated class.
     *
     * @param scannedPackage the package to scan.
     * @param assignable     the assignable type.
     * @param <T>            the type.
     * @return the instance.
     */
    @Nonnull
    public <T extends Annotation> List<?> getInstancesByAnnotation(@Nonnull String scannedPackage,
                                                                   @Nonnull Class<T> assignable) {
        Validate.notNull(assignable, "assignable cannot be null");
        Validate.notNull(scannedPackage, "scanned package cannot be null");

        return this.getClassesByAnnotation(scannedPackage, assignable).stream()
                .map(this::newInstance)
                .collect(Collectors.toList());
    }

    /**
     * Gets list of the classes by type.
     *
     * @param scannedPackage the package to scan.
     * @param assignable     the assignable type.
     * @param <T>            the type.
     * @return the instance.
     */
    @Nonnull
    public <T> List<Class<T>> getClassesByType(@Nonnull String scannedPackage,
                                               @Nonnull Class<T> assignable) {
        Validate.notNull(assignable, "assignable cannot be null");
        Validate.notNull(scannedPackage, "scanned package cannot be null");

        return this.getClasses(scannedPackage).stream()
                .filter(assignable::isAssignableFrom)
                .map(clazz -> (Class<T>) clazz)
                .collect(Collectors.toList());
    }

    /**
     * Gets list of the annotated class.
     *
     * @param scannedPackage the package to scan.
     * @param assignable     the assignable type.
     * @param <T>            the type.
     * @return the instance.
     */
    @Nonnull
    public <T extends Annotation> List<Class<?>> getClassesByAnnotation(@Nonnull String scannedPackage,
                                                                        @Nonnull Class<T> assignable) {
        Validate.notNull(assignable, "assignable cannot be null");
        Validate.notNull(scannedPackage, "scanned package cannot be null");

        return this.getClasses(scannedPackage).stream()
                .filter(clazz -> clazz.isAnnotationPresent(assignable))
                .map(clazz -> (Class<?>) clazz)
                .collect(Collectors.toList());
    }

    /**
     * Gets list of the class.
     *
     * @param scannedPackage the package to scan.
     * @return the instance.
     */
    @Nonnull
    public List<Class<?>> getClasses(@Nonnull String scannedPackage) {
        Validate.notNull(scannedPackage, "scanned package cannot be null");

        try {
            File file = new File(this.plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            return this.getClasses(file, scannedPackage);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets list of the class.
     *
     * @param file           the file to scan.
     * @param scannedPackage the package to scan.
     * @return the instance.
     */
    @Nonnull
    public List<Class<?>> getClasses(@Nonnull File file,
                                     @Nonnull String scannedPackage) {
        Validate.notNull(file, "file cannot be null");
        Validate.notNull(scannedPackage, "scanned package cannot be null");

        try {
            List<Class<?>> classes = new ArrayList<>();
            ZipInputStream zip = new ZipInputStream(Files.newInputStream(file.toPath()));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (entry.isDirectory())
                    continue;
                else if (!entry.getName().endsWith(".class"))
                    continue;
                else if (!entry.getName().startsWith(scannedPackage.replace('.', '/')))
                    continue;

                String className = entry.getName().replace('/', '.');
                classes.add(Class.forName(className.substring(0, className.length() - ".class".length())));
            }
            return classes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a new instance of the class.
     *
     * @param clazz the class.
     * @param <T>   the type.
     * @return the instance.
     */
    @Nullable
    public <T> T newInstance(@Nonnull Class<T> clazz) {
        Validate.notNull(clazz, "class cannot be null");

        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}