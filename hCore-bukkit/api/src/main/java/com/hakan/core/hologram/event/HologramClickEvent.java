package com.hakan.core.hologram.event;

import com.hakan.core.hologram.Hologram;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * HologramSpawnEvent class.
 */
public final class HologramClickEvent extends Event {

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


    private final Player player;
    private final HologramLine line;

    /**
     * Creates new instance of this class.
     *
     * @param player Event player.
     * @param line   Clicked line.
     */
    public HologramClickEvent(@Nonnull Player player, @Nonnull HologramLine line) {
        this.player = Validate.notNull(player, "player cannot be null!");
        this.line = Validate.notNull(line, "line cannot be null!");
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
     * Gets event player.
     *
     * @return Event player.
     */
    @Nonnull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets event hologram.
     *
     * @return Event hologram.
     */
    @Nonnull
    public Hologram getHologram() {
        return this.line.getHologram();
    }

    /**
     * Gets event line.
     *
     * @return Event line.
     */
    @Nonnull
    public HologramLine getLine() {
        return this.line;
    }
}
