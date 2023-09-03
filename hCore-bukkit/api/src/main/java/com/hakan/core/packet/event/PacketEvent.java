package com.hakan.core.packet.event;

import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Packet custom event class
 * to listen packets.
 */
@SuppressWarnings({"unused", "unchecked"})
public final class PacketEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    /**
     * Gets handler list.
     *
     * @return Handler list.
     */
    @Nonnull
    public static HandlerList getHandlerList() {
        return handlerList;
    }


    private final Player player;
    private final Object packet;
    private final Type type;
    private boolean cancelled;

    /**
     * Creates new instance of this class.
     *
     * @param player Player.
     * @param packet Packet.
     * @param type   Packet type.
     */
    public PacketEvent(@Nonnull Player player, @Nonnull Object packet, @Nonnull Type type) {
        this.player = Validate.notNull(player, "player cannot be null!");
        this.packet = Validate.notNull(packet, "packet cannot be null!");
        this.type = Validate.notNull(type, "packet type cannot be null!");
    }

    /**
     * Gets handler list.
     *
     * @return Handler list.
     */
    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * Gets event player.
     *
     * @return Event player.
     */
    @Nonnull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets packet.
     *
     * @param <T> Packet class.
     * @return Packet.
     */
    @Nonnull
    public <T> T getPacket() {
        return (T) this.packet;
    }

    /**
     * Gets packet as clazz.
     *
     * @param clazz Class of packet.
     * @return Packet.
     */
    @Nonnull
    public <T> T getPacket(@Nonnull Class<T> clazz) {
        return Validate.notNull(clazz, "packet class cannot be null!").cast(this.packet);
    }

    /**
     * Gets packet type.
     *
     * @return Packet type.
     */
    @Nonnull
    public Type getType() {
        return this.type;
    }

    /**
     * Checks event is cancelled.
     *
     * @return If event is cancelled, returns true.
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets cancel status of event.
     *
     * @param cancelled Cancel status.
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets value from field in packet.
     *
     * @param fieldName Field.
     * @param <T>       Value class.
     * @return Value from field.
     */
    @Nonnull
    public <T> T getValue(@Nonnull String fieldName) {
        Validate.notNull(fieldName, "field name cannot be null!");
        return Validate.notNull(ReflectionUtils.getField(this.packet, fieldName), "field cannot be null!");
    }

    /**
     * Gets value from field in packet.
     *
     * @param fieldName Field.
     * @param clazz     Packet class.
     * @param <T>       Value class.
     * @return Value from field.
     */
    @Nonnull
    public <T> T getValue(@Nonnull String fieldName, @Nonnull Class<T> clazz) {
        Validate.notNull(clazz, "class cannot be null!");
        Validate.notNull(fieldName, "field cannot be null!");
        return clazz.cast(this.getValue(fieldName));
    }

    /**
     * Packet types.
     */
    public enum Type {

        READ,
        WRITE
    }
}
