package com.hakan.core.message.actionbar;

import com.hakan.core.HCore;
import net.md_5.bungee.api.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public final class HActionBarHandler_v1_19_R1 implements HActionBarHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull String text) {
        Objects.requireNonNull(player, "player cannot be null!");
        Objects.requireNonNull(text, "text cannot be null!");

        IChatBaseComponent baseComponent = CraftChatMessage.fromStringOrNull(text);
        HCore.sendPacket(player, new ClientboundSystemChatPacket(baseComponent, ChatMessageType.ACTION_BAR.ordinal()));
    }
}