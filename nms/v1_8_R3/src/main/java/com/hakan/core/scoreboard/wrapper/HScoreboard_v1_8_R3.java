package com.hakan.core.scoreboard.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.scoreboard.HScoreboard;
import com.hakan.core.scoreboard.HScoreboardHandler;
import com.hakan.core.utils.ColorUtil;
import com.hakan.core.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;

/**
 * {@inheritDoc}
 */
public final class HScoreboard_v1_8_R3 extends HScoreboard {

    private int mode = 0;

    /**
     * {@inheritDoc}
     */
    public HScoreboard_v1_8_R3(@Nonnull Player player, @Nonnull String title) {
        super(player, title);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HScoreboard show() {
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
            String line = super.lines[i];
            if (line == null) continue;

            String[] split = this.splitLine(i, line, 16, 16);
            String prefix = ColorUtil.colored(split[0]);
            String middle = ColorUtil.colored(split[1]);
            String suffix = ColorUtil.colored(split[2]);

            PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();
            ReflectionUtils.setField(team, "a", "team_" + i);
            ReflectionUtils.setField(team, "b", "team_" + i);
            ReflectionUtils.setField(team, "c", prefix);
            ReflectionUtils.setField(team, "d", suffix);
            ReflectionUtils.setField(team, "i", 1);
            ReflectionUtils.setField(team, "e", "always");
            ReflectionUtils.setField(team, "f", -1);
            ReflectionUtils.setField(team, "h", this.mode);
            ReflectionUtils.setField(team, "g", (this.mode == 0) ? Collections.singletonList(middle) : new ArrayList<>());

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
    public HScoreboard delete() {
        PacketPlayOutScoreboardObjective objective = new PacketPlayOutScoreboardObjective();
        ReflectionUtils.setField(objective, "a", "board");
        ReflectionUtils.setField(objective, "d", 1);

        HCore.sendPacket(super.player, objective);
        HScoreboardHandler.getContent().remove(super.player.getUniqueId());
        return this;
    }
}