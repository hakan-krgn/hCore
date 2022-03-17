package com.hakan.core.renderer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;

public class HRenderer {

    private double radius;
    private boolean deleted;
    private boolean showEveryone;
    private boolean useYAxis;
    private Location location;
    private Set<UUID> viewers;
    private Consumer<List<Player>> showConsumer;
    private Consumer<List<Player>> hideConsumer;
    private Consumer<HRenderer> deleteConsumer;
    private final Set<UUID> shownViewers;

    public HRenderer(Location location, double radius, Set<UUID> viewers,
                     Consumer<List<Player>> showConsumer,
                     Consumer<List<Player>> hideConsumer,
                     Consumer<HRenderer> deleteConsumer) {
        this.location = location;
        this.radius = radius;
        this.viewers = viewers;
        this.shownViewers = new HashSet<>();
        this.showEveryone = false;
        this.deleted = false;
        this.useYAxis = true;
        this.showConsumer = showConsumer;
        this.hideConsumer = hideConsumer;
        this.deleteConsumer = deleteConsumer;
    }

    public HRenderer(Location location, double radius,
                     Consumer<List<Player>> showConsumer,
                     Consumer<List<Player>> hideConsumer,
                     Consumer<HRenderer> deleteConsumer) {
        this(location, radius, new HashSet<>(), showConsumer, hideConsumer, deleteConsumer);
        this.showEveryone = true;
    }

    public double getRadius() {
        return this.radius;
    }

    public HRenderer setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public boolean isUseYAxis() {
        return this.useYAxis;
    }

    public HRenderer setUseYAxis(boolean useYAxis) {
        this.useYAxis = useYAxis;
        return this;
    }

    public boolean canShowEveryone() {
        return this.showEveryone;
    }

    public HRenderer showEveryone(boolean showEveryone) {
        this.showEveryone = showEveryone;
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public HRenderer setLocation(Location location) {
        this.location = location;
        return this;
    }

    public Set<UUID> getShownViewers() {
        return new HashSet<>(this.shownViewers);
    }

    public List<Player> getShownViewersAsPlayer() {
        List<Player> players = new ArrayList<>();
        this.shownViewers.forEach(uid -> {
            Player player = Bukkit.getPlayer(uid);
            if (player != null) players.add(player);
        });
        return players;
    }

    public Set<UUID> getViewers() {
        return this.viewers;
    }

    public HRenderer setViewers(Set<UUID> viewers) {
        this.viewers = viewers;
        return this;
    }

    public HRenderer setViewers(List<Player> viewers) {
        this.viewers.clear();
        viewers.forEach(player -> this.viewers.add(player.getUniqueId()));
        return this;
    }

    public HRenderer addViewer(UUID uid) {
        this.viewers.add(uid);
        return this;
    }

    public HRenderer removeViewer(UUID uid) {
        this.viewers.remove(uid);
        return this;
    }

    public HRenderer addViewer(Player player) {
        return this.addViewer(player.getUniqueId());
    }

    public HRenderer removeViewer(Player player) {
        return this.removeViewer(player.getUniqueId());
    }

    public void setShowConsumer(Consumer<List<Player>> showConsumer) {
        this.showConsumer = showConsumer;
    }

    public void setHideConsumer(Consumer<List<Player>> hideConsumer) {
        this.hideConsumer = hideConsumer;
    }

    public void setDeleteConsumer(Consumer<HRenderer> deleteConsumer) {
        this.deleteConsumer = deleteConsumer;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void delete() {
        if (this.deleteConsumer != null)
            this.deleteConsumer.accept(this);
        this.deleted = true;
    }

    public double calculateDistance(Location target) {
        double xDis = Math.pow(target.getX() - this.location.getX(), 2);
        double zDis = Math.pow(target.getZ() - this.location.getZ(), 2);
        return (this.useYAxis) ? Math.sqrt(xDis + Math.pow(target.getY() - this.location.getY(), 2) + zDis) : Math.sqrt(xDis + zDis);
    }

    public Set<UUID> calculateViewers() {
        if (!this.showEveryone)
            return this.viewers;

        Set<UUID> viewers = new HashSet<>();
        this.location.getWorld().getPlayers()
                .forEach(player -> viewers.add(player.getUniqueId()));
        return viewers;
    }

    public HRenderer render() {
        if (this.deleted)
            return this;

        List<Player> addShown = new ArrayList<>();
        List<Player> removedShown = new ArrayList<>();
        Set<UUID> viewers = this.calculateViewers();

        if (this.hideConsumer != null) {
            new ArrayList<>(this.shownViewers).forEach(uid -> {
                Player player = Bukkit.getPlayer(uid);

                if (player == null) {
                    this.shownViewers.remove(uid);
                } else if (this.calculateDistance(player.getLocation()) > this.radius) {
                    this.shownViewers.remove(uid);
                    removedShown.add(player);
                }
            });

            if (!removedShown.isEmpty())
                this.hideConsumer.accept(removedShown);
        }

        viewers.forEach(uid -> {
            Player player = Bukkit.getPlayer(uid);
            if (player == null)
                return;
            else if (this.calculateDistance(player.getLocation()) > this.radius)
                return;
            else if (this.shownViewers.contains(uid))
                return;

            addShown.add(player);
            this.shownViewers.add(uid);
        });

        if (this.showConsumer != null && !addShown.isEmpty())
            this.showConsumer.accept(addShown);
        return this;
    }
}