package com.hakan.core.particle;

import com.hakan.core.HCore;
import com.hakan.core.particle.wrapper.HParticleWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public final class HParticleAPI {

    private static HParticleWrapper wrapper;

    public static void initialize() {
        try {
            HParticleAPI.wrapper = (HParticleWrapper) Class.forName("com.hakan.core.particle.wrapper.HParticleWrapper_" + HCore.getVersionString()).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void play(Player player, Location location, HParticle particle) {
        HParticleAPI.wrapper.play(player, location, particle);
    }

    public static void play(Collection<Player> player, Location location, HParticle particle) {
        player.forEach(p -> HParticleAPI.play(p, location, particle));
    }
}