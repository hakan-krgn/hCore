package com.hakan.core.item.skull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Skull data class to
 * save the texture and owner
 * of the skull.
 */
public final class HSkullData {

    private static final Map<String, HSkullData> skullDataMap = new HashMap<>();

    /**
     * Finds the skull data
     * by player name.
     *
     * @param playerName Player name.
     * @return Skull data as optional.
     */
    @Nonnull
    public static Optional<HSkullData> findByPlayer(@Nonnull String playerName) {
        Objects.requireNonNull(playerName, "player name cannot be null!");
        return Optional.ofNullable(skullDataMap.get(playerName));
    }

    /**
     * Gets the skull data
     * by player name.
     *
     * @param playerName Player name.
     * @return Skull data.
     */
    @Nonnull
    public static HSkullData getByPlayer(@Nonnull String playerName) {
        return findByPlayer(playerName).orElseThrow(() -> new IllegalArgumentException("skull texture not found!"));
    }

    /**
     * Registers the skull data.
     *
     * @param playerName Player name.
     * @param texture    Texture.
     * @return Skull data.
     */
    @Nonnull
    public static HSkullData register(@Nonnull String playerName, @Nonnull String texture) {
        HSkullData hSkullData = new HSkullData(playerName, texture);
        skullDataMap.put(playerName, hSkullData);
        return hSkullData;
    }


    private final String playerName;
    private final String texture;

    /**
     * Creates a new skull data.
     *
     * @param playerName Player name.
     * @param texture    Texture.
     */
    public HSkullData(@Nonnull String playerName, @Nonnull String texture) {
        this.playerName = Objects.requireNonNull(playerName, "player name cannot be null!");
        this.texture = Objects.requireNonNull(texture, "texture cannot be null!");
        skullDataMap.put(playerName, this);
    }

    /**
     * Gets the player name.
     *
     * @return Player name.
     */
    @Nonnull
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Gets the texture.
     *
     * @return Texture.
     */
    @Nonnull
    public String getTexture() {
        return this.texture;
    }
}