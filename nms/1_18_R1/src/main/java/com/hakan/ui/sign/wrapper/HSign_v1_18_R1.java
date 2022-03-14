package com.hakan.ui.sign.wrapper;

import com.hakan.ui.sign.HSign;
import com.hakan.ui.sign.HSignHandler;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntitySign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HSign_v1_18_R1 extends HSign {

    public HSign_v1_18_R1(Material type, String... lines) {
        super(type, lines);
    }

    @Override
    public void open(Player player) {
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().b;
        BlockPosition blockPosition = new BlockPosition(player.getLocation().getBlockX(), 1, player.getLocation().getBlockZ());

        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(blockPosition, Blocks.cg.n());
        playerConnection.a(packet);

        IChatBaseComponent[] components = CraftSign.sanitizeLines(this.lines);
        TileEntitySign sign = new TileEntitySign(new BlockPosition(blockPosition.u(), blockPosition.v(), blockPosition.w()), Blocks.cg.n());
        System.arraycopy(components, 0, sign.d, 0, sign.d.length);
        playerConnection.a(sign.c());

        playerConnection.a(new PacketPlayOutOpenSignEditor(blockPosition));
        HSignHandler.getContent().put(player.getUniqueId(), this);
    }

    @Override
    protected <T> void listen(Player player, T packet) {
        PacketPlayInUpdateSign packetPlayInUpdateSign = (PacketPlayInUpdateSign) packet;

        BlockPosition position = packetPlayInUpdateSign.b();
        Block block = player.getWorld().getBlockAt(position.u(), position.v(), position.w());
        block.setType(block.getType());

        if (this.consumer != null)
            this.consumer.accept(packetPlayInUpdateSign.c());
    }
}