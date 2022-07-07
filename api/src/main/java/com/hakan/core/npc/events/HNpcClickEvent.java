package com.hakan.core.npc.events;

import com.hakan.core.npc.HNPC;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * HNpcClickEvent class.
 */
public final class HNpcClickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    /**
     * Gets handler list.
     *
     * @return Handler list.
     */
    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }


    private final HNPC npc;
    private final Player player;
    private final HNPC.Action action;

    /**
     * Creates new instance of this class.
     *
     * @param npc    HNPC class.
     * @param player Player.
     * @param action Action.
     */
    public HNpcClickEvent(@Nonnull HNPC npc, @Nonnull Player player, @Nonnull HNPC.Action action) {
        this.npc = Validate.notNull(npc, "npc cannot be null!");
        this.player = Validate.notNull(player, "player cannot be null!");
        this.action = Validate.notNull(action, "action cannot be null!");
    }

    /**
     * Gets handler list.
     *
     * @return Handler list.
     */
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets event NPC.
     *
     * @return event NPC.
     */
    @Nonnull
    public HNPC getNpc() {
        return this.npc;
    }

    /**
     * Gets event player.
     *
     * @return event player.
     */
    @Nonnull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets click type of event.
     *
     * @return click type of event.
     */
    @Nonnull
    public HNPC.Action getAction() {
        return this.action;
    }
}
