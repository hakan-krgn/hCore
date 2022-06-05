package com.hakan.core.ui.inventory.listeners;

import com.hakan.core.HCore;
import com.hakan.core.ui.GUIHandler;
import com.hakan.core.ui.inventory.HInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

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

        GUIHandler.findInventoryByPlayer(player).ifPresent(hInventory -> {
            if (hInventory.hasOption(HInventory.Option.CLOSABLE)) {
                try {
                    Method method = HInventory.class.getDeclaredMethod("onClose", Player.class);
                    method.setAccessible(true);
                    method.invoke(hInventory, player);
                    method.setAccessible(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GUIHandler.getContent().remove(player.getUniqueId());
                player.updateInventory();
            } else {
                HCore.syncScheduler().after(1).run(() -> player.openInventory(hInventory.toInventory()));
            }
        });
    }
}