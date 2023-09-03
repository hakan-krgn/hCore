package com.hakan.core.npc.listener;

import com.hakan.core.npc.Npc;
import com.hakan.core.npc.NpcHandler;
import com.hakan.core.packet.event.PacketEvent;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class NpcClickListener_v1_19_R3 extends NpcClickListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityInteractEvent(@Nonnull PacketEvent event, @Nonnull Player player) {
        int id = event.getValue("a");

        NpcHandler.findByEntityID(id).ifPresent(npc -> {
            if (event.getValue("b").getClass().getName().equals("net.minecraft.network.protocol.game.PacketPlayInUseEntity$1")) {
                npc.getAction().onClick(player, Npc.Action.LEFT_CLICK);
            } else {
                npc.getAction().onClick(player, Npc.Action.RIGHT_CLICK);
            }
        });
    }
}
