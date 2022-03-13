package com.hakan.packet;

import com.hakan.HCore;
import com.hakan.listener.HListenerAdapter;
import com.hakan.packet.listeners.PlayerConnectionListener;
import com.hakan.packet.player.HPacketPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HPacketHandler {

    private static final Map<Player, HPacketPlayer> packetPlayers = new HashMap<>();

    public static void initialize(JavaPlugin plugin) {
        HListenerAdapter.register(new PlayerConnectionListener(plugin));
    }


    public static Map<Player, HPacketPlayer> getPacketPlayerMap() {
        return HPacketHandler.packetPlayers;
    }

    public static Collection<HPacketPlayer> getPacketPlayers() {
        return HPacketHandler.packetPlayers.values();
    }

    public static void create(Player player) {
        try {
            HPacketPlayer packetPlayer = (HPacketPlayer) Class.forName("com.hakan.packet.player.HPacketPlayer_" + HCore.getVersionString())
                    .getConstructor(Player.class).newInstance(player);
            HPacketHandler.packetPlayers.put(player, packetPlayer);
            packetPlayer.register();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void remove(Player player) {
        HPacketPlayer packetPlayer = HPacketHandler.packetPlayers.remove(player);
        packetPlayer.unregister();
    }
}