package com.hakan.core.ui;

import com.hakan.core.HCore;
import com.hakan.core.packet.event.PacketEvent;
import com.hakan.core.ui.anvil.HAnvil;
import com.hakan.core.ui.anvil.builder.HAnvilBuilder;
import com.hakan.core.ui.anvil.listeners.AnvilClickListener;
import com.hakan.core.ui.anvil.listeners.AnvilCloseListener;
import com.hakan.core.ui.inventory.HInventory;
import com.hakan.core.ui.inventory.builder.HInventoryBuilder;
import com.hakan.core.ui.inventory.listeners.InventoryClickListener;
import com.hakan.core.ui.inventory.listeners.InventoryCloseListener;
import com.hakan.core.ui.sign.HSign;
import com.hakan.core.ui.sign.builder.HSignBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * GUIHandler class to handle all GUIs
 * (Signs, Anvils, Inventory, etc.)
 */
public final class GUIHandler {

    private static final Map<UUID, GUI> guiMap = new HashMap<>();

    /**
     * Initializes the inventory system.
     */
    public static void initialize() {

        //INVENTORY
        HCore.registerEvent(PlayerQuitEvent.class)
                .consume(event -> {
                    Player player = event.getPlayer();
                    GUIHandler.findInventoryByPlayer(player).ifPresent(gui -> gui.close(player));
                });

        HCore.registerEvent(PluginDisableEvent.class)
                .filter(event -> event.getPlugin().equals(HCore.getInstance()))
                .consume(event -> Bukkit.getOnlinePlayers()
                        .forEach(player -> GUIHandler.findInventoryByPlayer(player).ifPresent(gui -> gui.close(player))));

        HCore.registerListeners(
                new InventoryClickListener(),
                new InventoryCloseListener()
        );


        //ANVIL
        HCore.registerEvent(PlayerQuitEvent.class)
                .consume(event -> {
                    Player player = event.getPlayer();
                    GUIHandler.findAnvilByPlayer(player).ifPresent(HAnvil::close);
                });

        HCore.registerEvent(PluginDisableEvent.class)
                .filter(event -> event.getPlugin().equals(HCore.getInstance()))
                .consume(event -> Bukkit.getOnlinePlayers()
                        .forEach(player -> GUIHandler.findAnvilByPlayer(player).ifPresent(HAnvil::close)));

        HCore.registerListeners(
                new AnvilClickListener(),
                new AnvilCloseListener()
        );


        //SIGN
        HCore.registerEvent(PacketEvent.class)
                .filter(event -> event.getPacket().toString().contains("PacketPlayInUpdateSign"))
                .consume(event -> GUIHandler.findSignByPlayer(event.getPlayer())
                        .ifPresent(hSign -> hSign.listen(event.getPacket())));
    }


    /*
    GENERAL
     */

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, GUI> getContentSafe() {
        return new HashMap<>(GUIHandler.guiMap);
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, GUI> getContent() {
        return GUIHandler.guiMap;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<GUI> getValuesSafe() {
        return new ArrayList<>(GUIHandler.guiMap.values());
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<GUI> getValues() {
        return GUIHandler.guiMap.values();
    }

    /**
     * Finds HInventory by player.
     *
     * @param player Player.
     * @return HInventory as optional.
     */
    @Nonnull
    public static Optional<GUI> findByPlayer(@Nonnull Player player) {
        return GUIHandler.findByUID(player.getUniqueId());
    }

    /**
     * Gets HInventory by player.
     *
     * @param player Player.
     * @return HInventory.
     */
    @Nonnull
    public static GUI getByPlayer(@Nonnull Player player) {
        return GUIHandler.findByPlayer(player).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    /**
     * Finds HInventory by UID.
     *
     * @param uid Player UID.
     * @return HInventory as optional.
     */
    @Nonnull
    public static Optional<GUI> findByUID(@Nonnull UUID uid) {
        return Optional.ofNullable(GUIHandler.guiMap.get(Objects.requireNonNull(uid, "UID cannot be null!")));
    }

    /**
     * Gets HInventory by UID.
     *
     * @param uid Player UId.
     * @return HInventory.
     */
    @Nonnull
    public static GUI getByUID(@Nonnull UUID uid) {
        return GUIHandler.findByUID(uid).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }


    /*
    INVENTORY
     */

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, HInventory> getInventoryContentSafe() {
        Map<UUID, HInventory> map = new HashMap<>();
        for (Map.Entry<UUID, GUI> entry : GUIHandler.guiMap.entrySet())
            if (entry.getValue() instanceof HInventory)
                map.put(entry.getKey(), (HInventory) entry.getValue());
        return map;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HInventory> getInventoryValuesSafe() {
        List<HInventory> map = new ArrayList<>();
        for (GUI gui : GUIHandler.guiMap.values())
            if (gui instanceof HInventory)
                map.add((HInventory) gui);
        return map;
    }

    /**
     * Finds HInventory by player.
     *
     * @param player Player.
     * @return HInventory as optional.
     */
    @Nonnull
    public static Optional<HInventory> findInventoryByPlayer(@Nonnull Player player) {
        return GUIHandler.findInventoryByUID(player.getUniqueId());
    }

    /**
     * Gets HInventory by player.
     *
     * @param player Player.
     * @return HInventory.
     */
    @Nonnull
    public static HInventory getInventoryByPlayer(@Nonnull Player player) {
        return GUIHandler.findInventoryByPlayer(player).orElseThrow(() -> new NullPointerException("player " + player.getName() + " doesn't have a inventory!"));
    }

    /**
     * Finds HInventory by UID.
     *
     * @param uid Player UID.
     * @return HInventory as optional.
     */
    @Nonnull
    public static Optional<HInventory> findInventoryByUID(@Nonnull UUID uid) {
        GUI gui = GUIHandler.guiMap.get(Objects.requireNonNull(uid, "UID cannot be null!"));
        return (gui instanceof HInventory) ? Optional.of((HInventory) gui) : Optional.empty();
    }

    /**
     * Gets HInventory by UID.
     *
     * @param uid Player UId.
     * @return HInventory.
     */
    @Nonnull
    public static HInventory getInventoryByUID(@Nonnull UUID uid) {
        return GUIHandler.findInventoryByUID(uid).orElseThrow(() -> new NullPointerException("player " + uid + " doesn't have a inventory!"));
    }

    /**
     * Creates builder with ID.
     *
     * @param id ID.
     * @return HInventoryBuilder.
     */
    @Nonnull
    public static HInventoryBuilder inventoryBuilder(@Nonnull String id) {
        return new HInventoryBuilder(id);
    }


    /*
    SIGN
     */

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, HSign> getSignContentSafe() {
        Map<UUID, HSign> map = new HashMap<>();
        for (Map.Entry<UUID, GUI> entry : GUIHandler.guiMap.entrySet())
            if (entry.getValue() instanceof HSign)
                map.put(entry.getKey(), (HSign) entry.getValue());
        return map;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HSign> getSignValuesSafe() {
        List<HSign> map = new ArrayList<>();
        for (GUI gui : GUIHandler.guiMap.values())
            if (gui instanceof HSign)
                map.add((HSign) gui);
        return map;
    }

    /**
     * Finds HSign by player.
     *
     * @param player Player.
     * @return HSign as optional.
     */
    @Nonnull
    public static Optional<HSign> findSignByPlayer(@Nonnull Player player) {
        return GUIHandler.findSignByUID(player.getUniqueId());
    }

    /**
     * Gets HSign by player.
     *
     * @param player Player.
     * @return HSign.
     */
    @Nonnull
    public static HSign getSignByPlayer(@Nonnull Player player) {
        return GUIHandler.findSignByPlayer(player).orElseThrow(() -> new NullPointerException("player " + player.getName() + " doesn't have a inventory!"));
    }

    /**
     * Finds HSign by player UID.
     *
     * @param uid Player UID.
     * @return HSign as optional.
     */
    @Nonnull
    public static Optional<HSign> findSignByUID(@Nonnull UUID uid) {
        GUI gui = GUIHandler.guiMap.get(Objects.requireNonNull(uid, "UID cannot be null!"));
        return (gui instanceof HSign) ? Optional.of((HSign) gui) : Optional.empty();
    }

    /**
     * Gets HSign by player UID.
     *
     * @param uid Player UID.
     * @return HSign.
     */
    @Nonnull
    public static HSign getSignByUID(@Nonnull UUID uid) {
        return GUIHandler.findSignByUID(uid).orElseThrow(() -> new NullPointerException("player " + uid + " doesn't have a inventory!"));
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


    /*
    ANVIL
     */

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, HAnvil> getAnvilContentSafe() {
        Map<UUID, HAnvil> map = new HashMap<>();
        for (Map.Entry<UUID, GUI> entry : GUIHandler.guiMap.entrySet())
            if (entry.getValue() instanceof HAnvil)
                map.put(entry.getKey(), (HAnvil) entry.getValue());
        return map;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HAnvil> getAnvilValuesSafe() {
        List<HAnvil> map = new ArrayList<>();
        for (GUI gui : GUIHandler.guiMap.values())
            if (gui instanceof HAnvil)
                map.add((HAnvil) gui);
        return map;
    }

    /**
     * Finds HSign by player.
     *
     * @param player Player.
     * @return HSign as optional.
     */
    @Nonnull
    public static Optional<HAnvil> findAnvilByPlayer(@Nonnull Player player) {
        return GUIHandler.findAnvilByUID(player.getUniqueId());
    }

    /**
     * Gets HSign by player.
     *
     * @param player Player.
     * @return HSign.
     */
    @Nonnull
    public static HAnvil getAnvilByPlayer(@Nonnull Player player) {
        return GUIHandler.findAnvilByPlayer(player).orElseThrow(() -> new NullPointerException("player " + player.getName() + " doesn't have a inventory!"));
    }

    /**
     * Finds HSign by player UID.
     *
     * @param uid Player UID.
     * @return HSign as optional.
     */
    @Nonnull
    public static Optional<HAnvil> findAnvilByUID(@Nonnull UUID uid) {
        GUI gui = GUIHandler.guiMap.get(Objects.requireNonNull(uid, "UID cannot be null!"));
        return (gui instanceof HAnvil) ? Optional.of((HAnvil) gui) : Optional.empty();
    }

    /**
     * Gets HSign by player UID.
     *
     * @param uid Player UID.
     * @return HSign.
     */
    @Nonnull
    public static HAnvil getAnvilByUID(@Nonnull UUID uid) {
        return GUIHandler.findAnvilByUID(uid).orElseThrow(() -> new NullPointerException("player " + uid + " doesn't have a inventory!"));
    }

    /**
     * Creates sign builder.
     *
     * @param player Player.
     * @return HSignBuilder.
     */
    @Nonnull
    public static HAnvilBuilder anvilBuilder(@Nonnull Player player) {
        return new HAnvilBuilder(player);
    }
}