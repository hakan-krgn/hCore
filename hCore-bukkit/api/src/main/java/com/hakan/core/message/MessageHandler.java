package com.hakan.core.message;

import com.hakan.core.HCore;
import com.hakan.core.message.actionbar.ActionBarWrapper;
import com.hakan.core.message.bossbar.BossBar;
import com.hakan.core.message.bossbar.meta.BarColor;
import com.hakan.core.message.bossbar.meta.BarFlag;
import com.hakan.core.message.bossbar.meta.BarStyle;
import com.hakan.core.message.bossbar.versions.BossBarEmpty;
import com.hakan.core.message.bossbar.versions.BossBarImpl;
import com.hakan.core.message.title.Title;
import com.hakan.core.message.title.TitleWrapper;
import com.hakan.core.protocol.ProtocolVersion;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * MessageHandler class to send
 * action bar, title and boss bar.
 */
public final class MessageHandler {

    private static List<BossBar> bossBars;
    private static TitleWrapper titleWrapper;
    private static ActionBarWrapper actionBarWrapper;

    /**
     * Initializes message api.
     */
    public static void initialize() {
        bossBars = new ArrayList<>();
        titleWrapper = ReflectionUtils.newInstance("com.hakan.core.message.title.TitleWrapper_%s");
        actionBarWrapper = ReflectionUtils.newInstance("com.hakan.core.message.actionbar.ActionBarWrapper_%s");
    }


    /*
    TITLE
     */

    /**
     * Sends title to player.
     *
     * @param player Player.
     * @param title  Title class.
     */
    public static void sendTitle(@Nonnull Player player, @Nonnull Title title) {
        titleWrapper.send(Validate.notNull(player, "player cannot be null!"), Validate.notNull(title, "title cannot be null!"));
    }

    /**
     * Sends title to player.
     *
     * @param player   Player.
     * @param title    Title.
     * @param subTitle Subtitle.
     */
    public static void sendTitle(@Nonnull Player player, @Nonnull String title, @Nonnull String subTitle) {
        MessageHandler.sendTitle(player, new Title(title, subTitle));
    }

    /**
     * Sends title to player.
     *
     * @param player   Player.
     * @param title    Title.
     * @param subTitle Subtitle.
     * @param stay     Stay time.
     * @param fadein   Fade in time.
     * @param fadeout  Fade out time.
     */
    public static void sendTitle(@Nonnull Player player, @Nonnull String title, @Nonnull String subTitle, int stay, int fadein, int fadeout) {
        MessageHandler.sendTitle(player, new Title(title, subTitle, stay, fadein, fadeout));
    }

    /**
     * Sends title to players.
     *
     * @param players Players.
     * @param title   Title class.
     */
    public static void sendTitle(@Nonnull Collection<Player> players, @Nonnull Title title) {
        Validate.notNull(players, "players cannot be null!").forEach(player -> MessageHandler.sendTitle(player, title));
    }

    /**
     * Sends title to players.
     *
     * @param players  Players.
     * @param title    Title.
     * @param subTitle Subtitle.
     */
    public static void sendTitle(@Nonnull Collection<Player> players, @Nonnull String title, @Nonnull String subTitle) {
        MessageHandler.sendTitle(players, new Title(title, subTitle));
    }

    /**
     * Sends title to players.
     *
     * @param players  Players.
     * @param title    Title.
     * @param subTitle Subtitle.
     * @param stay     Stay time.
     * @param fadein   Fade in time.
     * @param fadeout  Fade out time.
     */
    public static void sendTitle(@Nonnull Collection<Player> players, @Nonnull String title, @Nonnull String subTitle, int stay, int fadein, int fadeout) {
        MessageHandler.sendTitle(players, new Title(title, subTitle, stay, fadein, fadeout));
    }


    /*
    ACTION BAR
     */

    /**
     * Sends action bar to player.
     *
     * @param player Player.
     * @param text   Text.
     */
    public static void sendActionBar(@Nonnull Player player, @Nonnull String text) {
        actionBarWrapper.send(Validate.notNull(player, "player cannot be null!"), Validate.notNull(text, "text cannot be null!"));
    }

    /**
     * Sends action bar to players.
     *
     * @param players Players.
     * @param text    Text.
     */
    public static void sendActionBar(@Nonnull Collection<Player> players, @Nonnull String text) {
        players.forEach(player -> MessageHandler.sendActionBar(player, text));
    }


    /*
    BOSS BAR
     */

    /**
     * Gets bossbar list as safe.
     *
     * @return Bossbar list.
     */
    @Nonnull
    public static List<BossBar> getBossBarsSafe() {
        return new ArrayList<>(bossBars);
    }

    /**
     * Gets bossbar list.
     *
     * @return Bossbar list.
     */
    @Nonnull
    public static List<BossBar> getBossBars() {
        return bossBars;
    }

    /**
     * Gets list of boss bars from player.
     *
     * @param player Player.
     * @return List of boss bars.
     */
    @Nonnull
    public static List<BossBar> getBossBarsByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");

        List<BossBar> bossBarList = new ArrayList<>();
        for (BossBar bossBar : bossBars)
            if (bossBar.getPlayers().contains(player))
                bossBarList.add(bossBar);
        return bossBarList;
    }

    /**
     * Finds first bossbar from player.
     *
     * @param player Player.
     * @return Bossbar as optional.
     */
    @Nonnull
    public static Optional<BossBar> findFirstBossBarByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");

        for (BossBar bossBar : bossBars)
            if (bossBar.getPlayers().contains(player))
                return Optional.of(bossBar);
        return Optional.empty();
    }

    /**
     * Gets first bossbar from player.
     *
     * @param player Player.
     * @return Bossbar.
     */
    @Nonnull
    public static BossBar getFirstBossBarByPlayer(@Nonnull Player player) {
        return MessageHandler.findFirstBossBarByPlayer(player)
                .orElseThrow(() -> new IllegalArgumentException("player " + player.getName() + " has no bossbar!"));
    }

    /**
     * Creates bossbar.
     *
     * @param title Title.
     * @return New instance of BossBar.
     */
    @Nonnull
    public static BossBar createBossBar(@Nonnull String title) {
        return MessageHandler.createBossBar(title, BarColor.PURPLE);
    }

    /**
     * Creates bossbar.
     *
     * @param title Title.
     * @param color Color.
     * @return New instance of BossBar.
     */
    @Nonnull
    public static BossBar createBossBar(@Nonnull String title,
                                        @Nonnull BarColor color) {
        return MessageHandler.createBossBar(title, color, BarStyle.SOLID);
    }

    /**
     * Creates bossbar.
     *
     * @param title Title.
     * @param color Color.
     * @param style Style.
     * @return New instance of BossBar.
     */
    @Nonnull
    public static BossBar createBossBar(@Nonnull String title,
                                        @Nonnull BarColor color,
                                        @Nonnull BarStyle style) {
        return MessageHandler.createBossBar(title, color, style, new BarFlag[0]);
    }

    /**
     * Creates bossbar.
     *
     * @param title Title.
     * @param color Color.
     * @param style Style.
     * @param flags Flags.
     * @return New instance of BossBar.
     */
    @Nonnull
    public static BossBar createBossBar(@Nonnull String title,
                                        @Nonnull BarColor color,
                                        @Nonnull BarStyle style,
                                        @Nonnull BarFlag... flags) {
        Validate.notNull(title, "title cannot be null!");
        Validate.notNull(color, "color cannot be null!");
        Validate.notNull(style, "style cannot be null!");
        Validate.notNull(flags, "flags cannot be null!");

        BossBar bossBar = HCore.getProtocolVersion().equals(ProtocolVersion.v1_8_R3) ?
                new BossBarEmpty(title, color, style, flags) :
                new BossBarImpl(title, color, style, flags);

        bossBars.add(bossBar);
        return bossBar;
    }
}
