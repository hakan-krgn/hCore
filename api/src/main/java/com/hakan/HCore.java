package com.hakan;

import com.hakan.command.HCommand;
import com.hakan.command.HCommandExecutor;
import com.hakan.hooks.Metrics;
import com.hakan.packet.HPacketHandler;
import com.hakan.scheduler.HScheduler;
import com.hakan.ui.inventory.HInventory;
import com.hakan.ui.inventory.HInventoryHandler;
import com.hakan.ui.inventory.builder.HInventoryBuilder;
import com.hakan.ui.sign.HSign;
import com.hakan.ui.sign.HSignHandler;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * Main class of this core.
 * You can reach all APIs from this
 * class as static.
 */
@SuppressWarnings({"unused"})
public class HCore {

    private static JavaPlugin instance;

    public static JavaPlugin getInstance() {
        return HCore.instance;
    }

    /**
     * Initializes all APIs.
     *
     * @param plugin Instance of main class.
     */
    public static void initialize(@Nonnull JavaPlugin plugin) {
        Validate.notNull(plugin, "plugin cannot be null!");

        HCore.instance = plugin;

        Metrics.initialize(plugin);
        HPacketHandler.initialize(plugin);
        HInventoryHandler.initialize(plugin);
        HSignHandler.initialize(plugin);
    }


    /*
    OTHERS
     */
    public static String getVersionString() {
        return Bukkit.getServer().getClass().getName().split("\\.")[3];
    }


    /*
    SCHEDULER
     */
    public static HScheduler scheduler(boolean async) {
        return new HScheduler(HCore.instance, async);
    }

    public static HScheduler asyncScheduler() {
        return new HScheduler(HCore.instance, true);
    }

    public static HScheduler syncScheduler() {
        return new HScheduler(HCore.instance, false);
    }


    /*
    INVENTORY API
     */
    public static HInventoryBuilder buildInventory(String id) {
        return new HInventoryBuilder(id);
    }

    public static HInventory createInventory(String id, String title, InventoryType type, int size) {
        return new HInventoryBuilder(id).title(title).type(type).size(size).build();
    }

    public static Optional<HInventory> findInventoryByPlayer(Player player) {
        return HInventoryHandler.findByPlayer(player);
    }

    public static HInventory getInventoryByPlayer(Player player) {
        return HInventoryHandler.getByPlayer(player);
    }

    public static Optional<HInventory> findInventoryByUID(UUID uid) {
        return HInventoryHandler.findByUID(uid);
    }

    public static HInventory getInventoryByUID(UUID uid) {
        return HInventoryHandler.getByUID(uid);
    }


    /*
    SIGN
     */
    public static HSign createSign(Material type, String... lines) {
        return HSignHandler.create(type, lines);
    }

    public static Optional<HSign> findSignByPlayer(Player player) {
        return HSignHandler.findByPlayer(player);
    }

    public static HSign getSignByPlayer(Player player) {
        return HSignHandler.getByPlayer(player);
    }

    public static Optional<HSign> findSignByUID(UUID uid) {
        return HSignHandler.findByUID(uid);
    }

    public static HSign getSignByUID(UUID uid) {
        return HSignHandler.getByUID(uid);
    }


    /*
    COMMAND
     */
    public static HCommand registerCommand(String command, String... aliases) {
        return new HCommand(command, aliases).register();
    }

    public static void registerCommand(HCommandExecutor... executors) {
        Arrays.asList(executors).forEach(HCommandExecutor::register);
    }


    /*
    PACKET
     */
    public static void sendPacket(Player player, Object packet) {
        HPacketHandler.getPacketPlayerMap().get(player).send(packet);
    }

    public static void sendPacket(Player player, Object... packets) {
        HPacketHandler.getPacketPlayerMap().get(player).send(packets);
    }
}