package com.hakan.core.item.skull;

import com.hakan.core.HCore;
import com.hakan.core.item.HItemBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * HSkullBuilder class to create and
 * manage skull item stacks easily.
 */
public class HSkullBuilder extends HItemBuilder {

    private static Material SKULL;

    /**
     * Initialize the skull builder class.
     */
    public static void initialize() {
        try {
            SKULL = Material.valueOf("PLAYER_HEAD");
        } catch (Exception e) {
            SKULL = Material.valueOf("SKULL_ITEM");
        }
    }


    private String texture;
    private String ownerName;

    /**
     * {@inheritDoc}
     */
    public HSkullBuilder() {
        this(1);
    }

    /**
     * {@inheritDoc}
     */
    public HSkullBuilder(int amount) {
        super(SKULL, amount, (short) 3);
        this.texture = null;
        this.ownerName = null;
    }

    /**
     * {@inheritDoc}
     */
    public HSkullBuilder(@Nonnull HItemBuilder builder) {
        super(builder.type(SKULL).durability((short) 3));
        this.texture = null;
        this.ownerName = null;
    }

    /**
     * {@inheritDoc}
     */
    public HSkullBuilder(@Nonnull HSkullBuilder builder) {
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
    public HSkullBuilder shortTexture(@Nullable String texture) {
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
    public HSkullBuilder texture(@Nullable String texture) {
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
    public HSkullBuilder textureByPlayer(@Nullable String owner) {
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
    public HSkullBuilder textureByPlayer(@Nullable Player player) {
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
            try {
                SkullMeta skullMeta = (SkullMeta) meta;

                if (this.ownerName != null) {
                    HSkullData skullData = HSkullData.findByPlayer(this.ownerName).orElse(null);
                    if (skullData == null)
                        skullData = HSkullData.register(this.ownerName);
                    this.texture = skullData.getTexture();
                }

                if (this.texture != null) {
                    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                    profile.getProperties().put("textures", new Property("textures", this.texture));

                    Field profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, profile);
                    profileField.setAccessible(false);
                }

                item.setItemMeta(skullMeta);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return item;
    }
}