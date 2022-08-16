package com.hakan.core;

import com.hakan.core.command.HCommandHandler;
import com.hakan.core.database.DatabaseHandler;
import com.hakan.core.database.DatabaseObject;
import com.hakan.core.database.DatabaseProvider;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.HHologramHandler;
import com.hakan.core.item.HItemBuilder;
import com.hakan.core.item.nbt.HNbtManager;
import com.hakan.core.item.skull.HSkullBuilder;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.message.HMessageHandler;
import com.hakan.core.message.bossbar.HBarColor;
import com.hakan.core.message.bossbar.HBarFlag;
import com.hakan.core.message.bossbar.HBarStyle;
import com.hakan.core.message.bossbar.HBossBar;
import com.hakan.core.message.title.HTitle;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.HNPCHandler;
import com.hakan.core.npc.builder.HNpcBuilder;
import com.hakan.core.packet.HPacketHandler;
import com.hakan.core.particle.HParticle;
import com.hakan.core.particle.HParticleHandler;
import com.hakan.core.scheduler.HScheduler;
import com.hakan.core.scoreboard.HScoreboard;
import com.hakan.core.scoreboard.HScoreboardHandler;
import com.hakan.core.spam.HSpam;
import com.hakan.core.ui.GUI;
import com.hakan.core.ui.GUIHandler;
import com.hakan.core.ui.anvil.HAnvil;
import com.hakan.core.ui.anvil.builder.HAnvilBuilder;
import com.hakan.core.ui.inventory.HInventory;
import com.hakan.core.ui.inventory.builder.HInventoryBuilder;
import com.hakan.core.ui.sign.HSign;
import com.hakan.core.ui.sign.builder.HSignBuilder;
import com.hakan.core.utils.ProtocolVersion;
import com.hakan.core.utils.Serializer;
import com.hakan.core.utils.Validate;
import com.hakan.core.utils.hooks.Metrics;
import com.hakan.core.worldborder.HWorldBorderHandler;
import com.hakan.core.worldborder.border.HBorderColor;
import com.hakan.core.worldborder.border.HWorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Main class of this core.
 * You can reach all APIs from this
 * class as static.
 */
@SuppressWarnings({"unused"})
public final class HCore {

    private static JavaPlugin INSTANCE;
    private static ProtocolVersion VERSION;

    /**
     * Gets instance.
     *
     * @return Instance.
     */
    @Nonnull
    public static JavaPlugin getInstance() {
        return HCore.INSTANCE;
    }

    /**
     * Sets instance plugin of hCore.
     *
     * @param instance Instance.
     */
    public static void setInstance(@Nonnull JavaPlugin instance) {
        HCore.INSTANCE = Validate.notNull(instance, "instance cannot be null!");
    }

    /**
     * Initializes all APIs.
     *
     * @param plugin Instance of main class.
     */
    public static void initialize(@Nonnull JavaPlugin plugin) {
        if (HCore.INSTANCE != null) return;

        HCore.INSTANCE = Validate.notNull(plugin, "plugin cannot be null!");
        HCore.VERSION = ProtocolVersion.getCurrentVersion();

        Metrics.initialize(plugin);
        HPacketHandler.initialize();
        GUIHandler.initialize();
        HNPCHandler.initialize();
        HItemBuilder.initialize();
        HMessageHandler.initialize();
        HParticleHandler.initialize();
        HHologramHandler.initialize();
        HScoreboardHandler.initialize();
        HWorldBorderHandler.initialize();
    }


    /*
    OTHERS
     */

    /**
     * Gets version string (example: v1_8_R3)
     *
     * @return Version string.
     */
    @Nonnull
    public static String getVersionString() {
        return HCore.VERSION.getKey();
    }

    /**
     * Gets protocol version of current server.
     *
     * @return Protocol version.
     */
    @Nonnull
    public static ProtocolVersion getProtocolVersion() {
        return HCore.VERSION;
    }


    /*
    SCHEDULER
     */

    /**
     * Creates scheduler.
     *
     * @param async Async status.
     * @return HScheduler.
     */
    @Nonnull
    public static HScheduler scheduler(boolean async) {
        return new HScheduler(HCore.INSTANCE, async);
    }

    /**
     * Creates async scheduler.
     *
     * @return HScheduler.
     */
    @Nonnull
    public static HScheduler asyncScheduler() {
        return new HScheduler(HCore.INSTANCE, true);
    }

    /**
     * Creates sync scheduler.
     *
     * @return HScheduler.
     */
    @Nonnull
    public static HScheduler syncScheduler() {
        return new HScheduler(HCore.INSTANCE, false);
    }


    /*
    UI API
     */

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, GUI> getGUIContentSafe() {
        return GUIHandler.getContentSafe();
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, GUI> getGUIContent() {
        return GUIHandler.getContent();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<GUI> getGUIValuesSafe() {
        return GUIHandler.getValuesSafe();
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<GUI> getGUIValues() {
        return GUIHandler.getValues();
    }

    /**
     * Finds HInventory by player.
     *
     * @param player Player.
     * @return HInventory as optional.
     */
    @Nonnull
    public static Optional<GUI> findGUIByPlayer(@Nonnull Player player) {
        return GUIHandler.findByPlayer(player);
    }

    /**
     * Gets HInventory by player.
     *
     * @param player Player.
     * @return HInventory.
     */
    @Nonnull
    public static GUI getGUIByPlayer(@Nonnull Player player) {
        return GUIHandler.getByPlayer(player);
    }

    /**
     * Finds HInventory by UID.
     *
     * @param uid Player UID.
     * @return HInventory as optional.
     */
    @Nonnull
    public static Optional<GUI> findGUIByUID(@Nonnull UUID uid) {
        return GUIHandler.findByUID(uid);
    }

    /**
     * Gets HInventory by UID.
     *
     * @param uid Player UId.
     * @return HInventory.
     */
    @Nonnull
    public static GUI getGUIByUID(@Nonnull UUID uid) {
        return GUIHandler.getByUID(uid);
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, HInventory> getInventoryContentSafe() {
        return GUIHandler.getInventoryContentSafe();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HInventory> getInventoryValuesSafe() {
        return GUIHandler.getInventoryValuesSafe();
    }

    /**
     * Finds HInventory by player.
     *
     * @param player Player.
     * @return HInventory as optional.
     */
    @Nonnull
    public static Optional<HInventory> findInventoryByPlayer(@Nonnull Player player) {
        return GUIHandler.findInventoryByPlayer(player);
    }

    /**
     * Gets HInventory by player.
     *
     * @param player Player.
     * @return HInventory.
     */
    @Nonnull
    public static HInventory getInventoryByPlayer(@Nonnull Player player) {
        return GUIHandler.getInventoryByPlayer(player);
    }

    /**
     * Finds HInventory by UID.
     *
     * @param uid Player UID.
     * @return HInventory as optional.
     */
    @Nonnull
    public static Optional<HInventory> findInventoryByUID(@Nonnull UUID uid) {
        return GUIHandler.findInventoryByUID(uid);
    }

    /**
     * Gets HInventory by UID.
     *
     * @param uid Player UId.
     * @return HInventory.
     */
    @Nonnull
    public static HInventory getInventoryByUID(@Nonnull UUID uid) {
        return GUIHandler.getInventoryByUID(uid);
    }

    /**
     * Creates builder with ID.
     *
     * @param id ID.
     * @return HInventoryBuilder.
     */
    @Nonnull
    public static HInventoryBuilder inventoryBuilder(@Nonnull String id) {
        return GUIHandler.inventoryBuilder(id);
    }

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, HSign> getSignContentSafe() {
        return GUIHandler.getSignContentSafe();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HSign> getSignValuesSafe() {
        return GUIHandler.getSignValuesSafe();
    }

    /**
     * Finds HSign by player.
     *
     * @param player Player.
     * @return HSign as optional.
     */
    @Nonnull
    public static Optional<HSign> findSignByPlayer(@Nonnull Player player) {
        return GUIHandler.findSignByPlayer(player);
    }

    /**
     * Gets HSign by player.
     *
     * @param player Player.
     * @return HSign.
     */
    @Nonnull
    public static HSign getSignByPlayer(@Nonnull Player player) {
        return GUIHandler.getSignByPlayer(player);
    }

    /**
     * Finds HSign by player UID.
     *
     * @param uid Player UID.
     * @return HSign as optional.
     */
    @Nonnull
    public static Optional<HSign> findSignByUID(@Nonnull UUID uid) {
        return GUIHandler.findSignByUID(uid);
    }

    /**
     * Gets HSign by player UID.
     *
     * @param uid Player UID.
     * @return HSign.
     */
    @Nonnull
    public static HSign getSignByUID(@Nonnull UUID uid) {
        return GUIHandler.getSignByUID(uid);
    }

    /**
     * Creates sign builder.
     *
     * @param player Player.
     * @return HSignBuilder.
     */
    @Nonnull
    public static HSignBuilder signBuilder(@Nonnull Player player) {
        return new HSignBuilder(player);
    }

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, HAnvil> getAnvilContentSafe() {
        return GUIHandler.getAnvilContentSafe();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HAnvil> getAnvilValuesSafe() {
        return GUIHandler.getAnvilValuesSafe();
    }

    /**
     * Finds HSign by player.
     *
     * @param player Player.
     * @return HSign as optional.
     */
    @Nonnull
    public static Optional<HAnvil> findAnvilByPlayer(@Nonnull Player player) {
        return GUIHandler.findAnvilByPlayer(player);
    }

    /**
     * Gets HSign by player.
     *
     * @param player Player.
     * @return HSign.
     */
    @Nonnull
    public static HAnvil getAnvilByPlayer(@Nonnull Player player) {
        return GUIHandler.getAnvilByPlayer(player);
    }

    /**
     * Finds HSign by player UID.
     *
     * @param uid Player UID.
     * @return HSign as optional.
     */
    @Nonnull
    public static Optional<HAnvil> findAnvilByUID(@Nonnull UUID uid) {
        return GUIHandler.findAnvilByUID(uid);
    }

    /**
     * Gets HSign by player UID.
     *
     * @param uid Player UID.
     * @return HSign.
     */
    @Nonnull
    public static HAnvil getAnvilByUID(@Nonnull UUID uid) {
        return GUIHandler.getAnvilByUID(uid);
    }

    /**
     * Creates sign builder.
     *
     * @param player Player.
     * @return HSignBuilder.
     */
    @Nonnull
    public static HAnvilBuilder anvilBuilder(@Nonnull Player player) {
        return GUIHandler.anvilBuilder(player);
    }


    /*
    COMMAND
     */

    /**
     * Registers commands to server.
     *
     * @param adapters List of command adapters.
     */
    public static void registerCommands(@Nonnull Object... adapters) {
        HCommandHandler.register(adapters);
    }


    /*
    LISTENERS
     */

    /**
     * Registers listeners to server.
     *
     * @param listeners List of listeners.
     */
    public static void registerListeners(@Nonnull Listener... listeners) {
        Arrays.asList(Validate.notNull(listeners, "listeners cannot be null!"))
                .forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, INSTANCE));
    }

    /**
     * Registers listeners to server.
     *
     * @param eventClass Class of event.
     * @param <T>        Event type.
     * @return Listener.
     */
    @Nonnull
    public static <T extends Event> HListenerAdapter<T> registerEvent(@Nonnull Class<T> eventClass) {
        return new HListenerAdapter<>(eventClass);
    }


    /*
    PACKET
     */

    /**
     * Sends packets to player.
     *
     * @param player  Player.
     * @param packets Packets.
     */
    public static void sendPacket(@Nonnull Player player, @Nonnull Object... packets) {
        HPacketHandler.findByPlayer(player).ifPresent(packetPlayer -> packetPlayer.send(packets));
    }

    /**
     * Sends packet to players
     *
     * @param player Player.
     * @param packet Packet.
     */
    public static void sendPacket(@Nonnull Player player, @Nonnull Object packet) {
        HCore.sendPacket(player, new Object[]{packet});
    }

    /**
     * Sends packet to player list.
     *
     * @param players Player list.
     * @param packet  Packet.
     */
    public static void sendPacket(@Nonnull Collection<Player> players, @Nonnull Object packet) {
        HCore.sendPacket(players, new Object[]{packet});
    }

    /**
     * Sends packets to player list.
     *
     * @param players Player list.
     * @param packets Packets.
     */
    public static void sendPacket(@Nonnull Collection<Player> players, @Nonnull Object... packets) {
        Validate.notNull(players, "players cannot be null!").forEach(player -> HCore.sendPacket(player, packets));
    }


    /*
    PARTICLE
     */

    /**
     * Plays particle for player.
     *
     * @param player   Player.
     * @param location Location.
     * @param particle HParticle class.
     */
    public static void playParticle(@Nonnull Player player, @Nonnull Location location, @Nonnull HParticle particle) {
        HParticleHandler.play(player, location, particle);
    }

    /**
     * Plays particle for players.
     *
     * @param players  Players.
     * @param location Location.
     * @param particle HParticle class.
     */
    public static void playParticle(@Nonnull Collection<Player> players, @Nonnull Location location, @Nonnull HParticle particle) {
        HParticleHandler.play(players, location, particle);
    }

    /**
     * Plays particle for all players.
     *
     * @param location Location.
     * @param particle HParticle class.
     */
    public static void playParticle(@Nonnull Location location, @Nonnull HParticle particle) {
        HParticleHandler.play(location, particle);
    }


    /*
    MESSAGE
     */

    /**
     * Sends title to player.
     *
     * @param player Player.
     * @param hTitle HTitle class.
     */
    public static void sendTitle(@Nonnull Player player, @Nonnull HTitle hTitle) {
        HMessageHandler.sendTitle(player, hTitle);
    }

    /**
     * Sends title to player.
     *
     * @param player   Player.
     * @param title    Title.
     * @param subTitle Subtitle.
     */
    public static void sendTitle(@Nonnull Player player, @Nonnull String title, @Nonnull String subTitle) {
        HMessageHandler.sendTitle(player, title, subTitle);
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
        HMessageHandler.sendTitle(player, title, subTitle, stay, fadein, fadeout);
    }

    /**
     * Sends title to players.
     *
     * @param players Players.
     * @param hTitle  HTitle class.
     */
    public static void sendTitle(@Nonnull Collection<Player> players, @Nonnull HTitle hTitle) {
        HMessageHandler.sendTitle(players, hTitle);
    }

    /**
     * Sends title to players.
     *
     * @param players  Players.
     * @param title    Title.
     * @param subTitle Subtitle.
     */
    public static void sendTitle(@Nonnull Collection<Player> players, @Nonnull String title, @Nonnull String subTitle) {
        HMessageHandler.sendTitle(players, title, subTitle);
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
        HMessageHandler.sendTitle(players, title, subTitle, stay, fadein, fadeout);
    }

    /**
     * Sends action bar to player.
     *
     * @param player Player.
     * @param text   Text.
     */
    public static void sendActionBar(@Nonnull Player player, @Nonnull String text) {
        HMessageHandler.sendActionBar(player, text);
    }

    /**
     * Sends action bar to players.
     *
     * @param players Players.
     * @param text    Text.
     */
    public static void sendActionBar(@Nonnull Collection<Player> players, @Nonnull String text) {
        HMessageHandler.sendActionBar(players, text);
    }

    /**
     * Gets bossbar list as safe.
     *
     * @return Bossbar list.
     */
    @Nonnull
    public static List<HBossBar> getBossBarsSafe() {
        return HMessageHandler.getBossBarsSafe();
    }

    /**
     * Gets bossbar list.
     *
     * @return Bossbar list.
     */
    @Nonnull
    public static List<HBossBar> getBossBars() {
        return HMessageHandler.getBossBars();
    }

    /**
     * Gets list of bossbars from player.
     *
     * @param player Player.
     * @return List of boss bars.
     */
    @Nonnull
    public static List<HBossBar> getBossBarsByPlayer(@Nonnull Player player) {
        return HMessageHandler.getBossBarsByPlayer(player);
    }

    /**
     * Finds first bossbar from player.
     *
     * @param player Player.
     * @return Bossbar as optional.
     */
    @Nonnull
    public static Optional<HBossBar> findFirstBossBarByPlayer(@Nonnull Player player) {
        return HMessageHandler.findFirstBossBarByPlayer(player);
    }

    /**
     * Gets first bossbar from player.
     *
     * @param player Player.
     * @return Bossbar.
     */
    @Nonnull
    public static HBossBar getFirstBossBarByPlayer(@Nonnull Player player) {
        return HMessageHandler.getFirstBossBarByPlayer(player);
    }

    /**
     * Deletes bossbar.
     *
     * @param hBossBar Bossbar.
     */
    public static void deleteBossBar(@Nonnull HBossBar hBossBar) {
        HMessageHandler.deleteBossBar(hBossBar);
    }

    /**
     * Creates bossbar.
     *
     * @param title Title.
     * @param color Color.
     * @param style Style.
     * @param flags Flags.
     * @return New instance of HBossBar.
     */
    public static HBossBar createBossBar(@Nonnull String title, @Nonnull HBarColor color, @Nonnull HBarStyle style, @Nonnull HBarFlag... flags) {
        return HMessageHandler.createBossBar(title, color, style, flags);
    }

    /**
     * Creates bossbar.
     *
     * @param title Title.
     * @param color Color.
     * @param style Style.
     * @return New instance of HBossBar.
     */
    public static HBossBar createBossBar(@Nonnull String title, @Nonnull HBarColor color, @Nonnull HBarStyle style) {
        return HMessageHandler.createBossBar(title, color, style);
    }


    /*
    SCOREBOARD
     */

    /**
     * Gets scoreboard map as safe.
     *
     * @return Scoreboard map.
     */
    @Nonnull
    public static Map<UUID, HScoreboard> getScoreboardContentSafe() {
        return HScoreboardHandler.getContentSafe();
    }

    /**
     * Gets scoreboard map.
     *
     * @return Scoreboard map.
     */
    @Nonnull
    public static Map<UUID, HScoreboard> getScoreboardContent() {
        return HScoreboardHandler.getContent();
    }

    /**
     * Gets all scoreboards as safe.
     *
     * @return Scoreboard list.
     */
    @Nonnull
    public static Collection<HScoreboard> getScoreboardValuesSafe() {
        return HScoreboardHandler.getValuesSafe();
    }

    /**
     * Gets all scoreboards.
     *
     * @return Scoreboard list.
     */
    @Nonnull
    public static Collection<HScoreboard> getScoreboardValues() {
        return HScoreboardHandler.getValues();
    }

    /**
     * Checks if scoreboard exists for player.
     *
     * @param uid UID of player.
     * @return if scoreboard exists for player, returns true.
     */
    public static boolean hasScoreboard(@Nonnull UUID uid) {
        return HScoreboardHandler.has(uid);
    }

    /**
     * Finds a created scoreboard
     *
     * @param player scoreboard id that you want
     * @return scoreboard from id
     */
    @Nonnull
    public static Optional<HScoreboard> findScoreboardByPlayer(@Nonnull Player player) {
        return HScoreboardHandler.findByPlayer(player);
    }

    /**
     * Gets a created scoreboard
     *
     * @param player Player.
     * @return scoreboard from id
     */
    @Nonnull
    public static HScoreboard getScoreboardByPlayer(@Nonnull Player player) {
        return HScoreboardHandler.getByPlayer(player);
    }

    /**
     * Finds a created scoreboard
     *
     * @param uid UID of player.
     * @return scoreboard from id
     */
    @Nonnull
    public static Optional<HScoreboard> findScoreboardByUID(@Nonnull UUID uid) {
        return HScoreboardHandler.findByUID(uid);
    }

    /**
     * Gets a created scoreboard
     *
     * @param uid UID of player.
     * @return scoreboard from id
     */
    @Nonnull
    public static HScoreboard getScoreboardByUID(@Nonnull UUID uid) {
        return HScoreboardHandler.getByUID(uid);
    }

    /**
     * Creates new instance of HScoreboard as force.
     *
     * @param player Player.
     * @return new instance of HScoreboard.
     */
    @Nonnull
    public static HScoreboard forceCreateScoreboard(@Nonnull Player player, @Nonnull String title) {
        return HScoreboardHandler.forceCreate(player, title);
    }

    /**
     * Creates new instance of HScoreboard as force.
     *
     * @param uid UID of player.
     * @return new instance of HScoreboard.
     */
    @Nonnull
    public static HScoreboard forceCreateScoreboard(@Nonnull UUID uid, @Nonnull String title) {
        return HScoreboardHandler.forceCreate(uid, title);
    }

    /**
     * Creates new Instance of this class.
     *
     * @param player player
     * @return new instance of HScoreboard
     */
    @Nonnull
    public static HScoreboard createScoreboard(@Nonnull Player player, @Nonnull String title) {
        return HScoreboardHandler.create(player, title);
    }

    /**
     * Creates new Instance of this class.
     *
     * @param uid UID of player
     * @return new instance of HScoreboard
     */
    @Nonnull
    public static HScoreboard createScoreboard(@Nonnull UUID uid, @Nonnull String title) {
        return HScoreboardHandler.create(uid, title);
    }


    /*
    NPC
     */

    /**
     * Gets content as safe.
     *
     * @return npc map.
     */
    @Nonnull
    public static Map<String, HNPC> getNpcContentSafe() {
        return HNPCHandler.getContentSafe();
    }

    /**
     * Gets content.
     *
     * @return npc map.
     */
    @Nonnull
    public static Map<String, HNPC> getNpcContent() {
        return HNPCHandler.getContent();
    }

    /**
     * Gets npc list as safe.
     *
     * @return npc.
     */
    @Nonnull
    public static Collection<HNPC> getNpcValuesSafe() {
        return HNPCHandler.getValuesSafe();
    }

    /**
     * Gets npc list.
     *
     * @return NPC.
     */
    @Nonnull
    public static Collection<HNPC> getNpcValues() {
        return HNPCHandler.getValues();
    }

    /**
     * Checks if npc exists.
     *
     * @param id Npc id.
     * @return True if exists.
     */
    public static boolean hasNpc(@Nonnull String id) {
        return HNPCHandler.has(id);
    }

    /**
     * Finds a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Optional<HNPC> findNpcByID(@Nonnull String id) {
        return HNPCHandler.findByID(id);
    }

    /**
     * Gets a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static HNPC getNpcByID(@Nonnull String id) {
        return HNPCHandler.getByID(id);
    }

    /**
     * Finds a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Optional<HNPC> findNpcByEntityID(int id) {
        return HNPCHandler.findByEntityID(id);
    }

    /**
     * Gets a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static HNPC getNpcByEntityID(int id) {
        return HNPCHandler.getByEntityID(id);
    }

    /**
     * Deletes a npc with given id.
     *
     * @param id NPC id.
     * @return Deleted NPC.
     */
    @Nonnull
    public static HNPC deleteNpc(@Nonnull String id) {
        return HNPCHandler.delete(id);
    }

    /**
     * Creates a new HNpcBuilder instance.
     *
     * @param id NPC id.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public static HNpcBuilder npcBuilder(@Nonnull String id) {
        return HNPCHandler.npcBuilder(id);
    }


    /*
    ITEM
     */

    /**
     * Gets NbtManager object.
     *
     * @return NbtManager object.
     */
    @Nonnull
    public static HNbtManager getNbtManager() {
        return HItemBuilder.getNbtManager();
    }

    /**
     * Creates new item stack builder.
     *
     * @param type Material.
     * @return New instance of HItemBuilder.
     */
    @Nonnull
    public static HItemBuilder itemBuilder(@Nonnull Material type) {
        return new HItemBuilder(type);
    }

    /**
     * Creates new item stack builder.
     *
     * @param type   Material.
     * @param amount Amount.
     * @return New instance of HItemBuilder.
     */
    @Nonnull
    public static HItemBuilder itemBuilder(@Nonnull Material type, int amount) {
        return new HItemBuilder(type, amount);
    }

    /**
     * Creates new item stack builder.
     *
     * @param type       Material.
     * @param amount     Amount.
     * @param durability Durability.
     * @return New instance of HItemBuilder.
     */
    @Nonnull
    public static HItemBuilder itemBuilder(@Nonnull Material type, int amount, short durability) {
        return new HItemBuilder(type, amount, durability);
    }

    /**
     * Creates new item stack builder.
     *
     * @param itemStack Item stack.
     * @return New instance of HItemBuilder.
     */
    @Nonnull
    public static HItemBuilder itemBuilder(@Nonnull ItemStack itemStack) {
        return new HItemBuilder(itemStack);
    }

    /**
     * Creates new item stack builder.
     *
     * @param itemBuilder Item builder.
     * @return New instance of HItemBuilder.
     */
    @Nonnull
    public static HItemBuilder itemBuilder(@Nonnull HItemBuilder itemBuilder) {
        return new HItemBuilder(itemBuilder);
    }

    /**
     * Creates new item stack builder.
     *
     * @return New instance of HSkullBuilder.
     */
    @Nonnull
    public static HSkullBuilder skullBuilder() {
        return new HSkullBuilder();
    }

    /**
     * Creates new item stack builder.
     *
     * @param amount Amount.
     * @return New instance of HSkullBuilder.
     */
    @Nonnull
    public static HSkullBuilder skullBuilder(int amount) {
        return new HSkullBuilder(amount);
    }

    /**
     * Creates new item stack builder.
     *
     * @param skullBuilder Skull builder.
     * @return New instance of HSkullBuilder.
     */
    @Nonnull
    public static HSkullBuilder skullBuilder(@Nonnull HSkullBuilder skullBuilder) {
        return new HSkullBuilder(skullBuilder);
    }

    /**
     * Creates new item stack builder.
     *
     * @param texture Texture of skull.
     * @return New instance of HSkullBuilder.
     */
    @Nonnull
    public static HSkullBuilder skullBuilder(@Nullable String texture) {
        return new HSkullBuilder().texture(texture);
    }

    /**
     * Creates new item stack builder.
     *
     * @param owner Owner of texture.
     * @return New instance of HSkullBuilder.
     */
    @Nonnull
    public static HSkullBuilder skullBuilderByPlayer(@Nullable String owner) {
        return new HSkullBuilder().textureByPlayer(owner);
    }

    /**
     * Creates new item stack builder.
     *
     * @param owner Owner of texture.
     * @return New instance of HSkullBuilder.
     */
    @Nonnull
    public static HSkullBuilder skullBuilderByPlayer(@Nullable Player owner) {
        return new HSkullBuilder().textureByPlayer(owner);
    }


    /*
    WORLD BORDER
     */

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HWorldBorder> getBorderValuesSafe() {
        return HWorldBorderHandler.getValuesSafe();
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HWorldBorder> getBorderValues() {
        return HWorldBorderHandler.getValues();
    }

    /**
     * Finds world border from player.
     *
     * @param player Player.
     * @return HWorldBorder from player as optional.
     */
    @Nonnull
    public static Optional<HWorldBorder> findBorderByPlayer(@Nonnull Player player) {
        return HWorldBorderHandler.findByPlayer(player);
    }

    /**
     * Gets world border from player.
     *
     * @param player Player.
     * @return HWorldBorder from player.
     */
    @Nonnull
    public static HWorldBorder getBorderByPlayer(@Nonnull Player player) {
        return HWorldBorderHandler.getByPlayer(player);
    }

    /**
     * Creates world border.
     *
     * @param location        Center location.
     * @param size            Size of world border.
     * @param damageAmount    Damage amount.
     * @param damageBuffer    Damage buffer.
     * @param warningDistance Warning distance.
     * @param warningTime     Warning time.
     * @param color           Color of border.
     * @return Created world border.
     */
    @Nonnull
    public static HWorldBorder createBorder(@Nonnull Location location, double size, double damageAmount, double damageBuffer, int warningDistance, int warningTime, @Nonnull HBorderColor color) {
        return HWorldBorderHandler.create(location, size, damageAmount, damageBuffer, warningDistance, warningTime, color);
    }


    /*
    HOLOGRAM
     */

    /**
     * Gets content as safe.
     *
     * @return holograms map.
     */
    @Nonnull
    public static Map<String, HHologram> getHologramContentSafe() {
        return HHologramHandler.getContentSafe();
    }

    /**
     * Gets content.
     *
     * @return holograms map.
     */
    @Nonnull
    public static Map<String, HHologram> getHologramContent() {
        return HHologramHandler.getContent();
    }

    /**
     * Gets holograms as safe.
     *
     * @return holograms.
     */
    @Nonnull
    public static Collection<HHologram> getHologramValuesSafe() {
        return HHologramHandler.getValuesSafe();
    }

    /**
     * Gets holograms.
     *
     * @return holograms.
     */
    @Nonnull
    public static Collection<HHologram> getHologramValues() {
        return HHologramHandler.getValues();
    }

    /**
     * Checks if hologram exists.
     *
     * @param id Hologram id.
     * @return True if hologram exists.
     */
    public static boolean hasHologram(@Nonnull String id) {
        return HHologramHandler.has(id);
    }

    /**
     * Finds a created hologram
     *
     * @param id hologram id that you want
     * @return hologram from id
     */
    @Nonnull
    public static Optional<HHologram> findHologramByID(@Nonnull String id) {
        return HHologramHandler.findByID(id);
    }

    /**
     * Gets a created hologram
     *
     * @param id hologram id that you want
     * @return hologram from id
     */
    @Nonnull
    public static HHologram getHologramByID(@Nonnull String id) {
        return HHologramHandler.getByID(id);
    }

    /**
     * Creates a new hologram
     *
     * @param id       hologram id
     * @param location location
     * @param players  player list
     * @return new hologram
     */
    @Nonnull
    public static HHologram createHologram(@Nonnull String id, @Nonnull Location location, @Nullable Set<UUID> players) {
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
    public static HHologram createHologram(@Nonnull String id, @Nonnull Location location) {
        return HHologramHandler.create(id, location);
    }

    /**
     * Deletes hologram by id
     *
     * @param id id of hologram
     * @return hologram to be deleted
     */
    @Nonnull
    public static HHologram deleteHologram(@Nonnull String id) {
        return HHologramHandler.delete(id);
    }


    /*
    DATABASE
     */

    /**
     * Registers database provider.
     *
     * @param objectClass Database object class.
     * @param provider    Database provider.
     * @param <T>         Database object class.
     */
    @Nonnull
    public static <T extends DatabaseObject> DatabaseProvider<T> registerDatabaseProvider(@Nonnull Class<T> objectClass, @Nonnull DatabaseProvider<T> provider) {
        return DatabaseHandler.registerProvider(objectClass, provider);
    }

    /**
     * Finds database provider.
     *
     * @param objectClass Database object class.
     * @param <T>         Database object class.
     * @return Database provider.
     */
    @Nonnull
    public static <T extends DatabaseObject> Optional<DatabaseProvider<T>> findDatabaseProvider(@Nonnull Class<T> objectClass) {
        return DatabaseHandler.findProvider(objectClass);
    }

    /**
     * Gets database provider.
     *
     * @param objectClass Database object class.
     * @param <T>         Database object class.
     * @return Database provider.
     */
    @Nonnull
    public static <T extends DatabaseObject> DatabaseProvider<T> getDatabaseProvider(@Nonnull Class<T> objectClass) {
        return DatabaseHandler.getProvider(objectClass);
    }


    /*
    SPAM CONTROLLER
     */

    /**
     * Gets difference between now
     * and end time of spam as millisecond.
     *
     * @param id The id to check.
     * @return Difference between now
     * and end time of spam as millisecond.
     */
    public static long remainTime(@Nonnull String id) {
        return HSpam.remainTime(id);
    }

    /**
     * Gets difference between now
     * and end time of spam as unit.
     *
     * @param id   The id to check.
     * @param unit The time unit.
     * @return Difference between now
     * and end time of spam as unit.
     */
    public static long remainTime(@Nonnull String id, @Nonnull TimeUnit unit) {
        return HSpam.remainTime(id, unit);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id   The id to check.
     * @param time Time.
     * @param unit Time unit.
     * @return True if spamming.
     */
    public static boolean spam(@Nonnull String id, int time, @Nonnull TimeUnit unit) {
        return HSpam.spam(id, time, unit);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id       The id to check.
     * @param duration The duration to check.
     * @return True if spamming.
     */
    public static boolean spam(@Nonnull String id, @Nonnull Duration duration) {
        return HSpam.spam(id, duration);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id    The id to check.
     * @param ticks The time in ticks.
     * @return True if spamming.
     */
    public static boolean spam(@Nonnull String id, long ticks) {
        return HSpam.spam(id, ticks);
    }


    /*
    SERIALIZER
     */

    /**
     * Serializes object.
     *
     * @param object Object.
     * @return Serialized string as optional.
     */
    @Nonnull
    public synchronized static Optional<String> serializeSafe(@Nonnull Object object) {
        return Serializer.serializeSafe(object);
    }

    /**
     * Serializes object.
     *
     * @param object Object.
     * @return Serialized string.
     */
    @Nonnull
    public synchronized static String serialize(@Nonnull Object object) {
        return Serializer.serialize(object);
    }

    /**
     * Deserializes object.
     *
     * @param serializedText Text that want to deserialize.
     * @param clazz          Object type class.
     * @param <T>            Object type.
     * @return Deserialized object as optional.
     */
    @Nonnull
    public synchronized static <T> Optional<T> deserializeSafe(@Nonnull String serializedText, @Nonnull Class<T> clazz) {
        return Serializer.deserializeSafe(serializedText, clazz);
    }

    /**
     * Deserializes object.
     *
     * @param serializedText Text that want to deserialize.
     * @param clazz          Object type class.
     * @param <T>            Object type.
     * @return Deserialized object as optional.
     */
    @Nonnull
    public synchronized static <T> T deserialize(@Nonnull String serializedText, @Nonnull Class<T> clazz) {
        return Serializer.deserialize(serializedText, clazz);
    }
}