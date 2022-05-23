package com.hakan.core.npc;

import com.hakan.core.HCore;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
        HCore.asyncScheduler().every(10)
                .run(() -> {
                    for (HNPC hnpc : HNPCHandler.npcList.values()) {
                        hnpc.renderer.render();
                    }
                });
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

    /**
     * Creates a npc.
     *
     * @param id       NPC id.
     * @param location NPC location.
     * @param lines    NPC lines.
     * @return Created NPC.
     */
    @Nonnull
    public static HNPC create(@Nonnull String id, @Nonnull Location location, @Nonnull List<String> lines) {
        try {
            Objects.requireNonNull(id, "id cannot be null!");
            Objects.requireNonNull(location, "location cannot be null!");
            Objects.requireNonNull(lines, "lines cannot be null!");

            Class<?> aClass = Class.forName("com.hakan.core.npc.wrapper.HNPC_" + HCore.getVersionString());
            Constructor<?> constructor = aClass.getDeclaredConstructor(String.class, Location.class, List.class);
            HNPC npc = (HNPC) constructor.newInstance(id, location, lines);

            HNPCHandler.npcList.put(id, npc);

            return npc;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Creates a npc.
     *
     * @param id       NPC id.
     * @param location NPC location.
     * @param lines    NPC lines.
     * @param viewers  NPC viewers for client side.
     * @return Created NPC.
     */
    @Nonnull
    public static HNPC create(@Nonnull String id, @Nonnull Location location, @Nonnull List<String> lines, @Nonnull Set<UUID> viewers) {
        try {
            Objects.requireNonNull(id, "id cannot be null!");
            Objects.requireNonNull(location, "location cannot be null!");
            Objects.requireNonNull(lines, "lines cannot be null!");
            Objects.requireNonNull(viewers, "viewers cannot be null!");

            Class<?> aClass = Class.forName("com.hakan.core.npc.wrapper.HNPC_" + HCore.getVersionString());
            Constructor<?> constructor = aClass.getDeclaredConstructor(String.class, Location.class, List.class, Set.class);
            HNPC npc = (HNPC) constructor.newInstance(id, location, lines, viewers);

            HNPCHandler.npcList.put(id, npc);

            return npc;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}