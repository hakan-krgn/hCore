package com.hakan.core.scoreboard;

import com.hakan.core.HCore;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * HScoreboard class for creating
 * scoreboards for player.
 */
public final class HScoreboardHandler {

    private static final Map<UUID, HScoreboard> scoreboards = new HashMap<>();
    private static Class<?> SCOREBOARD_CLASS;

    /**
     * Initialize method of HScoreboard.
     */
    public static void initialize() {
        //Handles player quit event.
        HCore.registerEvent(PlayerQuitEvent.class)
                .consume(event -> HScoreboardHandler.findByPlayer(event.getPlayer()).ifPresent(HScoreboard::delete));

        try {
            SCOREBOARD_CLASS = Class.forName("com.hakan.core.scoreboard.wrapper.HScoreboard_" + HCore.getVersionString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets scoreboard map as safe.
     *
     * @return Scoreboard map.
     */
    @Nonnull
    public static Map<UUID, HScoreboard> getContentSafe() {
        return new HashMap<>(HScoreboardHandler.scoreboards);
    }

    /**
     * Gets scoreboard map.
     *
     * @return Scoreboard map.
     */
    @Nonnull
    public static Map<UUID, HScoreboard> getContent() {
        return HScoreboardHandler.scoreboards;
    }

    /**
     * Gets all scoreboards as safe.
     *
     * @return Scoreboard list.
     */
    @Nonnull
    public static Collection<HScoreboard> getValuesSafe() {
        return new ArrayList<>(HScoreboardHandler.scoreboards.values());
    }

    /**
     * Gets all scoreboards.
     *
     * @return Scoreboard list.
     */
    @Nonnull
    public static Collection<HScoreboard> getValues() {
        return HScoreboardHandler.scoreboards.values();
    }

    /**
     * Checks if scoreboard exists for player.
     *
     * @param uid UID of player.
     * @return if scoreboard exists for player, returns true.
     */
    public static boolean has(@Nonnull UUID uid) {
        Validate.notNull(uid, "uid cannot be null!");
        return HScoreboardHandler.scoreboards.containsKey(uid);
    }

    /**
     * Finds a created scoreboard.
     *
     * @param player Player.
     * @return Scoreboard from id.
     */
    @Nonnull
    public static Optional<HScoreboard> findByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return HScoreboardHandler.findByUID(player.getUniqueId());
    }

    /**
     * Gets a created scoreboard.
     *
     * @param player Player.
     * @return Scoreboard from id.
     */
    @Nonnull
    public static HScoreboard getByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return HScoreboardHandler.getByUID(player.getUniqueId());
    }

    /**
     * Finds a created scoreboard.
     *
     * @param uid UID of player.
     * @return Scoreboard from uid.
     */
    @Nonnull
    public static Optional<HScoreboard> findByUID(@Nonnull UUID uid) {
        return Optional.ofNullable(HScoreboardHandler.scoreboards.get(Validate.notNull(uid, "uid cannot be null")));
    }

    /**
     * Gets a created scoreboard
     *
     * @param uid UID of player.
     * @return Scoreboard from uid.
     */
    @Nonnull
    public static HScoreboard getByUID(@Nonnull UUID uid) {
        return HScoreboardHandler.findByUID(uid).orElseThrow(() -> new NullPointerException("scoreboard(" + uid + ") cannot be null!"));
    }

    /**
     * Creates new Instance of this class.
     *
     * @param player Player.
     * @return new instance of HScoreboard.
     */
    @Nonnull
    public static HScoreboard create(@Nonnull Player player, @Nonnull String title) {
        return HScoreboardHandler.create(player.getUniqueId(), title);
    }

    /**
     * Creates new Instance of this class.
     *
     * @param uid UID of player.
     * @return new instance of HScoreboard.
     */
    @Nonnull
    public static HScoreboard create(@Nonnull UUID uid, @Nonnull String title) {
        Player player = Bukkit.getPlayer(uid);

        Validate.notNull(uid, "uid cannot be null!");
        Validate.isTrue(has(uid), "scoreboard of player(" + uid + ") already exists!");
        Validate.isTrue(player == null, "player(" + uid + ") must be online!");

        try {
            HScoreboard scoreboard = (HScoreboard) SCOREBOARD_CLASS
                    .getConstructor(Player.class, String.class)
                    .newInstance(player, title);
            HScoreboardHandler.scoreboards.put(player.getUniqueId(), scoreboard);
            return scoreboard;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}