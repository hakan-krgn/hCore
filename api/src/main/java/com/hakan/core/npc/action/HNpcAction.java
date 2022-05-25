package com.hakan.core.npc.action;

import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.events.HNpcClickEvent;
import com.hakan.core.npc.events.HNpcDeleteEvent;
import com.hakan.core.npc.events.HNpcSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * HNpcAction class to
 * handle NPC actions.
 */
public class HNpcAction {

    private final HNPC hnpc;

    /**
     * HNpcAction constructor.
     *
     * @param hnpc HNPC object.
     */
    public HNpcAction(@Nonnull HNPC hnpc) {
        this.hnpc = Objects.requireNonNull(hnpc, "HNPC object cannot be null!");
    }

    /**
     * Gets HNPC object.
     *
     * @return HNPC object.
     */
    @Nonnull
    public HNPC getNPC() {
        return this.hnpc;
    }

    /**
     * Triggers when spawns the NPC.
     */
    public void onSpawn() {
        HNpcSpawnEvent event = new HNpcSpawnEvent(this.hnpc);
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Triggers when delete the NPC.
     */
    public void onDelete() {
        HNpcDeleteEvent event = new HNpcDeleteEvent(this.hnpc);
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Triggers when anyone
     * clicks the NPC.
     */
    public void onClick(@Nonnull Player player, @Nonnull HNPC.Action action) {
        Objects.requireNonNull(action, "action cannot be null!");
        HNpcClickEvent event = new HNpcClickEvent(this.hnpc, player, action);
        Bukkit.getPluginManager().callEvent(event);
    }
}