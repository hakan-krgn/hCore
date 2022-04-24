package com.hakan.core.ui.sign;

import com.hakan.core.HCore;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.ui.sign.listeners.HSignListener;
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

    private static final Map<UUID, HSign> SIGN_MAP = new HashMap<>();
    private static Class<?> SIGN_CLASS;

    /**
     * Initializes sign system.
     *
     * @param plugin Main class of plugin.
     */
    public static void initialize(@Nonnull JavaPlugin plugin) {

        try {

            Class<?> clazz = Class.forName("com.hakan.core.ui.sign.wrapper.HSign_" + HCore.getVersionString());
            if (HSign.class.isAssignableFrom(clazz)) {
                HSignHandler.SIGN_CLASS = clazz;
            }

            HListenerAdapter.register(new HSignListener(plugin));
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning("Could not initialize sign system. Probably you are using an unsupported version. (" + HCore.getVersionString() + ")");
        }

    }

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, HSign> getContentSafe() {
        return new HashMap<>(HSignHandler.SIGN_MAP);
    }

    /**
     * Gets content.
     *
     * @return Content
     */
    @Nonnull
    public static Map<UUID, HSign> getContent() {
        return HSignHandler.SIGN_MAP;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HSign> getValuesSafe() {
        return new ArrayList<>(HSignHandler.SIGN_MAP.values());
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HSign> getValues() {
        return HSignHandler.SIGN_MAP.values();
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
        return Optional.ofNullable(HSignHandler.SIGN_MAP.get(Objects.requireNonNull(uid, "UID cannot be null!")));
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

        Material type;
        try {
            type = Material.valueOf("SIGN_POST");
        } catch (Exception e) {
            type = Material.valueOf("LEGACY_SIGN");
        }

        return create(type, lines);
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
     * @param type  Sign type as Material.
     * @param lines Lines of sign.
     * @return Created HSign.
     */
    @Nonnull
    public static HSign create(@Nonnull Material type, @Nonnull String... lines) {
        Objects.requireNonNull(type, "type cannot be null!");
        Objects.requireNonNull(lines, "lines cannot be null!");

        try {
            return (HSign) HSignHandler.SIGN_CLASS.getConstructor(Material.class, String[].class).newInstance(type, lines);
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

}