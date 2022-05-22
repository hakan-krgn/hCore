package com.hakan.core.command.test;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@BaseCommand(name = "example",
        description = "Example command",
        usage = "/example",
        aliases = {"ex"}
)
public class ExampleCommand implements HCommandAdapter {

    @SubCommand(permission = "example.main")
    public void mainCommand(CommandSender sender, String[] args) {
        sender.sendMessage("ana komudu yazdın");
    }

    @SubCommand(
            args = {"admin", "deneme"},
            permission = "example.admin.deneme"
    )
    public void testCommand2(Player player, String[] args) {
        player.sendMessage("example admin deneme kullandın");
    }

    @SubCommand(
            args = {"admin", "set", "<arg>", "<arg>"},
            permission = "example.admin.set"
    )
    public void testCommand(CommandSender sender, String[] args) {
        sender.sendMessage("example admin set " + args[2] + " " + args[3] + " kullandın");
    }
}