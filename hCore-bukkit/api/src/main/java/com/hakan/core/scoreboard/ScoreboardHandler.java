package com.hakan.core.scoreboard;

import com.hakan.core.HCore;
import com.hakan.core.utils.ReflectionUtils;
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
 * Scoreboard class for creating
 * scoreboards for player.
 */
public final class ScoreboardHandler {

    private static final Map<UUID, Scoreboard> scoreboards = new HashMap<>();

    /**
     * Initialize method of Scoreboard.
     */
    public static void initialize() {
        HCore.registerEvent(PlayerQuitEvent.class)
                .consume(event -> ScoreboardHandler.findByPlayer(event.getPlayer()).ifPresent(Scoreboard::delete));
    }


    /**
     * Gets scoreboard map as safe.
     *
     * @return Scoreboard map.
     */
    @Nonnull
    public static Map<UUID, Scoreboard> getContentSafe() {
        return new HashMap<>(scoreboards);
    }

    /**
     * Gets scoreboard map.
     *
     * @return Scoreboard map.
     */
    @Nonnull
    public static Map<UUID, Scoreboard> getContent() {
        return scoreboards;
    }

    /**
     * Gets all scoreboards as safe.
     *
     * @return Scoreboard list.
     */
    @Nonnull
    public static Collection<Scoreboard> getValuesSafe() {
        return new ArrayList<>(scoreboards.values());
    }

    /**
     * Gets all scoreboards.
     *
     * @return Scoreboard list.
     */
    @Nonnull
    public static Collection<Scoreboard> getValues() {
        return scoreboards.values();
    }

    /**
     * Checks if scoreboard exists for player.
     *
     * @param uid UID of player.
     * @return if scoreboard exists for player, returns true.
     */
    public static boolean has(@Nonnull UUID uid) {
        Validate.notNull(uid, "uid cannot be null!");
        return scoreboards.containsKey(uid);
    }

    /**
     * Finds a created scoreboard.
     *
     * @param player Player.
     * @return Scoreboard from id.
     */
    @Nonnull
    public static Optional<Scoreboard> findByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return ScoreboardHandler.findByUID(player.getUniqueId());
    }

    /**
     * Gets a created scoreboard.
     *
     * @param player Player.
     * @return Scoreboard from id.
     */
    @Nonnull
    public static Scoreboard getByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return ScoreboardHandler.getByUID(player.getUniqueId());
    }

    /**
     * Finds a created scoreboard.
     *
     * @param uid UID of player.
     * @return Scoreboard from uid.
     */
    @Nonnull
    public static Optional<Scoreboard> findByUID(@Nonnull UUID uid) {
        return Optional.ofNullable(scoreboards.get(Validate.notNull(uid, "uid cannot be null")));
    }

    /**
     * Gets a created scoreboard.
     *
     * @param uid UID of player.
     * @return Scoreboard from uid.
     */
    @Nonnull
    public static Scoreboard getByUID(@Nonnull UUID uid) {
        return ScoreboardHandler.findByUID(uid).orElseThrow(() -> new NullPointerException("scoreboard(" + uid + ") cannot be null!"));
    }

    /**
     * Creates new instance of Scoreboard as force.
     *
     * @param player Player.
     * @return new instance of Scoreboard.
     */
    @Nonnull
    public static Scoreboard forceCreate(@Nonnull Player player, @Nonnull String title) {
        ScoreboardHandler.findByPlayer(player).ifPresent(Scoreboard::delete);
        return ScoreboardHandler.create(player, title);
    }

    /**
     * Creates new instance of Scoreboard as force.
     *
     * @param uid UID of player.
     * @return new instance of Scoreboard.
     */
    @Nonnull
    public static Scoreboard forceCreate(@Nonnull UUID uid, @Nonnull String title) {
        ScoreboardHandler.findByUID(uid).ifPresent(Scoreboard::delete);
        return ScoreboardHandler.create(uid, title);
    }

    /**
     * Creates new instance of Scoreboard.
     *
     * @param player Player.
     * @return new instance of Scoreboard.
     */
    @Nonnull
    public static Scoreboard create(@Nonnull Player player, @Nonnull String title) {
        return ScoreboardHandler.create(player.getUniqueId(), title);
    }

    /**
     * Creates new instance of Scoreboard.
     *
     * @param uid UID of player.
     * @return new instance of Scoreboard.
     */
    @Nonnull
    public static Scoreboard create(@Nonnull UUID uid, @Nonnull String title) {
        Player player = Bukkit.getPlayer(uid);

        Validate.notNull(uid, "uid cannot be null!");
        Validate.isTrue(has(uid), "scoreboard of player(" + uid + ") already exists!");
        Validate.isTrue(player == null, "player(" + uid + ") must be online!");

        Scoreboard scoreboard = ReflectionUtils.newInstance("com.hakan.core.scoreboard.versions.Scoreboard_%s",
                new Class[]{Player.class, String.class}, new Object[]{player, title});
        scoreboards.put(player.getUniqueId(), scoreboard);
        return scoreboard;
    }
}
