package com.hakan.core.npc.listeners;

import com.hakan.core.listener.HListenerAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * HNPCListeners class.
 */
public final class HNpcTargetListener extends HListenerAdapter {

    /**
     * Creates new instance of this class.
     *
     * @param plugin Plugin class.
     */
    public HNpcTargetListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Cancels the target event if the target is not a villager.
     *
     * @param event The event.
     */
    @EventHandler
    public void onTarget(@Nonnull EntityTargetEvent event) {
        if (event.getEntity() instanceof LivingEntity && event.getTarget() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getEntity();
            LivingEntity target = (LivingEntity) event.getTarget();
            double health = 11.91231632232666d;
            if (attacker.getHealth() == health && target.getHealth() != health) {
                event.setCancelled(true);
            }
        }
    }
}