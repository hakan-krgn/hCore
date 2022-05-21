package com.hakan.core.npc;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.renderer.HRenderer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * //todo do
 */
public abstract class HNPC {

    protected final String id;
    protected EntityType type;
    protected Location location;
    protected HRenderer renderer;
    protected HHologram hologram;
    protected boolean walking = false;
    protected final Map<HNPCSlotType, ItemStack> items = new HashMap<>();

    /**
     * Constructor to create new NPC.
     *
     * @param id       NPC id.
     * @param type     NPC type.
     * @param location NPC location.
     * @param lines    NPC hologram lines.
     */
    HNPC(@Nonnull String id,
         @Nonnull EntityType type,
         @Nonnull Location location,
         @Nonnull List<String> lines) {
        this.id = Objects.requireNonNull(id, "id cannot be null!");
        this.type = Objects.requireNonNull(type, "type cannot be null!");
        this.location = Objects.requireNonNull(location, "location cannot be null!");
        this.hologram = HCore.createHologram("hcore_npc_hologram:" + id, location);
        this.hologram.addLines(Objects.requireNonNull(lines, "lines cannot be null!"));

        double radius = this.hologram.getRenderer().getRadius();
        this.renderer = new HRenderer(location, radius,
                this::show,
                this::hide,
                renderer -> this.hide(renderer.getShownViewersAsPlayer())
        ).showEveryone(true);
    }

    /**
     * Constructor to create new NPC.
     *
     * @param id       NPC id.
     * @param type     NPC type.
     * @param location NPC location.
     * @param lines    NPC hologram lines.
     * @param viewers  NPC viewers.
     */
    HNPC(@Nonnull String id,
         @Nonnull EntityType type,
         @Nonnull Location location,
         @Nonnull List<String> lines,
         @Nonnull Set<UUID> viewers) {
        this.id = Objects.requireNonNull(id, "id cannot be null!");
        this.type = Objects.requireNonNull(type, "type cannot be null!");
        this.location = Objects.requireNonNull(location, "location cannot be null!");
        this.hologram = HCore.createHologram("hcore_npc_hologram:" + id, location);
        this.hologram.addLines(Objects.requireNonNull(lines, "lines cannot be null!"));

        double radius = this.hologram.getRenderer().getRadius();
        this.renderer = new HRenderer(location, radius, viewers,
                this::show,
                this::hide,
                renderer -> this.hide(renderer.getShownViewersAsPlayer())
        ).showEveryone(true);
    }

    /**
     * ID of NPC.
     *
     * @return NPC id.
     */
    @Nonnull
    public String getId() {
        return this.id;
    }

    /**
     * Gets entity type.
     *
     * @return EntityType.
     */
    @Nonnull
    public EntityType getType() {
        return this.type;
    }

    /**
     * Sets entity type.
     * Attention! Creates a new NPC.
     *
     * @param type EntityType.
     * @return New vezorNPC.
     */
    @Nonnull
    public HNPC setType(@Nonnull EntityType type) {
        this.type = Objects.requireNonNull(type, "entityType cannot be null!");
        return this;
    }

    /**
     * Gets npc location.
     *
     * @return Location.
     */
    @Nonnull
    public Location getLocation() {
        return this.location;
    }

    /**
     * Updates location.
     *
     * @param location Location.
     * @return HNPC for chain.
     */
    @Nonnull
    public HNPC setLocation(@Nonnull Location location) {
        Objects.requireNonNull(location, "location cannot be null!");
        this.location = location;
        this.renderer.setLocation(location);
        this.hologram.setLocation(location);
        return this;
    }

    /**
     * Gets renderer.
     *
     * @return VezorRenderer.
     */
    @Nonnull
    public HRenderer getRenderer() {
        return this.renderer;
    }

    /**
     * Gets hologram.
     *
     * @return VezorHologram.
     */
    @Nonnull
    public HHologram getHologram() {
        return this.hologram;
    }

    /**
     * Is client side mode active?
     *
     * @return if client side mode active, return true
     */
    public boolean canSeeEveryone() {
        return this.renderer.canSeeEveryone();
    }

    /**
     * If set this mode as false, everyone
     * can see this hologram.
     *
     * @param mode Mode status.
     * @return Instance of this class.
     */
    @Nonnull
    public HNPC showEveryone(boolean mode) {
        this.renderer.showEveryone(mode);
        this.hologram.showEveryone(mode);
        return this;
    }

    /**
     * NPC expires after a certain time.
     *
     * @param expire   Amount.
     * @param timeUnit Time Unit.
     * @return HNPC for chain.
     */
    @Nonnull
    public HNPC expire(int expire, @Nonnull TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit, "time unit cannot be null!");
        HCore.syncScheduler().after(expire, timeUnit)
                .run(this::delete);
        return this;
    }

    /**
     * Adds player to hologram to show
     *
     * @param players Player list
     * @return instance of this class
     */
    @Nonnull
    public HNPC addPlayer(@Nonnull List<Player> players) {
        Objects.requireNonNull(players, "players cannot be null!");
        players.forEach(player -> this.renderer.addViewer(player));
        return this;
    }

    /**
     * Adds player to hologram to show
     *
     * @param players Player list
     * @return instance of this class
     */
    @Nonnull
    public HNPC addPlayer(@Nonnull Player... players) {
        Objects.requireNonNull(players, "players cannot be null!");
        Arrays.asList(players).forEach(player -> this.renderer.addViewer(player));
        return this;
    }

    /**
     * Removes player to hologram to hide
     *
     * @param players Player list
     * @return instance of this class
     */
    @Nonnull
    public HNPC removePlayer(@Nonnull List<Player> players) {
        Objects.requireNonNull(players, "players cannot be null!");
        players.forEach(player -> this.renderer.removeViewer(player));
        return this;
    }

    /**
     * Removes player to hologram to hide
     *
     * @param players Player list
     * @return instance of this class
     */
    @Nonnull
    public HNPC removePlayer(@Nonnull Player... players) {
        Objects.requireNonNull(players, "players cannot be null!");
        Arrays.asList(players).forEach(player -> this.renderer.removeViewer(player));
        return this;
    }

    /**
     * Gets items.
     *
     * @return Slot and ItemStack map.
     */
    @Nonnull
    public Map<HNPCSlotType, ItemStack> getItems() {
        return new HashMap<>(this.items);
    }

    /**
     * Gets walking status.
     *
     * @return Walking.
     */
    public boolean isWalking() {
        return this.walking;
    }

    /**
     * Deletes NPC.
     */
    public abstract void delete();

    /**
     * Moves NPC.
     *
     * @param moveTo Destination location.
     * @param speed  Speed.
     */
    public abstract void move(Location moveTo, double speed);

    /**
     * Who sees NPC?
     *
     * @param players Player list.
     */
    public abstract void show(List<Player> players);

    /**
     * From whom should this NPC be hidden?
     *
     * @param players Player list.
     */
    public abstract void hide(List<Player> players);

    /**
     * Sets skin on NPC.
     *
     * @param username Skin username.
     */
    public abstract void setSkin(String username);

    /**
     * Equip NPC with items.
     *
     * @param slotType  Slot type. Ex: HAND_ITEM, LEGGINGS,
     * @param itemStack Item.
     */
    public abstract void equipment(HNPCSlotType slotType, ItemStack itemStack);
}