package com.hakan.core.npc.skin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

/**
 * HNPCSkin class to handle
 * skin of NPCs.
 */
public final class HNPCSkin {

    public static final HNPCSkin EMPTY = new HNPCSkin("", "");

    /**
     * Gets skin of player.
     *
     * @param player Player.
     * @return Skin.
     */
    @Nonnull
    public static HNPCSkin from(@Nonnull Player player) {
        Objects.requireNonNull(player, "player cannot be null!");
        return from(player.getName());
    }

    /**
     * Gets skin of player.
     *
     * @param playerName Player name.
     * @return Skin.
     */
    @Nonnull
    public static HNPCSkin from(@Nonnull String playerName) {
        try {
            Objects.requireNonNull(playerName, "player name cannot be null!");

            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();

            return new HNPCSkin(texture, signature);
        } catch (IOException e) {
            return new HNPCSkin("", "");
        }
    }


    private final String texture;
    private final String signature;

    /**
     * Constructor.
     *
     * @param texture   Texture.
     * @param signature Signature.
     */
    public HNPCSkin(@Nonnull String texture, @Nonnull String signature) {
        this.texture = Objects.requireNonNull(texture, "texture cannot be null!");
        this.signature = Objects.requireNonNull(signature, "signature cannot be null!");
    }

    /**
     * Gets texture.
     *
     * @return Texture.
     */
    @Nonnull
    public String getTexture() {
        return this.texture;
    }

    /**
     * Gets signature.
     *
     * @return Signature.
     */
    @Nonnull
    public String getSignature() {
        return this.signature;
    }
}