package com.hakan.core;

import org.bukkit.plugin.java.JavaPlugin;

public class Bootstrap extends JavaPlugin {

    /*
    STARTER
     */
    @Override
    public void onEnable() {
        HCore.initialize(this);
    }
}