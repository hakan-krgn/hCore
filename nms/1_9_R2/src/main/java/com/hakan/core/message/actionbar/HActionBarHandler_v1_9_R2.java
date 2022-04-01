package com.hakan.core.message.actionbar;

import com.hakan.core.HCore;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class HActionBarHandler_v1_9_R2 implements HActionBarHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull String text) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(text, "text cannot be null!");

        IChatBaseComponent baseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}");
        HCore.sendPacket(player, new PacketPlayOutChat(baseComponent, (byte) 2));
    }
}
