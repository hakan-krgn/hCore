package com.hakan.core.npc;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.npc.types.HNPCEquipmentType;
import com.hakan.core.renderer.HRenderer;
import org.bukkit.Location;
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
 * HNPC class to create and manages
 * NPCs' easily and client-side.
 */
public abstract class HNPC {

    protected final String id;
    protected final Map<HNPCEquipmentType, ItemStack> equipments;

    protected HRenderer renderer;
    protected HHologram hologram;
    protected boolean walking = false;

    /**
     * Constructor to create new NPC.
     *
     * @param id           NPC id.
     * @param location     NPC location.
     * @param lines        NPC hologram lines.
     * @param viewers      NPC viewers.
     * @param equipments   NPC equipments.
     * @param showEveryone Show everyone or not.
     */
    public HNPC(@Nonnull String id,
                @Nonnull Location location,
                @Nonnull List<String> lines,
                @Nonnull Set<UUID> viewers,
                @Nonnull Map<HNPCEquipmentType, ItemStack> equipments,
                boolean showEveryone) {
        this.id = Objects.requireNonNull(id, "id cannot be null!");
        this.hologram = HCore.createHologram("hcore_npc_hologram:" + id, location);
        this.hologram.addLines(Objects.requireNonNull(lines, "lines cannot be null!"));
        this.equipments = Objects.requireNonNull(equipments, "equipments cannot be null!");
        this.renderer = new HRenderer(location, 30, viewers,
                this::show, this::hide,
                renderer -> this.hide(renderer.getShownViewersAsPlayer())
        ).showEveryone(showEveryone);
    }

    /**
     * ID of NPC.
     *
     * @return NPC id.
     */
    @Nonnull
    public final String getId() {
        return this.id;
    }

    /**
     * Gets npc location.
     *
     * @return Location.
     */
    @Nonnull
    public final Location getLocation() {
        return this.renderer.getLocation();
    }

    /**
     * Gets renderer.
     *
     * @return HRenderer.
     */
    @Nonnull
    public final HRenderer getRenderer() {
        return this.renderer;
    }

    /**
     * Gets hologram.
     *
     * @return HHologram.
     */
    @Nonnull
    public final HHologram getHologram() {
        return this.hologram;
    }

    /**
     * Is client side mode active?
     *
     * @return if client side mode active, return true.
     */
    public final boolean canSeeEveryone() {
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
    public final HNPC showEveryone(boolean mode) {
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
    public final HNPC expire(int expire, @Nonnull TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit, "time unit cannot be null!");
        HCore.syncScheduler().after(expire, timeUnit)
                .run(this::delete);
        return this;
    }

    /**
     * Adds player to hologram to show.
     *
     * @param players Player list.
     * @return instance of this class.
     */
    @Nonnull
    public final HNPC addViewer(@Nonnull List<Player> players) {
        Objects.requireNonNull(players, "players cannot be null!");
        this.hologram.addPlayer(players);
        players.forEach(player -> this.renderer.addViewer(player));
        return this;
    }

    /**
     * Adds player to hologram to show.
     *
     * @param players Player list.
     * @return instance of this class.
     */
    @Nonnull
    public final HNPC addViewer(@Nonnull Player... players) {
        Objects.requireNonNull(players, "players cannot be null!");
        return this.addViewer(Arrays.asList(players));
    }

    /**
     * Adds player to hologram to show.
     *
     * @param uids Player list.
     * @return instance of this class.
     */
    @Nonnull
    public final HNPC addViewerByUID(@Nonnull List<UUID> uids) {
        Objects.requireNonNull(uids, "uids cannot be null!");
        this.hologram.addPlayerByUID(uids);
        uids.forEach(uid -> this.renderer.addViewer(uid));
        return this;
    }

    /**
     * Adds player to hologram to show.
     *
     * @param uids Player list.
     * @return instance of this class.
     */
    @Nonnull
    public final HNPC addViewerByUID(@Nonnull UUID... uids) {
        Objects.requireNonNull(uids, "uids cannot be null!");
        return this.addViewerByUID(Arrays.asList(uids));
    }

    /**
     * Removes player from hologram to hide.
     *
     * @param players Player list.
     * @return instance of this class.
     */
    @Nonnull
    public final HNPC removeViewer(@Nonnull List<Player> players) {
        Objects.requireNonNull(players, "players cannot be null!");
        this.hologram.removePlayer(players);
        players.forEach(player -> this.renderer.removeViewer(player));
        return this;
    }

    /**
     * Removes player from hologram to hide.
     *
     * @param players Player list.
     * @return instance of this class.
     */
    @Nonnull
    public final HNPC removeViewer(@Nonnull Player... players) {
        Objects.requireNonNull(players, "players cannot be null!");
        return this.removeViewer(Arrays.asList(players));
    }

    /**
     * Removes player from hologram to hide.
     *
     * @param uids Player list.
     * @return instance of this class.
     */
    @Nonnull
    public final HNPC removeViewerByUID(@Nonnull List<UUID> uids) {
        Objects.requireNonNull(uids, "uids cannot be null!");
        this.hologram.removePlayerByUID(uids);
        uids.forEach(uid -> this.renderer.removeViewer(uid));
        return this;
    }

    /**
     * Removes player from npc to hide.
     *
     * @param uids Player list.
     * @return instance of this class.
     */
    @Nonnull
    public final HNPC removeViewerByUID(@Nonnull UUID... uids) {
        Objects.requireNonNull(uids, "uids cannot be null!");
        return this.removeViewerByUID(Arrays.asList(uids));
    }

    /**
     * Gets items as safe.
     *
     * @return Slot and ItemStack map.
     */
    @Nonnull
    public final Map<HNPCEquipmentType, ItemStack> getEquipmentsSafe() {
        return new HashMap<>(this.equipments);
    }

    /**
     * Gets items.
     *
     * @return Slot and ItemStack map.
     */
    @Nonnull
    public final Map<HNPCEquipmentType, ItemStack> getEquipments() {
        return this.equipments;
    }

    /**
     * Gets walking status.
     *
     * @return Walking.
     */
    public final boolean isWalking() {
        return this.walking;
    }


    /**
     * Moves NPC.
     *
     * @param to    Destination location.
     * @param speed Speed.
     * @return instance of this class.
     */
    @Nonnull
    public abstract HNPC walk(@Nonnull Location to, double speed);

    /**
     * Updates location.
     *
     * @param location Location.
     * @return instance of this class.
     */
    @Nonnull
    public abstract HNPC setLocation(@Nonnull Location location);

    /**
     * Sets skin on NPC.
     *
     * @param playerName Skin username.
     * @return instance of this class.
     */
    @Nonnull
    public abstract HNPC setSkin(@Nonnull String playerName);

    /**
     * Equips NPC with items.
     *
     * @param slotType  Slot type. Ex: HAND_ITEM, LEGGINGS,
     * @param itemStack Item.
     * @return instance of this class.
     */
    @Nonnull
    public abstract HNPC setEquipment(@Nonnull HNPCEquipmentType slotType, @Nonnull ItemStack itemStack);

    /**
     * Who sees NPC?
     *
     * @param players Player list.
     * @return instance of this class.
     */
    @Nonnull
    public abstract HNPC show(@Nonnull List<Player> players);

    /**
     * From whom should this NPC be hidden?
     *
     * @param players Player list.
     * @return instance of this class.
     */
    @Nonnull
    public abstract HNPC hide(@Nonnull List<Player> players);

    /**
     * Deletes NPC.
     *
     * @return instance of this class.
     */
    @Nonnull
    public abstract HNPC delete();
}