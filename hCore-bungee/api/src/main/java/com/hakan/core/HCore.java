package com.hakan.core;

import com.hakan.core.command.CommandHandler;
import com.hakan.core.configuration.ConfigHandler;
import com.hakan.core.configuration.containers.ConfigContainer;
import com.hakan.core.dependency.DependencyHandler;
import com.hakan.core.listener.ListenerAdapter;
import com.hakan.core.scheduler.Scheduler;
import com.hakan.core.spam.Spam;
import com.hakan.core.utils.Serializer;
import com.hakan.core.utils.Validate;
import com.hakan.core.utils.hooks.Metrics;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Main class of this core.
 * You can reach all APIs from this
 * class as static.
 */
@SuppressWarnings({"unused"})
public final class HCore {

    private static Plugin INSTANCE;

    /**
     * Gets instance.
     *
     * @return Instance.
     */
    @Nonnull
    public static Plugin getInstance() {
        return INSTANCE;
    }

    /**
     * Sets instance plugin of hCore.
     *
     * @param plugin Instance.
     */
    public static void setInstance(@Nonnull Plugin plugin) {
        INSTANCE = Validate.notNull(plugin, "instance cannot be null!");
    }

    /**
     * Initializes all APIs.
     *
     * @param plugin Instance of main class.
     */
    public static void initialize(@Nonnull Plugin plugin) {
        if (INSTANCE != null) return;

        HCore.setInstance(plugin);
        Metrics.initialize(plugin);
    }


    /*
    SCHEDULER
     */

    /**
     * Creates scheduler.
     *
     * @param async Async status.
     * @return Scheduler.
     */
    @Nonnull
    public static Scheduler scheduler(boolean async) {
        return new Scheduler(INSTANCE, async);
    }

    /**
     * Creates async scheduler.
     *
     * @return Scheduler.
     */
    @Nonnull
    public static Scheduler asyncScheduler() {
        return HCore.scheduler(true);
    }

    /**
     * Creates sync scheduler.
     *
     * @return Scheduler.
     */
    @Nonnull
    public static Scheduler syncScheduler() {
        return HCore.scheduler(false);
    }


    /*
    COMMAND
     */

    /**
     * Registers commands to server.
     *
     * @param adapters List of command adapters.
     */
    public static void registerCommands(@Nonnull Object... adapters) {
        CommandHandler.register(adapters);
    }


    /*
    LISTENERS
     */

    /**
     * Registers listeners to server.
     *
     * @param listeners List of listeners.
     */
    public static void registerListeners(@Nonnull Listener... listeners) {
        Arrays.asList(Validate.notNull(listeners, "listeners cannot be null!"))
                .forEach(listener -> INSTANCE.getProxy().getPluginManager().registerListener(INSTANCE, listener));
    }

    /**
     * Registers listeners to server.
     *
     * @param eventClass Class of event.
     * @param <T>        Event type.
     * @return Listener.
     */
    @Nonnull
    public static <T extends Event> ListenerAdapter<T> registerEvent(@Nonnull Class<T> eventClass) {
        return new ListenerAdapter<>(eventClass);
    }


    /*
    CONFIGURATION
     */

    /**
     * Loads configuration container.
     *
     * @param configClass Config class.
     * @return Config container class.
     */
    @Nonnull
    public static <T extends ConfigContainer> T loadConfig(@Nonnull Object configClass) {
        return ConfigHandler.load(configClass);
    }

    /**
     * Loads config container.
     *
     * @param file Configuration container.
     * @return Configuration container.
     */
    @Nonnull
    public static <T extends ConfigContainer> T loadConfig(@Nonnull ConfigContainer file) {
        return ConfigHandler.load(file);
    }

    /**
     * Finds configuration container.
     *
     * @param path Configuration container path.
     * @return Configuration container.
     */
    @Nonnull
    public static Optional<ConfigContainer> findConfigByPath(@Nonnull String path) {
        return ConfigHandler.findByPath(path);
    }

    /**
     * Gets configuration file.
     *
     * @param path Configuration file container.
     * @return Configuration container.
     */
    @Nonnull
    public static ConfigContainer getConfigByPath(@Nonnull String path) {
        return ConfigHandler.getByPath(path);
    }


    /*
    DEPENDENCY
     */

    /**
     * Loads the dependencies of
     * the class to given path.
     *
     * @param object Instance of dependency class.
     */
    public static void loadDependencies(@Nonnull Object object) {
        DependencyHandler.load(object);
    }


    /*
    SPAM CONTROLLER
     */

    /**
     * Gets difference between now
     * and end time of spam as millisecond.
     *
     * @param id The id to check.
     * @return Difference between now
     * and end time of spam as millisecond.
     */
    public static long remainTimeSpam(@Nonnull String id) {
        return Spam.remainTime(id);
    }

    /**
     * Gets difference between now
     * and end time of spam as unit.
     *
     * @param id   The id to check.
     * @param unit The time unit.
     * @return Difference between now
     * and end time of spam as unit.
     */
    public static long remainTimeSpam(@Nonnull String id, @Nonnull TimeUnit unit) {
        return Spam.remainTime(id, unit);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id   The id to check.
     * @param time Time.
     * @param unit Time unit.
     * @return True if spamming.
     */
    public static boolean checkSpam(@Nonnull String id, int time, @Nonnull TimeUnit unit) {
        return Spam.check(id, time, unit);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id       The id to check.
     * @param duration The duration to check.
     * @return True if spamming.
     */
    public static boolean checkSpam(@Nonnull String id, @Nonnull Duration duration) {
        return Spam.check(id, duration);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id    The id to check.
     * @param ticks The time in ticks.
     * @return True if spamming.
     */
    public static boolean checkSpam(@Nonnull String id, long ticks) {
        return Spam.check(id, ticks);
    }


    /*
    SERIALIZER
     */

    /**
     * Serializes object.
     *
     * @param object Object.
     * @return Serialized string as optional.
     */
    @Nonnull
    public synchronized static Optional<String> serializeSafe(@Nonnull Object object) {
        return Serializer.serializeSafe(object);
    }

    /**
     * Serializes object.
     *
     * @param object Object.
     * @return Serialized string.
     */
    @Nonnull
    public synchronized static String serialize(@Nonnull Object object) {
        return Serializer.serialize(object);
    }

    /**
     * Deserializes object.
     *
     * @param serializedText Text that want to deserialize.
     * @param clazz          Object type class.
     * @param <T>            Object type.
     * @return Deserialized object as optional.
     */
    @Nonnull
    public synchronized static <T> Optional<T> deserializeSafe(@Nonnull String serializedText, @Nonnull Class<T> clazz) {
        return Serializer.deserializeSafe(serializedText, clazz);
    }

    /**
     * Deserializes object.
     *
     * @param serializedText Text that want to deserialize.
     * @param clazz          Object type class.
     * @param <T>            Object type.
     * @return Deserialized object as optional.
     */
    @Nonnull
    public synchronized static <T> T deserialize(@Nonnull String serializedText, @Nonnull Class<T> clazz) {
        return Serializer.deserialize(serializedText, clazz);
    }
}
