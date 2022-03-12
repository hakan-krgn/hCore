package com.hakan.packet;

import com.hakan.listener.HListenerAdapter;
import com.hakan.packet.listeners.PlayerConnectionListener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class HPacket {

    public static void initialize(JavaPlugin plugin) {
        HListenerAdapter.register(new PlayerConnectionListener(plugin));
    }


    public HPacket() {

    }
}