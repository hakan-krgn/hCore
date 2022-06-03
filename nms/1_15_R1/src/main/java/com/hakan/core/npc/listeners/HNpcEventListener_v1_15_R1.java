package com.hakan.core.npc.listeners;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.HNPCHandler;
import com.hakan.core.npc.events.HNpcEventListener;
import com.hakan.core.packet.event.PacketEvent;
import net.minecraft.server.v1_15_R1.PacketPlayInUseEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HNpcEventListener_v1_15_R1 implements HNpcEventListener {

    @EventHandler
    @Override
    public void packetEvent(PacketEvent event) {
        if (event.getPacket() instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packet = event.getPacket();

            int id = event.getValue("a");
            if (!HNPCHandler.getNpcIDByEntityID().containsKey(id)) return;

            HNPC npc = HNPCHandler.getContent().get(HNPCHandler.getNpcIDByEntityID().get(id));
            if (npc == null) return;

            Player player = event.getPlayer();
            if (HCore.spam("hcore_npc_click_" + id + "_" + player.getUniqueId(), npc.getAction().getClickDelay()))
                return;

            PacketPlayInUseEntity.EnumEntityUseAction useAction = packet.b();
            if (useAction.equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                npc.getAction().getClickBiConsumer().accept(player, HNPC.Action.LEFT_CLICK);
            } else if (useAction.equals(PacketPlayInUseEntity.EnumEntityUseAction.INTERACT)) {
                npc.getAction().getClickBiConsumer().accept(player, HNPC.Action.RIGHT_CLICK);
            } else {
                npc.getAction().getClickBiConsumer().accept(player, HNPC.Action.RIGHT_CLICK);
            }
        }
    }


}
