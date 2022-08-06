package com.hakan.core.particle;

import com.hakan.core.particle.wrapper.HParticleWrapper;
import com.hakan.core.utils.GeneralUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * HParticleHandler class to
 * send particles to player.
 */
public final class HParticleHandler {

    private static HParticleWrapper wrapper;

    /**
     * Initializes the particle api.
     */
    public static void initialize() {
        wrapper = GeneralUtils.createNewInstance("com.hakan.core.particle.wrapper.HParticleWrapper_%s");
    }

    /**
     * Plays particle for player.
     *
     * @param player   Player.
     * @param location Location.
     * @param particle HParticle class.
     */
    public static void play(@Nonnull Player player, @Nonnull Location location, @Nonnull HParticle particle) {
        Validate.notNull(player, "player cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(particle, "particle cannot be null!");
        HParticleHandler.wrapper.play(player, location, particle);
    }

    /**
     * Plays particle for players.
     *
     * @param players  Players.
     * @param location Location.
     * @param particle HParticle class.
     */
    public static void play(@Nonnull Collection<Player> players, @Nonnull Location location, @Nonnull HParticle particle) {
        Validate.notNull(players, "players cannot be null!");
        players.forEach(player -> HParticleHandler.play(player, location, particle));
    }

    /**
     * Plays particle for all players.
     *
     * @param location Location.
     * @param particle HParticle class.
     */
    public static void play(@Nonnull Location location, @Nonnull HParticle particle) {
        Bukkit.getOnlinePlayers().forEach(player -> HParticleHandler.play(player, location, particle));
    }
}