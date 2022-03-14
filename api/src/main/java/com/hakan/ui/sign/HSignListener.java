package com.hakan.ui.sign;

import com.hakan.listener.HListenerAdapter;
import com.hakan.packet.event.PacketEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class HSignListener extends HListenerAdapter {

    public HSignListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPacketEvent(PacketEvent event) {
        if (event.getPacket().toString().contains("PacketPlayInUpdateSign")) {
            HSignHandler.findByPlayer(event.getPlayer())
                    .ifPresent(hSign -> hSign.listen(event.getPlayer(), event.getPacket()));
        }
    }
}