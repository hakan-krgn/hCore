package com.hakan.core.scoreboard.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.scoreboard.Scoreboard;
import com.hakan.core.scoreboard.ScoreboardHandler;
import com.hakan.core.utils.ColorUtil;
import com.hakan.core.utils.ReflectionUtils;
import net.minecraft.server.v1_16_R3.EnumChatFormat;
import net.minecraft.server.v1_16_R3.IScoreboardCriteria;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_16_R3.ScoreboardServer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;

/**
 * {@inheritDoc}
 */
public final class ScoreboardWrapper_v1_16_R3 extends ScoreboardWrapper {

    private int mode = 0;

    /**
     * {@inheritDoc}
     */
    private ScoreboardWrapper_v1_16_R3(@Nonnull Scoreboard scoreboard) {
        super(scoreboard);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        PacketPlayOutScoreboardObjective objective = new PacketPlayOutScoreboardObjective();
        ReflectionUtils.setField(objective, "a", "board");
        ReflectionUtils.setField(objective, "b", CraftChatMessage.fromStringOrNull(super.scoreboard.getTitle()));
        ReflectionUtils.setField(objective, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        ReflectionUtils.setField(objective, "d", this.mode);

        PacketPlayOutScoreboardDisplayObjective displayObjective = new PacketPlayOutScoreboardDisplayObjective();
        ReflectionUtils.setField(displayObjective, "a", 1);
        ReflectionUtils.setField(displayObjective, "b", "board");

        HCore.sendPacket(super.scoreboard.getPlayer(), objective, displayObjective);

        int length = super.scoreboard.getLines().length;
        for (int i = 0; i < length; i++) {
            String line = super.scoreboard.getLine(i);
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

            HCore.sendPacket(super.scoreboard.getPlayer(), team, score);
        }

        this.mode = 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete() {
        PacketPlayOutScoreboardObjective objective = new PacketPlayOutScoreboardObjective();
        ReflectionUtils.setField(objective, "a", "board");
        ReflectionUtils.setField(objective, "d", 1);

        HCore.sendPacket(super.scoreboard.getPlayer(), objective);
        ScoreboardHandler.getContent().remove(super.scoreboard.getPlayer().getUniqueId());
    }
}