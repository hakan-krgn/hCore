package com.hakan.core.renderer;

import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Renderer class.
 * This class helps renders the players.
 * returns players in radius.
 */
public final class Renderer {

    private double radius;
    private boolean deleted;
    private boolean showEveryone;
    private boolean useYAxis;
    private Location location;
    private Set<UUID> viewers;
    private Set<UUID> shownViewers;

    private final Consumer<List<Player>> showConsumer;
    private final Consumer<List<Player>> hideConsumer;
    private final Consumer<Renderer> deleteConsumer;

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
    public Renderer(@Nonnull Location location, double radius,
                    @Nonnull Set<UUID> viewers,
                    @Nonnull Consumer<List<Player>> showConsumer,
                    @Nonnull Consumer<List<Player>> hideConsumer,
                    @Nonnull Consumer<Renderer> deleteConsumer) {
        this.location = Validate.notNull(location, "location cannot be null!");
        this.viewers = Validate.notNull(viewers, "viewers cannot be null!");
        this.showConsumer = Validate.notNull(showConsumer, "show consumer cannot be null!");
        this.hideConsumer = Validate.notNull(hideConsumer, "hide consumer cannot be null!");
        this.deleteConsumer = Validate.notNull(deleteConsumer, "delete consumer cannot be null!");
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
    public Renderer(@Nonnull Location location, double radius,
                    @Nonnull Consumer<List<Player>> showConsumer,
                    @Nonnull Consumer<List<Player>> hideConsumer,
                    @Nonnull Consumer<Renderer> deleteConsumer) {
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
    public Renderer setRadius(double radius) {
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
    public Renderer setUseYAxis(boolean useYAxis) {
        this.useYAxis = useYAxis;
        return this;
    }

    /**
     * Can everyone includes this renderer.
     *
     * @return If includes, returns true.
     */
    public boolean canEveryoneSee() {
        return this.showEveryone;
    }

    /**
     * Everyone includes this renderer.
     *
     * @param showEveryone Show mode.
     * @return This class.
     */
    @Nonnull
    public Renderer showEveryone(boolean showEveryone) {
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
        return this.location.clone();
    }

    /**
     * Sets center location.
     *
     * @param location Center location.
     * @return This class.
     */
    @Nonnull
    public Renderer setLocation(@Nonnull Location location) {
        this.location = Validate.notNull(location, "location cannot be null!");
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
    public List<Player> getShownPlayers() {
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
    public Renderer setViewers(@Nonnull Set<UUID> viewers) {
        this.viewers = Validate.notNull(viewers, "viewers cannot be null!");
        return this;
    }

    /**
     * Set viewers.
     *
     * @param viewers Viewers.
     * @return This class.
     */
    @Nonnull
    public Renderer setViewers(@Nonnull List<Player> viewers) {
        this.viewers.clear();
        Validate.notNull(viewers, "viewers cannot be null!")
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
    public Renderer addViewer(@Nonnull UUID uid) {
        this.viewers.add(Validate.notNull(uid, "uid cannot be null!"));
        return this;
    }

    /**
     * Removes viewer from renderer.
     *
     * @param uid UID of player.
     * @return This class.
     */
    @Nonnull
    public Renderer removeViewer(@Nonnull UUID uid) {
        this.viewers.remove(Validate.notNull(uid, "uid cannot be null!"));
        return this;
    }

    /**
     * Adds viewer to renderer.
     *
     * @param player Player.
     * @return This class.
     */
    @Nonnull
    public Renderer addViewer(@Nonnull Player player) {
        return this.addViewer(Validate.notNull(player, "player cannot be null!").getUniqueId());
    }

    /**
     * Removes viewer from renderer.
     *
     * @param player Player.
     * @return This class.
     */
    @Nonnull
    public Renderer removeViewer(@Nonnull Player player) {
        return this.removeViewer(Validate.notNull(player, "player cannot be null!").getUniqueId());
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
    public Renderer delete() {
        if (this.deleteConsumer != null)
            this.deleteConsumer.accept(this);
        this.deleted = true;
        return this;
    }

    /**
     * Checks player can see the location.
     *
     * @param uid UID of player.
     * @return If player can see the location, returns true.
     */
    public boolean canSee(@Nonnull UUID uid) {
        Player player = Bukkit.getPlayer(Validate.notNull(uid, "uid cannot be null!"));
        if (player == null)
            return false;

        double distance = this.calculateDistance(player.getLocation());
        if (distance == -1)
            return false;

        return !(distance > this.radius);
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
        Validate.notNull(target.getWorld(), "target world cannot be null!");

        if (!target.getWorld().equals(this.location.getWorld()))
            return -1;

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
    public Renderer render() {
        if (this.deleted)
            return this;

        List<UUID> viewers = new ArrayList<>(this.calculateViewers());
        List<UUID> oldShown = new ArrayList<>(this.shownViewers);
        List<UUID> newShown = new ArrayList<>();

        viewers.forEach(uid -> {
            if (this.canSee(uid))
                newShown.add(uid);
        });

        if (this.hideConsumer != null) {
            List<Player> hide = new ArrayList<>();

            oldShown.forEach(uid -> {
                if (!newShown.contains(uid)) {
                    Player player = Bukkit.getPlayer(uid);
                    if (player != null) hide.add(Bukkit.getPlayer(uid));
                }
            });

            if (hide.size() > 0)
                this.hideConsumer.accept(hide);
        }

        if (this.showConsumer != null) {
            List<Player> show = new ArrayList<>();

            newShown.forEach(uid -> {
                if (!oldShown.contains(uid)) {
                    show.add(Bukkit.getPlayer(uid));
                }
            });

            if (show.size() > 0)
                this.showConsumer.accept(show);
        }

        this.shownViewers = new HashSet<>(newShown);
        return this;
    }
}
