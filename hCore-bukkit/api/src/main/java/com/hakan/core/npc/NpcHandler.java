package com.hakan.core.npc;

import com.hakan.core.HCore;
import com.hakan.core.npc.builder.NpcBuilder;
import com.hakan.core.npc.listener.NpcClickListener;
import com.hakan.core.npc.utils.NpcUtils;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * NpcHandler class to create
 * and get NPCs.
 */
public final class NpcHandler {

    private static final Map<String, Npc> npcList = new HashMap<>();

    /**
     * Initializes the NPC system.
     */
    public static void initialize() {
        HCore.syncScheduler().after(1).run(() -> Bukkit.getWorlds().stream()
                .flatMap(world -> world.getLivingEntities().stream())
                .filter(entity -> entity.getHealth() == 2.5179998874664307f)
                .forEach(Entity::remove));

        HCore.asyncScheduler().every(10)
                .freezeIf((task) -> npcList.isEmpty())
                .run(() -> npcList.values().forEach(npc -> npc.getRenderer().render()));

        HCore.asyncScheduler().every(1)
                .freezeIf((task) -> npcList.isEmpty())
                .freezeIf((task) -> Bukkit.getOnlinePlayers().isEmpty())
                .run(() -> npcList.values().forEach(npc -> {
                    if (npc.getLookTarget().equals(Npc.LookTarget.NEAREST)) {
                        Player nearestPlayer = NpcUtils.getNearestPlayer(npc);
                        if (nearestPlayer != null) npc.lookAt(nearestPlayer);
                    } else if (npc.getLookTarget().equals(Npc.LookTarget.INDIVIDUAL)) {
                        npc.getRenderer().getShownPlayers().forEach(player -> {
                            Location targetLocation = player.getEyeLocation();
                            Location npcLocation = npc.getLocation().add(0, 1.62, 0);
                            Location teleportLocation = NpcUtils.calculateVectorAsLocation(npcLocation, targetLocation);

                            npc.getRenderer().setLocation(teleportLocation.subtract(0, 1.62, 0));
                            npc.getEntity().updateHeadRotation(Collections.singletonList(player));
                        });
                    }
                }));

        NpcClickListener clickListener = ReflectionUtils.newInstance("com.hakan.core.npc.listener.NpcClickListener_%s");
        HCore.registerListeners(clickListener);
    }

    /**
     * Gets content as safe.
     *
     * @return npc map.
     */
    @Nonnull
    public static Map<String, Npc> getContentSafe() {
        return new HashMap<>(npcList);
    }

    /**
     * Gets content.
     *
     * @return npc map.
     */
    @Nonnull
    public static Map<String, Npc> getContent() {
        return npcList;
    }

    /**
     * Gets npc list as safe.
     *
     * @return npc.
     */
    @Nonnull
    public static Collection<Npc> getValuesSafe() {
        return new ArrayList<>(npcList.values());
    }

    /**
     * Gets npc list.
     *
     * @return NPC.
     */
    @Nonnull
    public static Collection<Npc> getValues() {
        return npcList.values();
    }

    /**
     * Checks if npc exists.
     *
     * @param id Npc id.
     * @return True if exists.
     */
    public static boolean has(@Nonnull String id) {
        return npcList.containsKey(Validate.notNull(id, "id cannot be null!"));
    }

    /**
     * Finds a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Optional<Npc> findByID(@Nonnull String id) {
        return Optional.ofNullable(npcList.get(Validate.notNull(id, "id cannot be null!")));
    }

    /**
     * Gets a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Npc getByID(@Nonnull String id) {
        return NpcHandler.findByID(id).orElseThrow(() -> new IllegalArgumentException("NPC with id " + id + " not found!"));
    }

    /**
     * Finds a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Optional<Npc> findByEntityID(int id) {
        for (Npc npc : npcList.values())
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
    public static Npc getByEntityID(int id) {
        return NpcHandler.findByEntityID(id).orElseThrow(() -> new IllegalArgumentException("NPC with id " + id + " not found!"));
    }

    /**
     * Creates a new NpcBuilder instance.
     *
     * @param id NPC id.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public static NpcBuilder npcBuilder(@Nonnull String id) {
        return new NpcBuilder(id);
    }

    /**
     * Deletes a npc with given id.
     *
     * @param id NPC id.
     * @return Deleted NPC.
     */
    @Nonnull
    public static Npc delete(@Nonnull String id) {
        Npc npc = NpcHandler.getByID(Validate.notNull(id, "id cannot be null!"));
        return npc.delete();
    }
}
