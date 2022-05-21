package com.hakan.core.npc.wrapper;

import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.types.HNPCSlotType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
public class HNPC_v1_8_R3 extends HNPC {

    /**
     * {@inheritDoc}
     */
    protected HNPC_v1_8_R3(@Nonnull String id,
                           @Nonnull EntityType type,
                           @Nonnull Location location,
                           @Nonnull List<String> lines) {
        super(id, type, location, lines);
    }

    /**
     * {@inheritDoc}
     */
    protected HNPC_v1_8_R3(@Nonnull String id,
                           @Nonnull EntityType type,
                           @Nonnull Location location,
                           @Nonnull List<String> lines,
                           @Nonnull Set<UUID> viewers) {
        super(id, type, location, lines, viewers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull HNPC move(@Nonnull Location to, double speed) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC setLocation(@Nonnull Location location) {
        Objects.requireNonNull(location, "location cannot be null!");
        this.renderer.setLocation(location);
        this.hologram.setLocation(location);

        //todo do

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC setSkin(@Nonnull String username) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC setEquipment(@Nonnull HNPCSlotType slotType, @Nonnull ItemStack itemStack) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC show(@Nonnull List<Player> players) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC hide(@Nonnull List<Player> players) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC delete() {
        this.hologram.delete();
        this.renderer.delete();

        //todo do

        return this;
    }
}