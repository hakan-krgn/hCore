package com.hakan.core.ui.anvil.listeners;

import com.hakan.core.HCore;
import com.hakan.core.ui.GUIHandler;
import com.hakan.core.ui.anvil.HAnvil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

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

        GUIHandler.findAnvilByPlayer(player).ifPresent(hAnvil -> {
            if (hAnvil.isClosable()) {
                HCore.syncScheduler().after(1).run(() -> {
                    try {
                        Field field = HAnvil.class.getDeclaredField("closeRunnable");
                        field.setAccessible(true);
                        Runnable closeRunnable = (Runnable) field.get(hAnvil);
                        if (closeRunnable != null)
                            closeRunnable.run();
                        field.setAccessible(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                GUIHandler.getContent().remove(player.getUniqueId());
                player.updateInventory();
            } else {
                HCore.syncScheduler().after(1).run(() -> hAnvil.clone().open(false));
            }
        });
    }
}