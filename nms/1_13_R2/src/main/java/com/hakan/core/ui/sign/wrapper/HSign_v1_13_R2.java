package com.hakan.core.ui.sign.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.ui.sign.HSign;
import com.hakan.core.ui.sign.HSignHandler;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayInUpdateSign;
import net.minecraft.server.v1_13_R2.PacketPlayOutBlockChange;
import net.minecraft.server.v1_13_R2.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_13_R2.TileEntitySign;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.block.CraftSign;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public final class HSign_v1_13_R2 extends HSign {

    /**
     * {@inheritDoc}
     */
    public HSign_v1_13_R2(@Nonnull Material type, @Nonnull String... lines) {
        super(type, lines);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(@Nonnull Player player) {
        Objects.requireNonNull(player, "player cannot be null!");

        Location location = player.getLocation();
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), LOWEST_Y_AXIS + 1, location.getBlockZ());

        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld) player.getWorld()).getHandle(), blockPosition);
        packet.block = CraftMagicNumbers.getBlock(this.type).getBlockData();
        HCore.sendPacket(player, packet);

        IChatBaseComponent[] components = CraftSign.sanitizeLines(this.lines);
        TileEntitySign sign = new TileEntitySign();
        sign.setPosition(new BlockPosition(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()));
        System.arraycopy(components, 0, sign.lines, 0, sign.lines.length);
        HCore.sendPacket(player, sign.getUpdatePacket());

        HCore.sendPacket(player, new PacketPlayOutOpenSignEditor(blockPosition));
        HSignHandler.getContent().put(player.getUniqueId(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void listen(@Nonnull Player player, @Nonnull T packet) {
        Objects.requireNonNull(player, "player cannot be null!");
        Objects.requireNonNull(packet, "packet cannot be null!");
        PacketPlayInUpdateSign packetPlayInUpdateSign = (PacketPlayInUpdateSign) packet;

        BlockPosition position = packetPlayInUpdateSign.b();
        Block block = player.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
        block.setType(block.getType());

        if (this.consumer != null)
            this.consumer.accept(packetPlayInUpdateSign.c());

        HSignHandler.getContent().remove(player.getUniqueId());
    }
}