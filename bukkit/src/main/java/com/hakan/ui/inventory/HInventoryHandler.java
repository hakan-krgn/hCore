package com.hakan.ui.inventory;

import com.hakan.listener.HListenerAdapter;
import com.hakan.ui.inventory.builder.HInventoryBuilder;
import com.hakan.ui.inventory.listeners.bukkit.PlayerQuitListener;
import com.hakan.ui.inventory.listeners.bukkit.PluginDisableListener;
import com.hakan.ui.inventory.listeners.inventory.InventoryClickListener;
import com.hakan.ui.inventory.listeners.inventory.InventoryCloseListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.*;

public final class HInventoryHandler {

    private static final Map<UUID, HInventory> inventoryMap = new HashMap<>();

    public static void initialize(JavaPlugin plugin) {
        HListenerAdapter.register(new PlayerQuitListener(plugin),
                new PluginDisableListener(plugin),
                new InventoryClickListener(plugin),
                new InventoryCloseListener(plugin));
    }

    public static Map<UUID, HInventory> getContentSafe() {
        return new HashMap<>(HInventoryHandler.inventoryMap);
    }

    public static Map<UUID, HInventory> getContent() {
        return HInventoryHandler.inventoryMap;
    }

    public static Collection<HInventory> getValuesSafe() {
        return new ArrayList<>(HInventoryHandler.inventoryMap.values());
    }

    public static Collection<HInventory> getValues() {
        return HInventoryHandler.inventoryMap.values();
    }

    public static Optional<HInventory> findByPlayer(Player player) {
        return HInventoryHandler.findByUID(player.getUniqueId());
    }

    public static HInventory getByPlayer(Player player) {
        return HInventoryHandler.findByPlayer(player).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    public static Optional<HInventory> findByUID(UUID uid) {
        return Optional.ofNullable(HInventoryHandler.inventoryMap.get(uid));
    }

    public static HInventory getByUID(UUID uid) {
        return HInventoryHandler.findByUID(uid).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    public static HInventoryBuilder builder(@Nonnull String id) {
        return new HInventoryBuilder(id);
    }
}