package com.hakan.core.scoreboard.versions;

import com.hakan.core.HCore;
import com.hakan.core.scoreboard.Scoreboard;
import com.hakan.core.scoreboard.ScoreboardHandler;
import com.hakan.core.utils.ColorUtil;
import com.hakan.core.utils.ReflectionUtils;
import net.minecraft.server.v1_10_R1.IScoreboardCriteria;
import net.minecraft.server.v1_10_R1.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_10_R1.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_10_R1.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_10_R1.PacketPlayOutScoreboardTeam;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;

/**
 * {@inheritDoc}
 */
public final class Scoreboard_v1_10_R1 extends Scoreboard {

    private int mode = 0;

    /**
     * {@inheritDoc}
     */
    private Scoreboard_v1_10_R1(@Nonnull Player player, @Nonnull String title) {
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
        ReflectionUtils.setField(objective, "b", super.title);
        ReflectionUtils.setField(objective, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        ReflectionUtils.setField(objective, "d", this.mode);

        PacketPlayOutScoreboardDisplayObjective displayObjective = new PacketPlayOutScoreboardDisplayObjective();
        ReflectionUtils.setField(displayObjective, "a", 1);
        ReflectionUtils.setField(displayObjective, "b", "board");

        HCore.sendPacket(super.player, objective, displayObjective);

        int length = super.lines.length;
        for (int i = 0; i < length; i++) {
            String line = super.getLine(i);
            if (line == null) continue;

            String[] split = super.splitLine(i, line, 16, 16);
            String prefix = ColorUtil.colored(split[0]);
            String middle = ColorUtil.colored(split[1]);
            String suffix = ColorUtil.colored(split[2]);

            PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();
            ReflectionUtils.setField(team, "a", "team_" + i);
            ReflectionUtils.setField(team, "b", "team_" + i);
            ReflectionUtils.setField(team, "c", prefix);
            ReflectionUtils.setField(team, "d", suffix);
            ReflectionUtils.setField(team, "e", "always");
            ReflectionUtils.setField(team, "f", "always");
            ReflectionUtils.setField(team, "g", -1);
            ReflectionUtils.setField(team, "h", (this.mode == 0) ? Collections.singletonList(middle) : new ArrayList<>());
            ReflectionUtils.setField(team, "i", this.mode);
            ReflectionUtils.setField(team, "j", 1);

            PacketPlayOutScoreboardScore score = new PacketPlayOutScoreboardScore();
            ReflectionUtils.setField(score, "a", middle);
            ReflectionUtils.setField(score, "b", "board");
            ReflectionUtils.setField(score, "c", 15 - i);
            ReflectionUtils.setField(score, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);

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