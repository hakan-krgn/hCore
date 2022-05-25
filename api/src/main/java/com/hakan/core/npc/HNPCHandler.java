package com.hakan.core.npc;

import com.hakan.core.HCore;
import com.hakan.core.npc.listeners.HNpcTargetListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * HNPCHandler class to create
 * and get NPCs.
 */
public final class HNPCHandler {

    private static final Map<String, HNPC> npcList = new HashMap<>();

    /**
     * Initializes the NPC system.
     */
    public static void initialize() {
        HCore.registerListeners(new HNpcTargetListener());

        Bukkit.getWorlds().stream().flatMap(world -> world.getLivingEntities().stream())
                .filter(entity -> entity.getHealth() == 11.91231632232666d).forEach(Entity::remove);
        HCore.asyncScheduler().every(10)
                .run(() -> HNPCHandler.npcList.values().forEach(hnpc -> hnpc.renderer.render()));
    }

    /**
     * Gets content as safe.
     *
     * @return npc map.
     */
    @Nonnull
    public static Map<String, HNPC> getContentSafe() {
        return new HashMap<>(HNPCHandler.npcList);
    }

    /**
     * Gets content.
     *
     * @return npc map.
     */
    @Nonnull
    public static Map<String, HNPC> getContent() {
        return HNPCHandler.npcList;
    }

    /**
     * Gets npc list as safe.
     *
     * @return npc.
     */
    @Nonnull
    public static Collection<HNPC> getValuesSafe() {
        return new ArrayList<>(HNPCHandler.npcList.values());
    }

    /**
     * Gets npc list.
     *
     * @return NPC.
     */
    @Nonnull
    public static Collection<HNPC> getValues() {
        return HNPCHandler.npcList.values();
    }

    /**
     * Finds a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Optional<HNPC> findByID(@Nonnull String id) {
        return Optional.ofNullable(HNPCHandler.npcList.get(Objects.requireNonNull(id, "id cannot be null!")));
    }

    /**
     * Gets a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static HNPC getByID(@Nonnull String id) {
        return HNPCHandler.findByID(id).orElseThrow(() -> new IllegalArgumentException("NPC with id " + id + " not found!"));
    }

    /**
     * Creates a new HNPCBuilder instance.
     *
     * @param id NPC id.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public static HNPCBuilder build(@Nonnull String id) {
        return new HNPCBuilder(id);
    }

    /**
     * Deletes a npc with given id.
     *
     * @param id NPC id.
     * @return Deleted NPC.
     */
    @Nonnull
    public static HNPC delete(@Nonnull String id) {
        HNPC npc = HNPCHandler.getByID(Objects.requireNonNull(id, "id cannot be null!"));
        return npc.delete();
    }
}