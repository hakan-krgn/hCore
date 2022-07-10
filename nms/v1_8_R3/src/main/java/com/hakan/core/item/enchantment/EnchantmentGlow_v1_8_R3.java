package com.hakan.core.item.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public class EnchantmentGlow_v1_8_R3 extends Enchantment {

    /**
     * Gets instance of this class
     *
     * @param id enchantment id
     */
    private EnchantmentGlow_v1_8_R3(int id) {
        super(id);
    }

    /**
     * Gets enchantment name
     *
     * @return enchantment name
     */
    @Nonnull
    @Override
    public String getName() {
        return "GlowEnchantment";
    }

    /**
     * Gets max level of enchantment
     *
     * @return max level of enchantment
     */
    @Override
    public int getMaxLevel() {
        return 0;
    }

    /**
     * Gets minimum level of enchantment
     *
     * @return minimum level of enchantment
     */
    @Override
    public int getStartLevel() {
        return 0;
    }

    /**
     * Gets item targets of enchantment
     *
     * @return item targets of enchantment
     */
    @Nonnull
    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    /**
     * Gets item targets of enchantment
     *
     * @return item targets of enchantment
     */
    @Override
    public boolean conflictsWith(@Nonnull Enchantment enchantment) {
        return false;
    }

    /**
     * Can we enchant with param item stack by this enchantment
     *
     * @return enchant with param item stack by this enchantment
     */
    @Override
    public boolean canEnchantItem(@Nonnull ItemStack itemStack) {
        return false;
    }
}