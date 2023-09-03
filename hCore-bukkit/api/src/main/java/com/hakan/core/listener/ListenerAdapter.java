package com.hakan.core.listener;

import com.hakan.core.HCore;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ListenerAdapter class for registering listeners
 * and consume events.
 *
 * @param <T> Event type.
 */
@SuppressWarnings({"unchecked"})
public final class ListenerAdapter<T extends Event> implements Listener, EventExecutor {

    private final Class<T> eventClass;
    private final List<Function<T, Boolean>> filters;

    private int limit;
    private EventPriority priority;
    private Consumer<T> consumer;
    private Consumer<T> consumerAsync;

    /**
     * Creates new instance of this class.
     *
     * @param eventClass Event class.
     */
    public ListenerAdapter(@Nonnull Class<T> eventClass) {
        this.eventClass = Validate.notNull(eventClass, "event class cannot be null!");
        this.filters = new ArrayList<>();
        this.priority = EventPriority.NORMAL;
        this.limit = -10;
        this.register();
    }

    /**
     * Sets event priority.
     *
     * @param priority Event priority.
     * @return This class.
     */
    @Nonnull
    public ListenerAdapter<T> priority(@Nonnull EventPriority priority) {
        this.priority = Validate.notNull(priority, "priority cannot be null!");
        return this;
    }

    /**
     * Sets event filter.
     *
     * @param filter Event filter.
     * @return This class.
     */
    @Nonnull
    public ListenerAdapter<T> filter(@Nonnull Function<T, Boolean> filter) {
        this.filters.add(Validate.notNull(filter, "filter cannot be null!"));
        return this;
    }

    /**
     * Sets event limit.
     * When limit is reached, event will not be consumed.
     *
     * @param limit Event limit.
     * @return This class.
     */
    @Nonnull
    public ListenerAdapter<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * This event will remove in duration
     * if this method is called.
     *
     * @param duration Duration.
     * @param unit     Unit.
     * @return This class.
     */
    @Nonnull
    public ListenerAdapter<T> expire(int duration, @Nonnull TimeUnit unit) {
        Validate.notNull(unit, "time unit cannot be null!");
        HCore.syncScheduler().after(duration, unit).run(this::unregister);
        return this;
    }

    /**
     * This event will remove in duration
     * if this method is called.
     *
     * @param duration Duration.
     * @return This class.
     */
    @Nonnull
    public ListenerAdapter<T> expire(@Nonnull Duration duration) {
        Validate.notNull(duration, "duration cannot be null!");
        HCore.syncScheduler().after(duration).run(this::unregister);
        return this;
    }

    /**
     * This event will remove in ticks
     * if this method is called.
     *
     * @param ticks ticks.
     * @return This class.
     */
    @Nonnull
    public ListenerAdapter<T> expire(int ticks) {
        HCore.syncScheduler().after(ticks)
                .run(this::unregister);
        return this;
    }

    /**
     * Sets event consumer.
     * If event triggers, this consumer
     * will call.
     *
     * @param consumer Consumer.
     * @return This class.
     */
    @Nonnull
    public ListenerAdapter<T> consume(@Nonnull Consumer<T> consumer) {
        this.consumer = Validate.notNull(consumer, "consumer cannot be null!");
        return this;
    }

    /**
     * Sets event consumer.
     * If event triggers, this consumer
     * will call as async.
     *
     * @param consumer Consumer.
     * @return This class.
     */
    @Nonnull
    public ListenerAdapter<T> consumeAsync(@Nonnull Consumer<T> consumer) {
        this.consumerAsync = Validate.notNull(consumer, "consumer cannot be null!");
        return this;
    }

    /**
     * Registers this listener to server.
     *
     * @return This class.
     */
    @Nonnull
    public ListenerAdapter<T> register() {
        Bukkit.getPluginManager().registerEvent(this.eventClass, this, this.priority, this, HCore.getInstance(), false);
        return this;
    }

    /**
     * Unregisters this listener from server.
     *
     * @return This class.
     */
    @Nonnull
    public ListenerAdapter<T> unregister() {
        HandlerList.unregisterAll(this);
        return this;
    }

    /**
     * Executes event.
     *
     * @param event Event.
     */
    @Override
    public void execute(@Nonnull Listener listener, @Nonnull Event event) {
        if (event.getClass().equals(this.eventClass)) {
            T t = (T) event;

            for (Function<T, Boolean> filter : this.filters)
                if (!filter.apply(t))
                    return;

            if (this.consumer != null)
                this.consumer.accept(t);
            if (this.consumerAsync != null)
                HCore.asyncScheduler().run(() -> this.consumerAsync.accept(t));
            if (this.limit != -10 && --this.limit == 0)
                this.unregister();
        }
    }
}
