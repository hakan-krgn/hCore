package com.hakan.core.message.title;

import com.hakan.core.HCore;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public final class HTitleHandler_v1_8_R3 implements HTitleHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull HTitle hTitle) {
        Objects.requireNonNull(player, "player cannot be null!");
        Objects.requireNonNull(player, "hTitle class cannot be null!");

        IChatBaseComponent titleString = new ChatMessage(hTitle.getTitle());
        IChatBaseComponent subtitleString = new ChatMessage(hTitle.getSubtitle());

        HCore.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, hTitle.getFadeIn(), hTitle.getStay(), hTitle.getFadeOut()));
        HCore.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleString, hTitle.getFadeIn(), hTitle.getStay(), hTitle.getFadeOut()));
        HCore.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleString, hTitle.getFadeIn(), hTitle.getStay(), hTitle.getFadeOut()));
    }
}