package com.hakan.message.actionbar;

import com.hakan.HCore;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.bukkit.entity.Player;

public class ActionBarHandler_v1_16_R3 implements ActionBarHandler {

    @Override
    public void send(Player player, String text) {
        IChatBaseComponent baseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}");
        HCore.sendPacket(player, new PacketPlayOutChat(baseComponent, ChatMessageType.GAME_INFO, player.getUniqueId()));
    }
}
