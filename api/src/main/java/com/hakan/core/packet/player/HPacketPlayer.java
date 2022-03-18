package com.hakan.core.packet.player;

import io.netty.channel.ChannelPipeline;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * HPacketPlayer class to send
 * and listen the packets.
 */
public abstract class HPacketPlayer {

    protected static final String CHANNEL = "hcore_pipeline_channel";


    protected final Player player;
    protected ChannelPipeline pipeline;

    /**
     * Creates new instance of this class.
     *
     * @param player Player.
     */
    public HPacketPlayer(@Nonnull Player player) {
        this.player = Objects.requireNonNull(player, "player cannot be null!");
    }

    /**
     * Gets player.
     *
     * @return Player.
     */
    @Nonnull
    public final Player getPlayer() {
        return this.player;
    }

    /**
     * Gets pipeline.
     *
     * @return Pipeline.
     */
    @Nonnull
    public final ChannelPipeline getPipeline() {
        return this.pipeline;
    }

    /**
     * Sends packet to player.
     *
     * @param packets Packets.
     */
    public abstract void send(@Nonnull Object... packets);

    /**
     * Registers the packet listener.
     */
    public abstract void register();

    /**
     * Unregisters the packet listener.
     */
    public abstract void unregister();
}