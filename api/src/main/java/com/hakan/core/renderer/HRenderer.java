package com.hakan.core.renderer;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

/**
 * HRenderer class.
 * This class helps renders the players.
 * returns players in radius.
 */
public final class HRenderer {

    private double radius;
    private boolean deleted;
    private boolean showEveryone;
    private boolean useYAxis;
    private Location location;
    private Set<UUID> viewers;
    private Set<UUID> shownViewers;

    private final Consumer<List<Player>> showConsumer;
    private final Consumer<List<Player>> hideConsumer;
    private final Consumer<HRenderer> deleteConsumer;

    /**
     * Creates new instance of this class.
     *
     * @param location       Center location.
     * @param radius         Radius.
     * @param viewers        Viewers.
     * @param showConsumer   Show consumer.
     * @param hideConsumer   Hide consumer.
     * @param deleteConsumer Delete consumer.
     */
    public HRenderer(@Nonnull Location location, double radius, @Nonnull Set<UUID> viewers,
                     @Nonnull Consumer<List<Player>> showConsumer,
                     @Nonnull Consumer<List<Player>> hideConsumer,
                     @Nonnull Consumer<HRenderer> deleteConsumer) {
        this.location = Objects.requireNonNull(location, "location cannot be null!");
        this.viewers = Objects.requireNonNull(viewers, "viewers cannot be null!");
        this.showConsumer = Objects.requireNonNull(showConsumer, "show consumer cannot be null!");
        this.hideConsumer = Objects.requireNonNull(hideConsumer, "hide consumer cannot be null!");
        this.deleteConsumer = Objects.requireNonNull(deleteConsumer, "delete consumer cannot be null!");
        this.radius = radius;
        this.shownViewers = new HashSet<>();
        this.showEveryone = false;
        this.deleted = false;
        this.useYAxis = true;
    }

    /**
     * Creates new instance of this class.
     *
     * @param location       Center location.
     * @param radius         Radius.
     * @param showConsumer   Show consumer.
     * @param hideConsumer   Hide consumer.
     * @param deleteConsumer Delete consumer.
     */
    public HRenderer(@Nonnull Location location, double radius,
                     @Nonnull Consumer<List<Player>> showConsumer,
                     @Nonnull Consumer<List<Player>> hideConsumer,
                     @Nonnull Consumer<HRenderer> deleteConsumer) {
        this(location, radius, new HashSet<>(), showConsumer, hideConsumer, deleteConsumer);
        this.showEveryone = true;
    }

    /**
     * Gets radius.
     *
     * @return Radius.
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Sets radius.
     *
     * @param radius Radius.
     * @return This class.
     */
    @Nonnull
    public HRenderer setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    /**
     * Checks use y-axis to calculate
     * location distance.
     *
     * @return If use y-axis, returns true.
     */
    public boolean isUseYAxis() {
        return this.useYAxis;
    }

    /**
     * Sets use y-axis to calculate
     * location distance.
     *
     * @param useYAxis Use y-axis mode.
     * @return This class
     */
    @Nonnull
    public HRenderer setUseYAxis(boolean useYAxis) {
        this.useYAxis = useYAxis;
        return this;
    }

    /**
     * Can everyone includes this renderer.
     *
     * @return If includes, returns true.
     */
    public boolean canShowEveryone() {
        return this.showEveryone;
    }

    /**
     * Everyone includes this renderer.
     *
     * @param showEveryone Show mode.
     * @return This class.
     */
    @Nonnull
    public HRenderer showEveryone(boolean showEveryone) {
        this.showEveryone = showEveryone;
        return this;
    }

    /**
     * Gets location of renderer.
     *
     * @return Location of renderer.
     */
    @Nonnull
    public Location getLocation() {
        return this.location;
    }

    /**
     * Sets center location.
     *
     * @param location Center location.
     * @return This class.
     */
    @Nonnull
    public HRenderer setLocation(@Nonnull Location location) {
        this.location = Objects.requireNonNull(location, "location cannot be null!");
        return this;
    }

    /**
     * Gets shown viewers.
     *
     * @return Shown viewers.
     */
    @Nonnull
    public Set<UUID> getShownViewers() {
        return new HashSet<>(this.shownViewers);
    }

    /**
     * Gets shown viewers as player list.
     *
     * @return Shown viewers as player list.
     */
    @Nonnull
    public List<Player> getShownViewersAsPlayer() {
        List<Player> players = new ArrayList<>();
        this.shownViewers.forEach(uid -> {
            Player player = Bukkit.getPlayer(uid);
            if (player != null) players.add(player);
        });
        return players;
    }

    /**
     * Gets viewers.
     *
     * @return Viewers.
     */
    @Nonnull
    public Set<UUID> getViewers() {
        return this.viewers;
    }

    /**
     * Sets viewers.
     *
     * @param viewers Viewers.
     * @return This class.
     */
    @Nonnull
    public HRenderer setViewers(@Nonnull Set<UUID> viewers) {
        this.viewers = Objects.requireNonNull(viewers, "viewers cannot be null!");
        return this;
    }

    /**
     * Set viewers.
     *
     * @param viewers Viewers.
     * @return This class.
     */
    @Nonnull
    public HRenderer setViewers(@Nonnull List<Player> viewers) {
        this.viewers.clear();
        Objects.requireNonNull(viewers, "viewers cannot be null!")
                .forEach(this::addViewer);
        return this;
    }

    /**
     * Adds viewer to renderer.
     *
     * @param uid UID of player.
     * @return This class.
     */
    @Nonnull
    public HRenderer addViewer(@Nonnull UUID uid) {
        this.viewers.add(Objects.requireNonNull(uid, "uid cannot be null!"));
        return this;
    }

    /**
     * Removes viewer from renderer.
     *
     * @param uid UID of player.
     * @return This class.
     */
    @Nonnull
    public HRenderer removeViewer(@Nonnull UUID uid) {
        this.viewers.remove(Objects.requireNonNull(uid, "uid cannot be null!"));
        return this;
    }

    /**
     * Adds viewer to renderer.
     *
     * @param player Player.
     * @return This class.
     */
    @Nonnull
    public HRenderer addViewer(@Nonnull Player player) {
        return this.addViewer(Objects.requireNonNull(player, "player cannot be null!").getUniqueId());
    }

    /**
     * Removes viewer from renderer.
     *
     * @param player Player.
     * @return This class.
     */
    @Nonnull
    public HRenderer removeViewer(@Nonnull Player player) {
        return this.removeViewer(Objects.requireNonNull(player, "player cannot be null!").getUniqueId());
    }

    /**
     * Checks renderer is deleted.
     *
     * @return If renderer is deleted, returns true.
     */
    public boolean isDeleted() {
        return this.deleted;
    }

    /**
     * Deletes renderer.
     */
    public void delete() {
        if (this.deleteConsumer != null)
            this.deleteConsumer.accept(this);
        this.deleted = true;
    }

    /**
     * Checks player can see the location.
     *
     * @param uid UID of player.
     * @return If player can see the location, returns true.
     */
    public boolean canSee(@Nonnull UUID uid) {
        Player player = Bukkit.getPlayer(Objects.requireNonNull(uid, "uid cannot be null!"));
        if (player == null)
            return false;
        else if (this.calculateDistance(player.getLocation()) > this.radius)
            return false;
        return true;
    }

    /**
     * Calculates distance between center
     * and target.
     *
     * @param target Target location.
     * @return Distance as double.
     */
    public double calculateDistance(@Nonnull Location target) {
        Validate.notNull(target, "target location cannot be null!");

        double xDis = Math.pow(target.getX() - this.location.getX(), 2);
        double zDis = Math.pow(target.getZ() - this.location.getZ(), 2);
        return (this.useYAxis) ? Math.sqrt(xDis + Math.pow(target.getY() - this.location.getY(), 2) + zDis) : Math.sqrt(xDis + zDis);
    }

    /**
     * Calculates viewers.
     *
     * @return Viewers.
     */
    @Nonnull
    public Set<UUID> calculateViewers() {
        if (!this.showEveryone)
            return this.viewers;

        Set<UUID> viewers = new HashSet<>();
        this.location.getWorld().getPlayers()
                .forEach(player -> viewers.add(player.getUniqueId()));
        return viewers;
    }

    /**
     * Renders the players.
     *
     * @return This class.
     */
    @Nonnull
    public HRenderer render() {
        if (this.deleted)
            return this;

        Set<UUID> viewers = this.calculateViewers();
        Set<UUID> newShown = new HashSet<>();
        Set<UUID> oldShown = this.shownViewers;

        for (UUID uid : viewers)
            if (this.canSee(uid))
                newShown.add(uid);

        if (this.hideConsumer != null) {
            List<Player> hide = new ArrayList<>();
            for (UUID uid : newShown)
                if (!oldShown.contains(uid))
                    hide.add(Bukkit.getPlayer(uid));
            this.hideConsumer.accept(hide);
        }

        if (this.showConsumer != null) {
            List<Player> show = new ArrayList<>();
            for (UUID uid : oldShown)
                if (!newShown.contains(uid))
                    show.add(Bukkit.getPlayer(uid));
            this.showConsumer.accept(show);
        }

        this.shownViewers = newShown;
        return this;
    }
}