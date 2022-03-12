package com.hakan.ui.inventory.listeners.inventory;

import com.hakan.HCore;
import com.hakan.listener.HListenerAdapter;
import com.hakan.ui.inventory.HInventory;
import com.hakan.ui.inventory.HInventoryHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class InventoryCloseListener extends HListenerAdapter {

    public InventoryCloseListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        HInventoryHandler.findByPlayer(player).ifPresent(hInventory -> {
            if (hInventory.hasOption(HInventory.Option.CLOSABLE)) {
                hInventory.onClose(hInventory, player);
                HInventoryHandler.getContent().remove(player.getUniqueId());
                player.updateInventory();
            } else {
                HCore.syncScheduler().after(1).run(() -> player.openInventory(hInventory.toInventory()));
            }
        });
    }
}