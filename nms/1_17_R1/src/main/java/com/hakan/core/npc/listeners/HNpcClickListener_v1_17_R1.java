package com.hakan.core.npc.listeners;

import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.HNPCHandler;
import com.hakan.core.packet.event.PacketEvent;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class HNpcClickListener_v1_17_R1 extends HNpcClickListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityInteractEvent(@Nonnull PacketEvent event, @Nonnull Player player) {
        int id = event.getValue("a");

        HNPCHandler.findByEntityID(id).ifPresent(npc -> {
            if (event.getValue("b").getClass().getName().equals("net.minecraft.network.protocol.game.PacketPlayInUseEntity$1")) {
                npc.getAction().onClick(player, HNPC.Action.LEFT_CLICK);
            } else {
                npc.getAction().onClick(player, HNPC.Action.RIGHT_CLICK);
            }
        });
    }
}