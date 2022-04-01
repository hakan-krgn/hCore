package com.hakan.core;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Java plugin class to
 * initialize hCore.
 */
public final class Bootstrap extends JavaPlugin {

    /*
    STARTER
     */
    @Override
    public void onEnable() {
        HCore.initialize(this);
    }
}