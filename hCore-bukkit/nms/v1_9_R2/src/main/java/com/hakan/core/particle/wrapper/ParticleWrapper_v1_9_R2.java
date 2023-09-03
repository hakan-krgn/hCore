package com.hakan.core.particle.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.particle.Particle;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_9_R2.EnumParticle;
import net.minecraft.server.v1_9_R2.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class ParticleWrapper_v1_9_R2 implements ParticleWrapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public void play(@Nonnull Player player, @Nonnull Location location, @Nonnull Particle particle) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(particle, "particle class cannot be null!");

        EnumParticle enumParticle = null;
        try {
            enumParticle = EnumParticle.valueOf(particle.getName());
        } catch (Exception e) {
            for (EnumParticle _particle : EnumParticle.values()) {
                if (_particle.name().toLowerCase().contains(particle.getName().toLowerCase())) {
                    enumParticle = _particle;
                    break;
                }
            }
        }

        Validate.notNull(enumParticle, "particle couldn't find!");
        HCore.sendPacket(player, new PacketPlayOutWorldParticles(enumParticle, false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) particle.getOffset().getX(), (float) particle.getOffset().getY(), (float) particle.getOffset().getZ(), (float) particle.getSpeed(), particle.getAmount()));
    }
}
