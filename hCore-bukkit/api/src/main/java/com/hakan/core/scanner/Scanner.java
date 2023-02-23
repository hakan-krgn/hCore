package com.hakan.core.scanner;

import com.hakan.core.HCore;
import com.hakan.core.command.CommandHandler;
import com.hakan.core.command.executors.basecommand.BaseCommand;
import com.hakan.core.dependency.DependencyHandler;
import com.hakan.core.dependency.annotations.DependencyList;
import com.hakan.core.utils.Reflections;
import com.hakan.core.utils.Validate;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * Scanner class to
 * scan classes and register
 * them automatically.
 */
public final class Scanner {

    /**
     * Initialize the scanner.
     */
    public static void initialize(@Nonnull JavaPlugin plugin) {
        Validate.notNull(plugin, "plugin cannot be null!");

        if (!plugin.getClass().isAnnotationPresent(Scan.class))
            return;

        Scan scan = plugin.getClass().getAnnotation(Scan.class);
        String basePackage = scan.value();

        Reflections reflections = new Reflections(plugin);
        reflections.getInstancesByAnnotation(basePackage, BaseCommand.class)
                .forEach(CommandHandler::register);
        reflections.getInstancesByType(basePackage, Listener.class)
                .forEach(HCore::registerListeners);
        reflections.getInstancesByAnnotation(basePackage, DependencyList.class)
                .forEach(DependencyHandler::load);
    }
}