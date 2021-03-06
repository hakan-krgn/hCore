package com.hakan.core.npc;

import com.hakan.core.HCore;
import com.hakan.core.npc.builder.HNpcBuilder;
import com.hakan.core.npc.listener.HNpcClickListener;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
        try {
            HCore.syncScheduler().after(1).run(() -> Bukkit.getWorlds().stream()
                    .flatMap(world -> world.getLivingEntities().stream())
                    .filter(entity -> entity.getHealth() == 2.5179998874664307f)
                    .forEach(Entity::remove));

            HCore.asyncScheduler().every(10)
                    .run(() -> HNPCHandler.npcList.values().forEach(hnpc -> hnpc.getRenderer().render()));

            HNpcClickListener clickListener = (HNpcClickListener) Class.forName("com.hakan.core.npc.listener.HNpcClickListener_" + HCore.getVersionString())
                    .getConstructor().newInstance();
            HCore.registerListeners(clickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * Checks if npc exists.
     *
     * @param id Npc id.
     * @return True if exists.
     */
    public static boolean has(@Nonnull String id) {
        return HNPCHandler.npcList.containsKey(Validate.notNull(id, "id cannot be null!"));
    }

    /**
     * Finds a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Optional<HNPC> findByID(@Nonnull String id) {
        return Optional.ofNullable(HNPCHandler.npcList.get(Validate.notNull(id, "id cannot be null!")));
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
     * Finds a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Optional<HNPC> findByEntityID(int id) {
        for (HNPC npc : HNPCHandler.npcList.values())
            if (npc.getEntityID() == id)
                return Optional.of(npc);
        return Optional.empty();
    }

    /**
     * Gets a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static HNPC getByEntityID(int id) {
        return HNPCHandler.findByEntityID(id).orElseThrow(() -> new IllegalArgumentException("NPC with id " + id + " not found!"));
    }

    /**
     * Creates a new HNpcBuilder instance.
     *
     * @param id NPC id.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public static HNpcBuilder npcBuilder(@Nonnull String id) {
        return new HNpcBuilder(id);
    }

    /**
     * Deletes a npc with given id.
     *
     * @param id NPC id.
     * @return Deleted NPC.
     */
    @Nonnull
    public static HNPC delete(@Nonnull String id) {
        HNPC npc = HNPCHandler.getByID(Validate.notNull(id, "id cannot be null!"));
        return npc.delete();
    }
}