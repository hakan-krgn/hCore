package com.hakan.core.ui.anvil.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.ui.anvil.AnvilGui;
import net.minecraft.server.v1_16_R2.BlockPosition;
import net.minecraft.server.v1_16_R2.ContainerAccess;
import net.minecraft.server.v1_16_R2.ContainerAnvil;
import net.minecraft.server.v1_16_R2.Containers;
import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.IInventory;
import net.minecraft.server.v1_16_R2.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_16_R2.World;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class AnvilWrapper_v1_16_R2 extends AnvilWrapper {

    private final Player player;
    private final EntityPlayer entityPlayer;
    private final AnvilContainer container;
    private final int nextContainerId;

    /**
     * {@inheritDoc}
     */
    private AnvilWrapper_v1_16_R2(@Nonnull AnvilGui anvilGui) {
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
        this.entityPlayer.activeContainer = this.entityPlayer.defaultContainer;

        this.container.setItem(0, CraftItemStack.asNMSCopy(super.anvilGui.getLeftItem()));
        if (super.anvilGui.getRightItem() != null)
            this.container.setItem(1, CraftItemStack.asNMSCopy(super.anvilGui.getRightItem()));

        HCore.sendPacket(this.player, new PacketPlayOutOpenWindow(this.nextContainerId, Containers.ANVIL,
                CraftChatMessage.fromStringOrNull((super.anvilGui.getTitle()))));
        this.container.levelCost.set(0);
        this.container.addSlotListener(this.entityPlayer);
        this.entityPlayer.activeContainer = this.container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        this.entityPlayer.activeContainer = this.entityPlayer.defaultContainer;
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
            super(nextContainerId, entityHuman.inventory, ContainerAccess.at(entityHuman.world, new BlockPosition(0, 0, 0)));
            super.checkReachable = false;
            super.setTitle(CraftChatMessage.fromStringOrNull(anvilGui.getTitle()));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void e() {
            super.e();
            super.levelCost.set(0);
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
        protected void a(EntityHuman entityhuman, World world, IInventory iinventory) {

        }
    }
}