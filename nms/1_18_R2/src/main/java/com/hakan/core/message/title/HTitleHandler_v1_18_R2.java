package com.hakan.core.message.title;

import com.hakan.core.HCore;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public final class HTitleHandler_v1_18_R2 implements HTitleHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull HTitle hTitle) {
        Objects.requireNonNull(player, "player cannot be null!");
        Objects.requireNonNull(player, "hTitle class cannot be null!");

        IChatBaseComponent titleString = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + hTitle.getTitle() + "\"}");
        IChatBaseComponent subtitleString = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + hTitle.getSubtitle() + "\"}");

        HCore.sendPacket(player, new ClientboundSetTitlesAnimationPacket(hTitle.getFadeIn(), hTitle.getStay(), hTitle.getFadeOut()));
        HCore.sendPacket(player, new ClientboundSetTitleTextPacket(titleString));
        HCore.sendPacket(player, new ClientboundSetSubtitleTextPacket(subtitleString));
    }
}