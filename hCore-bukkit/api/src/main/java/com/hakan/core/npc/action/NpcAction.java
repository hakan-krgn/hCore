package com.hakan.core.npc.action;

import com.hakan.core.HCore;
import com.hakan.core.npc.Npc;
import com.hakan.core.npc.events.NpcClickEvent;
import com.hakan.core.npc.events.NpcDeleteEvent;
import com.hakan.core.npc.events.NpcSpawnEvent;
import com.hakan.core.spam.Spam;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * NpcAction class to
 * handle NPC actions.
 */
public final class NpcAction {

    private static final String SPAM_ID = "hcore_npc_click_%s_%s";


    private final Npc npc;
    private long clickDelay;
    private Consumer<Npc> spawnConsumer;
    private Consumer<Npc> deleteConsumer;
    private BiConsumer<Player, Npc.Action> clickConsumer;

    /**
     * NpcAction constructor.
     *
     * @param npc Npc object.
     */
    public NpcAction(@Nonnull Npc npc) {
        this.npc = Validate.notNull(npc, "Npc object cannot be null!");
    }

    /**
     * Gets Npc object.
     *
     * @return Npc object.
     */
    @Nonnull
    public Npc getNpc() {
        return this.npc;
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
    public void whenSpawned(@Nonnull Consumer<Npc> spawnConsumer) {
        this.spawnConsumer = Validate.notNull(spawnConsumer, "spawn consumer cannot be null!");
    }

    /**
     * This consumer will run when NPC is deleted.
     *
     * @param deleteConsumer Consumer.
     */
    public void whenDeleted(@Nonnull Consumer<Npc> deleteConsumer) {
        this.deleteConsumer = Validate.notNull(deleteConsumer, "delete consumer cannot be null!");
    }

    /**
     * This consumer will run when NPC is clicked by player.
     *
     * @param clickConsumer Consumer.
     */
    public void whenClicked(@Nonnull BiConsumer<Player, Npc.Action> clickConsumer) {
        this.clickConsumer = Validate.notNull(clickConsumer, "click consumer cannot be null!");
    }

    /**
     * This method will run spawn consumer.
     */
    public void onSpawn() {
        if (this.spawnConsumer != null)
            this.spawnConsumer.accept(this.npc);

        HCore.syncScheduler().run(() -> {
            NpcSpawnEvent event = new NpcSpawnEvent(this.npc);
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    /**
     * This method will run delete consumer.
     */
    public void onDelete() {
        if (this.deleteConsumer != null)
            this.deleteConsumer.accept(this.npc);

        HCore.syncScheduler().run(() -> {
            NpcDeleteEvent event = new NpcDeleteEvent(this.npc);
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    /**
     * This method will run click consumer.
     *
     * @param player Player who clicked.
     * @param action Action.
     */
    public void onClick(@Nonnull Player player, @Nonnull Npc.Action action) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(action, "action cannot be null!");

        if (Spam.check(String.format(SPAM_ID, this.npc.getID(), player.getUniqueId()), this.clickDelay))
            return;

        if (this.clickConsumer != null)
            this.clickConsumer.accept(player, action);

        HCore.syncScheduler().run(() -> {
            NpcClickEvent event = new NpcClickEvent(this.npc, player, action);
            Bukkit.getPluginManager().callEvent(event);
        });
    }
}
