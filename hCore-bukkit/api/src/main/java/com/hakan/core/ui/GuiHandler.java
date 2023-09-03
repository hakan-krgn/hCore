package com.hakan.core.ui;

import com.hakan.core.HCore;
import com.hakan.core.packet.event.PacketEvent;
import com.hakan.core.ui.anvil.AnvilGui;
import com.hakan.core.ui.anvil.builder.AnvilBuilder;
import com.hakan.core.ui.anvil.listeners.AnvilClickListener;
import com.hakan.core.ui.anvil.listeners.AnvilCloseListener;
import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.ui.inventory.builder.InventoryBuilder;
import com.hakan.core.ui.inventory.listeners.InventoryClickListener;
import com.hakan.core.ui.inventory.listeners.InventoryCloseListener;
import com.hakan.core.ui.sign.SignGui;
import com.hakan.core.ui.sign.builder.SignBuilder;
import com.hakan.core.utils.Validate;
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
import java.util.Optional;
import java.util.UUID;

/**
 * GuiHandler class to handle all GUIs
 * (Signs, Anvils, Inventory, etc.)
 */
public final class GuiHandler {

    private static final Map<UUID, Gui> guiMap = new HashMap<>();

    /**
     * Initializes the inventory system.
     */
    public static void initialize() {

        //INVENTORY
        HCore.registerEvent(PlayerQuitEvent.class)
                .consume(event -> {
                    Player player = event.getPlayer();
                    GuiHandler.findInventoryByPlayer(player).ifPresent(gui -> gui.close(player));
                });

        HCore.registerEvent(PluginDisableEvent.class)
                .filter(event -> event.getPlugin().equals(HCore.getInstance()))
                .consume(event -> Bukkit.getOnlinePlayers()
                        .forEach(player -> GuiHandler.findInventoryByPlayer(player).ifPresent(gui -> gui.close(player))));

        HCore.registerListeners(
                new InventoryClickListener(),
                new InventoryCloseListener()
        );


        //ANVIL
        HCore.registerEvent(PlayerQuitEvent.class)
                .consume(event -> {
                    Player player = event.getPlayer();
                    GuiHandler.findAnvilByPlayer(player).ifPresent(AnvilGui::close);
                });

        HCore.registerEvent(PluginDisableEvent.class)
                .filter(event -> event.getPlugin().equals(HCore.getInstance()))
                .consume(event -> Bukkit.getOnlinePlayers()
                        .forEach(player -> GuiHandler.findAnvilByPlayer(player).ifPresent(AnvilGui::close)));

        HCore.registerListeners(
                new AnvilClickListener(),
                new AnvilCloseListener()
        );


        //SIGN
        HCore.registerEvent(PacketEvent.class)
                .filter(event -> event.getType().equals(PacketEvent.Type.READ))
                .filter(event -> event.getPacket().toString().contains("PacketPlayInUpdateSign"))
                .consume(event -> GuiHandler.findSignByPlayer(event.getPlayer())
                        .ifPresent(gui -> gui.receiveInput(event.getPacket())));
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
    public static Map<UUID, Gui> getContentSafe() {
        return new HashMap<>(guiMap);
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, Gui> getContent() {
        return guiMap;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Gui> getValuesSafe() {
        return new ArrayList<>(guiMap.values());
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Gui> getValues() {
        return guiMap.values();
    }

    /**
     * Finds InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<Gui> findByPlayer(@Nonnull Player player) {
        return GuiHandler.findByUID(player.getUniqueId());
    }

    /**
     * Gets InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui.
     */
    @Nonnull
    public static Gui getByPlayer(@Nonnull Player player) {
        return GuiHandler.findByPlayer(player).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    /**
     * Finds InventoryGui by UID.
     *
     * @param uid Player UID.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<Gui> findByUID(@Nonnull UUID uid) {
        return Optional.ofNullable(guiMap.get(Validate.notNull(uid, "UID cannot be null!")));
    }

    /**
     * Gets InventoryGui by UID.
     *
     * @param uid Player UId.
     * @return InventoryGui.
     */
    @Nonnull
    public static Gui getByUID(@Nonnull UUID uid) {
        return GuiHandler.findByUID(uid).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
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
    public static Map<UUID, InventoryGui> getInventoryContentSafe() {
        Map<UUID, InventoryGui> map = new HashMap<>();
        for (Map.Entry<UUID, Gui> entry : guiMap.entrySet())
            if (entry.getValue() instanceof InventoryGui)
                map.put(entry.getKey(), (InventoryGui) entry.getValue());
        return map;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<InventoryGui> getInventoryValuesSafe() {
        List<InventoryGui> map = new ArrayList<>();
        for (Gui gui : guiMap.values())
            if (gui instanceof InventoryGui)
                map.add((InventoryGui) gui);
        return map;
    }

    /**
     * Finds InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<InventoryGui> findInventoryByPlayer(@Nonnull Player player) {
        return GuiHandler.findInventoryByUID(player.getUniqueId());
    }

    /**
     * Gets InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui.
     */
    @Nonnull
    public static InventoryGui getInventoryByPlayer(@Nonnull Player player) {
        return GuiHandler.findInventoryByPlayer(player).orElseThrow(() -> new NullPointerException("player " + player.getName() + " doesn't have a inventory!"));
    }

    /**
     * Finds InventoryGui by UID.
     *
     * @param uid Player UID.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<InventoryGui> findInventoryByUID(@Nonnull UUID uid) {
        Gui gui = guiMap.get(Validate.notNull(uid, "UID cannot be null!"));
        return (gui instanceof InventoryGui) ? Optional.of((InventoryGui) gui) : Optional.empty();
    }

    /**
     * Gets InventoryGui by UID.
     *
     * @param uid Player UId.
     * @return InventoryGui.
     */
    @Nonnull
    public static InventoryGui getInventoryByUID(@Nonnull UUID uid) {
        return GuiHandler.findInventoryByUID(uid).orElseThrow(() -> new NullPointerException("player " + uid + " doesn't have a inventory!"));
    }

    /**
     * Creates builder with ID.
     *
     * @param id ID.
     * @return InventoryBuilder.
     */
    @Nonnull
    public static InventoryBuilder inventoryBuilder(@Nonnull String id) {
        return new InventoryBuilder(id);
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
    public static Map<UUID, SignGui> getSignContentSafe() {
        Map<UUID, SignGui> map = new HashMap<>();
        for (Map.Entry<UUID, Gui> entry : guiMap.entrySet())
            if (entry.getValue() instanceof SignGui)
                map.put(entry.getKey(), (SignGui) entry.getValue());
        return map;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<SignGui> getSignValuesSafe() {
        List<SignGui> map = new ArrayList<>();
        for (Gui gui : guiMap.values())
            if (gui instanceof SignGui)
                map.add((SignGui) gui);
        return map;
    }

    /**
     * Finds SignGui by player.
     *
     * @param player Player.
     * @return SignGui as optional.
     */
    @Nonnull
    public static Optional<SignGui> findSignByPlayer(@Nonnull Player player) {
        return GuiHandler.findSignByUID(player.getUniqueId());
    }

    /**
     * Gets SignGui by player.
     *
     * @param player Player.
     * @return SignGui.
     */
    @Nonnull
    public static SignGui getSignByPlayer(@Nonnull Player player) {
        return GuiHandler.findSignByPlayer(player).orElseThrow(() -> new NullPointerException("player " + player.getName() + " doesn't have a inventory!"));
    }

    /**
     * Finds SignGui by player UID.
     *
     * @param uid Player UID.
     * @return SignGui as optional.
     */
    @Nonnull
    public static Optional<SignGui> findSignByUID(@Nonnull UUID uid) {
        Gui gui = guiMap.get(Validate.notNull(uid, "UID cannot be null!"));
        return (gui instanceof SignGui) ? Optional.of((SignGui) gui) : Optional.empty();
    }

    /**
     * Gets SignGui by player UID.
     *
     * @param uid Player UID.
     * @return SignGui.
     */
    @Nonnull
    public static SignGui getSignByUID(@Nonnull UUID uid) {
        return GuiHandler.findSignByUID(uid).orElseThrow(() -> new NullPointerException("player " + uid + " doesn't have a inventory!"));
    }

    /**
     * Creates sign builder.
     *
     * @param player Player.
     * @return SignBuilder.
     */
    @Nonnull
    public static SignBuilder signBuilder(@Nonnull Player player) {
        return new SignBuilder(player);
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
    public static Map<UUID, AnvilGui> getAnvilContentSafe() {
        Map<UUID, AnvilGui> map = new HashMap<>();
        for (Map.Entry<UUID, Gui> entry : guiMap.entrySet())
            if (entry.getValue() instanceof AnvilGui)
                map.put(entry.getKey(), (AnvilGui) entry.getValue());
        return map;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<AnvilGui> getAnvilValuesSafe() {
        List<AnvilGui> map = new ArrayList<>();
        for (Gui gui : guiMap.values())
            if (gui instanceof AnvilGui)
                map.add((AnvilGui) gui);
        return map;
    }

    /**
     * Finds SignGui by player.
     *
     * @param player Player.
     * @return SignGui as optional.
     */
    @Nonnull
    public static Optional<AnvilGui> findAnvilByPlayer(@Nonnull Player player) {
        return GuiHandler.findAnvilByUID(player.getUniqueId());
    }

    /**
     * Gets SignGui by player.
     *
     * @param player Player.
     * @return SignGui.
     */
    @Nonnull
    public static AnvilGui getAnvilByPlayer(@Nonnull Player player) {
        return GuiHandler.findAnvilByPlayer(player).orElseThrow(() -> new NullPointerException("player " + player.getName() + " doesn't have a inventory!"));
    }

    /**
     * Finds SignGui by player UID.
     *
     * @param uid Player UID.
     * @return SignGui as optional.
     */
    @Nonnull
    public static Optional<AnvilGui> findAnvilByUID(@Nonnull UUID uid) {
        Gui gui = guiMap.get(Validate.notNull(uid, "UID cannot be null!"));
        return (gui instanceof AnvilGui) ? Optional.of((AnvilGui) gui) : Optional.empty();
    }

    /**
     * Gets SignGui by player UID.
     *
     * @param uid Player UID.
     * @return SignGui.
     */
    @Nonnull
    public static AnvilGui getAnvilByUID(@Nonnull UUID uid) {
        return GuiHandler.findAnvilByUID(uid).orElseThrow(() -> new NullPointerException("player " + uid + " doesn't have a inventory!"));
    }

    /**
     * Creates sign builder.
     *
     * @param player Player.
     * @return SignBuilder.
     */
    @Nonnull
    public static AnvilBuilder anvilBuilder(@Nonnull Player player) {
        return new AnvilBuilder(player);
    }
}
