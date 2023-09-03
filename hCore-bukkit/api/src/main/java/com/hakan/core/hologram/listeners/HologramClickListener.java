package com.hakan.core.hologram.listeners;

import com.hakan.core.HCore;
import com.hakan.core.hologram.Hologram;
import com.hakan.core.hologram.HologramHandler;
import com.hakan.core.packet.event.PacketEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

/**
 * HologramClickListener class to listen
 * the hologram clicks.
 */
public final class HologramClickListener implements Listener {

    /**
     * Called when a packet is received.
     *
     * @param event PacketEvent.
     */
    @EventHandler
    public void onPacketEvent(@Nonnull PacketEvent event) {
        Player player = event.getPlayer();
        Object packet = event.getPacket();

        if (!packet.getClass().getName().contains("PacketPlayInUseEntity"))
            return;

        HCore.asyncScheduler().run(() -> {
            for (Hologram hologram : HologramHandler.getValues()) {
                if (hologram.hasLineByEntityID(event.getValue("a"))) {
                    Location playerLocation = player.getEyeLocation();
                    Location hologramLocation = hologram.getLocation();

                    double xP = playerLocation.getX();
                    double zP = playerLocation.getZ();
                    double xH = hologramLocation.getX();
                    double zH = hologramLocation.getZ();
                    double distance = Math.sqrt(Math.pow(xP - xH, 2) + Math.pow(zP - zH, 2));

                    float pitch = -playerLocation.getPitch();

                    double y1 = hologramLocation.getY() + ((hologram.getLines().size() - 1) * hologram.getLineDistance() + 0.24) / 2.0;
                    double y2 = playerLocation.getY() + distance * Math.tan(Math.toRadians(pitch));

                    int index = (int) Math.floor((y1 - y2) / hologram.getLineDistance());
                    if (index < 0 || index >= hologram.getLines().size())
                        break;

                    hologram.getAction().onClick(event.getPlayer(), hologram.getLine(index));
                    return;
                }
            }
        });
    }
}
