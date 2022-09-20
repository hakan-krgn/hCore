package com.hakan.core.hologram.builder;

import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.HHologramHandler;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * HHologramBuilder class to manage
 * hologram before creating.
 */
public final class HHologramBuilder {

    private final String id;
    private Location location;
    private Set<UUID> viewers;
    private boolean showEveryone;
    private double lineDistance;

    /**
     * Creates a new HHologramBuilder.
     *
     * @param id Hologram id.
     */
    public HHologramBuilder(@Nonnull String id) {
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
     * @return HHologramBuilder.
     */
    @Nonnull
    public HHologramBuilder location(@Nonnull Location location) {
        this.location = Validate.notNull(location, "location cannot be null!");
        return this;
    }

    /**
     * Adds viewer to hologram.
     *
     * @param player Player to add.
     * @return HHologramBuilder.
     */
    @Nonnull
    public HHologramBuilder addViewerByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return this.addViewerByUID(player.getUniqueId());
    }

    /**
     * Adds viewer to hologram.
     *
     * @param viewer UUID of player to add.
     * @return HHologramBuilder.
     */
    @Nonnull
    public HHologramBuilder addViewerByUID(@Nonnull UUID viewer) {
        this.viewers.add(Validate.notNull(viewer, "viewer cannot be null!"));
        return this;
    }

    /**
     * Adds viewer list to hologram.
     *
     * @param viewers Players to add.
     * @return HHologramBuilder.
     */
    @Nonnull
    public HHologramBuilder addViewersByPlayer(@Nonnull Set<Player> viewers) {
        Validate.notNull(viewers, "viewers cannot be null!");
        viewers.forEach(this::addViewerByPlayer);
        return this;
    }

    /**
     * Adds viewer list to hologram.
     *
     * @param viewers UUIDs of players to add.
     * @return HHologramBuilder.
     */
    @Nonnull
    public HHologramBuilder addViewersByUID(@Nonnull Set<UUID> viewers) {
        Validate.notNull(viewers, "viewers cannot be null!");
        viewers.forEach(this::addViewerByUID);
        return this;
    }

    /**
     * Sets viewers of hologram.
     *
     * @param viewers UUIDs to set.
     * @return HHologramBuilder.
     */
    @Nonnull
    public HHologramBuilder setViewers(@Nonnull Set<UUID> viewers) {
        this.viewers = Validate.notNull(viewers, "viewers cannot be null!");
        return this;
    }

    /**
     * Sets if hologram will be shown to everyone.
     *
     * @param showEveryone If hologram will be shown to everyone.
     * @return HHologramBuilder.
     */
    @Nonnull
    public HHologramBuilder showEveryone(boolean showEveryone) {
        this.showEveryone = showEveryone;
        return this;
    }

    /**
     * Sets line distance of hologram.
     * Distance between two lines.
     *
     * @param lineDistance Line distance of hologram.
     * @return HHologramBuilder.
     */
    @Nonnull
    public HHologramBuilder lineDistance(double lineDistance) {
        this.lineDistance = lineDistance;
        return this;
    }

    /**
     * Creates a new HHologram as force.
     *
     * @return HHologram.
     */
    @Nonnull
    public HHologram forceBuild() {
        HHologramHandler.findByID(this.id).ifPresent(HHologram::delete);
        return this.build();
    }

    /**
     * Creates a new HHologram.
     *
     * @return HHologram.
     */
    @Nonnull
    public HHologram build() {
        HHologram hHologram = new HHologram(this.id, this.location, this.viewers, this.showEveryone, this.lineDistance);
        HHologramHandler.getContent().put(this.id, hHologram);
        return hHologram;
    }
}