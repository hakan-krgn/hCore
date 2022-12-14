package com.hakan.core.hologram.action;

import com.hakan.core.HCore;
import com.hakan.core.hologram.Hologram;
import com.hakan.core.hologram.event.HologramClickEvent;
import com.hakan.core.hologram.event.HologramDeleteEvent;
import com.hakan.core.hologram.event.HologramSpawnEvent;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Hologram action class to call
 * custom events and other actions.
 */
public final class HologramAction {

    private final Hologram hologram;
    private Consumer<Hologram> spawnConsumer;
    private Consumer<Hologram> deleteConsumer;
    private BiConsumer<Player, HologramLine> clickConsumer;

    /**
     * Creates new hologram action.
     *
     * @param hologram Hologram of action.
     */
    public HologramAction(@Nonnull Hologram hologram) {
        this.hologram = Validate.notNull(hologram, "hologram cannot be null!");
    }

    /**
     * Gets hologram of action.
     *
     * @return Hologram of action.
     */
    @Nonnull
    public Hologram getHologram() {
        return this.hologram;
    }

    /**
     * When hologram is spawned
     * this consumer will be called.
     *
     * @param consumer Consumer of event.
     */
    public void whenSpawned(@Nonnull Consumer<Hologram> consumer) {
        this.spawnConsumer = Validate.notNull(consumer, "consumer cannot be null!");
    }

    /**
     * When hologram is deleted
     * this consumer will be called.
     *
     * @param consumer Consumer of event.
     */
    public void whenDeleted(@Nonnull Consumer<Hologram> consumer) {
        this.deleteConsumer = Validate.notNull(consumer, "consumer cannot be null!");
    }

    /**
     * When the player click on hologram,
     * this consumer will be called.
     *
     * @param consumer Consumer of event.
     */
    public void whenClicked(@Nonnull BiConsumer<Player, HologramLine> consumer) {
        this.clickConsumer = Validate.notNull(consumer, "consumer cannot be null!");
    }

    /**
     * Triggers this action when
     * hologram is spawned.
     */
    public void onSpawn() {
        if (this.spawnConsumer != null)
            this.spawnConsumer.accept(this.hologram);

        HCore.syncScheduler().run(() -> {
            HologramSpawnEvent event = new HologramSpawnEvent(this.hologram);
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    /**
     * Triggers this action when
     * hologram is deleted.
     */
    public void onDelete() {
        if (this.deleteConsumer != null)
            this.deleteConsumer.accept(this.hologram);

        HCore.syncScheduler().run(() -> {
            HologramDeleteEvent event = new HologramDeleteEvent(this.hologram);
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    /**
     * Triggers this action when
     * a player clicked on hologram.
     *
     * @param player Player who clicked.
     * @param line   Line which was clicked.
     */
    public void onClick(@Nonnull Player player, @Nonnull HologramLine line) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(line, "line cannot be null!");

        if (this.clickConsumer != null)
            this.clickConsumer.accept(player, line);

        HCore.syncScheduler().run(() -> {
            HologramClickEvent event = new HologramClickEvent(player, line);
            Bukkit.getPluginManager().callEvent(event);
        });
    }
}
