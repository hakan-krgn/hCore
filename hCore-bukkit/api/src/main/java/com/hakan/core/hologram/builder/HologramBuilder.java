package com.hakan.core.hologram.builder;

import com.hakan.core.hologram.Hologram;
import com.hakan.core.hologram.HologramHandler;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * HologramBuilder class to manage
 * hologram before creating.
 */
public final class HologramBuilder {

    private final String id;
    private Location location;
    private Set<UUID> viewers;
    private boolean showEveryone;
    private double lineDistance;
    private Consumer<Hologram> spawnConsumer;
    private Consumer<Hologram> deleteConsumer;
    private BiConsumer<Player, HologramLine> clickConsumer;

    /**
     * Creates a new HologramBuilder.
     *
     * @param id Hologram id.
     */
    public HologramBuilder(@Nonnull String id) {
        this.id = Validate.notNull(id, "id cannot be null!");
        this.location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        this.viewers = new HashSet<>();
        this.showEveryone = true;
        this.lineDistance = 0.25;
    }

    /**
     * Sets location of hologram.
     *
     * @param location Location of hologram.
     * @return HologramBuilder.
     */
    @Nonnull
    public HologramBuilder location(@Nonnull Location location) {
        this.location = Validate.notNull(location, "location cannot be null!");
        return this;
    }

    /**
     * Adds viewer to hologram.
     *
     * @param player Player to add.
     * @return HologramBuilder.
     */
    @Nonnull
    public HologramBuilder addViewerByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return this.addViewerByUID(player.getUniqueId());
    }

    /**
     * Adds viewer to hologram.
     *
     * @param viewer UUID of player to add.
     * @return HologramBuilder.
     */
    @Nonnull
    public HologramBuilder addViewerByUID(@Nonnull UUID viewer) {
        this.viewers.add(Validate.notNull(viewer, "viewer cannot be null!"));
        return this;
    }

    /**
     * Adds viewer list to hologram.
     *
     * @param viewers Players to add.
     * @return HologramBuilder.
     */
    @Nonnull
    public HologramBuilder addViewersByPlayer(@Nonnull Set<Player> viewers) {
        Validate.notNull(viewers, "viewers cannot be null!");
        viewers.forEach(this::addViewerByPlayer);
        return this;
    }

    /**
     * Adds viewer list to hologram.
     *
     * @param viewers UUIDs of players to add.
     * @return HologramBuilder.
     */
    @Nonnull
    public HologramBuilder addViewersByUID(@Nonnull Set<UUID> viewers) {
        Validate.notNull(viewers, "viewers cannot be null!");
        viewers.forEach(this::addViewerByUID);
        return this;
    }

    /**
     * Sets viewers of hologram.
     *
     * @param viewers UUIDs to set.
     * @return HologramBuilder.
     */
    @Nonnull
    public HologramBuilder setViewers(@Nonnull Set<UUID> viewers) {
        this.viewers = Validate.notNull(viewers, "viewers cannot be null!");
        return this;
    }

    /**
     * Sets if hologram will be shown to everyone.
     *
     * @param showEveryone If hologram will be shown to everyone.
     * @return HologramBuilder.
     */
    @Nonnull
    public HologramBuilder showEveryone(boolean showEveryone) {
        this.showEveryone = showEveryone;
        return this;
    }

    /**
     * Sets line distance of hologram.
     * Distance between two lines.
     *
     * @param lineDistance Line distance of hologram.
     * @return HologramBuilder.
     */
    @Nonnull
    public HologramBuilder lineDistance(double lineDistance) {
        this.lineDistance = lineDistance;
        return this;
    }

    /**
     * When hologram is spawned
     * this consumer will be called.
     *
     * @param consumer Consumer.
     * @return Instance of this class.
     */
    @Nonnull
    public HologramBuilder whenSpawned(@Nonnull Consumer<Hologram> consumer) {
        this.spawnConsumer = Validate.notNull(consumer, "consumer cannot be null!");
        return this;
    }

    /**
     * When hologram is deleted
     * this consumer will be called.
     *
     * @param consumer Consumer.
     * @return Instance of this class.
     */
    @Nonnull
    public HologramBuilder whenDeleted(@Nonnull Consumer<Hologram> consumer) {
        this.deleteConsumer = Validate.notNull(consumer, "consumer cannot be null!");
        return this;
    }

    /**
     * When the player click on hologram,
     * this consumer will be called.
     *
     * @param consumer Consumer.
     * @return Instance of this class.
     */
    @Nonnull
    public HologramBuilder whenClicked(@Nonnull BiConsumer<Player, HologramLine> consumer) {
        this.clickConsumer = Validate.notNull(consumer, "consumer cannot be null!");
        return this;
    }

    /**
     * Creates a new Hologram as force.
     *
     * @return Hologram.
     */
    @Nonnull
    public Hologram forceBuild() {
        HologramHandler.findByID(this.id).ifPresent(Hologram::delete);
        return this.build();
    }

    /**
     * Creates a new Hologram.
     *
     * @return Hologram.
     */
    @Nonnull
    public Hologram build() {
        Validate.isTrue(HologramHandler.has(this.id), "hologram with id " + this.id + " already exists!");


        Hologram hologram = new Hologram(this.id, this.location, this.viewers, this.showEveryone, this.lineDistance);
        if (this.spawnConsumer != null)
            hologram.whenSpawned(this.spawnConsumer);
        if (this.deleteConsumer != null)
            hologram.whenDeleted(this.deleteConsumer);
        if (this.clickConsumer != null)
            hologram.whenClicked(this.clickConsumer);

        HologramHandler.getContent().put(this.id, hologram);
        return hologram;
    }
}
