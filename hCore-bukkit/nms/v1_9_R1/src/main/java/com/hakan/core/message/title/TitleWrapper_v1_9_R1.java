package com.hakan.core.message.title;

import com.hakan.core.HCore;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_9_R1.ChatMessage;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class TitleWrapper_v1_9_R1 implements TitleWrapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Player player, @Nonnull Title title) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(player, "title class cannot be null!");

        IChatBaseComponent titleString = new ChatMessage(title.getTitle());
        IChatBaseComponent subtitleString = new ChatMessage(title.getSubtitle());

        HCore.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, title.getFadeIn(), title.getStay(), title.getFadeOut()));
        HCore.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleString, title.getFadeIn(), title.getStay(), title.getFadeOut()));
        HCore.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleString, title.getFadeIn(), title.getStay(), title.getFadeOut()));
    }
}
