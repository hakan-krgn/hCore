package com.hakan.core.particle.wrapper;

import com.hakan.core.particle.HParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Particle handler class to
 * play particle effects.
 */
public interface HParticleWrapper {

    /**
     * Players particle for player.
     *
     * @param player    Player.
     * @param location  Location.
     * @param hParticle HParticle class.
     */
    void play(@Nonnull Player player, @Nonnull Location location, @Nonnull HParticle hParticle);
}