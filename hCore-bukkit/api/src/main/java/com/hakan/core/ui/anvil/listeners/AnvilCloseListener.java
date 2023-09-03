package com.hakan.core.ui.anvil.listeners;

import com.hakan.core.HCore;
import com.hakan.core.ui.GuiHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.annotation.Nonnull;

/**
 * AnvilCloseListener class.
 */
public final class AnvilCloseListener implements Listener {

    /**
     * When anvil is closed.
     *
     * @param event Event.
     */
    @EventHandler
    public void onAnvilClose(@Nonnull InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        GuiHandler.findAnvilByPlayer(player).ifPresent(gui -> {
            if (gui.isClosable()) {
                gui.onClose(true);
                player.updateInventory();
            } else {
                HCore.syncScheduler().after(1).run(() -> gui.clone().open(false));
            }
        });
    }
}
