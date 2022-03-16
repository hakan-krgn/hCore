package com.hakan.core.worldborder;

import com.hakan.core.HCore;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.worldborder.border.HBorderColor;
import com.hakan.core.worldborder.border.HWorldBorder;
import com.hakan.core.worldborder.listeners.HBorderPlayerActionListener;
import com.hakan.core.worldborder.listeners.HBorderPlayerConnectionListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class HWorldBorderHandler {

    private static final List<HWorldBorder> borders = new ArrayList<>();

    public static void initialize(JavaPlugin plugin) {
        HListenerAdapter.register(new HBorderPlayerActionListener(plugin),
                new HBorderPlayerConnectionListener(plugin));
    }


    public static Collection<HWorldBorder> getContentSafe() {
        return new ArrayList<>(HWorldBorderHandler.borders);
    }

    public static Collection<HWorldBorder> getContent() {
        return HWorldBorderHandler.borders;
    }

    public static Optional<HWorldBorder> findByPlayer(Player player) {
        for (HWorldBorder hWorldBorder : HWorldBorderHandler.borders)
            if (hWorldBorder.getShownViewers().contains(player))
                return Optional.of(hWorldBorder);
        return Optional.empty();
    }

    public static HWorldBorder getByPlayer(Player player) {
        return HWorldBorderHandler.findByPlayer(player).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    public static HWorldBorder create(Location location, double size, double damageAmount, double damageBuffer, int warningDistance, int warningTime, HBorderColor color) {
        try {
            HWorldBorder hWorldBorder = (HWorldBorder) Class.forName("com.hakan.core.worldborder.border.HWorldBorder_" + HCore.getVersionString())
                    .getConstructor(Location.class, double.class, double.class, double.class, int.class, int.class, HBorderColor.class)
                    .newInstance(location, size, damageAmount, damageBuffer, warningDistance, warningTime, color);
            HWorldBorderHandler.borders.add(hWorldBorder);
            return hWorldBorder;
        } catch (Exception e) {
            return null;
        }
    }
}