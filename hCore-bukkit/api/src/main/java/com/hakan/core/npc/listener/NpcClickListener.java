package com.hakan.core.npc.listener;

import com.hakan.core.packet.event.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

/**
 * NpcClickListener class to listen
 * all click actions of npc by players.
 */
public abstract class NpcClickListener implements Listener {

    /**
     * This will run every packet event.
     *
     * @param event PacketEvent.
     */
    @EventHandler
    public final void onPacketEvent(@Nonnull PacketEvent event) {
        if (!event.getType().equals(PacketEvent.Type.READ))
            return;
        else if (!event.getPacket().getClass().getName().contains("PacketPlayInUseEntity"))
            return;

        this.onEntityInteractEvent(event, event.getPlayer());
    }


    /**
     * This will run when a player click the npc.
     *
     * @param event  PacketEvent.
     * @param player Player who clicked the npc.
     */
    public abstract void onEntityInteractEvent(@Nonnull PacketEvent event, @Nonnull Player player);
}
