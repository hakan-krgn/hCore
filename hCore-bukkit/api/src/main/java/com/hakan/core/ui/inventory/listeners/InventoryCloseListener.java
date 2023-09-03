package com.hakan.core.ui.inventory.listeners;

import com.hakan.core.HCore;
import com.hakan.core.ui.GuiHandler;
import com.hakan.core.ui.inventory.InventoryGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.annotation.Nonnull;

/**
 * This class handles inventory
 * close event for CLOSABLE option.
 */
public final class InventoryCloseListener implements Listener {

    /**
     * Inventory close event.
     *
     * @param event Event.
     */
    @EventHandler
    public void onClose(@Nonnull InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        GuiHandler.findInventoryByPlayer(player).ifPresent(gui -> {
            if (gui.hasOption(InventoryGui.Option.CLOSABLE)) {
                gui.onClose(player);

                GuiHandler.getContent().remove(player.getUniqueId());
                player.updateInventory();
            } else {
                HCore.syncScheduler().after(1).run(() -> player.openInventory(gui.toInventory()));
            }
        });
    }
}
