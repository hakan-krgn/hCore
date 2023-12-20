package com.hakan.core.particle.wrapper;

import com.hakan.core.particle.Particle;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class ParticleWrapper_v1_20_R3 implements ParticleWrapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public void play(@Nonnull Player player, @Nonnull Location location, @Nonnull Particle particle) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(particle, "particle class cannot be null!");

        org.bukkit.Particle particleParam = null;
        try {
            particleParam = org.bukkit.Particle.valueOf(particle.getName());
        } catch (Exception e) {
            for (org.bukkit.Particle _particle : org.bukkit.Particle.values()) {
                if (_particle.name().toLowerCase().contains(particle.getName().toLowerCase())) {
                    particleParam = _particle;
                    break;
                }
            }
        }

        Validate.notNull(particleParam, "particle couldn't find!");
        player.spawnParticle(particleParam, location,
                particle.getAmount(),
                particle.getOffset().getX(),
                particle.getOffset().getY(),
                particle.getOffset().getZ(),
                particle.getSpeed()
        );
    }
}
