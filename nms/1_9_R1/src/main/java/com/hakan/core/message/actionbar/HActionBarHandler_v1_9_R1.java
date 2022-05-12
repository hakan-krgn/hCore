package com.hakan.core.message.actionbar;

import com.hakan.core.HCore;
import net.minecraft.server.v1_9_R1.ChatMessage;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public final class HActionBarHandler_v1_9_R1 implements HActionBarHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull String text) {
        Objects.requireNonNull(player, "player cannot be null!");
        Objects.requireNonNull(text, "text cannot be null!");

        IChatBaseComponent baseComponent = new ChatMessage(text);
        HCore.sendPacket(player, new PacketPlayOutChat(baseComponent, (byte) 2));
    }
}
