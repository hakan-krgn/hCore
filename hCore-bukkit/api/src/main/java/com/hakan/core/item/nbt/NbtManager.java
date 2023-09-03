package com.hakan.core.item.nbt;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * NbtManager class for multi
 * version support.
 */
public interface NbtManager {

    /**
     * Sets key of nbt to value.
     *
     * @param itemStack item stack.
     * @param key       key.
     * @param value     value.
     * @return item stack.
     */
    @Nonnull
    ItemStack set(@Nonnull ItemStack itemStack, @Nonnull String key, @Nonnull String value);

    /**
     * Sets nbt of item stack.
     *
     * @param itemStack item stack.
     * @param nbt       nbt as json.
     * @return item stack.
     */
    @Nonnull
    ItemStack set(@Nonnull ItemStack itemStack, @Nonnull String nbt);

    /**
     * Gets value of nbt from key.
     *
     * @param itemStack item stack.
     * @param key       key.
     * @return value from key.
     */
    @Nonnull
    String get(@Nonnull ItemStack itemStack, @Nonnull String key);

    /**
     * Gets nbt of item stack.
     *
     * @param itemStack item stack.
     * @return nbt of item stack.
     */
    @Nonnull
    String get(@Nonnull ItemStack itemStack);
}
