package com.hakan.core.scoreboard.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.scoreboard.HScoreboard;
import com.hakan.core.scoreboard.HScoreboardHandler;
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
        this.setField(objective, "a", "board");
        this.setField(objective, "b", super.title);
        this.setField(objective, "c", IScoreboardCriteria.b.c());
        this.setField(objective, "d", this.mode);

        PacketPlayOutScoreboardDisplayObjective displayObjective = new PacketPlayOutScoreboardDisplayObjective();
        this.setField(displayObjective, "a", 1);
        this.setField(displayObjective, "b", "board");

        HCore.sendPacket(super.player, objective, displayObjective);

        int length = super.lines.length;
        for (int i = 0; i < length; i++) {
            String line = super.lines[i];
            if (line == null) continue;

            String[] split = this.splitLine(i, line);
            String prefix = split[0];
            String middle = split[1];
            String suffix = split[2];

            PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();
            this.setField(team, "a", "team_" + i);
            this.setField(team, "b", "team_" + i);
            this.setField(team, "c", prefix);
            this.setField(team, "d", suffix);
            this.setField(team, "i", 1);
            this.setField(team, "e", "always");
            this.setField(team, "f", -1);
            this.setField(team, "h", this.mode);
            this.setField(team, "g", (this.mode == 0) ? Collections.singletonList(middle) : new ArrayList<>());

            PacketPlayOutScoreboardScore score = new PacketPlayOutScoreboardScore();
            this.setField(score, "a", middle);
            this.setField(score, "b", "board");
            this.setField(score, "c", 15 - i);
            this.setField(score, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);

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
        this.setField(objective, "a", "board");
        this.setField(objective, "b", super.title);
        this.setField(objective, "c", IScoreboardCriteria.b.c());
        this.setField(objective, "d", 1);

        HCore.sendPacket(super.player, objective);
        HScoreboardHandler.getContent().remove(super.player.getUniqueId());
        return this;
    }
}