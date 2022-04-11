package com.hakan.core.ui.inventory.listeners.inventory;

import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.ui.inventory.HInventory;
import com.hakan.core.ui.inventory.HInventoryHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * This class handles hInventory click
 * events.
 */
public final class InventoryClickListener extends HListenerAdapter {

    /**
     * Creates new instance of this class.
     *
     * @param plugin Main class of plugin.
     */
    public InventoryClickListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Inventory click event.
     *
     * @param event Event.
     */
    @EventHandler
    public void onClick(@Nonnull InventoryClickEvent event) {
        if (event.getClick() == ClickType.UNKNOWN) {
            event.setCancelled(true);
            return;
        } else if (event.getClickedInventory() == null) {
            event.setCancelled(true);
            return;
        } else if (!(event.getWhoClicked() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();
        HInventoryHandler.findByPlayer(player).ifPresent(hInventory -> {
            if (event.getClickedInventory().equals(hInventory.toInventory())) {
                if (hInventory.hasOption(HInventory.Option.CANCEL_TOP_CLICK))
                    event.setCancelled(true);
            } else {
                if (hInventory.hasOption(HInventory.Option.CANCEL_DOWN_CLICK))
                    event.setCancelled(true);
            }

            if (event.getClickedInventory().equals(hInventory.toInventory())) {
                hInventory.findItem(event.getSlot()).flatMap(clickableItem -> Optional.ofNullable(clickableItem.getClick()))
                        .ifPresent(clickEventConsumer -> {
                            event.setCancelled(true);
                            clickEventConsumer.accept(event);
                        });
            }
        });
    }
}