package com.hakan.core.hologram;

import com.hakan.core.HCore;
import com.hakan.core.hologram.action.HHologramAction;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.hologram.line.empty.EmptyLine;
import com.hakan.core.hologram.line.item.ItemLine;
import com.hakan.core.hologram.line.text.TextLine;
import com.hakan.core.renderer.HRenderer;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
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
public final class HHologram {

    private final String id;
    private final HRenderer renderer;
    private final HHologramAction action;
    private final List<HologramLine> lines;
    private double lineDistance = 0.25;

    /**
     * Creates new instance of this class.
     *
     * @param id         Hologram id that you want.
     * @param location   Hologram location.
     * @param playerList List of player who can see hologram.
     */
    HHologram(@Nonnull String id, @Nonnull Location location, @Nonnull Set<UUID> playerList) {
        Validate.notNull(id, "id cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(playerList, "player list cannot be null!");

        this.id = id;
        this.lines = new LinkedList<>();
        this.action = new HHologramAction(this);
        this.renderer = new HRenderer(location, 30, playerList,
                players -> this.lines.forEach(line -> line.show(players)),
                players -> this.lines.forEach(line -> line.hide(players)),
                renderer -> this.lines.forEach(line -> line.hide(renderer.getShownViewersAsPlayer())));

        this.renderer.showEveryone(false);
        this.renderer.render();
        this.action.onSpawn();
    }

    /**
     * Creates new instance of this class.
     *
     * @param id       Hologram id that you want.
     * @param location Hologram location.
     */
    HHologram(@Nonnull String id, @Nonnull Location location) {
        Validate.notNull(id, "id cannot be null!");
        Validate.notNull(location, "location cannot be null!");

        this.id = id;
        this.lines = new LinkedList<>();
        this.action = new HHologramAction(this);
        this.renderer = new HRenderer(location, 30,
                players -> this.lines.forEach(line -> line.show(players)),
                players -> this.lines.forEach(line -> line.hide(players)),
                renderer -> this.lines.forEach(line -> line.hide(renderer.getShownViewersAsPlayer())));

        this.renderer.showEveryone(true);
        this.renderer.render();
        this.action.onSpawn();
    }

    /**
     * Is hologram exist?
     *
     * @return If hologram exist, returns true.
     */
    public boolean isExist() {
        return HHologramHandler.getContent().containsKey(this.id);
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
    public HHologramAction getAction() {
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
    public HHologram setLineDistance(double lineDistance) {
        this.lineDistance = lineDistance;
        return this.setLocation(this.getLocation());
    }

    /**
     * Gets renderer.
     *
     * @return renderer.
     */
    @Nonnull
    public HRenderer getRenderer() {
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
    public HHologram showEveryone(boolean mode) {
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
    public HHologram whenSpawned(@Nonnull Consumer<HHologram> consumer) {
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
    public HHologram whenDeleted(@Nonnull Consumer<HHologram> consumer) {
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
    public HHologram whenClicked(@Nonnull BiConsumer<Player, HologramLine> consumer) {
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
        return (T) this.lines.stream().filter(line -> line.getClickableEntityID() == entityID)
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
        return tClass.cast(this.lines.stream().filter(line -> line.getClickableEntityID() == entityID)
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
    public HHologram expire(int expire, @Nonnull TimeUnit timeUnit) {
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
    public HHologram expire(@Nonnull Duration duration) {
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
    public HHologram expire(long ticks) {
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
    public HHologram addViewer(@Nonnull Collection<Player> players) {
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
    public HHologram addViewer(@Nonnull Player... players) {
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
    public HHologram addViewerByUID(@Nonnull Collection<UUID> uids) {
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
    public HHologram addViewerByUID(@Nonnull UUID... uids) {
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
    public HHologram removeViewer(@Nonnull Collection<Player> players) {
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
    public HHologram removeViewer(@Nonnull Player... players) {
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
    public HHologram removeViewerByUID(@Nonnull Collection<UUID> uids) {
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
    public HHologram removeViewerByUID(@Nonnull UUID... uids) {
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
    public HHologram setLocation(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");
        Location loc = location.clone().add(0, (this.lineDistance / 2) * (this.lines.size() + 2), 0);
        this.renderer.setLocation(location);
        this.lines.forEach(line -> line.setLocation(loc.subtract(0, this.lineDistance, 0)));
        return this;
    }

    /**
     * Inserts the text into given line.
     *
     * @param index Line to insert into.
     * @param line  Line text.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram insertLine(int index, @Nullable HologramLine line) {
        line = (line == null) ? EmptyLine.create(this) : line;

        this.lines.add(index, line);
        line.show(this.renderer.getShownViewersAsPlayer());
        return this.setLocation(this.getLocation());
    }

    /**
     * Inserts the text into given line.
     *
     * @param index Line to insert into.
     * @param line  Line text.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram insertTextLine(int index, @Nullable String line) {
        return this.insertLine(index, HologramLine.create(this, line));
    }

    /**
     * Inserts the text into given line.
     *
     * @param index     Line to insert into.
     * @param itemStack ItemStack to insert.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram insertItemLine(int index, @Nullable ItemStack itemStack) {
        return this.insertLine(index, HologramLine.create(this, itemStack));
    }

    /**
     * Adds new line to hologram.
     *
     * @param line HologramLine class.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram addLine(@Nullable HologramLine line) {
        line = (line == null) ? EmptyLine.create(this) : line;

        this.lines.add(line);
        line.show(this.renderer.getShownViewersAsPlayer());
        return this.setLocation(this.getLocation());
    }

    /**
     * Adds new text line to hologram.
     *
     * @param text Text.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram addTextLine(@Nullable String text) {
        return this.addLine(HologramLine.create(this, text));
    }

    /**
     * Adds new text line to hologram.
     *
     * @param itemStack ItemStack to insert.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram addItemLine(@Nullable ItemStack itemStack) {
        return this.addLine(HologramLine.create(this, itemStack));
    }

    /**
     * Adds new lines to hologram.
     *
     * @param lines HologramLine classes.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram addLines(@Nonnull HologramLine... lines) {
        Validate.notNull(lines, "lines cannot be null!");
        Arrays.asList(lines).forEach(this::addLine);
        return this;
    }

    /**
     * Adds new lines to hologram.
     *
     * @param texts Text.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram addTextLines(@Nonnull Collection<String> texts) {
        Validate.notNull(texts, "texts cannot be null!");
        texts.forEach(this::addTextLine);
        return this;
    }

    /**
     * Adds new lines to hologram.
     *
     * @param texts text.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram addTextLines(@Nonnull String... texts) {
        Validate.notNull(texts, "texts cannot be null!");
        Arrays.asList(texts).forEach(this::addTextLine);
        return this;
    }

    /**
     * Adds new lines to hologram.
     *
     * @param itemStacks ItemStacks to insert.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram addItemLines(@Nonnull Collection<ItemStack> itemStacks) {
        Validate.notNull(itemStacks, "item stacks cannot be null!");
        itemStacks.forEach(this::addItemLine);
        return this;
    }

    /**
     * Adds new lines to hologram.
     *
     * @param itemStacks text.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram addItemLines(@Nonnull ItemStack... itemStacks) {
        Validate.notNull(itemStacks, "item stacks cannot be null!");
        Arrays.asList(itemStacks).forEach(this::addItemLine);
        return this;
    }

    /**
     * Replaces line at row with text.
     *
     * @param row  Index of line.
     * @param line HologramLine class.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram setLine(int row, @Nullable HologramLine line) {
        HologramLine old = this.lines.get(row);
        if (old instanceof TextLine && line instanceof TextLine) {
            TextLine oldTextLine = (TextLine) old;
            TextLine newTextLine = (TextLine) line;
            oldTextLine.setText(newTextLine.getText());
        } else if (old instanceof ItemLine && line instanceof ItemLine) {
            ItemLine oldItemLine = (ItemLine) old;
            ItemLine newItemLine = (ItemLine) line;
            oldItemLine.setItem(newItemLine.getItem());
        } else {
            line = (line == null) ? EmptyLine.create(this) : line;

            this.lines.set(row, line);
            old.hide(this.renderer.getShownViewersAsPlayer());
            line.show(this.renderer.getShownViewersAsPlayer());
            this.setLocation(this.getLocation());
        }
        return this;
    }

    /**
     * Replaces line at row with text.
     *
     * @param row  Index of line.
     * @param text Text.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram setTextLine(int row, @Nullable String text) {
        return this.setLine(row, HologramLine.create(this, text));
    }

    /**
     * Replaces line at row with text.
     *
     * @param row       Index of line.
     * @param itemStack ItemStack to insert.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram setItemLine(int row, @Nullable ItemStack itemStack) {
        return this.setLine(row, HologramLine.create(this, itemStack));
    }

    /**
     * Replaces line at row with text.
     *
     * @param lines New lines.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram setLines(@Nonnull List<HologramLine> lines) {
        Validate.notNull(lines, "lines cannot be null!");
        Validate.isTrue(lines.isEmpty(), "lines cannot be empty!");

        if (lines.size() == this.lines.size()) {
            for (int i = 0; i < lines.size(); i++)
                this.setLine(i, lines.get(i));
        } else if (lines.size() > this.lines.size()) {
            for (int i = this.lines.size(); i < lines.size(); i++)
                this.addLine(lines.get(i));
            for (int i = 0; i < this.lines.size(); i++)
                this.setLine(i, lines.get(i));
            this.setLocation(this.getLocation());
        } else {
            for (int i = this.lines.size() - 1; i >= lines.size(); i--)
                this.removeLine(i);
            for (int i = 0; i < lines.size(); i++)
                this.setLine(i, lines.get(i));
            this.setLocation(this.getLocation());
        }

        return this;
    }

    /**
     * Replaces line at row with text.
     *
     * @param lines New lines.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram setLines(@Nonnull HologramLine... lines) {
        Validate.notNull(lines, "lines cannot be null!");
        return this.setLines(Arrays.asList(lines));
    }

    /**
     * Replaces line at row with text.
     *
     * @param texts New texts.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram setTextLines(@Nonnull List<String> texts) {
        Validate.notNull(texts, "texts cannot be null!");
        Validate.isTrue(texts.isEmpty(), "texts cannot be empty!");

        List<HologramLine> hologramLines = new ArrayList<>();
        for (String line : texts)
            hologramLines.add(HologramLine.create(this, line));
        return this.setLines(hologramLines);
    }

    /**
     * Replaces line at row with text.
     *
     * @param texts New lines.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram setTextLines(@Nonnull String... texts) {
        Validate.notNull(texts, "texts cannot be null!");
        return this.setTextLines(Arrays.asList(texts));
    }

    /**
     * Replaces line at row with item.
     *
     * @param itemStacks New lines.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram setItemLines(@Nonnull List<ItemStack> itemStacks) {
        Validate.notNull(itemStacks, "item stacks cannot be null!");
        Validate.isTrue(itemStacks.isEmpty(), "item stacks cannot be empty!");

        List<HologramLine> hologramLines = new ArrayList<>();
        for (ItemStack itemStack : itemStacks)
            hologramLines.add(HologramLine.create(this, itemStack));
        return this.setLines(hologramLines);
    }

    /**
     * Replaces line at row with item.
     *
     * @param itemStacks New itemStacks.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram setItemLines(@Nonnull ItemStack... itemStacks) {
        Validate.notNull(itemStacks, "item stacks cannot be null!");
        return this.setItemLines(Arrays.asList(itemStacks));
    }

    /**
     * Removes line from hologram.
     *
     * @param index Index.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram removeLine(int index) {
        HologramLine line = this.lines.remove(index);
        line.hide(this.renderer.getShownViewersAsPlayer());
        return this.setLocation(this.getLocation());
    }

    /**
     * Removes line from hologram.
     *
     * @param indexes Indexes.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram removeLines(@Nonnull Collection<Integer> indexes) {
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
    public HHologram removeLines(int... indexes) {
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
    public HHologram clearLines() {
        for (int i = this.lines.size() - 1; i >= 0; i--)
            this.removeLine(i);
        return this;
    }

    /**
     * Deletes hologram.
     *
     * @return Instance of this class.
     */
    @Nonnull
    public HHologram delete() {
        if (this.isExist()) {
            HHologramHandler.getContent().remove(this.id);
            this.action.onDelete();
            this.renderer.delete();
            this.lines.clear();
        }
        return this;
    }
}