package com.hakan.core;

import com.hakan.core.border.Border;
import com.hakan.core.border.BorderHandler;
import com.hakan.core.border.builder.BorderBuilder;
import com.hakan.core.command.CommandHandler;
import com.hakan.core.configuration.ConfigHandler;
import com.hakan.core.configuration.containers.ConfigContainer;
import com.hakan.core.dependency.DependencyHandler;
import com.hakan.core.hologram.Hologram;
import com.hakan.core.hologram.HologramHandler;
import com.hakan.core.hologram.builder.HologramBuilder;
import com.hakan.core.item.ItemBuilder;
import com.hakan.core.item.nbt.NbtManager;
import com.hakan.core.item.skull.SkullBuilder;
import com.hakan.core.listener.ListenerAdapter;
import com.hakan.core.message.MessageHandler;
import com.hakan.core.message.bossbar.BossBar;
import com.hakan.core.message.bossbar.meta.BarColor;
import com.hakan.core.message.bossbar.meta.BarFlag;
import com.hakan.core.message.bossbar.meta.BarStyle;
import com.hakan.core.message.title.Title;
import com.hakan.core.npc.Npc;
import com.hakan.core.npc.NpcHandler;
import com.hakan.core.npc.builder.NpcBuilder;
import com.hakan.core.packet.PacketHandler;
import com.hakan.core.particle.Particle;
import com.hakan.core.particle.ParticleHandler;
import com.hakan.core.protocol.ProtocolVersion;
import com.hakan.core.scheduler.Scheduler;
import com.hakan.core.scoreboard.Scoreboard;
import com.hakan.core.scoreboard.ScoreboardHandler;
import com.hakan.core.spam.Spam;
import com.hakan.core.ui.Gui;
import com.hakan.core.ui.GuiHandler;
import com.hakan.core.ui.anvil.AnvilGui;
import com.hakan.core.ui.anvil.builder.AnvilBuilder;
import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.ui.inventory.builder.InventoryBuilder;
import com.hakan.core.ui.sign.SignGui;
import com.hakan.core.ui.sign.builder.SignBuilder;
import com.hakan.core.utils.Serializer;
import com.hakan.core.utils.Validate;
import com.hakan.core.utils.hooks.Metrics;
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
        return INSTANCE;
    }

    /**
     * Sets instance plugin of hCore.
     *
     * @param plugin Instance.
     */
    public static void setInstance(@Nonnull JavaPlugin plugin) {
        INSTANCE = Validate.notNull(plugin, "instance cannot be null!");
        VERSION = ProtocolVersion.getCurrentVersion();
    }

    /**
     * Initializes all APIs.
     *
     * @param plugin Instance of the main class.
     */
    public static void initialize(@Nonnull JavaPlugin plugin) {
        if (INSTANCE != null) return;

        HCore.setInstance(plugin);
        Metrics.initialize(plugin);

        PacketHandler.initialize();
        GuiHandler.initialize();
        NpcHandler.initialize();
        ItemBuilder.initialize();
        BorderHandler.initialize();
        MessageHandler.initialize();
        ParticleHandler.initialize();
        HologramHandler.initialize();
        ScoreboardHandler.initialize();
    }


    /*
    OTHERS
     */

    /**
     * Gets the protocol version
     * of the current server.
     *
     * @return Protocol version.
     */
    @Nonnull
    public static ProtocolVersion getProtocolVersion() {
        return VERSION;
    }

    /**
     * Gets version string (example: v1_8_R3)
     *
     * @return Version string.
     */
    @Nonnull
    public static String getVersionString() {
        return VERSION.getKey();
    }


    /*
    SCHEDULER
     */

    /**
     * Creates scheduler.
     *
     * @param async Async status.
     * @return Scheduler.
     */
    @Nonnull
    public static Scheduler scheduler(boolean async) {
        return new Scheduler(INSTANCE, async);
    }

    /**
     * Creates async scheduler.
     *
     * @return Scheduler.
     */
    @Nonnull
    public static Scheduler asyncScheduler() {
        return HCore.scheduler(true);
    }

    /**
     * Creates sync scheduler.
     *
     * @return Scheduler.
     */
    @Nonnull
    public static Scheduler syncScheduler() {
        return HCore.scheduler(false);
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
    public static Map<UUID, Gui> getGUIContentSafe() {
        return GuiHandler.getContentSafe();
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, Gui> getGUIContent() {
        return GuiHandler.getContent();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Gui> getGUIValuesSafe() {
        return GuiHandler.getValuesSafe();
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Gui> getGUIValues() {
        return GuiHandler.getValues();
    }

    /**
     * Finds InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<Gui> findGUIByPlayer(@Nonnull Player player) {
        return GuiHandler.findByPlayer(player);
    }

    /**
     * Gets InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui.
     */
    @Nonnull
    public static Gui getGUIByPlayer(@Nonnull Player player) {
        return GuiHandler.getByPlayer(player);
    }

    /**
     * Finds InventoryGui by UID.
     *
     * @param uid Player UID.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<Gui> findGUIByUID(@Nonnull UUID uid) {
        return GuiHandler.findByUID(uid);
    }

    /**
     * Gets InventoryGui by UID.
     *
     * @param uid Player UId.
     * @return InventoryGui.
     */
    @Nonnull
    public static Gui getGUIByUID(@Nonnull UUID uid) {
        return GuiHandler.getByUID(uid);
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, InventoryGui> getInventoryContentSafe() {
        return GuiHandler.getInventoryContentSafe();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<InventoryGui> getInventoryValuesSafe() {
        return GuiHandler.getInventoryValuesSafe();
    }

    /**
     * Finds InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<InventoryGui> findInventoryByPlayer(@Nonnull Player player) {
        return GuiHandler.findInventoryByPlayer(player);
    }

    /**
     * Gets InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui.
     */
    @Nonnull
    public static InventoryGui getInventoryByPlayer(@Nonnull Player player) {
        return GuiHandler.getInventoryByPlayer(player);
    }

    /**
     * Finds InventoryGui by UID.
     *
     * @param uid Player UID.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<InventoryGui> findInventoryByUID(@Nonnull UUID uid) {
        return GuiHandler.findInventoryByUID(uid);
    }

    /**
     * Gets InventoryGui by UID.
     *
     * @param uid Player UId.
     * @return InventoryGui.
     */
    @Nonnull
    public static InventoryGui getInventoryByUID(@Nonnull UUID uid) {
        return GuiHandler.getInventoryByUID(uid);
    }

    /**
     * Creates builder with ID.
     *
     * @param id ID.
     * @return InventoryBuilder.
     */
    @Nonnull
    public static InventoryBuilder inventoryBuilder(@Nonnull String id) {
        return GuiHandler.inventoryBuilder(id);
    }

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, SignGui> getSignContentSafe() {
        return GuiHandler.getSignContentSafe();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<SignGui> getSignValuesSafe() {
        return GuiHandler.getSignValuesSafe();
    }

    /**
     * Finds SignGui by player.
     *
     * @param player Player.
     * @return SignGui as optional.
     */
    @Nonnull
    public static Optional<SignGui> findSignByPlayer(@Nonnull Player player) {
        return GuiHandler.findSignByPlayer(player);
    }

    /**
     * Gets SignGui by player.
     *
     * @param player Player.
     * @return SignGui.
     */
    @Nonnull
    public static SignGui getSignByPlayer(@Nonnull Player player) {
        return GuiHandler.getSignByPlayer(player);
    }

    /**
     * Finds SignGui by player UID.
     *
     * @param uid Player UID.
     * @return SignGui as optional.
     */
    @Nonnull
    public static Optional<SignGui> findSignByUID(@Nonnull UUID uid) {
        return GuiHandler.findSignByUID(uid);
    }

    /**
     * Gets SignGui by player UID.
     *
     * @param uid Player UID.
     * @return SignGui.
     */
    @Nonnull
    public static SignGui getSignByUID(@Nonnull UUID uid) {
        return GuiHandler.getSignByUID(uid);
    }

    /**
     * Creates sign builder.
     *
     * @param player Player.
     * @return SignBuilder.
     */
    @Nonnull
    public static SignBuilder signBuilder(@Nonnull Player player) {
        return GuiHandler.signBuilder(player);
    }

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, AnvilGui> getAnvilContentSafe() {
        return GuiHandler.getAnvilContentSafe();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<AnvilGui> getAnvilValuesSafe() {
        return GuiHandler.getAnvilValuesSafe();
    }

    /**
     * Finds SignGui by player.
     *
     * @param player Player.
     * @return SignGui as optional.
     */
    @Nonnull
    public static Optional<AnvilGui> findAnvilByPlayer(@Nonnull Player player) {
        return GuiHandler.findAnvilByPlayer(player);
    }

    /**
     * Gets SignGui by player.
     *
     * @param player Player.
     * @return SignGui.
     */
    @Nonnull
    public static AnvilGui getAnvilByPlayer(@Nonnull Player player) {
        return GuiHandler.getAnvilByPlayer(player);
    }

    /**
     * Finds SignGui by player UID.
     *
     * @param uid Player UID.
     * @return SignGui as optional.
     */
    @Nonnull
    public static Optional<AnvilGui> findAnvilByUID(@Nonnull UUID uid) {
        return GuiHandler.findAnvilByUID(uid);
    }

    /**
     * Gets SignGui by player UID.
     *
     * @param uid Player UID.
     * @return SignGui.
     */
    @Nonnull
    public static AnvilGui getAnvilByUID(@Nonnull UUID uid) {
        return GuiHandler.getAnvilByUID(uid);
    }

    /**
     * Creates sign builder.
     *
     * @param player Player.
     * @return SignBuilder.
     */
    @Nonnull
    public static AnvilBuilder anvilBuilder(@Nonnull Player player) {
        return GuiHandler.anvilBuilder(player);
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
        CommandHandler.register(adapters);
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
    public static <T extends Event> ListenerAdapter<T> registerEvent(@Nonnull Class<T> eventClass) {
        return new ListenerAdapter<>(eventClass);
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
        PacketHandler.findByPlayer(player).ifPresent(packetPlayer -> packetPlayer.send(packets));
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
     * @param particle Particle class.
     */
    public static void playParticle(@Nonnull Player player, @Nonnull Location location, @Nonnull Particle particle) {
        ParticleHandler.play(player, location, particle);
    }

    /**
     * Plays particle for players.
     *
     * @param players  Players.
     * @param location Location.
     * @param particle Particle class.
     */
    public static void playParticle(@Nonnull Collection<Player> players, @Nonnull Location location, @Nonnull Particle particle) {
        ParticleHandler.play(players, location, particle);
    }

    /**
     * Plays particle for all players.
     *
     * @param location Location.
     * @param particle Particle class.
     */
    public static void playParticle(@Nonnull Location location, @Nonnull Particle particle) {
        ParticleHandler.play(location, particle);
    }


    /*
    MESSAGE
     */

    /**
     * Sends title to player.
     *
     * @param player Player.
     * @param title  Title class.
     */
    public static void sendTitle(@Nonnull Player player, @Nonnull Title title) {
        MessageHandler.sendTitle(player, title);
    }

    /**
     * Sends title to player.
     *
     * @param player   Player.
     * @param title    Title.
     * @param subTitle Subtitle.
     */
    public static void sendTitle(@Nonnull Player player, @Nonnull String title, @Nonnull String subTitle) {
        MessageHandler.sendTitle(player, title, subTitle);
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
        MessageHandler.sendTitle(player, title, subTitle, stay, fadein, fadeout);
    }

    /**
     * Sends title to players.
     *
     * @param players Players.
     * @param title   Title class.
     */
    public static void sendTitle(@Nonnull Collection<Player> players, @Nonnull Title title) {
        MessageHandler.sendTitle(players, title);
    }

    /**
     * Sends title to players.
     *
     * @param players  Players.
     * @param title    Title.
     * @param subTitle Subtitle.
     */
    public static void sendTitle(@Nonnull Collection<Player> players, @Nonnull String title, @Nonnull String subTitle) {
        MessageHandler.sendTitle(players, title, subTitle);
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
        MessageHandler.sendTitle(players, title, subTitle, stay, fadein, fadeout);
    }

    /**
     * Sends action bar to player.
     *
     * @param player Player.
     * @param text   Text.
     */
    public static void sendActionBar(@Nonnull Player player, @Nonnull String text) {
        MessageHandler.sendActionBar(player, text);
    }

    /**
     * Sends action bar to players.
     *
     * @param players Players.
     * @param text    Text.
     */
    public static void sendActionBar(@Nonnull Collection<Player> players, @Nonnull String text) {
        MessageHandler.sendActionBar(players, text);
    }

    /**
     * Gets bossbar list as safe.
     *
     * @return Bossbar list.
     */
    @Nonnull
    public static List<BossBar> getBossBarsSafe() {
        return MessageHandler.getBossBarsSafe();
    }

    /**
     * Gets bossbar list.
     *
     * @return Bossbar list.
     */
    @Nonnull
    public static List<BossBar> getBossBars() {
        return MessageHandler.getBossBars();
    }

    /**
     * Gets list of bossbars from player.
     *
     * @param player Player.
     * @return List of boss bars.
     */
    @Nonnull
    public static List<BossBar> getBossBarsByPlayer(@Nonnull Player player) {
        return MessageHandler.getBossBarsByPlayer(player);
    }

    /**
     * Finds first bossbar from player.
     *
     * @param player Player.
     * @return Bossbar as optional.
     */
    @Nonnull
    public static Optional<BossBar> findFirstBossBarByPlayer(@Nonnull Player player) {
        return MessageHandler.findFirstBossBarByPlayer(player);
    }

    /**
     * Gets first bossbar from player.
     *
     * @param player Player.
     * @return Bossbar.
     */
    @Nonnull
    public static BossBar getFirstBossBarByPlayer(@Nonnull Player player) {
        return MessageHandler.getFirstBossBarByPlayer(player);
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
    public static BossBar createBossBar(@Nonnull String title, @Nonnull BarColor color, @Nonnull BarStyle style, @Nonnull BarFlag... flags) {
        return MessageHandler.createBossBar(title, color, style, flags);
    }

    /**
     * Creates bossbar.
     *
     * @param title Title.
     * @param color Color.
     * @param style Style.
     * @return New instance of BossBar.
     */
    public static BossBar createBossBar(@Nonnull String title, @Nonnull BarColor color, @Nonnull BarStyle style) {
        return MessageHandler.createBossBar(title, color, style);
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
    public static Map<UUID, Scoreboard> getScoreboardContentSafe() {
        return ScoreboardHandler.getContentSafe();
    }

    /**
     * Gets scoreboard map.
     *
     * @return Scoreboard map.
     */
    @Nonnull
    public static Map<UUID, Scoreboard> getScoreboardContent() {
        return ScoreboardHandler.getContent();
    }

    /**
     * Gets all scoreboards as safe.
     *
     * @return Scoreboard list.
     */
    @Nonnull
    public static Collection<Scoreboard> getScoreboardValuesSafe() {
        return ScoreboardHandler.getValuesSafe();
    }

    /**
     * Gets all scoreboards.
     *
     * @return Scoreboard list.
     */
    @Nonnull
    public static Collection<Scoreboard> getScoreboardValues() {
        return ScoreboardHandler.getValues();
    }

    /**
     * Checks if scoreboard exists for player.
     *
     * @param uid UID of player.
     * @return if scoreboard exists for player, returns true.
     */
    public static boolean hasScoreboard(@Nonnull UUID uid) {
        return ScoreboardHandler.has(uid);
    }

    /**
     * Finds a created scoreboard
     *
     * @param player scoreboard id that you want
     * @return Scoreboard from player.
     */
    @Nonnull
    public static Optional<Scoreboard> findScoreboardByPlayer(@Nonnull Player player) {
        return ScoreboardHandler.findByPlayer(player);
    }

    /**
     * Gets a created scoreboard
     *
     * @param player Player.
     * @return Scoreboard from player.
     */
    @Nonnull
    public static Scoreboard getScoreboardByPlayer(@Nonnull Player player) {
        return ScoreboardHandler.getByPlayer(player);
    }

    /**
     * Finds a created scoreboard
     *
     * @param uid UID of player.
     * @return Scoreboard from uid.
     */
    @Nonnull
    public static Optional<Scoreboard> findScoreboardByUID(@Nonnull UUID uid) {
        return ScoreboardHandler.findByUID(uid);
    }

    /**
     * Gets a created scoreboard
     *
     * @param uid UID of player.
     * @return Scoreboard from uid.
     */
    @Nonnull
    public static Scoreboard getScoreboardByUID(@Nonnull UUID uid) {
        return ScoreboardHandler.getByUID(uid);
    }

    /**
     * Creates new instance of Scoreboard as force.
     *
     * @param player Player.
     * @param title  Title.
     * @return new instance of Scoreboard.
     */
    @Nonnull
    public static Scoreboard forceCreateScoreboard(@Nonnull Player player, @Nonnull String title) {
        return ScoreboardHandler.forceCreate(player, title);
    }

    /**
     * Creates new instance of Scoreboard as force.
     *
     * @param uid   UID of player.
     * @param title Title.
     * @return new instance of Scoreboard.
     */
    @Nonnull
    public static Scoreboard forceCreateScoreboard(@Nonnull UUID uid, @Nonnull String title) {
        return ScoreboardHandler.forceCreate(uid, title);
    }

    /**
     * Creates new Instance of this class.
     *
     * @param player Player.
     * @param title  Title.
     * @return new instance of Scoreboard.
     */
    @Nonnull
    public static Scoreboard createScoreboard(@Nonnull Player player, @Nonnull String title) {
        return ScoreboardHandler.create(player, title);
    }

    /**
     * Creates new Instance of this class.
     *
     * @param uid   UID of player.
     * @param title Title.
     * @return new instance of Scoreboard.
     */
    @Nonnull
    public static Scoreboard createScoreboard(@Nonnull UUID uid, @Nonnull String title) {
        return ScoreboardHandler.create(uid, title);
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
    public static Map<String, Npc> getNpcContentSafe() {
        return NpcHandler.getContentSafe();
    }

    /**
     * Gets content.
     *
     * @return npc map.
     */
    @Nonnull
    public static Map<String, Npc> getNpcContent() {
        return NpcHandler.getContent();
    }

    /**
     * Gets npc list as safe.
     *
     * @return npc.
     */
    @Nonnull
    public static Collection<Npc> getNpcValuesSafe() {
        return NpcHandler.getValuesSafe();
    }

    /**
     * Gets npc list.
     *
     * @return NPC.
     */
    @Nonnull
    public static Collection<Npc> getNpcValues() {
        return NpcHandler.getValues();
    }

    /**
     * Checks if npc exists.
     *
     * @param id Npc id.
     * @return True if exists.
     */
    public static boolean hasNpc(@Nonnull String id) {
        return NpcHandler.has(id);
    }

    /**
     * Finds a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Optional<Npc> findNpcByID(@Nonnull String id) {
        return NpcHandler.findByID(id);
    }

    /**
     * Gets a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Npc getNpcByID(@Nonnull String id) {
        return NpcHandler.getByID(id);
    }

    /**
     * Finds a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Optional<Npc> findNpcByEntityID(int id) {
        return NpcHandler.findByEntityID(id);
    }

    /**
     * Gets a created npc.
     *
     * @param id NPC id that you want.
     * @return NPC from id.
     */
    @Nonnull
    public static Npc getNpcByEntityID(int id) {
        return NpcHandler.getByEntityID(id);
    }

    /**
     * Deletes a npc with given id.
     *
     * @param id NPC id.
     * @return Deleted NPC.
     */
    @Nonnull
    public static Npc deleteNpc(@Nonnull String id) {
        return NpcHandler.delete(id);
    }

    /**
     * Creates a new NpcBuilder instance.
     *
     * @param id NPC id.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public static NpcBuilder npcBuilder(@Nonnull String id) {
        return NpcHandler.npcBuilder(id);
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
    public static NbtManager getNbtManager() {
        return ItemBuilder.getNbtManager();
    }

    /**
     * Creates new item stack builder.
     *
     * @param type Material.
     * @return New instance of ItemBuilder.
     */
    @Nonnull
    public static ItemBuilder itemBuilder(@Nonnull Material type) {
        return new ItemBuilder(type);
    }

    /**
     * Creates new item stack builder.
     *
     * @param type   Material.
     * @param amount Amount.
     * @return New instance of ItemBuilder.
     */
    @Nonnull
    public static ItemBuilder itemBuilder(@Nonnull Material type, int amount) {
        return new ItemBuilder(type, amount);
    }

    /**
     * Creates new item stack builder.
     *
     * @param type       Material.
     * @param amount     Amount.
     * @param durability Durability.
     * @return New instance of ItemBuilder.
     */
    @Nonnull
    public static ItemBuilder itemBuilder(@Nonnull Material type, int amount, short durability) {
        return new ItemBuilder(type, amount, durability);
    }

    /**
     * Creates new item stack builder.
     *
     * @param itemStack Item stack.
     * @return New instance of ItemBuilder.
     */
    @Nonnull
    public static ItemBuilder itemBuilder(@Nonnull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    /**
     * Creates new item stack builder.
     *
     * @param itemBuilder Item builder.
     * @return New instance of ItemBuilder.
     */
    @Nonnull
    public static ItemBuilder itemBuilder(@Nonnull ItemBuilder itemBuilder) {
        return new ItemBuilder(itemBuilder);
    }

    /**
     * Creates new item stack builder.
     *
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilder() {
        return new SkullBuilder();
    }

    /**
     * Creates new item stack builder.
     *
     * @param amount Amount.
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilder(int amount) {
        return new SkullBuilder(amount);
    }

    /**
     * Creates new item stack builder.
     *
     * @param skullBuilder Skull builder.
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilder(@Nonnull SkullBuilder skullBuilder) {
        return new SkullBuilder(skullBuilder);
    }

    /**
     * Creates new item stack builder.
     *
     * @param texture Texture of skull.
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilder(@Nullable String texture) {
        return new SkullBuilder().texture(texture);
    }

    /**
     * Creates new item stack builder.
     *
     * @param owner Owner of texture.
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilderByPlayer(@Nullable String owner) {
        return new SkullBuilder().textureByPlayer(owner);
    }

    /**
     * Creates new item stack builder.
     *
     * @param owner Owner of texture.
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilderByPlayer(@Nullable Player owner) {
        return new SkullBuilder().textureByPlayer(owner);
    }


    /*
    WORLD BORDER
     */

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<Player, Border> getBorderContentSafe() {
        return BorderHandler.getContentSafe();
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<Player, Border> getBorderContent() {
        return BorderHandler.getContent();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Border> getBorderValuesSafe() {
        return BorderHandler.getValuesSafe();
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Border> getBorderValues() {
        return BorderHandler.getValues();
    }

    /**
     * Finds world border from player.
     *
     * @param player Player.
     * @return Border from player as optional.
     */
    @Nonnull
    public static Optional<Border> findBorderByPlayer(@Nonnull Player player) {
        return BorderHandler.findByPlayer(player);
    }

    /**
     * Gets world border from player.
     *
     * @param player Player.
     * @return Border from player.
     */
    @Nonnull
    public static Border getBorderByPlayer(@Nonnull Player player) {
        return BorderHandler.getByPlayer(player);
    }

    /**
     * Creates world border builder.
     *
     * @param viewer Viewer.
     * @return World border builder.
     */
    @Nonnull
    public static BorderBuilder borderBuilder(@Nonnull Player viewer) {
        return BorderHandler.builder(viewer);
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
    public static Map<String, Hologram> getHologramContentSafe() {
        return HologramHandler.getContentSafe();
    }

    /**
     * Gets content.
     *
     * @return holograms map.
     */
    @Nonnull
    public static Map<String, Hologram> getHologramContent() {
        return HologramHandler.getContent();
    }

    /**
     * Gets holograms as safe.
     *
     * @return holograms.
     */
    @Nonnull
    public static Collection<Hologram> getHologramValuesSafe() {
        return HologramHandler.getValuesSafe();
    }

    /**
     * Gets holograms.
     *
     * @return holograms.
     */
    @Nonnull
    public static Collection<Hologram> getHologramValues() {
        return HologramHandler.getValues();
    }

    /**
     * Checks if hologram exists.
     *
     * @param id Hologram id.
     * @return True if hologram exists.
     */
    public static boolean hasHologram(@Nonnull String id) {
        return HologramHandler.has(id);
    }

    /**
     * Finds a created hologram
     *
     * @param id hologram id that you want
     * @return hologram from id
     */
    @Nonnull
    public static Optional<Hologram> findHologramByID(@Nonnull String id) {
        return HologramHandler.findByID(id);
    }

    /**
     * Gets a created hologram
     *
     * @param id hologram id that you want
     * @return hologram from id
     */
    @Nonnull
    public static Hologram getHologramByID(@Nonnull String id) {
        return HologramHandler.getByID(id);
    }

    /**
     * Creates a hologram builder.
     *
     * @param id Hologram id.
     * @return Created hologram.
     */
    @Nonnull
    public static HologramBuilder hologramBuilder(@Nonnull String id) {
        return HologramHandler.builder(id);
    }


    /*
    CONFIGURATION
     */

    /**
     * Loads configuration container.
     *
     * @param configClass Config class.
     * @return Config container class.
     */
    @Nonnull
    public static <T extends ConfigContainer> T loadConfig(@Nonnull Object configClass) {
        return ConfigHandler.load(configClass);
    }

    /**
     * Loads config container.
     *
     * @param file Configuration container.
     * @return Configuration container.
     */
    @Nonnull
    public static <T extends ConfigContainer> T loadConfig(@Nonnull ConfigContainer file) {
        return ConfigHandler.load(file);
    }

    /**
     * Finds configuration container.
     *
     * @param path Configuration container path.
     * @return Configuration container.
     */
    @Nonnull
    public static Optional<ConfigContainer> findConfigByPath(@Nonnull String path) {
        return ConfigHandler.findByPath(path);
    }

    /**
     * Gets configuration file.
     *
     * @param path Configuration file container.
     * @return Configuration container.
     */
    @Nonnull
    public static ConfigContainer getConfigByPath(@Nonnull String path) {
        return ConfigHandler.getByPath(path);
    }


    /*
    DEPENDENCY
     */

    /**
     * Loads the dependencies of
     * the class to given path.
     *
     * @param object Instance of dependency class.
     */
    public static void loadDependencies(@Nonnull Object object) {
        DependencyHandler.load(object);
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
    public static long remainTimeSpam(@Nonnull String id) {
        return Spam.remainTime(id);
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
    public static long remainTimeSpam(@Nonnull String id, @Nonnull TimeUnit unit) {
        return Spam.remainTime(id, unit);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id   The id to check.
     * @param time Time.
     * @param unit Time unit.
     * @return True if spamming.
     */
    public static boolean checkSpam(@Nonnull String id, int time, @Nonnull TimeUnit unit) {
        return Spam.check(id, time, unit);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id       The id to check.
     * @param duration The duration to check.
     * @return True if spamming.
     */
    public static boolean checkSpam(@Nonnull String id, @Nonnull Duration duration) {
        return Spam.check(id, duration);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id    The id to check.
     * @param ticks The time in ticks.
     * @return True if spamming.
     */
    public static boolean checkSpam(@Nonnull String id, long ticks) {
        return Spam.check(id, ticks);
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
