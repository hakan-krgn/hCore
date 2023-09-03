package com.hakan.core.skin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hakan.core.utils.Validate;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Skin class to handle
 * save or get texture and signature
 */
public class Skin {

    private static final Pattern PATTERN = Pattern.compile("\"(http://textures\\.minecraft\\.net/texture/)(?<shortTexture>\\w+)\"");
    public static final Skin STEVE = new Skin(
            "ewogICJ0aW1lc3RhbXAiIDogMTY2MTY3OTM3OTkwMCwKICAicHJvZmlsZUlkIiA6ICI4NjY3YmE3MWI4NWE0MDA0YWY1NDQ1N2E5" +
            "NzM0ZWVkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdGV2ZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR" +
            "1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dH" +
            "VyZS82MGE1YmQwMTZiM2M5YTFiOTI3MmU0OTI5ZTMwODI3YTY3YmU0ZWJiMjE5MDE3YWRiYmM0YTRkMjJlYmQ1YjEiCiAgICB9L" +
            "AogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk1M2Nh" +
            "YzhiNzc5ZmU0MTM4M2U2NzVlZTJiODYwNzFhNzE2NThmMjE4MGY1NmZiY2U4YWEzMTVlYTcwZTJlZDYiCiAgICB9CiAgfQp9",

            "ucw8h2rj66lroaieZON3VyQRbSLDdZzS/lOiAIrNbcKgsrKuD8UAiC9daUiRJ7aQCilOhCnnKVwwXX74PmHWL4nAPpPb903ZHwh" +
            "mRS8Mshc0mPZrpvWfpkoZhDVdiFpqHSfGdwRweZyCifa39DRJstAuPmqdp3Ioyot6Rx4bCauXV8WQq0yMgP+oDrkx8O2aBr19h6" +
            "6OXDl7Er28e8IgDnNHAcanSCNnWgV/RYiBqIzBmItLescpyTqCnVl3uYZfXVyEvNEy5IIBM6nhV4VoP9sf8Ld0AF6bXsSCaMbaJ" +
            "8h99+jqCUlSGbFuMYlU8Ih0us9vvaAolqQasjvNxpXbN390mU3lw3NupCzSNNG+47Os5XeZ8C6nkY1kq3eHTNW/hDYwIi1A2TQv" +
            "qvmeU0dMclV9L/Oj85YA1RbCYkK/3DuQBf10rek26EiOL1qyBL5A8jBorK5mR2ZWzDgWorN9XXclwPnadt+nN80m+fDuZBGS6yY" +
            "Rljc/gqckTBprd055Vk93K3LmqBKS87nIILt9QRQO8x8rLYMZ4uz0Kwzagj/Gb4V7FHtZ4VMyzcqS9iYJfGGzBuvL72CzkAM96P" +
            "YXO4PjSTgzQ4FwlMkNndcyt4TecwcrmfKpusW1j4i4QM092ktbDvMt9LFVCZkVRqnsiN/va5/RnNBfLJpcp7IzLLU="
    );

    /**
     * Gets com.hakan.core.skin of player from
     * mojang api.
     *
     * @param player Player.
     * @return Skin.
     */
    @Nonnull
    public static Skin from(@Nonnull ProxiedPlayer player) {
        Validate.notNull(player, "player cannot be null!");
        return from(player.getName());
    }

    /**
     * Gets com.hakan.core.skin of player from
     * mojang api.
     *
     * @param playerName Player name.
     * @return Skin.
     * @throws RuntimeException If there is no com.hakan.core.skin for player.
     */
    @Nonnull
    public static Skin from(@Nonnull String playerName) {
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

            return new Skin(texture, signature);
        } catch (Exception e) {
            return STEVE;
        }
    }



    private final String texture;
    private final String signature;
    private final String shortTexture;

    /**
     * Constructor to create com.hakan.core.skin
     * from texture and signature.
     *
     * @param texture   Texture.
     * @param signature Signature.
     */
    public Skin(@Nonnull String texture,
                @Nonnull String signature) {
        this.texture = Validate.notNull(texture, "texture cannot be null!");
        this.signature = Validate.notNull(signature, "signature cannot be null!");

        Matcher matcher = PATTERN.matcher(new String(Base64.getDecoder().decode(texture.getBytes())));
        this.shortTexture = matcher.find() ? matcher.group("shortTexture") : "";
    }

    /**
     * Gets texture.
     *
     * @return Texture.
     */
    @Nonnull
    public final String getTexture() {
        return this.texture;
    }

    /**
     * Gets signature.
     *
     * @return Signature.
     */
    @Nonnull
    public final String getSignature() {
        return this.signature;
    }

    /**
     * Gets short texture.
     *
     * @return Short texture.
     */
    @Nonnull
    public final String getShortTexture() {
        return this.shortTexture;
    }
}
