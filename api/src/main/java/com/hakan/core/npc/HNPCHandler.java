package com.hakan.core.npc;

import com.hakan.core.HCore;
import com.hakan.core.npc.events.HNpcEventListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * HNPCHandler class to create
 * and get NPCs.
 */
public final class HNPCHandler {

    private static final Map<String, HNPC> npcList = new HashMap<>();
    private static final Map<Integer, String> npcIDByEntityID = new HashMap<>();
    private static HNpcEventListener HNpcEventListener;

    /**
     * Initializes the NPC system.
     */
    public static void initialize() {
        HCore.registerEvent(EntityTargetEvent.class)
                .filter(event -> event.getEntity() instanceof LivingEntity)
                .filter(event -> event.getTarget() instanceof LivingEntity)
                .filter(event -> ((LivingEntity) event.getEntity()).getHealth() == 11.91231632232666d)
                .filter(event -> event.getTarget() != null && ((LivingEntity) event.getTarget()).getHealth() != 11.91231632232666d)
                .consume(event -> event.setCancelled(true));

        Bukkit.getWorlds().stream().flatMap(world -> world.getLivingEntities().stream())
                .filter(entity -> entity.getHealth() == 11.91231632232666d).forEach(Entity::remove);

        HCore.asyncScheduler().every(10)
                .run(() -> HNPCHandler.npcList.values().forEach(hnpc -> hnpc.renderer.render()));

        try {
            HNpcEventListener = (HNpcEventListener) Class.forName("com.hakan.core.npc.listeners.HNpcEventListener_" + HCore.getVersionString()).getConstructor().newInstance();
            Bukkit.getPluginManager().registerEvents(HNpcEventListener, HCore.getInstance());
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
     * @return npcIDByEntityID map.
     */
    @Nonnull
    public static Map<Integer, String> getNpcIDByEntityID() {
        return HNPCHandler.npcIDByEntityID;
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