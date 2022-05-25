package com.hakan.core;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.HNPCHandler;
import com.hakan.core.npc.types.HNPCEquipmentType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BaseCommand(name = "test")
public class NpcCommand implements HCommandAdapter {

    @SubCommand
    public void execute(Player player, String[] args) {
        String id = player.getName() + System.currentTimeMillis();
        Location location = player.getLocation();
        List<String> lines = Arrays.asList("this is a", "client-side npc", "with client-side hologram");

        HNPC npc = HNPCHandler.build(id)
                .showEveryone(true)
                .location(location)
                .lines(lines)
                .skin(player.getName())
                .build();

        npc.setEquipment(HNPCEquipmentType.CHEST, new ItemStack(Material.DIAMOND_CHESTPLATE));
        npc.setEquipment(HNPCEquipmentType.LEGS, new ItemStack(Material.LEATHER_LEGGINGS));
        npc.expire(15, TimeUnit.SECONDS);

        HCore.syncScheduler().after(20 * 5)
                .run(() -> npc.walk(player.getLocation(), 0.25));
    }
}