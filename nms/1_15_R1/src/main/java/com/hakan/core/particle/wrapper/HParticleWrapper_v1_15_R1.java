package com.hakan.core.particle.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.particle.HParticle;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_15_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_15_R1.ParticleParam;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_15_R1.CraftParticle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class HParticleWrapper_v1_15_R1 implements HParticleWrapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public void play(@Nonnull Player player, @Nonnull Location location, @Nonnull HParticle hParticle) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(hParticle, "hParticle class cannot be null!");

        ParticleParam particleParam = null;
        try {
            particleParam = CraftParticle.toNMS(Particle.valueOf(hParticle.getParticleName()));
        } catch (Exception e) {
            for (Particle particle : Particle.values()) {
                if (particle.name().toLowerCase().contains(hParticle.getParticleName().toLowerCase())) {
                    particleParam = CraftParticle.toNMS(particle);
                    break;
                }
            }
        }

        Validate.notNull(particleParam, "Particle not found!");
        HCore.sendPacket(player, new PacketPlayOutWorldParticles(particleParam, false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) hParticle.getOffset().getX(), (float) hParticle.getOffset().getY(), (float) hParticle.getOffset().getZ(), (float) hParticle.getSpeed(), hParticle.getAmount()));
    }
}