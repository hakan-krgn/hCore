package com.hakan.core.scoreboard.versions;

import com.hakan.core.HCore;
import com.hakan.core.scoreboard.Scoreboard;
import com.hakan.core.scoreboard.ScoreboardHandler;
import com.hakan.core.utils.ColorUtil;
import com.hakan.core.utils.ReflectionUtils;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
public final class Scoreboard_v1_20_R2 extends Scoreboard {

    private int mode = 0;

    /**
     * {@inheritDoc}
     */
    private Scoreboard_v1_20_R2(@Nonnull Player player, @Nonnull String title) {
        super(player, title);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Scoreboard show() {
        net.minecraft.world.scores.Scoreboard scoreboard = new net.minecraft.world.scores.Scoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.a("",
                IScoreboardCriteria.a,
                CraftChatMessage.fromStringOrNull(" "),
                IScoreboardCriteria.EnumScoreboardHealthDisplay.a);
        ScoreboardTeam scoreboardTeam = new ScoreboardTeam(new net.minecraft.world.scores.Scoreboard(), "team_");


        PacketPlayOutScoreboardObjective objective = new PacketPlayOutScoreboardObjective(scoreboardObjective, 0);
        ReflectionUtils.setField(objective, "d", "board");
        ReflectionUtils.setField(objective, "e", CraftChatMessage.fromStringOrNull(super.title));
        ReflectionUtils.setField(objective, "f", IScoreboardCriteria.EnumScoreboardHealthDisplay.a);
        ReflectionUtils.setField(objective, "g", this.mode);

        PacketPlayOutScoreboardDisplayObjective displayObjective = new PacketPlayOutScoreboardDisplayObjective(DisplaySlot.b, scoreboardObjective);
        ReflectionUtils.setField(displayObjective, "a", 1);
        ReflectionUtils.setField(displayObjective, "b", "board");

        HCore.sendPacket(super.player, objective, displayObjective);


        int length = super.lines.length;
        for (int i = 0; i < length; i++) {
            String line = super.getLine(i);
            if (line == null) continue;

            String color = (i >= 10) ? "§" + new String[]{"a", "b", "c", "d", "e", "f"}[i - 10] : "§" + i;

            PacketPlayOutScoreboardTeam.b b = new PacketPlayOutScoreboardTeam.b(scoreboardTeam);
            ReflectionUtils.setField(b, "a", CraftChatMessage.fromStringOrNull("team_" + i));
            ReflectionUtils.setField(b, "b", CraftChatMessage.fromStringOrNull(ColorUtil.colored(line)));
            ReflectionUtils.setField(b, "c", CraftChatMessage.fromStringOrNull(" "));
            ReflectionUtils.setField(b, "d", "always");
            ReflectionUtils.setField(b, "e", "always");
            ReflectionUtils.setField(b, "f", EnumChatFormat.v);
            ReflectionUtils.setField(b, "g", 1);

            PacketPlayOutScoreboardTeam team = PacketPlayOutScoreboardTeam.a(scoreboardTeam);
            ReflectionUtils.setField(team, "h", this.mode);
            ReflectionUtils.setField(team, "i", "team_" + i);
            ReflectionUtils.setField(team, "j", (this.mode == 0) ? Collections.singletonList(color) : new ArrayList<>());
            ReflectionUtils.setField(team, "k", Optional.of(b));

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
    public Scoreboard delete() {
        net.minecraft.world.scores.Scoreboard scoreboard = new net.minecraft.world.scores.Scoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.a("",
                IScoreboardCriteria.a,
                CraftChatMessage.fromStringOrNull(" "),
                IScoreboardCriteria.EnumScoreboardHealthDisplay.a);

        PacketPlayOutScoreboardObjective objective = new PacketPlayOutScoreboardObjective(scoreboardObjective, 1);
        ReflectionUtils.setField(objective, "d", "board");
        ReflectionUtils.setField(objective, "g", 1);

        HCore.sendPacket(super.player, objective);
        ScoreboardHandler.getContent().remove(super.player.getUniqueId());
        return this;
    }
}
