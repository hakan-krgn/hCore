package com.hakan.core.ui.inventory.listeners;

import com.hakan.core.ui.GuiHandler;
import com.hakan.core.ui.inventory.InventoryGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * This class handles inventory
 * click events.
 */
public final class InventoryClickListener implements Listener {

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
        } else if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        GuiHandler.findInventoryByPlayer(player).ifPresent(gui -> {
            if (event.getClickedInventory() == null) {
                event.setCancelled(true);
                return;
            }

            if (event.getClickedInventory().equals(gui.toInventory())) {
                if (gui.hasOption(InventoryGui.Option.CANCEL_TOP_CLICK))
                    event.setCancelled(true);
            } else {
                if (gui.hasOption(InventoryGui.Option.CANCEL_DOWN_CLICK))
                    event.setCancelled(true);
            }

            if (event.getClickedInventory().equals(gui.toInventory())) {
                gui.findItem(event.getSlot()).flatMap(clickableItem -> Optional.ofNullable(clickableItem.getClick()))
                        .ifPresent(clickEventConsumer -> {
                            event.setCancelled(true);
                            clickEventConsumer.accept(event);
                        });
            }
        });
    }
}
