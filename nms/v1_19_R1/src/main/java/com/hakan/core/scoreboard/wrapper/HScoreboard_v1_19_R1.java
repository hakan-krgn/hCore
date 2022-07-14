package com.hakan.core.scoreboard.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.scoreboard.HScoreboard;
import com.hakan.core.scoreboard.HScoreboardHandler;
import com.hakan.core.utils.ColorUtil;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
public final class HScoreboard_v1_19_R1 extends HScoreboard {

    private int mode = 0;

    /**
     * {@inheritDoc}
     */
    public HScoreboard_v1_19_R1(@Nonnull Player player, @Nonnull String title) {
        super(player, title);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HScoreboard show() {
        Scoreboard scoreboard = new Scoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.a("",
                IScoreboardCriteria.a,
                CraftChatMessage.fromStringOrNull(" "),
                IScoreboardCriteria.EnumScoreboardHealthDisplay.a);
        ScoreboardTeam scoreboardTeam = new ScoreboardTeam(new Scoreboard(), "team_");


        PacketPlayOutScoreboardObjective objective = new PacketPlayOutScoreboardObjective(scoreboardObjective, 0);
        this.setField(objective, "d", "board");
        this.setField(objective, "e", CraftChatMessage.fromStringOrNull(super.title));
        this.setField(objective, "f", IScoreboardCriteria.EnumScoreboardHealthDisplay.a);
        this.setField(objective, "g", this.mode);

        PacketPlayOutScoreboardDisplayObjective displayObjective = new PacketPlayOutScoreboardDisplayObjective(1, scoreboardObjective);
        this.setField(displayObjective, "a", 1);
        this.setField(displayObjective, "b", "board");

        HCore.sendPacket(super.player, objective, displayObjective);


        int length = super.lines.length;
        for (int i = 0; i < length; i++) {
            String line = super.lines[i];
            if (line == null) continue;

            String color = (i >= 10) ? "§" + new String[]{"a", "b", "c", "d", "e", "f"}[i - 10] : "§" + i;

            PacketPlayOutScoreboardTeam.b b = new PacketPlayOutScoreboardTeam.b(scoreboardTeam);
            this.setField(b, "a", CraftChatMessage.fromStringOrNull("team_" + i));
            this.setField(b, "b", CraftChatMessage.fromStringOrNull(ColorUtil.colored(line)));
            this.setField(b, "c", CraftChatMessage.fromStringOrNull(""));
            this.setField(b, "d", "always");
            this.setField(b, "e", "always");
            this.setField(b, "f", EnumChatFormat.v);
            this.setField(b, "g", 1);

            PacketPlayOutScoreboardTeam team = PacketPlayOutScoreboardTeam.a(scoreboardTeam);
            this.setField(team, "h", this.mode);
            this.setField(team, "i", "team_" + i);
            this.setField(team, "j", (this.mode == 0) ? Collections.singletonList(color) : new ArrayList<>());
            this.setField(team, "k", Optional.of(b));

            PacketPlayOutScoreboardScore score = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.a, "board", color, 15 - i);

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
        Scoreboard scoreboard = new Scoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.a("",
                IScoreboardCriteria.a,
                CraftChatMessage.fromStringOrNull(" "),
                IScoreboardCriteria.EnumScoreboardHealthDisplay.a);

        PacketPlayOutScoreboardObjective objective = new PacketPlayOutScoreboardObjective(scoreboardObjective, 1);
        this.setField(objective, "d", "board");
        this.setField(objective, "g", 1);

        HCore.sendPacket(super.player, objective);
        HScoreboardHandler.getContent().remove(super.player.getUniqueId());
        return this;
    }
}