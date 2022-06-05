package com.hakan.core.npc;

import com.hakan.core.HCore;
import com.hakan.core.npc.builder.HNPCBuilder;
import com.hakan.core.npc.listeners.HNpcClickListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;

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
            HNpcClickListener clickListener = (HNpcClickListener) Class.forName("com.hakan.core.npc.listeners.HNpcClickListener_" + HCore.getVersionString())
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
     * Creates a new HNPCBuilder instance.
     *
     * @param id NPC id.
     * @return HNPCBuilder instance.
     */
    @Nonnull
    public static HNPCBuilder npcBuilder(@Nonnull String id) {
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