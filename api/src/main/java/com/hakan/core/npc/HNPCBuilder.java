package com.hakan.core.npc;

import com.hakan.core.HCore;
import com.hakan.core.npc.types.HNPCEquipmentType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * HNPCBuilder class to build
 * NPCs' easily.
 */
public final class HNPCBuilder {

    private final String id;
    private Boolean show;
    private String skin;
    private Location location;
    private Set<UUID> viewers;
    private List<String> lines;
    private Map<HNPCEquipmentType, ItemStack> equipments;

    /**
     * Constructor to create builder.
     *
     * @param id NPC id.
     */
    public HNPCBuilder(@Nonnull String id) {
        this.id = Objects.requireNonNull(id, "id cannot be null!");
        this.skin = "Steve";
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
     * Sets skin of npc.
     *
     * @param skin Skin.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public HNPCBuilder skin(@Nonnull String skin) {
        Objects.requireNonNull(skin, "skin cannot be null!");
        this.skin = skin;
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
    public HNPCBuilder equipment(@Nonnull HNPCEquipmentType type, @Nonnull ItemStack item) {
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
    public HNPCBuilder equipments(@Nonnull Map<HNPCEquipmentType, ItemStack> equipments) {
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
            this.show = false;

        try {
            Class<?> wrapper = Class.forName("com.hakan.core.npc.wrapper.HNPC_" + HCore.getVersionString());

            Constructor<?> constructor = wrapper.getDeclaredConstructor(String.class,
                    String.class,
                    Location.class,
                    List.class,
                    Set.class,
                    Map.class,
                    boolean.class);
            HNPC npc = (HNPC) constructor.newInstance(this.id,
                    this.skin,
                    this.location,
                    this.lines,
                    this.viewers,
                    this.equipments,
                    this.show);

            HNPCHandler.getContent().put(this.id, npc);

            return npc;
        } catch (Exception e) {
            throw new RuntimeException("Failed to build NPC!", e);
        }
    }
}