package com.hakan.core.npc.listeners;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.HNPCHandler;
import com.hakan.core.npc.events.HNpcEventListener;
import com.hakan.core.packet.event.PacketEvent;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HNpcEventListener_v1_17_R1 implements HNpcEventListener {

    @EventHandler
    @Override
    public void packetEvent(PacketEvent event) {
        if (event.getPacket() instanceof PacketPlayInUseEntity) {
            int id = event.getValue("a");
            if (!HNPCHandler.getNpcIDByEntityID().containsKey(id)) return;

            HNPC npc = HNPCHandler.getContent().get(HNPCHandler.getNpcIDByEntityID().get(id));
            if (npc == null) return;

            Player player = event.getPlayer();
            if (HCore.spam("hcore_npc_click_" + id + "_" + player.getUniqueId(), 3)) return;

            if (event.getValue("b").getClass().getName().equals("net.minecraft.network.protocol.game.PacketPlayInUseEntity$1")) {
                npc.getAction().getClickBiConsumer().accept(player, HNPC.Action.LEFT_CLICK);
            } else {
                npc.getAction().getClickBiConsumer().accept(player, HNPC.Action.RIGHT_CLICK);
            }
        }
    }


}
