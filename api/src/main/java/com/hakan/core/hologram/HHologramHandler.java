package com.hakan.core.hologram;

import com.hakan.core.HCore;
import com.hakan.core.hologram.builder.HHologramBuilder;
import com.hakan.core.hologram.listeners.HologramClickListener;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        HCore.asyncScheduler().every(10)
                .run(() -> HHologramHandler.getValues().forEach(hologram -> hologram.getRenderer().render()));
        HCore.registerListeners(new HologramClickListener());
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
     * Finds a created hologram.
     *
     * @param id Hologram id that you want.
     * @return Hologram from id.
     */
    @Nonnull
    public static Optional<HHologram> findByID(@Nonnull String id) {
        return Optional.ofNullable(HHologramHandler.holograms.get(Validate.notNull(id, "id cannot be null!")));
    }

    /**
     * Gets a created hologram.
     *
     * @param id Hologram id that you want.
     * @return Hologram from id.
     */
    @Nonnull
    public static HHologram getByID(@Nonnull String id) {
        return HHologramHandler.findByID(id).orElseThrow(() -> new NullPointerException("hologram(" + id + ") cannot be null!"));
    }

    /**
     * Creates a hologram builder.
     *
     * @param id Hologram id.
     * @return New hologram.
     */
    @Nonnull
    public static HHologramBuilder builder(@Nonnull String id) {
        return new HHologramBuilder(id);
    }
}