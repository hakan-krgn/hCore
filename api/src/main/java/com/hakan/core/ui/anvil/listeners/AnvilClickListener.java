package com.hakan.core.ui.anvil.listeners;

import com.hakan.core.ui.GUIHandler;
import com.hakan.core.ui.anvil.HAnvil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * AnvilClickListener class.
 */
@SuppressWarnings({"unchecked"})
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
        GUIHandler.findAnvilByPlayer(player).ifPresent(hAnvil -> {
            ItemStack clickedItem = event.getCurrentItem();

            if (event.getClickedInventory() == null) {
                event.setCancelled(true);
                return;
            } else if (clickedItem == null || clickedItem.getItemMeta() == null) {
                return;
            }

            if (event.getClickedInventory().equals(hAnvil.toInventory())) {
                event.setCancelled(true);

                if (event.getSlot() == 2) {
                    String input = clickedItem.getItemMeta().getDisplayName();
                    hAnvil.close();

                    try {
                        Field field = HAnvil.class.getDeclaredField("inputConsumer");
                        field.setAccessible(true);
                        Consumer<String> inputConsumer = (Consumer<String>) field.get(hAnvil);
                        if (inputConsumer != null)
                            inputConsumer.accept(input);
                        field.setAccessible(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}