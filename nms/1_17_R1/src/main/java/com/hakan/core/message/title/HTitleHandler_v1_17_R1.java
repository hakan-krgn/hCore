package com.hakan.core.message.title;

import com.hakan.core.HCore;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public final class HTitleHandler_v1_17_R1 implements HTitleHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull HTitle hTitle) {
        Objects.requireNonNull(player, "player cannot be null!");
        Objects.requireNonNull(player, "hTitle class cannot be null!");

        IChatBaseComponent titleString = CraftChatMessage.fromStringOrNull(hTitle.getTitle());
        IChatBaseComponent subtitleString = CraftChatMessage.fromStringOrNull(hTitle.getSubtitle());

        HCore.sendPacket(player, new ClientboundSetTitlesAnimationPacket(hTitle.getFadeIn(), hTitle.getStay(), hTitle.getFadeOut()));
        HCore.sendPacket(player, new ClientboundSetTitleTextPacket(titleString));
        HCore.sendPacket(player, new ClientboundSetSubtitleTextPacket(subtitleString));
    }
}