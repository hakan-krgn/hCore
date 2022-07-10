package com.hakan.core.ui.sign.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.ui.GUIHandler;
import com.hakan.core.ui.sign.HSign;
import com.hakan.core.ui.sign.HSignType;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayInUpdateSign;
import net.minecraft.server.v1_9_R1.PacketPlayOutBlockChange;
import net.minecraft.server.v1_9_R1.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_9_R1.TileEntitySign;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class HSign_v1_9_R1 extends HSign {

    /**
     * {@inheritDoc}
     */
    public HSign_v1_9_R1(@Nonnull Player player, @Nonnull HSignType type, @Nonnull String... lines) {
        super(player, type, lines);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() {
        Location location = super.player.getLocation();
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), LOWEST_Y_AXIS + 1, location.getBlockZ());

        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld) super.player.getWorld()).getHandle(), blockPosition);
        packet.block = CraftMagicNumbers.getBlock(super.type.asMaterial()).getBlockData();
        HCore.sendPacket(super.player, packet);

        IChatBaseComponent[] components = CraftSign.sanitizeLines(super.lines);
        TileEntitySign sign = new TileEntitySign();
        sign.a(new BlockPosition(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()));
        System.arraycopy(components, 0, sign.lines, 0, sign.lines.length);
        HCore.sendPacket(super.player, sign.getUpdatePacket());

        HCore.sendPacket(super.player, new PacketPlayOutOpenSignEditor(blockPosition));
        GUIHandler.getContent().put(super.player.getUniqueId(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void listen(@Nonnull T packet) {
        PacketPlayInUpdateSign packetPlayInUpdateSign = (PacketPlayInUpdateSign) packet;

        BlockPosition position = packetPlayInUpdateSign.a();
        Block block = super.player.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
        PacketPlayOutBlockChange packetPlayOutBlockChange = new PacketPlayOutBlockChange(((CraftWorld) super.player.getWorld()).getHandle(), position);
        packetPlayOutBlockChange.block = CraftMagicNumbers.getBlock(block.getType()).getBlockData();
        HCore.sendPacket(super.player, packetPlayOutBlockChange);

        if (this.consumer != null)
            this.consumer.accept(packetPlayInUpdateSign.b());

        GUIHandler.getContent().remove(super.player.getUniqueId());
    }
}