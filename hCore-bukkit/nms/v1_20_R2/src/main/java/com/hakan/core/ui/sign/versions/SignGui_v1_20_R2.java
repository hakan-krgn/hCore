package com.hakan.core.ui.sign.versions;

import com.hakan.core.HCore;
import com.hakan.core.ui.sign.SignGui;
import com.hakan.core.ui.sign.type.SignType;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.TileEntitySign;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class SignGui_v1_20_R2 extends SignGui {

    /**
     * {@inheritDoc}
     */
    private SignGui_v1_20_R2(@Nonnull Player player,
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
        Location location = super.player.getEyeLocation();
        location.clone().add(location.getDirection().multiply(-3));

        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        IBlockData data = CraftMagicNumbers.getBlock(super.type.asMaterial()).n();

        HCore.sendPacket(super.player, new PacketPlayOutBlockChange(blockPosition, data));

        TileEntitySign sign = new TileEntitySign(blockPosition, null);
        SignText signText = sign.a(true);
        for (int i = 0; i < super.lines.length; i++)
            signText = signText.a(i, IChatBaseComponent.a(super.lines[i]));
        sign.a(signText, true);
        HCore.sendPacket(super.player, sign.j());

        HCore.sendPacket(super.player, new PacketPlayOutOpenSignEditor(blockPosition, true));

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
        Block block = super.player.getWorld().getBlockAt(position.u(), position.v(), position.w());
        IBlockData data = CraftMagicNumbers.getBlock(block.getType()).n();
        HCore.sendPacket(super.player, new PacketPlayOutBlockChange(position, data));

        String[] b = packetPlayInUpdateSign.e();
        String[] lines = new String[b.length];
        System.arraycopy(b, 0, lines, 0, b.length);

        return super.onInputReceive(lines);
    }
}
