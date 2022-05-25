package com.hakan.core.ui.sign;

import com.hakan.core.HCore;
import com.hakan.core.ui.sign.listeners.HSignListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * HSignHandler class to find
 * and create the signs.
 */
public final class HSignHandler {

    private static Class<?> signClass;
    private static final Map<UUID, HSign> signMap = new HashMap<>();

    /**
     * Initializes sign system.
     */
    public static void initialize() {
        try {
            Class<?> clazz = Class.forName("com.hakan.core.ui.sign.wrapper.HSign_" + HCore.getVersionString());
            if (HSign.class.isAssignableFrom(clazz)) {
                HSignHandler.signClass = clazz;
            }

            HCore.registerListeners(new HSignListener());
        } catch (Exception e) {
            Bukkit.getLogger().warning("Could not initialize sign system. Probably you are using an unsupported version. (" + HCore.getVersionString() + ")");
            e.printStackTrace();
        }
    }

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, HSign> getContentSafe() {
        return new HashMap<>(HSignHandler.signMap);
    }

    /**
     * Gets content.
     *
     * @return Content
     */
    @Nonnull
    public static Map<UUID, HSign> getContent() {
        return HSignHandler.signMap;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HSign> getValuesSafe() {
        return new ArrayList<>(HSignHandler.signMap.values());
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HSign> getValues() {
        return HSignHandler.signMap.values();
    }

    /**
     * Finds HSign by player.
     *
     * @param player Player.
     * @return HSign as optional.
     */
    @Nonnull
    public static Optional<HSign> findByPlayer(@Nonnull Player player) {
        return HSignHandler.findByUID(player.getUniqueId());
    }

    /**
     * Gets HSign by player.
     *
     * @param player Player.
     * @return HSign.
     */
    @Nonnull
    public static HSign getByPlayer(@Nonnull Player player) {
        return HSignHandler.findByPlayer(player).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    /**
     * Finds HSign by player UID.
     *
     * @param uid Player UID.
     * @return HSign as optional.
     */
    @Nonnull
    public static Optional<HSign> findByUID(@Nonnull UUID uid) {
        return Optional.ofNullable(HSignHandler.signMap.get(Objects.requireNonNull(uid, "UID cannot be null!")));
    }

    /**
     * Gets HSign by player UID.
     *
     * @param uid Player UID.
     * @return HSign.
     */
    @Nonnull
    public static HSign getByUID(@Nonnull UUID uid) {
        return HSignHandler.findByUID(uid).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    /**
     * Creates HSign.
     *
     * @param lines Lines of sign.
     * @return Created HSign.
     */
    @Nonnull
    public static HSign create(@Nonnull String... lines) {
        Objects.requireNonNull(lines, "lines cannot be null!");
        try {
            return create(Material.valueOf("SIGN_POST"), lines);
        } catch (Exception e) {
            return create(Material.valueOf("LEGACY_SIGN"), lines);
        }
    }

    /**
     * Creates HSign.
     *
     * @param type  Sign type as Material.
     * @param lines Lines of sign.
     * @return Created HSign.
     */
    @Nonnull
    public static HSign create(@Nonnull Material type, @Nonnull String... lines) {
        Objects.requireNonNull(type, "type cannot be null!");
        Objects.requireNonNull(lines, "lines cannot be null!");

        try {
            return (HSign) HSignHandler.signClass.getConstructor(Material.class, String[].class).newInstance(type, lines);
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}