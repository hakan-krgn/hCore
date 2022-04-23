package com.hakan.core.particle.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.particle.HParticle;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.network.protocol.game.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_18_R1.CraftParticle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public final class HParticleWrapper_v1_18_R1 implements HParticleWrapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public void play(@Nonnull Player player, @Nonnull Location location, @Nonnull HParticle hParticle) {
        Objects.requireNonNull(player, "player cannot be null!");
        Objects.requireNonNull(location, "location cannot be null!");
        Objects.requireNonNull(hParticle, "hParticle class cannot be null!");

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

        Objects.requireNonNull(particleParam, "Particle not found!");
        HCore.sendPacket(player, new PacketPlayOutWorldParticles(particleParam, false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) hParticle.getOffset().getX(), (float) hParticle.getOffset().getY(), (float) hParticle.getOffset().getZ(), (float) hParticle.getSpeed(), hParticle.getAmount()));
    }
}