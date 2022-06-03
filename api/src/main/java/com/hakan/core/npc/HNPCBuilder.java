package com.hakan.core.npc;

import com.hakan.core.HCore;
import com.hakan.core.npc.skin.HNPCSkin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * HNPCBuilder class to build
 * NPCs' easily.
 */
public final class HNPCBuilder {

    private final String id;
    private Boolean show;
    private HNPCSkin skin;
    private Location location;
    private Set<UUID> viewers;
    private List<String> lines;
    private Map<HNPC.EquipmentType, ItemStack> equipments;

    private Consumer<HNPC> spawnConsumer;
    private Consumer<HNPC> deleteConsumer;
    private BiConsumer<Player, HNPC.Action> clickBiConsumer;
    private long clickDelay;


    /**
     * Constructor to create builder.
     *
     * @param id NPC id.
     */
    public HNPCBuilder(@Nonnull String id) {
        this.id = Objects.requireNonNull(id, "id cannot be null!");
        this.skin = HNPCSkin.EMPTY;
        this.lines = new ArrayList<>();
        this.viewers = new HashSet<>();
        this.equipments = new HashMap<>();
        this.location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    }

    /**
     * Can everyone see this npc or not.
     *
     * @param show Show or not.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder showEveryone(boolean show) {
        this.show = show;
        return this;
    }

    /**
     * Set viewers of npc.
     *
     * @param viewers Viewers.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder viewers(@Nonnull UUID... viewers) {
        Objects.requireNonNull(viewers, "viewers cannot be null!");
        this.viewers = new HashSet<>(Arrays.asList(viewers));
        return this;
    }

    /**
     * Set viewers of npc.
     *
     * @param viewers Viewers.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder viewers(@Nonnull Set<UUID> viewers) {
        Objects.requireNonNull(viewers, "viewers cannot be null!");
        this.viewers = viewers;
        return this;
    }

    /**
     * Adds viewers to npc.
     *
     * @param viewers Viewers.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder addViewers(@Nonnull UUID... viewers) {
        Objects.requireNonNull(viewers, "viewers cannot be null!");
        this.viewers.addAll(Arrays.asList(viewers));
        return this;
    }

    /**
     * Adds viewers to npc hologram.
     *
     * @param viewers Viewers.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder addViewers(@Nonnull Set<UUID> viewers) {
        Objects.requireNonNull(viewers, "viewers cannot be null!");
        this.viewers.addAll(viewers);
        return this;
    }

    /**
     * Sets lines of npc hologram.
     *
     * @param lines Lines.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder lines(@Nonnull List<String> lines) {
        Objects.requireNonNull(lines, "lines cannot be null!");
        this.lines = lines;
        return this;
    }

    /**
     * Sets lines of npc hologram.
     *
     * @param lines Lines.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder lines(@Nonnull String... lines) {
        Objects.requireNonNull(lines, "lines cannot be null!");
        this.lines = Arrays.asList(lines);
        return this;
    }

    /**
     * Appends lines to npc hologram.
     *
     * @param lines Lines.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder appendLines(@Nonnull List<String> lines) {
        Objects.requireNonNull(lines, "lines cannot be null!");
        this.lines.addAll(lines);
        return this;
    }

    /**
     * Appends lines to npc hologram.
     *
     * @param lines Lines.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder appendLines(@Nonnull String... lines) {
        Objects.requireNonNull(lines, "lines cannot be null!");
        this.lines.addAll(Arrays.asList(lines));
        return this;
    }

    /**
     * Sets location of npc.
     *
     * @param location Location.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder location(@Nonnull Location location) {
        Objects.requireNonNull(location, "location cannot be null!");
        this.location = location;
        return this;
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on click.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder onClick(BiConsumer<Player, HNPC.Action> action) {
        return onClick(action, 3);
    }

    /**
     * Sets click action of npc.
     *
     * @param action    action on click.
     * @param delayInMS delay between clicks
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder onClick(BiConsumer<Player, HNPC.Action> action, long delayInMS) {
        Objects.requireNonNull(action, "action cannot be null!");
        this.clickBiConsumer = action;
        this.clickDelay = delayInMS <= 0 ? 3 : delayInMS;
        return this;
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on spawn.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder onSpawn(Consumer<HNPC> action) {
        Objects.requireNonNull(action, "action cannot be null!");
        this.spawnConsumer = action;
        return this;
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on delete.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder onDelete(Consumer<HNPC> action) {
        Objects.requireNonNull(action, "action cannot be null!");
        this.deleteConsumer = action;
        return this;
    }

    /**
     * Sets skin of npc.
     *
     * @param skin Skin.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder skin(@Nonnull HNPCSkin skin) {
        Objects.requireNonNull(skin, "skin cannot be null!");
        this.skin = skin;
        return this;
    }

    /**
     * Sets skin of npc.
     *
     * @param skin Skin.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder skin(@Nonnull String skin) {
        HCore.asyncScheduler().run(() -> skin(HNPCSkin.from(skin)));
        return this;
    }

    /**
     * Sets equipment of npc.
     *
     * @param type Equipment type.
     * @param item Item.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder equipment(@Nonnull HNPC.EquipmentType type, @Nonnull ItemStack item) {
        Objects.requireNonNull(type, "type cannot be null!");
        Objects.requireNonNull(item, "item cannot be null!");
        this.equipments.put(type, item);
        return this;
    }

    /**
     * Sets equipments of npc.
     *
     * @param equipments Equipments.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder equipments(@Nonnull Map<HNPC.EquipmentType, ItemStack> equipments) {
        Objects.requireNonNull(equipments, "equipments cannot be null!");
        this.equipments = equipments;
        return this;
    }

    /**
     * Builds npc.
     *
     * @return HNPC instance.
     */
    @Nonnull
    public HNPC build() {
        if (this.show == null)
            this.show = (this.viewers.size() > 0);

        try {
            Class<?> wrapper = Class.forName("com.hakan.core.npc.wrapper.HNPC_" + HCore.getVersionString());

            Constructor<?> constructor = wrapper.getDeclaredConstructor(String.class,
                    HNPCSkin.class,
                    Location.class,
                    List.class,
                    Set.class,
                    Map.class,
                    Consumer.class,
                    Consumer.class,
                    BiConsumer.class,
                    long.class,
                    boolean.class);
            HNPC npc = (HNPC) constructor.newInstance(this.id,
                    this.skin,
                    this.location,
                    this.lines,
                    this.viewers,
                    this.equipments,
                    this.spawnConsumer,
                    this.deleteConsumer,
                    this.clickBiConsumer,
                    this.clickDelay,
                    this.show);

            HNPCHandler.getContent().put(this.id, npc);
            HNPCHandler.getNpcIDByEntityID().put(npc.getInternalEntityID(), npc.getId());

            return npc;
        } catch (Exception e) {
            throw new RuntimeException("Failed to build NPC!", e);
        }
    }
}