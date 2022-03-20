package com.hakan.core.particle;

import com.hakan.core.HCore;
import com.hakan.core.particle.wrapper.HParticleWrapper;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;

public final class HParticleHandler {

    private static HParticleWrapper wrapper;

    /**
     * Initializes the particle api.
     */
    public static void initialize() {
        try {
            HParticleHandler.wrapper = (HParticleWrapper) Class.forName("com.hakan.core.particle.wrapper.HParticleWrapper_" + HCore.getVersionString())
                    .getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}