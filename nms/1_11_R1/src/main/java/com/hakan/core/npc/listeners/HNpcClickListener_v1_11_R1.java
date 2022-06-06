package com.hakan.core.npc.listeners;

import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.HNPCHandler;
import com.hakan.core.packet.event.PacketEvent;
import net.minecraft.server.v1_11_R1.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class HNpcClickListener_v1_11_R1 extends HNpcClickListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityInteractEvent(@Nonnull PacketEvent event, @Nonnull Player player) {
        PacketPlayInUseEntity packet = event.getPacket();
        int id = event.getValue("a");

        HNPCHandler.findByEntityID(id).ifPresent(npc -> {
            PacketPlayInUseEntity.EnumEntityUseAction useAction = packet.a();
            if (useAction.equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                npc.getAction().onClick(player, HNPC.Action.LEFT_CLICK);
            } else if (useAction.equals(PacketPlayInUseEntity.EnumEntityUseAction.INTERACT)) {
                npc.getAction().onClick(player, HNPC.Action.RIGHT_CLICK);
            } else {
                npc.getAction().onClick(player, HNPC.Action.RIGHT_CLICK);
            }
        });
    }
}