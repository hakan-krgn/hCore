package com.hakan.core.ui.anvil.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.ui.anvil.AnvilGui;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.Blocks;
import net.minecraft.server.v1_10_R1.ChatMessage;
import net.minecraft.server.v1_10_R1.ContainerAnvil;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class AnvilWrapper_v1_10_R1 extends AnvilWrapper {

    private final Player player;
    private final EntityPlayer entityPlayer;
    private final AnvilContainer container;
    private final int nextContainerId;

    /**
     * {@inheritDoc}
     */
    private AnvilWrapper_v1_10_R1(@Nonnull AnvilGui anvilGui) {
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

        HCore.sendPacket(this.player, new PacketPlayOutOpenWindow(this.nextContainerId, "minecraft:anvil", new ChatMessage(Blocks.ANVIL.a() + ".name")));
        this.container.a = 0;
        this.container.windowId = this.nextContainerId;
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
    private static final class AnvilContainer extends ContainerAnvil {

        /**
         * Constructor of AnvilContainer.
         *
         * @param entityHuman EntityHuman.
         */
        public AnvilContainer(@Nonnull EntityHuman entityHuman) {
            super(entityHuman.inventory, entityHuman.world, new BlockPosition(0, 0, 0), entityHuman);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean a(@Nonnull EntityHuman entityhuman) {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void b(@Nonnull EntityHuman entityhuman) {

        }
    }
}