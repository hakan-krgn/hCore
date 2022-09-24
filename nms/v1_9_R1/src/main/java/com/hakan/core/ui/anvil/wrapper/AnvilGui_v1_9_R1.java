package com.hakan.core.ui.anvil.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.ui.GuiHandler;
import com.hakan.core.ui.anvil.AnvilGui;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.Blocks;
import net.minecraft.server.v1_9_R1.ChatMessage;
import net.minecraft.server.v1_9_R1.ContainerAnvil;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_9_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * {@inheritDoc}
 */
public final class AnvilGui_v1_9_R1 extends AnvilGui {

    private final EntityPlayer entityPlayer;
    private final AnvilContainer container;
    private final int nextContainerId;


    /**
     * {@inheritDoc}
     */
    public AnvilGui_v1_9_R1(@Nonnull Player player,
                            @Nonnull String title,
                            @Nonnull String text,
                            @Nonnull ItemStack leftItem,
                            @Nullable ItemStack rightItem) {
        super(player, title, text, leftItem, rightItem);
        this.entityPlayer = ((CraftPlayer) player).getHandle();
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
        //NMS
        this.entityPlayer.activeContainer = this.entityPlayer.defaultContainer;

        this.container.setItem(0, CraftItemStack.asNMSCopy(super.leftItem));
        if (this.rightItem != null)
            this.container.setItem(1, CraftItemStack.asNMSCopy(super.rightItem));

        HCore.sendPacket(super.player, new PacketPlayOutOpenWindow(this.nextContainerId, "minecraft:anvil", new ChatMessage(Blocks.ANVIL.a() + ".name")));
        this.container.a = 0;
        this.container.windowId = this.nextContainerId;
        this.container.addSlotListener(this.entityPlayer);
        this.entityPlayer.activeContainer = this.container;

        //HANDLER
        if (super.openRunnable != null && runnableActivity)
            super.openRunnable.run();
        GuiHandler.getContent().put(super.player.getUniqueId(), this);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public AnvilGui close() {
        super.setClosable(true);

        this.entityPlayer.activeContainer = this.entityPlayer.defaultContainer;
        HCore.sendPacket(super.player, new PacketPlayOutCloseWindow(this.nextContainerId));

        if (super.closeRunnable != null)
            super.closeRunnable.run();
        return this;
    }


    /**
     * AnvilContainer class.
     */
    private static final class AnvilContainer extends ContainerAnvil {

        public AnvilContainer(@Nonnull EntityHuman entityhuman) {
            super(entityhuman.inventory, entityhuman.world, new BlockPosition(0, 0, 0), entityhuman);
        }

        @Override
        public boolean a(@Nonnull EntityHuman entityhuman) {
            return true;
        }

        @Override
        public void b(@Nonnull EntityHuman entityhuman) {

        }
    }
}