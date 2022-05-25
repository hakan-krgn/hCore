package com.hakan.core.npc.events;

import com.hakan.core.npc.HNPC;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * HNpcSpawnEvent class.
 */
public final class HNpcSpawnEvent extends Event {

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

    /**
     * Creates new instance of this class.
     *
     * @param npc HNPC class.
     */
    public HNpcSpawnEvent(@Nonnull HNPC npc) {
        this.npc = Objects.requireNonNull(npc, "npc cannot be null!");
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
}