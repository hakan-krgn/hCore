package com.hakan.core.npc.listeners;

import com.hakan.core.HCore;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.wrapper.HNPC_v1_14_R1;
import com.hakan.core.packet.event.PacketEvent;
import net.minecraft.server.v1_14_R1.PacketPlayInUseEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * HNpcClickListener_v1_8_R3 class.
 */
public class HNpcClickListener_v1_14_R1 extends HListenerAdapter {

    private final HNPC_v1_14_R1 handle;

    /**
     * Constructor.
     *
     * @param handle NMS class of HNPC.
     */
    public HNpcClickListener_v1_14_R1(@Nonnull HNPC_v1_14_R1 handle) {
        super(HCore.getInstance());
        this.handle = Objects.requireNonNull(handle, "handle cannot be null!");
    }

    /**
     * Checks if any player is clicked on any NPC.
     *
     * @param event The event.
     */
    @EventHandler
    public void onPacket(@Nonnull PacketEvent event) {
        if (event.getPacket() instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packet = event.getPacket();

            int id = event.getValue("a");
            if (this.handle.getEntityPlayer().getId() == id) {
                Player player = event.getPlayer();

                if (HCore.spam("hcore_npc_click_" + player.getUniqueId(), 3))
                    return;

                PacketPlayInUseEntity.EnumEntityUseAction useAction = packet.b();
                if (useAction.equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                    this.handle.getAction().onClick(player, HNPC.Action.LEFT_CLICK);
                } else if (useAction.equals(PacketPlayInUseEntity.EnumEntityUseAction.INTERACT)) {
                    this.handle.getAction().onClick(player, HNPC.Action.RIGHT_CLICK);
                }
            }
        }
    }
}