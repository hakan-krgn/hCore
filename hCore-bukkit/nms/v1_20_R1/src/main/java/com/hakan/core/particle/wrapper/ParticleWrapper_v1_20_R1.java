package com.hakan.core.particle.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.particle.Particle;
import com.hakan.core.utils.Validate;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.network.protocol.game.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftParticle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class ParticleWrapper_v1_20_R1 implements ParticleWrapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public void play(@Nonnull Player player, @Nonnull Location location, @Nonnull Particle particle) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(particle, "particle class cannot be null!");

        ParticleParam particleParam = null;
        try {
            particleParam = CraftParticle.toNMS(org.bukkit.Particle.valueOf(particle.getName()));
        } catch (Exception e) {
            for (org.bukkit.Particle _particle : org.bukkit.Particle.values()) {
                if (_particle.name().toLowerCase().contains(particle.getName().toLowerCase())) {
                    particleParam = CraftParticle.toNMS(_particle);
                    break;
                }
            }
        }

        Validate.notNull(particleParam, "particle couldn't find!");
        HCore.sendPacket(player, new PacketPlayOutWorldParticles(particleParam, false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) particle.getOffset().getX(), (float) particle.getOffset().getY(), (float) particle.getOffset().getZ(), (float) particle.getSpeed(), particle.getAmount()));
    }
}
