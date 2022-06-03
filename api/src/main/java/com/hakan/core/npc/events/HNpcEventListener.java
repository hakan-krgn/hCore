package com.hakan.core.npc.events;

import com.hakan.core.packet.event.PacketEvent;
import org.bukkit.event.Listener;

public interface HNpcEventListener extends Listener {

    void packetEvent(PacketEvent event);

}
