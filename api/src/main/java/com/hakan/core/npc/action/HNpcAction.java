package com.hakan.core.npc.action;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.events.HNpcClickEvent;
import com.hakan.core.npc.events.HNpcDeleteEvent;
import com.hakan.core.npc.events.HNpcSpawnEvent;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * HNpcAction class to
 * handle NPC actions.
 */
public final class HNpcAction {

    private static final String SPAM_ID = "hcore_npc_click_%s_%s";


    private final HNPC hnpc;
    private long clickDelay;
    private Consumer<HNPC> spawnConsumer;
    private Consumer<HNPC> deleteConsumer;
    private BiConsumer<Player, HNPC.Action> clickConsumer;

    /**
     * HNpcAction constructor.
     *
     * @param hnpc HNPC object.
     */
    public HNpcAction(@Nonnull HNPC hnpc) {
        this.hnpc = Validate.notNull(hnpc, "HNPC object cannot be null!");
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
     * Gets click delay.
     *
     * @return Click delay.
     */
    public long getClickDelay() {
        return this.clickDelay;
    }

    /**
     * Sets click delay.
     *
     * @param clickDelay Click delay.
     */
    public void setClickDelay(long clickDelay) {
        this.clickDelay = clickDelay;
    }

    /**
     * This consumer will run when NPC is spawned.
     *
     * @param spawnConsumer Consumer.
     */
    public void whenSpawned(@Nonnull Consumer<HNPC> spawnConsumer) {
        this.spawnConsumer = Validate.notNull(spawnConsumer, "spawn consumer cannot be null!");
    }

    /**
     * This consumer will run when NPC is deleted.
     *
     * @param deleteConsumer Consumer.
     */
    public void whenDeleted(@Nonnull Consumer<HNPC> deleteConsumer) {
        this.deleteConsumer = Validate.notNull(deleteConsumer, "delete consumer cannot be null!");
    }

    /**
     * This consumer will run when NPC is clicked by player.
     *
     * @param clickConsumer Consumer.
     */
    public void whenClicked(@Nonnull BiConsumer<Player, HNPC.Action> clickConsumer) {
        this.clickConsumer = Validate.notNull(clickConsumer, "click consumer cannot be null!");
    }

    /**
     * This method will run spawn consumer.
     */
    public void onSpawn() {
        if (this.spawnConsumer != null)
            this.spawnConsumer.accept(this.hnpc);

        HCore.syncScheduler().run(() -> {
            HNpcSpawnEvent event = new HNpcSpawnEvent(this.hnpc);
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    /**
     * This method will run delete consumer.
     */
    public void onDelete() {
        if (this.deleteConsumer != null)
            this.deleteConsumer.accept(this.hnpc);

        HCore.syncScheduler().run(() -> {
            HNpcDeleteEvent event = new HNpcDeleteEvent(this.hnpc);
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    /**
     * This method will run click consumer.
     *
     * @param player Player who clicked.
     * @param action Action.
     */
    public void onClick(@Nonnull Player player, @Nonnull HNPC.Action action) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(action, "action cannot be null!");

        if (HCore.spam(String.format(SPAM_ID, this.hnpc.getID(), player.getUniqueId()), this.clickDelay))
            return;

        if (this.clickConsumer != null)
            this.clickConsumer.accept(player, action);

        HCore.syncScheduler().run(() -> {
            HNpcClickEvent event = new HNpcClickEvent(this.hnpc, player, action);
            Bukkit.getPluginManager().callEvent(event);
        });
    }
}