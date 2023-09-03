package com.hakan.core.npc.events;

import com.hakan.core.npc.Npc;
import com.hakan.core.utils.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * NpcSpawnEvent class.
 */
public final class NpcSpawnEvent extends Event {

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

    /**
     * Creates new instance of this class.
     *
     * @param npc Npc class.
     */
    public NpcSpawnEvent(@Nonnull Npc npc) {
        this.npc = Validate.notNull(npc, "npc cannot be null!");
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
}
