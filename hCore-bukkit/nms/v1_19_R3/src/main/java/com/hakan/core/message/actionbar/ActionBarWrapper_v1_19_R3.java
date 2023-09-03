package com.hakan.core.message.actionbar;

import com.hakan.core.HCore;
import com.hakan.core.utils.Validate;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class ActionBarWrapper_v1_19_R3 implements ActionBarWrapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull String text) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(text, "text cannot be null!");

        IChatBaseComponent baseComponent = CraftChatMessage.fromStringOrNull(text);
        HCore.sendPacket(player, new ClientboundSystemChatPacket(baseComponent, true));
    }
}
