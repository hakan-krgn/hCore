package com.hakan.core.ui.inventory;

import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.builder.HInventoryBuilder;
import com.hakan.core.ui.inventory.listeners.InventoryClickListener;
import com.hakan.core.ui.inventory.listeners.InventoryCloseListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * HInventoryHandler class to create
 * and find HInventory.
 */
public final class HInventoryHandler {

    private static final Map<UUID, HInventory> inventoryMap = new HashMap<>();

    /**
     * Initializes the inventory system.
     */
    public static void initialize() {
        HCore.registerEvent(PlayerQuitEvent.class)
                .consume(event -> {
                    Player player = event.getPlayer();
                    HInventoryHandler.findByPlayer(player)
                            .ifPresent(hInventory -> hInventory.close(player));
                });

        HCore.registerEvent(PluginDisableEvent.class)
                .consume(event -> {
                    if (!event.getPlugin().equals(HCore.getInstance()))
                        return;

                    Bukkit.getOnlinePlayers().forEach(player -> HInventoryHandler.findByPlayer(player)
                            .ifPresent(hInventory -> hInventory.close(player)));
                });

        HCore.registerListeners(
                new InventoryClickListener(),
                new InventoryCloseListener()
        );
    }


    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, HInventory> getContentSafe() {
        return new HashMap<>(HInventoryHandler.inventoryMap);
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, HInventory> getContent() {
        return HInventoryHandler.inventoryMap;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HInventory> getValuesSafe() {
        return new ArrayList<>(HInventoryHandler.inventoryMap.values());
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HInventory> getValues() {
        return HInventoryHandler.inventoryMap.values();
    }

    /**
     * Finds HInventory by player.
     *
     * @param player Player.
     * @return HInventory as optional.
     */
    @Nonnull
    public static Optional<HInventory> findByPlayer(@Nonnull Player player) {
        return HInventoryHandler.findByUID(player.getUniqueId());
    }

    /**
     * Gets HInventory by player.
     *
     * @param player Player.
     * @return HInventory.
     */
    @Nonnull
    public static HInventory getByPlayer(@Nonnull Player player) {
        return HInventoryHandler.findByPlayer(player).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    /**
     * Finds HInventory by UID.
     *
     * @param uid Player UID.
     * @return HInventory as optional.
     */
    @Nonnull
    public static Optional<HInventory> findByUID(@Nonnull UUID uid) {
        return Optional.ofNullable(HInventoryHandler.inventoryMap.get(Objects.requireNonNull(uid, "UID cannot be null!")));
    }

    /**
     * Gets HInventory by UID.
     *
     * @param uid Player UId.
     * @return HInventory.
     */
    @Nonnull
    public static HInventory getByUID(@Nonnull UUID uid) {
        return HInventoryHandler.findByUID(uid).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    /**
     * Creates builder with ID.
     *
     * @param id ID.
     * @return HInventoryBuilder.
     */
    @Nonnull
    public static HInventoryBuilder builder(@Nonnull String id) {
        return new HInventoryBuilder(id);
    }
}