package com.hakan.core.npc.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import javax.annotation.Nonnull;

/**
 * HNPCListeners class.
 */
public final class HNpcTargetListener implements Listener {

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