package com.hakan.core.ui.sign.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.ui.sign.SignGui;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.world.level.block.entity.TileEntitySign;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class SignWrapper_v1_17_R1 extends SignWrapper {

    /**
     * {@inheritDoc}
     */
    private SignWrapper_v1_17_R1(@Nonnull SignGui signGui) {
        super(signGui);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() {
        Location location = super.signGui.getPlayer().getLocation();
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), LOWEST_Y_AXIS + 1, location.getBlockZ());
        IBlockData data = CraftMagicNumbers.getBlock(super.signGui.getType().asMaterial()).getBlockData();

        HCore.sendPacket(super.signGui.getPlayer(), new PacketPlayOutBlockChange(blockPosition, data));

        IChatBaseComponent[] components = CraftSign.sanitizeLines(super.signGui.getLines());
        TileEntitySign sign = new TileEntitySign(new BlockPosition(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()), data);
        System.arraycopy(components, 0, sign.d, 0, sign.d.length);
        HCore.sendPacket(super.signGui.getPlayer(), sign.getUpdatePacket());

        HCore.sendPacket(super.signGui.getPlayer(), new PacketPlayOutOpenSignEditor(blockPosition));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public <T> String[] inputReceive(@Nonnull T packet) {
        PacketPlayInUpdateSign packetPlayInUpdateSign = (PacketPlayInUpdateSign) packet;

        BlockPosition position = packetPlayInUpdateSign.b();
        Block block = super.signGui.getPlayer().getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
        IBlockData data = CraftMagicNumbers.getBlock(block.getType()).getBlockData();
        HCore.sendPacket(super.signGui.getPlayer(), new PacketPlayOutBlockChange(position, data));

        String[] b = packetPlayInUpdateSign.c();
        String[] lines = new String[b.length];
        System.arraycopy(b, 0, lines, 0, b.length);

        return lines;
    }
}