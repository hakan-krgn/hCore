package com.hakan.core.particle;

import com.hakan.core.particle.wrapper.ParticleWrapper;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * ParticleHandler class to
 * send particles to player.
 */
public final class ParticleHandler {

    private static ParticleWrapper wrapper;

    /**
     * Initializes the particle api.
     */
    public static void initialize() {
        wrapper = ReflectionUtils.newInstance("com.hakan.core.particle.wrapper.ParticleWrapper_%s");
    }

    /**
     * Plays particle for player.
     *
     * @param player   Player.
     * @param location Location.
     * @param particle Particle class.
     */
    public static void play(@Nonnull Player player, @Nonnull Location location, @Nonnull Particle particle) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(particle, "particle cannot be null!");
        wrapper.play(player, location, particle);
    }

    /**
     * Plays particle for players.
     *
     * @param players  Players.
     * @param location Location.
     * @param particle Particle class.
     */
    public static void play(@Nonnull Collection<Player> players, @Nonnull Location location, @Nonnull Particle particle) {
        Validate.notNull(players, "players cannot be null!");
        players.forEach(player -> ParticleHandler.play(player, location, particle));
    }

    /**
     * Plays particle for all players.
     *
     * @param location Location.
     * @param particle Particle class.
     */
    public static void play(@Nonnull Location location, @Nonnull Particle particle) {
        Bukkit.getOnlinePlayers().forEach(player -> ParticleHandler.play(player, location, particle));
    }
}
