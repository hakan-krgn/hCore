package com.hakan.core.ui.sign.versions;

import com.hakan.core.HCore;
import com.hakan.core.ui.sign.SignGui;
import com.hakan.core.ui.sign.type.SignType;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_8_R3.TileEntitySign;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class SignGui_v1_8_R3 extends SignGui {

    /**
     * {@inheritDoc}
     */
    private SignGui_v1_8_R3(@Nonnull Player player,
                            @Nonnull SignType type,
                            @Nonnull String... lines) {
        super(player, type, lines);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public SignGui open() {
        Location location = super.player.getLocation();
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), LOWEST_Y_AXIS + 1, location.getBlockZ());

        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld) super.player.getWorld()).getHandle(), blockPosition);
        packet.block = CraftMagicNumbers.getBlock(super.type.asMaterial()).getBlockData();
        HCore.sendPacket(super.player, packet);

        IChatBaseComponent[] components = CraftSign.sanitizeLines(super.lines);
        TileEntitySign sign = new TileEntitySign();
        sign.a(blockPosition);
        System.arraycopy(components, 0, sign.lines, 0, sign.lines.length);
        HCore.sendPacket(super.player, sign.getUpdatePacket());

        HCore.sendPacket(super.player, new PacketPlayOutOpenSignEditor(blockPosition));

        return super.onOpen();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public <T> SignGui receiveInput(@Nonnull T packet) {
        PacketPlayInUpdateSign packetPlayInUpdateSign = (PacketPlayInUpdateSign) packet;

        BlockPosition position = packetPlayInUpdateSign.a();
        Block block = super.player.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
        PacketPlayOutBlockChange packetChange = new PacketPlayOutBlockChange(((CraftWorld) super.player.getWorld()).getHandle(), position);
        packetChange.block = CraftMagicNumbers.getBlock(block.getType()).getBlockData();
        HCore.sendPacket(super.player, packetChange);

        IChatBaseComponent[] b = packetPlayInUpdateSign.b();
        String[] lines = new String[b.length];
        for (int i = 0; i < b.length; i++)
            lines[i] = b[i].getText();

        return super.onInputReceive(lines);
    }
}
