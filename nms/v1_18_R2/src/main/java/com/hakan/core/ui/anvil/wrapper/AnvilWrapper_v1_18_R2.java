package com.hakan.core.ui.anvil.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.ui.GuiHandler;
import com.hakan.core.ui.anvil.AnvilGui;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutCloseWindow;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.ContainerAccess;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.Containers;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * {@inheritDoc}
 */
public final class AnvilWrapper_v1_18_R2 extends AnvilGui {

    private final EntityPlayer entityPlayer;
    private final AnvilContainer container;
    private final int nextContainerId;


    /**
     * {@inheritDoc}
     */
    public AnvilWrapper_v1_18_R2(@Nonnull Player player,
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
        this.entityPlayer.bV = this.entityPlayer.bU;

        this.container.getBukkitView().setItem(0, super.leftItem);
        if (this.rightItem != null)
            this.container.getBukkitView().setItem(1, super.rightItem);

        HCore.sendPacket(super.player, new PacketPlayOutOpenWindow(this.nextContainerId, Containers.h, new ChatMessage(super.title)));
        this.container.w.a(0);
        this.entityPlayer.a(this.container);
        this.entityPlayer.bV = this.container;

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

        this.entityPlayer.bV = this.entityPlayer.bU;
        HCore.sendPacket(super.player, new PacketPlayOutCloseWindow(this.nextContainerId));

        if (super.closeRunnable != null)
            super.closeRunnable.run();
        return this;
    }


    /**
     * AnvilContainer class.
     */
    private final class AnvilContainer extends ContainerAnvil {

        public AnvilContainer(@Nonnull EntityHuman entityhuman) {
            super(nextContainerId, entityhuman.fr(), ContainerAccess.a(entityhuman.s, new BlockPosition(0, 0, 0)));
            super.checkReachable = false;
            super.setTitle(CraftChatMessage.fromStringOrNull(title));
        }

        @Override
        public void l() {
            super.l();
            super.w.a(0);
        }

        @Override
        public void b(EntityHuman entityhuman) {

        }

        @Override
        protected void a(EntityHuman entityhuman, IInventory iinventory) {

        }
    }
}