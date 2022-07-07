package com.hakan.core.scoreboard;

import com.hakan.core.HCore;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class HScoreboardHandler {

    private static final Map<UUID, HScoreboard> scoreboards = new HashMap<>();

    /**
     * Initialize method of HScoreboard
     */
    public static void initialize() {
        //Handles player quit event.
        HCore.registerEvent(PlayerQuitEvent.class)
                .consume(event -> HScoreboardHandler.findByPlayer(event.getPlayer()).ifPresent(HScoreboard::delete));
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
     * Finds a created scoreboard
     *
     * @param player scoreboard id that you want
     * @return scoreboard from id
     */
    @Nonnull
    public static Optional<HScoreboard> findByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return HScoreboardHandler.findByUID(player.getUniqueId());
    }

    /**
     * Gets a created scoreboard
     *
     * @param player Player.
     * @return scoreboard from id
     */
    @Nonnull
    public static HScoreboard getByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return HScoreboardHandler.getByUID(player.getUniqueId());
    }

    /**
     * Finds a created scoreboard
     *
     * @param uid UID of player.
     * @return scoreboard from id
     */
    @Nonnull
    public static Optional<HScoreboard> findByUID(@Nonnull UUID uid) {
        return Optional.ofNullable(HScoreboardHandler.scoreboards.get(Validate.notNull(uid, "uid cannot be null")));
    }

    /**
     * Gets a created scoreboard
     *
     * @param uid UID of player.
     * @return scoreboard from id
     */
    @Nonnull
    public static HScoreboard getByUID(@Nonnull UUID uid) {
        return HScoreboardHandler.findByUID(uid).orElseThrow(() -> new NullPointerException("scoreboard(" + uid + ") cannot be null!"));
    }

    /**
     * Creates new Instance of this class.
     *
     * @param player player
     * @return new instance of HScoreboard
     */
    @Nonnull
    public static HScoreboard create(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return HScoreboardHandler.create(player.getUniqueId());
    }

    /**
     * Creates new Instance of this class.
     *
     * @param uid UID of player
     * @return new instance of HScoreboard
     */
    @Nonnull
    public static HScoreboard create(@Nonnull UUID uid) {
        Validate.notNull(uid, "uid cannot be null");
        HScoreboardHandler.findByUID(uid).ifPresent(HScoreboard::delete);

        HScoreboard hScoreboard = new HScoreboard(uid);
        HScoreboardHandler.scoreboards.put(uid, hScoreboard);
        return hScoreboard;
    }
}