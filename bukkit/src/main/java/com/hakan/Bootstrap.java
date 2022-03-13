package com.hakan;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of this core.
 * You can reach all APIs from this
 * class as static.
 */
public class Bootstrap extends JavaPlugin {

    /*
    STARTER
     */
    @Override
    public void onEnable() {
        HCore.initialize(this);

        HCore.registerCommand("sa").onCommand(commandData -> {
            Player player = (Player) commandData.getSender();


        });
    }
}