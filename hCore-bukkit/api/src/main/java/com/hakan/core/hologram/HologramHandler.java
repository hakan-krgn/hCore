package com.hakan.core.hologram;

import com.hakan.core.HCore;
import com.hakan.core.hologram.builder.HologramBuilder;
import com.hakan.core.hologram.listeners.HologramClickListener;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * HologramHandler class to handle
 * all holograms.
 */
public final class HologramHandler {

    private static final Map<String, Hologram> holograms = new HashMap<>();

    /**
     * Initializes holograms.
     */
    public static void initialize() {
        HCore.asyncScheduler().every(10)
                .run(() -> HologramHandler.getValues().forEach(hologram -> hologram.getRenderer().render()));
        HCore.registerListeners(new HologramClickListener());
    }

    /**
     * Gets content as safe.
     *
     * @return Holograms map.
     */
    @Nonnull
    public static Map<String, Hologram> getContentSafe() {
        return new HashMap<>(holograms);
    }

    /**
     * Gets content.
     *
     * @return Holograms map.
     */
    @Nonnull
    public static Map<String, Hologram> getContent() {
        return holograms;
    }

    /**
     * Gets holograms as safe.
     *
     * @return Holograms.
     */
    @Nonnull
    public static Collection<Hologram> getValuesSafe() {
        return new ArrayList<>(holograms.values());
    }

    /**
     * Gets holograms.
     *
     * @return Holograms.
     */
    @Nonnull
    public static Collection<Hologram> getValues() {
        return holograms.values();
    }

    /**
     * Checks if hologram exists.
     *
     * @param id Hologram id.
     * @return True if hologram exists.
     */
    public static boolean has(@Nonnull String id) {
        return holograms.containsKey(Validate.notNull(id, "id cannot be null!"));
    }

    /**
     * Finds a created hologram.
     *
     * @param id Hologram id that you want.
     * @return Hologram from id.
     */
    @Nonnull
    public static Optional<Hologram> findByID(@Nonnull String id) {
        return Optional.ofNullable(holograms.get(Validate.notNull(id, "id cannot be null!")));
    }

    /**
     * Gets a created hologram.
     *
     * @param id Hologram id that you want.
     * @return Hologram from id.
     */
    @Nonnull
    public static Hologram getByID(@Nonnull String id) {
        return HologramHandler.findByID(id).orElseThrow(() -> new NullPointerException("hologram(" + id + ") cannot be null!"));
    }

    /**
     * Creates a hologram builder.
     *
     * @param id Hologram id.
     * @return New hologram.
     */
    @Nonnull
    public static HologramBuilder builder(@Nonnull String id) {
        return new HologramBuilder(id);
    }
}
