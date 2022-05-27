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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftSign;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public final class HSign_v1_17_R1 extends HSign {

    /**
     * {@inheritDoc}
     */
    public HSign_v1_17_R1(@Nonnull Material type, @Nonnull String... lines) {
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

        HCore.sendPacket(player, new PacketPlayOutBlockChange(blockPosition, Blocks.cg.getBlockData()));

        IChatBaseComponent[] components = CraftSign.sanitizeLines(this.lines);
        TileEntitySign sign = new TileEntitySign(new BlockPosition(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()), Blocks.cg.getBlockData());
        System.arraycopy(components, 0, sign.d, 0, sign.d.length);
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