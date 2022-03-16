package com.hakan.core.particle.wrapper;

import com.hakan.core.particle.HParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface HParticleWrapper {

    void play(Player player, Location location, HParticle hParticle);
}
