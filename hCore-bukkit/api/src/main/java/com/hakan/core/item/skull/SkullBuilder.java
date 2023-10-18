package com.hakan.core.item.skull;

import com.hakan.core.HCore;
import com.hakan.core.item.ItemBuilder;
import com.hakan.core.skin.Skin;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * SkullBuilder class to create and
 * manage skull item stacks easily.
 */
public class SkullBuilder extends ItemBuilder {

    private static Material SKULL;
    private static Map<String, Skin> skullDataMap;

    /**
     * Initialize the skull builder class.
     */
    public static void initialize() {
        try {
            SKULL = Material.valueOf("PLAYER_HEAD");
        } catch (Exception e) {
            SKULL = Material.valueOf("SKULL_ITEM");
        }
        skullDataMap = new HashMap<>();
    }

    /**
     * Gets the skin from map,
     * if it doesn't exist, it will
     * get from mojang.
     *
     * @param name Name of the player.
     * @return Skin of the player.
     */
    @Nonnull
    public static Skin getOrLoad(@Nonnull String name) {
        Validate.notNull(name, "name cannot be null!");

        if (skullDataMap.containsKey(name))
            return skullDataMap.get(name);

        Skin skin = Skin.from(name);
        skullDataMap.put(name, skin);
        return skin;
    }


    private String texture;
    private String ownerName;

    /**
     * {@inheritDoc}
     */
    public SkullBuilder() {
        this(1);
    }

    /**
     * {@inheritDoc}
     */
    public SkullBuilder(int amount) {
        super(SKULL, amount, (short) 3);
        this.texture = null;
        this.ownerName = null;
    }

    /**
     * {@inheritDoc}
     */
    public SkullBuilder(@Nonnull ItemBuilder builder) {
        super(builder.type(SKULL).durability((short) 3));
        this.texture = null;
        this.ownerName = null;
    }

    /**
     * {@inheritDoc}
     */
    public SkullBuilder(@Nonnull SkullBuilder builder) {
        super(builder);
        this.texture = builder.texture;
        this.ownerName = builder.ownerName;
    }

    /**
     * Gets texture of skull.
     *
     * @return texture of skull.
     */
    @Nullable
    public String getTexture() {
        return this.texture;
    }

    /**
     * Sets texture of skull.
     *
     * @param texture skull texture.
     * @return instance of this class.
     */
    @Nonnull
    public SkullBuilder shortTexture(@Nullable String texture) {
        String url = String.format("{textures:{SKIN:{url:\"%s\"}}}", "https://textures.minecraft.net/texture/" + texture);
        this.texture = new String(Base64.getEncoder().encode(url.getBytes()));
        return this;
    }

    /**
     * Sets texture of skull.
     *
     * @param texture skull texture.
     * @return instance of this class.
     */
    @Nonnull
    public SkullBuilder texture(@Nullable String texture) {
        this.texture = texture;
        return this;
    }

    /**
     * Sets texture of skull.
     *
     * @param owner Player name.
     * @return instance of this class.
     */
    @Nonnull
    public SkullBuilder textureByPlayer(@Nullable String owner) {
        this.ownerName = owner;
        return this;
    }

    /**
     * Sets texture of skull.
     *
     * @param player Player.
     * @return instance of this class.
     */
    @Nonnull
    public SkullBuilder textureByPlayer(@Nullable Player player) {
        this.ownerName = player != null ? player.getName() : null;
        return this;
    }

    /**
     * Builds skull item stack as async.
     *
     * @param skull Consumer for item stack.
     */
    public void buildAsync(Consumer<ItemStack> skull) {
        HCore.asyncScheduler().run(() -> skull.accept(this.build()));
    }

    /**
     * Builds skull item stack.
     *
     * @return skull item stack.
     */
    @Nonnull
    @Override
    public ItemStack build() {
        ItemStack item = super.build();

        ItemMeta meta = item.getItemMeta();
        if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;

            if (this.ownerName != null) {
                Skin skullData = SkullBuilder.getOrLoad(this.ownerName);
                this.texture = skullData.getTexture();
            }

            if (this.texture != null) {
                UUID uuid = UUID.randomUUID();
                GameProfile profile = new GameProfile(uuid, uuid.toString());
                profile.getProperties().put("textures", new Property("textures", this.texture));
                ReflectionUtils.setField(skullMeta, "profile", profile);
            }

            item.setItemMeta(skullMeta);
        }

        return item;
    }
}
