package com.hakan.core.ui.sign;

import com.hakan.core.HCore;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.ui.sign.listeners.HSignListener;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * HSignHandler class to find
 * and create the signs.
 */
public final class HSignHandler {

    private static final Map<UUID, HSign> signMap = new HashMap<>();

    /**
     * Initializes sign system.
     *
     * @param plugin Main class of plugin.
     */
    public static void initialize(@Nonnull JavaPlugin plugin) {
        HListenerAdapter.register(new HSignListener(plugin));
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
     * @param type  Sign type as Material.
     * @param lines Lines of sign.
     * @return Created HSign.
     */
    @Nonnull
    public static HSign create(@Nonnull Material type, @Nonnull String... lines) {
        Validate.notNull(type, "type cannot be null!");
        Validate.notNull(lines, "lines cannot be null!");

        try {
            return (HSign) Class.forName("com.hakan.core.ui.sign.wrapper.HSign_" + HCore.getVersionString())
                    .getConstructor(Material.class, String[].class).newInstance(type, lines);
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}