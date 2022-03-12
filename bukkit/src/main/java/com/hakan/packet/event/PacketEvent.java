package com.hakan.packet.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

@SuppressWarnings({"unused", "unchecked"})
public class PacketEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    @Nonnull
    public static HandlerList getHandlerList() {
        return PacketEvent.handlerList;
    }


    private final Player player;
    private final Object packet;
    private final Type type;
    private boolean cancelled;

    public PacketEvent(@Nonnull Player player, @Nonnull Object packet, @Nonnull Type type) {
        this.player = player;
        this.packet = packet;
        this.type = type;
    }

    @Nonnull
    public HandlerList getHandlers() {
        return PacketEvent.handlerList;
    }

    @Nonnull
    public Player getPlayer() {
        return this.player;
    }

    @Nonnull
    public <T> T getPacket() {
        return (T) this.packet;
    }

    @Nonnull
    public <T> T getPacket(Class<T> packetClass) {
        return packetClass.cast(this.packet);
    }

    @Nonnull
    public Type getType() {
        return this.type;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }


    public enum Type {
        READ,
        WRITE
    }
}