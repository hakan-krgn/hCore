package com.hakan.core.ui.sign.listeners;

import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.packet.event.PacketEvent;
import com.hakan.core.ui.sign.HSignHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * HSignListener class.
 */
public final class HSignListener extends HListenerAdapter {

    /**
     * Creates new instance of this class.
     *
     * @param plugin Main class of plugin.
     */
    public HSignListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

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