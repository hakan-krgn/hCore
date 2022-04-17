package com.hakan.core.ui.sign.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.ui.sign.HSign;
import com.hakan.core.ui.sign.HSignHandler;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntitySign;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftSign;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class HSign_v1_18_R2 extends HSign {

    /**
     * {@inheritDoc}
     */
    public HSign_v1_18_R2(@Nonnull Material type, @Nonnull String... lines) {
        super(type, lines);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        BlockPosition blockPosition = new BlockPosition(player.getLocation().getBlockX(), 1, player.getLocation().getBlockZ());

        HCore.sendPacket(player, new PacketPlayOutBlockChange(blockPosition, Blocks.cg.n()));

        IChatBaseComponent[] components = CraftSign.sanitizeLines(this.lines);
        TileEntitySign sign = new TileEntitySign(new BlockPosition(blockPosition.u(), blockPosition.v(), blockPosition.w()), Blocks.cg.n());
        System.arraycopy(components, 0, sign.d, 0, sign.d.length);
        HCore.sendPacket(player, sign.c());

        HCore.sendPacket(player, new PacketPlayOutOpenSignEditor(blockPosition));
        HSignHandler.getContent().put(player.getUniqueId(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void listen(@Nonnull Player player, @Nonnull T packet) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(packet, "packet cannot be null!");
        PacketPlayInUpdateSign packetPlayInUpdateSign = (PacketPlayInUpdateSign) packet;

        BlockPosition position = packetPlayInUpdateSign.b();
        Block block = player.getWorld().getBlockAt(position.u(), position.v(), position.w());
        block.setType(block.getType());

        if (this.consumer != null)
            this.consumer.accept(packetPlayInUpdateSign.c());

        HSignHandler.getContent().remove(player.getUniqueId());
    }
}