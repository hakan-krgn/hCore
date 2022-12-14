package com.hakan.core.message.actionbar;

import com.hakan.core.HCore;
import com.hakan.core.utils.Validate;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class ActionBarWrapper_v1_18_R1 implements ActionBarWrapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull String text) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(text, "text cannot be null!");

        IChatBaseComponent baseComponent = CraftChatMessage.fromStringOrNull(text);
        HCore.sendPacket(player, new PacketPlayOutChat(baseComponent, ChatMessageType.c, player.getUniqueId()));
    }
}
