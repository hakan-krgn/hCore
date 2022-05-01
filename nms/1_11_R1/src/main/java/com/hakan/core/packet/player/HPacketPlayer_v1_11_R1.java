package com.hakan.core.packet.player;

import com.hakan.core.HCore;
import com.hakan.core.packet.event.PacketEvent;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_11_R1.Packet;
import net.minecraft.server.v1_11_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@inheritDoc}
 */
public final class HPacketPlayer_v1_11_R1 extends HPacketPlayer {

    private final PlayerConnection connection;

    /**
     * {@inheritDoc}
     */
    public HPacketPlayer_v1_11_R1(@Nonnull Player player) {
        super(player);
        this.connection = ((CraftPlayer) player).getHandle().playerConnection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Object... packets) {
        Objects.requireNonNull(packets, "packets cannot be null!");
        for (Object packet : packets) {
            this.connection.sendPacket((Packet<?>) packet);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register() {
        try {
            this.pipeline = this.connection.networkManager.channel.pipeline().addBefore("packet_handler", CHANNEL, new ChannelDuplexHandler() {
                @Override
                public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
                    AtomicBoolean status = new AtomicBoolean(false);
                    HCore.syncScheduler().run(() -> {
                        PacketEvent packet_event = new PacketEvent(player, msg, PacketEvent.Type.READ);
                        Bukkit.getPluginManager().callEvent(packet_event);
                        status.set(packet_event.isCancelled());
                    });

                    if (status.get()) return;
                    super.channelRead(channelHandlerContext, msg);
                }

                @Override
                public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
                    AtomicBoolean status = new AtomicBoolean(false);
                    HCore.syncScheduler().run(() -> {
                        PacketEvent packet_event = new PacketEvent(player, o, PacketEvent.Type.WRITE);
                        Bukkit.getPluginManager().callEvent(packet_event);
                        status.set(packet_event.isCancelled());
                    });

                    if (status.get()) return;
                    super.write(channelHandlerContext, o, channelPromise);
                }
            });
        } catch (Exception ignored) {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister() {
        if (this.pipeline != null && this.pipeline.get(CHANNEL) != null)
            this.pipeline.remove(CHANNEL);
    }
}