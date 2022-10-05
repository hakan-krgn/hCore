package com.hakan.core;

import com.hakan.core.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Java plugin class to
 * initialize hCore.
 */
@Plugin(
        name = "hCore",
        authors = "Hakan",
        version = "0.6.6",
        apiVersion = "1.13",
        website = "https://github.com/hakan-krgn/hCore",
        description = "hCore is a plugin that offers many useful features for spigot developers."
)
public final class Bootstrap extends JavaPlugin {

    /*
    STARTER
     */
    @Override
    public void onEnable() {
        HCore.initialize(this);
    }
}