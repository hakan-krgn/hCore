package com.hakan.core.ui.anvil.versions;

import com.hakan.core.HCore;
import com.hakan.core.ui.anvil.AnvilGui;
import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.ChatMessage;
import net.minecraft.server.v1_14_R1.ContainerAccess;
import net.minecraft.server.v1_14_R1.ContainerAnvil;
import net.minecraft.server.v1_14_R1.Containers;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.IInventory;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_14_R1.World;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * {@inheritDoc}
 */
public final class AnvilGui_v1_14_R1 extends AnvilGui {

    private final EntityPlayer entityPlayer;
    private final AnvilContainer container;
    private final int nextContainerId;

    /**
     * {@inheritDoc}
     */
    private AnvilGui_v1_14_R1(@Nonnull Player player,
                              @Nonnull String title,
                              @Nonnull String text,
                              @Nonnull ItemStack leftItem,
                              @Nullable ItemStack rightItem) {
        super(player, title, text, leftItem, rightItem);
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
    @Nonnull
    @Override
    public AnvilGui open(boolean runnableActivity) {
        this.entityPlayer.activeContainer = this.entityPlayer.defaultContainer;

        this.container.setItem(0, CraftItemStack.asNMSCopy(super.leftItem));
        if (super.rightItem != null)
            this.container.setItem(1, CraftItemStack.asNMSCopy(super.rightItem));

        HCore.sendPacket(this.player, new PacketPlayOutOpenWindow(this.nextContainerId, Containers.ANVIL, new ChatMessage(super.title)));
        this.container.levelCost.set(0);
        this.container.addSlotListener(this.entityPlayer);
        this.entityPlayer.activeContainer = this.container;

        return super.onOpen(runnableActivity);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public AnvilGui close(boolean runnableActivity) {
        super.closable = true;
        this.entityPlayer.activeContainer = this.entityPlayer.defaultContainer;
        return super.onClose(runnableActivity);
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
            super.setTitle(new ChatMessage(title));
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
