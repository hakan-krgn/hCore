package com.hakan.core.message.title;

import com.hakan.core.HCore;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_11_R1.ChatMessage;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class HTitleHandler_v1_11_R1 implements HTitleHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull HTitle hTitle) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(player, "hTitle class cannot be null!");

        IChatBaseComponent titleString = new ChatMessage(hTitle.getTitle());
        IChatBaseComponent subtitleString = new ChatMessage(hTitle.getSubtitle());

        HCore.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, hTitle.getFadeIn(), hTitle.getStay(), hTitle.getFadeOut()));
        HCore.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleString, hTitle.getFadeIn(), hTitle.getStay(), hTitle.getFadeOut()));
        HCore.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleString, hTitle.getFadeIn(), hTitle.getStay(), hTitle.getFadeOut()));
    }
}