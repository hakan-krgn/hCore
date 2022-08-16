package com.hakan.core.ui.sign.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.ui.GUIHandler;
import com.hakan.core.ui.sign.HSign;
import com.hakan.core.ui.sign.HSignType;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.world.level.block.entity.TileEntitySign;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

/**
 * {@inheritDoc}
 */
public final class HSign_v1_19_1_R1 extends HSign {

    /**
     * {@inheritDoc}
     */
    public HSign_v1_19_1_R1(@Nonnull Player player, @Nonnull HSignType type, @Nonnull String... lines) {
        super(player, type, lines);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() {
        Location location = super.player.getLocation();
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), LOWEST_Y_AXIS + 1, location.getBlockZ());
        IBlockData data = CraftMagicNumbers.getBlock(super.type.asMaterial()).m();

        HCore.sendPacket(super.player, new PacketPlayOutBlockChange(blockPosition, data));

        IChatBaseComponent[] components = CraftSign.sanitizeLines(super.lines);
        TileEntitySign sign = new TileEntitySign(blockPosition, data);
        IntStream.range(0, super.lines.length).forEach(i -> sign.a(i, components[i]));
        HCore.sendPacket(super.player, sign.c());

        HCore.sendPacket(super.player, new PacketPlayOutOpenSignEditor(blockPosition));
        GUIHandler.getContent().put(super.player.getUniqueId(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void listen(@Nonnull T packet) {
        PacketPlayInUpdateSign packetPlayInUpdateSign = (PacketPlayInUpdateSign) packet;

        BlockPosition position = packetPlayInUpdateSign.b();
        Block block = super.player.getWorld().getBlockAt(position.u(), position.v(), position.w());
        IBlockData data = CraftMagicNumbers.getBlock(block.getType()).m();
        HCore.sendPacket(super.player, new PacketPlayOutBlockChange(position, data));

        if (this.consumer != null)
            this.consumer.accept(packetPlayInUpdateSign.c());

        GUIHandler.getContent().remove(super.player.getUniqueId());
    }
}