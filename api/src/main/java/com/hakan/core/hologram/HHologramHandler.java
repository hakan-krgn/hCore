package com.hakan.core.hologram;

import com.hakan.core.HCore;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.packet.event.PacketEvent;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * HHologramHandler class to handle
 * all holograms.
 */
public final class HHologramHandler {

    private static final Map<String, HHologram> holograms = new HashMap<>();

    /**
     * Initializes holograms.
     */
    public static void initialize() {
        HCore.syncScheduler().every(10)
                .run(() -> HHologramHandler.getValues().forEach(hologram -> hologram.getRenderer().render()));

        HCore.registerEvent(PacketEvent.class)
                .filter(event -> event.getPacket().getClass().getName().contains("PacketPlayInUseEntity"))
                .consumeAsync(event -> HHologramHandler.getValues().forEach(hologram -> {
                    HologramLine line = hologram.getLineByEntityID(event.getValue("a"));
                    if (line != null) hologram.getAction().onClick(event.getPlayer(), line);
                }));
    }

    /**
     * Gets content as safe.
     *
     * @return Holograms map.
     */
    @Nonnull
    public static Map<String, HHologram> getContentSafe() {
        return new HashMap<>(HHologramHandler.holograms);
    }

    /**
     * Gets content.
     *
     * @return Holograms map.
     */
    @Nonnull
    public static Map<String, HHologram> getContent() {
        return HHologramHandler.holograms;
    }

    /**
     * Gets holograms as safe.
     *
     * @return Holograms.
     */
    @Nonnull
    public static Collection<HHologram> getValuesSafe() {
        return new ArrayList<>(HHologramHandler.holograms.values());
    }

    /**
     * Gets holograms.
     *
     * @return Holograms.
     */
    @Nonnull
    public static Collection<HHologram> getValues() {
        return HHologramHandler.holograms.values();
    }

    /**
     * Checks if hologram exists.
     *
     * @param id Hologram id.
     * @return True if hologram exists.
     */
    public static boolean has(@Nonnull String id) {
        return HHologramHandler.holograms.containsKey(Validate.notNull(id, "id cannot be null!"));
    }

    /**
     * Finds a created hologram
     *
     * @param id Hologram id that you want.
     * @return Hologram from id.
     */
    @Nonnull
    public static Optional<HHologram> findByID(@Nonnull String id) {
        return Optional.ofNullable(HHologramHandler.holograms.get(Validate.notNull(id, "id cannot be null!")));
    }

    /**
     * Gets a created hologram
     *
     * @param id Hologram id that you want
     * @return Hologram from id
     */
    @Nonnull
    public static HHologram getByID(@Nonnull String id) {
        return HHologramHandler.findByID(id).orElseThrow(() -> new NullPointerException("hologram(" + id + ") cannot be null!"));
    }

    /**
     * Creates a hologram but if there
     * is a hologram with id, removes it.
     *
     * @param id       Hologram id.
     * @param location Hologram location.
     * @return Created hologram.
     */
    @Nonnull
    public static HHologram forceCreate(@Nonnull String id, @Nonnull Location location) {
        HHologramHandler.findByID(id).ifPresent(HHologram::delete);
        return HHologramHandler.create(id, location);
    }

    /**
     * Creates a hologram but if there
     * is a hologram with id, removes it.
     *
     * @param id       Hologram id.
     * @param location Hologram location.
     * @param players  Viewers.
     * @return Created hologram.
     */
    @Nonnull
    public static HHologram forceCreate(@Nonnull String id, @Nonnull Location location, @Nullable Set<UUID> players) {
        HHologramHandler.findByID(id).ifPresent(HHologram::delete);
        return HHologramHandler.create(id, location, players);
    }

    /**
     * Creates a new hologram
     *
     * @param id       hologram id
     * @param location location
     * @return new hologram
     */
    @Nonnull
    public static HHologram create(@Nonnull String id, @Nonnull Location location) {
        return HHologramHandler.create(id, location, null);
    }

    /**
     * Creates a new hologram
     *
     * @param id       Hologram id.
     * @param location Location.
     * @param players  Player list.
     * @return New hologram.
     */
    @Nonnull
    public static HHologram create(@Nonnull String id, @Nonnull Location location, @Nullable Set<UUID> players) {
        Validate.notNull(id, "id cannot be null");
        Validate.notNull(location, "location cannot be null!");
        Validate.isTrue(HHologramHandler.has(id), "hologram with id(" + id + ") already exists!");

        HHologram hHologram = (players != null) ? new HHologram(id, location, players) : new HHologram(id, location);
        HHologramHandler.holograms.put(id, hHologram);
        return hHologram;
    }

    /**
     * Deletes hologram by id.
     *
     * @param id ID of hologram.
     * @return Hologram to be deleted.
     */
    @Nonnull
    public static HHologram delete(@Nonnull String id) {
        HHologram oldHologram = HHologramHandler.getByID(id);
        return oldHologram.delete();
    }
}