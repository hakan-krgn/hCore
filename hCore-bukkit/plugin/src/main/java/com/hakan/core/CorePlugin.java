package com.hakan.core;

import com.hakan.core.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Plugin(
        name = "hCore",
        authors = "Hakan",
        version = "0.7.2.4",
        description = "A plugin which contains hCore.",
        website = "https://github.com/hakan-krgn/hCore"
)
public class CorePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        HCore.initialize(this);
    }
}
