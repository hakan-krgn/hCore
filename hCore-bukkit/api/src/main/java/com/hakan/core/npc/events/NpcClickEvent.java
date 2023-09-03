package com.hakan.core.npc.events;

import com.hakan.core.npc.Npc;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * NpcClickEvent class.
 */
public final class NpcClickEvent extends Event {

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


    private final Npc npc;
    private final Player player;
    private final Npc.Action action;

    /**
     * Creates new instance of this class.
     *
     * @param npc    Npc class.
     * @param player Player.
     * @param action Action.
     */
    public NpcClickEvent(@Nonnull Npc npc, @Nonnull Player player, @Nonnull Npc.Action action) {
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
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets event NPC.
     *
     * @return Event NPC.
     */
    @Nonnull
    public Npc getNpc() {
        return this.npc;
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
     * Gets click type of event.
     *
     * @return Click type of event.
     */
    @Nonnull
    public Npc.Action getAction() {
        return this.action;
    }
}
