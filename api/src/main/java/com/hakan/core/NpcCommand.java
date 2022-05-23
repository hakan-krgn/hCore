package com.hakan.core;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.HNPCHandler;
import org.bukkit.entity.Player;

import java.util.Arrays;

@BaseCommand(name = "npc")
public class NpcCommand implements HCommandAdapter {

    @SubCommand
    public void execute(Player player, String[] args) {
        HNPC npc = HNPCHandler.create("sa", player.getLocation(), Arrays.asList("hakan", "sa"));
        npc.setLocation(player.getLocation());
        npc.setSkin(player.getName());
    }
}