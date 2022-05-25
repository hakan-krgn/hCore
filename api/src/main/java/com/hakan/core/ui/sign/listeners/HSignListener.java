package com.hakan.core.ui.sign.listeners;

import com.hakan.core.packet.event.PacketEvent;
import com.hakan.core.ui.sign.HSignHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

/**
 * HSignListener class.
 */
public final class HSignListener implements Listener {

    /**
     * Packet event.
     *
     * @param event Event.
     */
    @EventHandler
    public void onPacketEvent(@Nonnull PacketEvent event) {
        if (event.getPacket().toString().contains("PacketPlayInUpdateSign")) {
            HSignHandler.findByPlayer(event.getPlayer())
                    .ifPresent(hSign -> hSign.listen(event.getPlayer(), event.getPacket()));
        }
    }
}