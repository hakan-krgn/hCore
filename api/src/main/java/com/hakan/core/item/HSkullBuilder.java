package com.hakan.core.item;

import com.hakan.core.HCore;
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
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

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

    /**
     * {@inheritDoc}
     */
    public HSkullBuilder() {
        super(SKULL);
    }

    /**
     * {@inheritDoc}
     */
    public HSkullBuilder(int amount) {
        super(SKULL, amount);
    }

    /**
     * {@inheritDoc}
     */
    public HSkullBuilder(int amount, short durability) {
        super(SKULL, amount, durability);
    }

    /**
     * {@inheritDoc}
     */
    public HSkullBuilder(@Nonnull HItemBuilder builder) {
        super(builder);
    }

    /**
     * {@inheritDoc}
     */
    public HSkullBuilder(@Nonnull ItemStack stack) {
        super(stack);
    }

    /**
     * Gets texture of skull
     *
     * @return texture of skull
     */
    @Nullable
    public String getTexture() {
        return this.texture;
    }

    /**
     * Sets texture of skull
     *
     * @param texture skull texture
     * @return instance of this class
     */
    @Nonnull
    public HSkullBuilder texture(@Nullable String texture) {
        Objects.requireNonNull(texture, "texture cannot be null!");
        this.texture = new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", "https://textures.minecraft.net/texture/" + texture).getBytes()));
        return this;
    }

    /**
     * Sets texture of skull
     *
     * @param player Player
     * @return instance of this class
     */
    @Nonnull
    public HSkullBuilder texture(@Nullable Player player) {
        try {
            Objects.requireNonNull(player, "player cannot be null!");

            Class<?> clazz = Class.forName("org.bukkit.craftbukkit." + HCore.getVersionString() + ".entity.CraftPlayer");
            Object craftPlayer = clazz.cast(player);
            GameProfile gameProfile = (GameProfile) clazz.getDeclaredMethod("getProfile").invoke(craftPlayer);
            Iterator<Property> property = gameProfile.getProperties().get("textures").iterator();

            this.texture = ((property.hasNext()) ? property.next().getValue() : "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Builds skull item stack
     *
     * @return skull item stack
     */
    @Nonnull
    @Override
    public ItemStack build() {
        ItemStack item = super.build();

        ItemMeta meta = item.getItemMeta();
        if (meta instanceof SkullMeta) {
            try {
                SkullMeta skullMeta = (SkullMeta) meta;

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