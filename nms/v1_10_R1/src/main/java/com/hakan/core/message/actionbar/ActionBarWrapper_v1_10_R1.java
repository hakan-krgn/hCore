package com.hakan.core.message.actionbar;

import com.hakan.core.HCore;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_10_R1.ChatMessage;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class ActionBarWrapper_v1_10_R1 implements ActionBarWrapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull String text) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(text, "text cannot be null!");

        IChatBaseComponent baseComponent = new ChatMessage(text);
        HCore.sendPacket(player, new PacketPlayOutChat(baseComponent, (byte) 2));
    }
}