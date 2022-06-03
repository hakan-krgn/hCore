package com.hakan.core.npc.action;

import com.hakan.core.npc.HNPC;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * HNpcAction class to
 * handle NPC actions.
 */
public final class HNpcAction {

    private final HNPC hnpc;

    private final Consumer<HNPC> spawnConsumer, deleteConsumer;
    private final BiConsumer<Player, HNPC.Action> clickBiConsumer;

    /**
     * HNpcAction constructor.
     *
     * @param hnpc            HNPC object.
     * @param spawnConsumer
     * @param deleteConsumer
     * @param clickBiConsumer
     */
    public HNpcAction(@Nonnull HNPC hnpc, Consumer<HNPC> spawnConsumer, Consumer<HNPC> deleteConsumer, BiConsumer<Player, HNPC.Action> clickBiConsumer) {
        this.hnpc = Objects.requireNonNull(hnpc, "HNPC object cannot be null!");

        this.spawnConsumer = spawnConsumer == null ? spawn -> {
        } : spawnConsumer;
        this.deleteConsumer = deleteConsumer == null ? delete -> {
        } : deleteConsumer;
        this.clickBiConsumer = clickBiConsumer == null ? (player, action) -> {
        } : clickBiConsumer;
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

    public BiConsumer<Player, HNPC.Action> getClickBiConsumer() {
        return clickBiConsumer;
    }

    public Consumer<HNPC> getDeleteConsumer() {
        return deleteConsumer;
    }

    public Consumer<HNPC> getSpawnConsumer() {
        return spawnConsumer;
    }
}