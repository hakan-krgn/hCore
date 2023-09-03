package com.hakan.core.ui.anvil.listeners;

import com.hakan.core.ui.GuiHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * AnvilClickListener class.
 */
public final class AnvilClickListener implements Listener {

    /**
     * When anvil is clicked.
     *
     * @param event Event.
     */
    @EventHandler
    public void onAnvilClick(@Nonnull InventoryClickEvent event) {
        if (event.getClick() == ClickType.UNKNOWN) {
            event.setCancelled(true);
            return;
        } else if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        GuiHandler.findAnvilByPlayer(player).ifPresent(gui -> {
            ItemStack clickedItem = event.getCurrentItem();

            if (event.getClickedInventory() == null) {
                event.setCancelled(true);
                return;
            } else if (clickedItem == null || clickedItem.getItemMeta() == null) {
                return;
            }

            if (event.getClickedInventory().equals(gui.toInventory())) {
                event.setCancelled(true);

                if (event.getSlot() == 2) {
                    String input = clickedItem.getItemMeta().getDisplayName();
                    gui.onInputReceive(input);
                }
            }
        });
    }
}
