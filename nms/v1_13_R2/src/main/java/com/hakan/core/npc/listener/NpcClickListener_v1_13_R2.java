package com.hakan.core.npc.listener;

import com.hakan.core.npc.Npc;
import com.hakan.core.npc.NpcHandler;
import com.hakan.core.packet.event.PacketEvent;
import net.minecraft.server.v1_13_R2.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class NpcClickListener_v1_13_R2 extends NpcClickListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityInteractEvent(@Nonnull PacketEvent event, @Nonnull Player player) {
        PacketPlayInUseEntity packet = event.getPacket();
        int id = event.getValue("a");

        NpcHandler.findByEntityID(id).ifPresent(npc -> {
            PacketPlayInUseEntity.EnumEntityUseAction useAction = packet.b();
            if (useAction.equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                npc.getAction().onClick(player, Npc.Action.LEFT_CLICK);
            } else if (useAction.equals(PacketPlayInUseEntity.EnumEntityUseAction.INTERACT)) {
                npc.getAction().onClick(player, Npc.Action.RIGHT_CLICK);
            } else {
                npc.getAction().onClick(player, Npc.Action.RIGHT_CLICK);
            }
        });
    }
}