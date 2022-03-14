package com.hakan.ui.sign;

import com.hakan.HCore;
import com.hakan.listener.HListenerAdapter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class HSignHandler {

    private static final Map<UUID, HSign> signMap = new HashMap<>();

    public static void initialize(JavaPlugin plugin) {
        HListenerAdapter.register(new HSignListener(plugin));
    }


    public static Map<UUID, HSign> getContentSafe() {
        return new HashMap<>(HSignHandler.signMap);
    }

    public static Map<UUID, HSign> getContent() {
        return HSignHandler.signMap;
    }

    public static Collection<HSign> getValuesSafe() {
        return new ArrayList<>(HSignHandler.signMap.values());
    }

    public static Collection<HSign> getValues() {
        return HSignHandler.signMap.values();
    }

    public static Optional<HSign> findByPlayer(Player player) {
        return HSignHandler.findByUID(player.getUniqueId());
    }

    public static HSign getByPlayer(Player player) {
        return HSignHandler.findByPlayer(player).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    public static Optional<HSign> findByUID(UUID uid) {
        return Optional.ofNullable(HSignHandler.signMap.get(uid));
    }

    public static HSign getByUID(UUID uid) {
        return HSignHandler.findByUID(uid).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    public static HSign create(Material type, String... lines) {
        try {
            return (HSign) Class.forName("com.hakan.ui.sign.wrapper.HSign_" + HCore.getVersionString())
                    .getConstructor(Material.class, String[].class).newInstance(type, lines);
        } catch (Exception e) {
            return null;
        }
    }
}