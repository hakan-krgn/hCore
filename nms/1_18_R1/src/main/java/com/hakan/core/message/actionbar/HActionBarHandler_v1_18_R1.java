package com.hakan.core.message.actionbar;

import com.hakan.core.HCore;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class HActionBarHandler_v1_18_R1 implements HActionBarHandler {

    @Override
    public void send(@Nonnull Player player, @Nonnull String text) {
        IChatBaseComponent baseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}");
        HCore.sendPacket(player, new PacketPlayOutChat(baseComponent, ChatMessageType.c, player.getUniqueId()));
    }
}
