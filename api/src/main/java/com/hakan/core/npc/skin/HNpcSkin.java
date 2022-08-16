package com.hakan.core.npc.skin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * HNpcSkin class to handle
 * skin of NPCs.
 */
public final class HNpcSkin {

    public static final HNpcSkin EMPTY = new HNpcSkin("", "");

    /**
     * Gets skin of player.
     *
     * @param player Player.
     * @return Skin.
     */
    @Nonnull
    public static HNpcSkin from(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return from(player.getName());
    }

    /**
     * Gets skin of player.
     *
     * @param playerName Player name.
     * @return Skin.
     */
    @Nonnull
    public static HNpcSkin from(@Nonnull String playerName) {
        try {
            Validate.notNull(playerName, "player name cannot be null!");

            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();

            return new HNpcSkin(texture, signature);
        } catch (Exception e) {
            throw new RuntimeException("there is no skin for player " + playerName, e);
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
    public HNpcSkin(@Nonnull String texture, @Nonnull String signature) {
        this.texture = Validate.notNull(texture, "texture cannot be null!");
        this.signature = Validate.notNull(signature, "signature cannot be null!");
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