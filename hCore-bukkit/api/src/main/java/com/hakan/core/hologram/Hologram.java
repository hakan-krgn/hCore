package com.hakan.core.hologram;

import com.hakan.core.HCore;
import com.hakan.core.hologram.action.HologramAction;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.hologram.line.empty.EmptyLine;
import com.hakan.core.hologram.line.item.ItemLine;
import com.hakan.core.hologram.line.text.TextLine;
import com.hakan.core.renderer.Renderer;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Hologram class to create and
 * manage the created hologram.
 */
@SuppressWarnings({"unused", "unchecked", "UnusedReturnValue"})
public final class Hologram {

    private final String id;
    private final Renderer renderer;
    private final HologramAction action;
    private final List<HologramLine> lines;
    private double lineDistance;

    /**
     * Creates new instance of this class.
     *
     * @param id           Hologram id that you want.
     * @param location     Hologram location.
     * @param lineDistance Line distance.
     * @param showEveryone Show everyone.
     * @param playerList   List of player who can see hologram.
     */
    public Hologram(@Nonnull String id,
                    @Nonnull Location location,
                    @Nonnull Set<UUID> playerList,
                    boolean showEveryone,
                    double lineDistance) {
        Validate.notNull(id, "id cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(playerList, "player list cannot be null!");

        this.id = id;
        this.lines = new LinkedList<>();
        this.lineDistance = lineDistance;
        this.action = new HologramAction(this);
        this.renderer = new Renderer(location, 30, playerList,
                players -> this.lines.forEach(line -> line.show(players)),
                players -> this.lines.forEach(line -> line.hide(players)),
                renderer -> this.lines.forEach(line -> line.hide(renderer.getShownPlayers())));

        this.renderer.showEveryone(showEveryone);
        this.renderer.render();
        this.action.onSpawn();
    }

    /**
     * Is hologram exist?
     *
     * @return If hologram exist, returns true.
     */
    public boolean isExist() {
        return HologramHandler.getContent().containsKey(this.id);
    }

    /**
     * Gets id of hologram.
     *
     * @return ID of hologram.
     */
    @Nonnull
    public String getId() {
        return this.id;
    }

    /**
     * Gets location of hologram.
     *
     * @return Location of hologram.
     */
    @Nonnull
    public Location getLocation() {
        return this.renderer.getLocation();
    }

    /**
     * Gets hologram action.
     *
     * @return Hologram action.
     */
    @Nonnull
    public HologramAction getAction() {
        return this.action;
    }

    /**
     * Gets distance between two lines of hologram.
     *
     * @return Distance between two lines of hologram.
     */
    public double getLineDistance() {
        return this.lineDistance;
    }

    /**
     * Sets distance between two lines.
     *
     * @param lineDistance Distance between two lines.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram setLineDistance(double lineDistance) {
        this.lineDistance = lineDistance;
        return this.setLocation(this.getLocation());
    }

    /**
     * Gets renderer.
     *
     * @return renderer.
     */
    @Nonnull
    public Renderer getRenderer() {
        return this.renderer;
    }

    /**
     * Checks everyone can
     * see the hologram.
     *
     * @return If everyone can see
     * hologram, returns true.
     */
    public boolean canEveryoneSee() {
        return this.renderer.canEveryoneSee();
    }

    /**
     * If set this as true, everyone
     * can see this hologram.
     *
     * @param mode Mode status.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram showEveryone(boolean mode) {
        this.renderer.showEveryone(mode);
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
    public Hologram whenSpawned(@Nonnull Consumer<Hologram> consumer) {
        this.action.whenSpawned(consumer);
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
    public Hologram whenDeleted(@Nonnull Consumer<Hologram> consumer) {
        this.action.whenDeleted(consumer);
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
    public Hologram whenClicked(@Nonnull BiConsumer<Player, HologramLine> consumer) {
        this.action.whenClicked(consumer);
        return this;
    }

    /**
     * Gets hologram lines.
     *
     * @return Hologram lines.
     */
    @Nonnull
    public List<HologramLine> getLines() {
        return new LinkedList<>(this.lines);
    }

    /**
     * Gets hologram line from index.
     *
     * @param index Index.
     * @return HologramLine class.
     */
    @Nonnull
    public <T extends HologramLine> T getLine(int index) {
        Validate.isTrue((index > this.lines.size() || index < 0), "index cannot bigger line size or smaller than 0: " + index);
        return (T) this.lines.get(index);
    }

    /**
     * Gets hologram line from index.
     *
     * @param index  Index.
     * @param tClass Class of hologram line.
     * @param <T>    Type of hologram line class.
     * @return HologramLine class.
     */
    @Nonnull
    public <T extends HologramLine> T getLine(int index, @Nonnull Class<T> tClass) {
        return tClass.cast(this.getLine(index));
    }

    /**
     * Checks hologram line is
     * exist by entity id.
     *
     * @param entityID Entity ID.
     * @return If hologram line is exist, returns true.
     */
    public boolean hasLineByEntityID(int entityID) {
        return this.getLineByEntityID(entityID) != null;
    }

    /**
     * Gets hologram line from entity id.
     *
     * @param entityID Entity ID.
     * @param <T>      Type of hologram line class.
     * @return HologramLine class.
     */
    @Nullable
    public <T extends HologramLine> T getLineByEntityID(int entityID) {
        return (T) this.lines.stream().filter(line -> line.getEntityID() == entityID)
                .findFirst().orElse(null);
    }

    /**
     * Gets hologram line from entity id.
     *
     * @param entityID Entity ID.
     * @param tClass   Class of hologram line.
     * @param <T>      Type of hologram line class.
     * @return HologramLine class.
     */
    @Nullable
    public <T extends HologramLine> T getLineByEntityID(int entityID, @Nonnull Class<T> tClass) {
        return tClass.cast(this.lines.stream().filter(line -> line.getEntityID() == entityID)
                .findFirst().orElse(null));
    }

    /**
     * Hologram expires after a certain time.
     *
     * @param expire   Amount.
     * @param timeUnit Time Unit.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram expire(int expire, @Nonnull TimeUnit timeUnit) {
        Validate.notNull(timeUnit, "time unit cannot be null!");
        HCore.syncScheduler().after(expire, timeUnit).run(this::delete);
        return this;
    }

    /**
     * Hologram expires after a certain time.
     *
     * @param duration Duration.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram expire(@Nonnull Duration duration) {
        Validate.notNull(duration, "duration cannot be null!");
        HCore.syncScheduler().after(duration).run(this::delete);
        return this;
    }

    /**
     * If you want to remove the hologram
     * when the time is up. You can use this.
     *
     * @param ticks Time. (as tick)
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram expire(long ticks) {
        HCore.syncScheduler().after(ticks)
                .run(this::delete);
        return this;
    }

    /**
     * Adds player to hologram to show.
     *
     * @param players Player list.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram addViewer(@Nonnull Collection<Player> players) {
        Validate.notNull(players, "players cannot be null!");
        players.forEach(player -> this.renderer.addViewer(player.getUniqueId()));
        return this;
    }

    /**
     * Adds player to hologram to show.
     *
     * @param players Player list.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram addViewer(@Nonnull Player... players) {
        Validate.notNull(players, "players cannot be null!");
        Arrays.asList(players).forEach(this.renderer::addViewer);
        return this;
    }

    /**
     * Adds player to hologram to show.
     *
     * @param uids UID list.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram addViewerByUID(@Nonnull Collection<UUID> uids) {
        Validate.notNull(uids, "uuids cannot be null!");
        uids.forEach(this.renderer::addViewer);
        return this;
    }

    /**
     * Adds player to hologram to show.
     *
     * @param uids UID list.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram addViewerByUID(@Nonnull UUID... uids) {
        Validate.notNull(uids, "uuids cannot be null!");
        Arrays.asList(uids).forEach(this.renderer::addViewer);
        return this;
    }

    /**
     * Removes player from hologram to hide.
     *
     * @param players Player list.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram removeViewer(@Nonnull Collection<Player> players) {
        Validate.notNull(players, "players cannot be null!");
        players.forEach(player -> this.renderer.removeViewer(player.getUniqueId()));
        return this;
    }

    /**
     * Removes player from hologram to hide.
     *
     * @param players Player list.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram removeViewer(@Nonnull Player... players) {
        Validate.notNull(players, "players cannot be null!");
        Arrays.asList(players).forEach(this.renderer::removeViewer);
        return this;
    }

    /**
     * Removes player from hologram to hide.
     *
     * @param uids UID list.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram removeViewerByUID(@Nonnull Collection<UUID> uids) {
        Validate.notNull(uids, "players cannot be null!");
        uids.forEach(this.renderer::removeViewer);
        return this;
    }

    /**
     * Removes player from hologram to hide.
     *
     * @param uids UID list.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram removeViewerByUID(@Nonnull UUID... uids) {
        Validate.notNull(uids, "players cannot be null!");
        Arrays.asList(uids).forEach(this.renderer::removeViewer);
        return this;
    }

    /**
     * Sets location of hologram.
     *
     * @param location Location to teleport.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram setLocation(@Nonnull Location location) {
        this.renderer.setLocation(Validate.notNull(location, "location cannot be null!"));
        for (int i = 0; i < this.lines.size(); i++)
            this.lines.get(i).setLocation(this.calculateLocation(i));
        return this;
    }

    /**
     * Removes line from hologram.
     *
     * @param index Index.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram removeLine(int index) {
        HologramLine line = this.lines.remove(index);
        line.hide(this.renderer.getShownPlayers());
        return this.setLocation(this.getLocation());
    }

    /**
     * Removes line from hologram.
     *
     * @param indexes Indexes.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram removeLines(@Nonnull Collection<Integer> indexes) {
        Validate.notNull(indexes, "indexes cannot be null!");
        indexes.forEach(this::removeLine);
        return this;
    }

    /**
     * Removes line from hologram.
     *
     * @param indexes Indexes.
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram removeLines(int... indexes) {
        for (int index : indexes)
            this.removeLine(index);
        return this;
    }

    /**
     * Clear lines from hologram.
     *
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram clearLines() {
        for (int i = this.lines.size() - 1; i >= 0; i--)
            this.removeLine(i);
        return this;
    }

    /**
     * Adds new line to hologram.
     *
     * @param value Value to add.
     * @return Instance of this class.
     */
    @Nonnull
    public <T> Hologram addLine(@Nullable T value) {
        return this.insertLine(this.lines.size(), value);
    }

    /**
     * Adds new lines to hologram.
     *
     * @param lines HologramLine classes.
     * @return Instance of this class.
     */
    @Nonnull
    public <T> Hologram addLines(@Nonnull T... lines) {
        Validate.notNull(lines, "lines cannot be null!");
        Arrays.asList(lines).forEach(this::addLine);
        return this;
    }

    /**
     * Adds new lines to hologram.
     *
     * @param lines HologramLine classes.
     * @return Instance of this class.
     */
    @Nonnull
    public <T> Hologram addLines(@Nonnull Collection<T> lines) {
        Validate.notNull(lines, "lines cannot be null!");
        lines.forEach(this::addLine);
        return this;
    }

    /**
     * Inserts the text into given line.
     *
     * @param index Line to insert into.
     * @param value Value to insert.
     * @return Instance of this class.
     */
    @Nonnull
    public <T> Hologram insertLine(int index, @Nullable T value) {
        if (value instanceof HologramLine) {
            HologramLine line = (HologramLine) value;

            this.lines.add(index, line);
            line.show(this.renderer.getShownPlayers());
            this.setLocation(this.getLocation());
        } else if (value instanceof String || value instanceof ItemStack || value == null) {
            Location location = this.calculateLocation(this.lines.size());
            this.insertLine(index, HologramLine.create(this, location, value));
        } else {
            throw new IllegalArgumentException("value must be HologramLine, String, ItemStack or null!");
        }

        return this;
    }

    /**
     * Sets lines with given lines.
     *
     * @param lines New lines.
     * @return Instance of this class.
     */
    @Nonnull
    public <T> Hologram setLines(@Nonnull Collection<T> lines) {
        Validate.notNull(lines, "lines cannot be null!");
        return this.setLines(lines.toArray());
    }

    /**
     * Sets lines with given lines.
     *
     * @param lines New lines.
     * @return Instance of this class.
     */
    @Nonnull
    public <T> Hologram setLines(@Nonnull T... lines) {
        Validate.notNull(lines, "lines cannot be null!");
        Validate.isTrue(lines.length == 0, "lines cannot be empty!");

        if (lines.length == this.lines.size()) {
            for (int i = 0; i < lines.length; i++)
                this.setLine(i, lines[i]);
        } else if (lines.length > this.lines.size()) {
            for (int i = 0; i < this.lines.size(); i++)
                this.setLine(i, lines[i]);
            for (int i = this.lines.size(); i < lines.length; i++)
                this.addLine(lines[i]);
            this.setLocation(this.getLocation());
        } else {
            for (int i = this.lines.size() - 1; i >= lines.length; i--)
                this.removeLine(i);
            for (int i = 0; i < lines.length; i++)
                this.setLine(i, lines[i]);
            this.setLocation(this.getLocation());
        }

        return this;
    }

    /**
     * Sets the value to given line by index
     *
     * @param index Index of line.
     * @param value Value to set.
     * @return Instance of this class.
     */
    @Nonnull
    public <T> Hologram setLine(int index, @Nullable T value) {
        if (value instanceof HologramLine) {
            HologramLine line = (HologramLine) value;
            HologramLine old = this.lines.set(index, line);
            old.hide(this.renderer.getShownPlayers());
            line.show(this.renderer.getShownPlayers());

            this.setLocation(this.getLocation());
        } else if (value instanceof String) {
            HologramLine old = this.lines.get(index);
            if (old instanceof TextLine) {
                TextLine textLine = (TextLine) old;
                textLine.setText((String) value);
            } else {
                Location location = this.calculateLocation(index);
                this.setLine(index, HologramLine.create(this, location, value));
            }
        } else if (value instanceof ItemStack) {
            HologramLine old = this.lines.get(index);
            if (old instanceof ItemLine) {
                ItemLine itemLine = (ItemLine) old;
                itemLine.setItem((ItemStack) value);
            } else {
                Location location = this.calculateLocation(index);
                this.setLine(index, HologramLine.create(this, location, value));
            }
        } else if (value == null) {
            this.setLine(index, EmptyLine.create(this));
        } else {
            throw new IllegalArgumentException("value must be HologramLine, String, ItemStack or null!");
        }

        return this;
    }

    /**
     * Deletes hologram.
     *
     * @return Instance of this class.
     */
    @Nonnull
    public Hologram delete() {
        if (this.isExist()) {
            HologramHandler.getContent().remove(this.id);
            this.action.onDelete();
            this.renderer.delete();
            this.lines.clear();
        }
        return this;
    }



    /**
     * Calculates hologram line location by index.
     *
     * @param index Index of line.
     * @return Location of line.
     */
    @Nonnull
    private Location calculateLocation(int index) {
        Location startLoc = this.getLocation().add(0, ((this.lines.size() - 1) * this.lineDistance + 0.24) / 2.0 - 0.28, 0);
        return startLoc.subtract(0, index * this.lineDistance + 0.24, 0);
    }
}
