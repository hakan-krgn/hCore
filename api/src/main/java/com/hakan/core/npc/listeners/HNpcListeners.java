package com.hakan.core.npc.listeners;

import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.packet.event.PacketEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * HNPCListeners class.
 */
public final class HNpcListeners extends HListenerAdapter {

    /**
     * Creates new instance of this class.
     *
     * @param plugin Plugin class.
     */
    public HNpcListeners(@Nonnull JavaPlugin plugin) {
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
            if (attacker.getHealth() == 11.91231632232666d && target.getHealth() != 11.91231632232666d) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Checks if any player is clicked on any NPC.
     *
     * @param event The event.
     */
    @EventHandler
    public void onPacket(@Nonnull PacketEvent event) {

    }
}