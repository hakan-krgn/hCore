package com.hakan.core.scoreboard.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.scoreboard.Scoreboard;
import com.hakan.core.scoreboard.ScoreboardHandler;
import com.hakan.core.utils.ColorUtil;
import com.hakan.core.utils.ReflectionUtils;
import net.minecraft.server.v1_13_R2.EnumChatFormat;
import net.minecraft.server.v1_13_R2.IScoreboardCriteria;
import net.minecraft.server.v1_13_R2.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_13_R2.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_13_R2.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_13_R2.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_13_R2.ScoreboardServer;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;

/**
 * {@inheritDoc}
 */
public final class Scoreboard_v1_13_R2 extends Scoreboard {

    private int mode = 0;

    /**
     * {@inheritDoc}
     */
    public Scoreboard_v1_13_R2(@Nonnull Player player, @Nonnull String title) {
        super(player, title);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Scoreboard show() {
        PacketPlayOutScoreboardObjective objective = new PacketPlayOutScoreboardObjective();
        ReflectionUtils.setField(objective, "a", "board");
        ReflectionUtils.setField(objective, "b", CraftChatMessage.fromStringOrNull(super.title));
        ReflectionUtils.setField(objective, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        ReflectionUtils.setField(objective, "d", this.mode);

        PacketPlayOutScoreboardDisplayObjective displayObjective = new PacketPlayOutScoreboardDisplayObjective();
        ReflectionUtils.setField(displayObjective, "a", 1);
        ReflectionUtils.setField(displayObjective, "b", "board");

        HCore.sendPacket(super.player, objective, displayObjective);

        int length = super.lines.length;
        for (int i = 0; i < length; i++) {
            String line = super.lines[i];
            if (line == null) continue;

            String color = (i >= 10) ? "§" + new String[]{"a", "b", "c", "d", "e", "f"}[i - 10] : "§" + i;

            PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();
            ReflectionUtils.setField(team, "a", "team_" + i);
            ReflectionUtils.setField(team, "b", CraftChatMessage.fromStringOrNull("team_" + i));
            ReflectionUtils.setField(team, "c", CraftChatMessage.fromStringOrNull(ColorUtil.colored(line)));
            ReflectionUtils.setField(team, "d", CraftChatMessage.fromStringOrNull(""));
            ReflectionUtils.setField(team, "e", "always");
            ReflectionUtils.setField(team, "f", "always");
            ReflectionUtils.setField(team, "g", EnumChatFormat.RESET);
            ReflectionUtils.setField(team, "h", (this.mode == 0) ? Collections.singletonList(color) : new ArrayList<>());
            ReflectionUtils.setField(team, "i", this.mode);
            ReflectionUtils.setField(team, "j", 1);

            PacketPlayOutScoreboardScore score = new PacketPlayOutScoreboardScore();
            ReflectionUtils.setField(score, "a", color);
            ReflectionUtils.setField(score, "b", "board");
            ReflectionUtils.setField(score, "c", 15 - i);
            ReflectionUtils.setField(score, "d", ScoreboardServer.Action.CHANGE);

            HCore.sendPacket(super.player, team, score);
        }

        this.mode = 2;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Scoreboard delete() {
        PacketPlayOutScoreboardObjective objective = new PacketPlayOutScoreboardObjective();
        ReflectionUtils.setField(objective, "a", "board");
        ReflectionUtils.setField(objective, "d", 1);

        HCore.sendPacket(super.player, objective);
        ScoreboardHandler.getContent().remove(super.player.getUniqueId());
        return this;
    }
}