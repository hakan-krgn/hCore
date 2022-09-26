package com.hakan.core.ui.anvil.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.ui.anvil.AnvilGui;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutCloseWindow;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.ContainerAccess;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.Containers;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class AnvilWrapper_v1_18_R1 extends AnvilWrapper {

    private final Player player;
    private final EntityPlayer entityPlayer;
    private final AnvilContainer container;
    private final int nextContainerId;

    /**
     * {@inheritDoc}
     */
    private AnvilWrapper_v1_18_R1(@Nonnull AnvilGui anvilGui) {
        super(anvilGui);
        this.player = anvilGui.getPlayer();
        this.entityPlayer = ((CraftPlayer) this.player).getHandle();
        this.nextContainerId = this.entityPlayer.nextContainerCounter();
        this.container = new AnvilContainer(this.entityPlayer);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Inventory toInventory() {
        return this.container.getBukkitView().getTopInventory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() {
        this.entityPlayer.bW = this.entityPlayer.bV;

        this.container.getBukkitView().setItem(0, super.anvilGui.getLeftItem());
        if (super.anvilGui.getRightItem() != null)
            this.container.getBukkitView().setItem(1, super.anvilGui.getRightItem());

        HCore.sendPacket(this.player, new PacketPlayOutOpenWindow(this.nextContainerId, Containers.h,
                CraftChatMessage.fromStringOrNull(super.anvilGui.getTitle())));
        this.container.w.a(0);
        this.entityPlayer.a(this.container);
        this.entityPlayer.bW = this.container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        this.entityPlayer.bW = this.entityPlayer.bV;
        HCore.sendPacket(this.player, new PacketPlayOutCloseWindow(this.nextContainerId));
    }



    /**
     * AnvilContainer class.
     */
    private final class AnvilContainer extends ContainerAnvil {

        /**
         * Constructor of AnvilContainer.
         *
         * @param entityHuman EntityHuman.
         */
        public AnvilContainer(@Nonnull EntityHuman entityHuman) {
            super(nextContainerId, entityHuman.fq(), ContainerAccess.a(entityHuman.t, new BlockPosition(0, 0, 0)));
            super.checkReachable = false;
            super.setTitle(CraftChatMessage.fromStringOrNull(anvilGui.getTitle()));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void l() {
            super.l();
            super.w.a(0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void b(EntityHuman entityhuman) {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void a(EntityHuman entityhuman, IInventory iinventory) {

        }
    }
}