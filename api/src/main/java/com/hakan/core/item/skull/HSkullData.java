package com.hakan.core.item.skull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        HSkullData skullData = new HSkullData(playerName, texture);
        skullDataMap.put(playerName, skullData);
        return skullData;
    }

    /**
     * Registers the skull data by player name.
     * <p>
     * Gets the texture from mojang's
     * website and saves it.
     *
     * @param playerName Player name.
     * @return Skull data.
     */
    @Nonnull
    public static HSkullData register(@Nonnull String playerName) {
        try {
            Objects.requireNonNull(playerName, "player name cannot be null!");

            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            InputStreamReader read = new InputStreamReader(url.openStream());
            JsonObject textureProperty = new JsonParser().parse(read).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();

            return register(playerName, textureProperty.get("value").getAsString());
        } catch (Exception e) {
            return new HSkullData("", "");
        }
    }


    private final String playerName;
    private final String texture;
    private final String shortTexture;

    /**
     * Creates a new skull data.
     *
     * @param playerName Player name.
     * @param texture    Texture.
     */
    public HSkullData(@Nonnull String playerName, @Nonnull String texture) {
        this.playerName = Objects.requireNonNull(playerName, "player name cannot be null!");
        this.texture = Objects.requireNonNull(texture, "texture cannot be null!");

        Matcher matcher = Pattern.compile("\"(http://textures\\.minecraft\\.net/texture/)(?<shortTexture>\\w+)\"")
                .matcher(new String(Base64.getDecoder().decode(texture.getBytes())));
        this.shortTexture = matcher.find() ? matcher.group("shortTexture") : "";
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

    /**
     * Gets the short texture from
     * decoded texture.
     *
     * @return Short texture.
     */
    @Nonnull
    public String getShortTexture() {
        return this.shortTexture;
    }
}