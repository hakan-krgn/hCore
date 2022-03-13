package com.hakan.packet.player;

import io.netty.channel.ChannelPipeline;
import org.bukkit.entity.Player;

public abstract class HPacketPlayer {

    protected static final String CHANNEL = "hcore_pipeline_channel";


    protected final Player player;
    protected ChannelPipeline pipeline;

    public HPacketPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ChannelPipeline getPipeline() {
        return this.pipeline;
    }

    public abstract void send(Object... packets);

    public abstract void register();

    public abstract void unregister();
}