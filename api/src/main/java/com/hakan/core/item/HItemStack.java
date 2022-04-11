package com.hakan.core.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

/**
 * HItemStack class to create item stack
 * and edit it easily.
 */
public class HItemStack extends ItemStack implements Serializable {

    private final ItemMeta meta;
    private boolean glow = false;

    /**
     * Creates new instance of this class.
     *
     * @param type Material type.
     */
    public HItemStack(@Nonnull Material type) {
        super(Objects.requireNonNull(type, "type cannot be null!"));
        this.meta(this.meta = super.getItemMeta());
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Material type.
     * @param amount Amount.
     */
    public HItemStack(@Nonnull Material type, int amount) {
        super(Objects.requireNonNull(type, "type cannot be null!"), amount);
        this.meta(this.meta = super.getItemMeta());
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Material type.
     * @param amount Amount.
     * @param damage Datavalue.
     */
    public HItemStack(@Nonnull Material type, int amount, short damage) {
        super(Objects.requireNonNull(type, "type cannot be null!"), amount, damage);
        this.meta(this.meta = super.getItemMeta());
    }

    /**
     * Creates new instance of this class from item stack.
     *
     * @param stack Item stack.
     */
    public HItemStack(@Nonnull ItemStack stack) {
        super(Objects.requireNonNull(stack, "item stack cannot be null!"));
        this.meta(this.meta = super.getItemMeta());
    }


    /**
     * Gets type of item stack.
     *
     * @return Type of item stack.
     */
    @Nonnull
    @Override
    public Material getType() {
        return super.getType();
    }

    /**
     * Sets type of item stack.
     *
     * @param type Material type.
     * @return This class.
     */
    @Nonnull
    public HItemStack type(@Nonnull Material type) {
        super.setType(Objects.requireNonNull(type, "type cannot be null!"));
        return this;
    }


    /**
     * Gets name of item stack.
     *
     * @return Name of item stack.
     */
    @Nonnull
    public String getName() {
        return this.meta.getDisplayName();
    }

    /**
     * Sets name of item stack.
     *
     * @param name Name of item stack.
     * @return This class.
     */
    @Nonnull
    public HItemStack name(@Nonnull String name) {
        this.meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(name, "name cannot be null!")));
        return this.meta(this.meta);
    }


    /**
     * Gets lore of item stack.
     *
     * @return Lore of item stack.
     */
    @Nonnull
    public List<String> getLore() {
        return this.meta.getLore();
    }

    /**
     * Sets lore of item stack.
     *
     * @param lore Lore of item stack
     * @return This class.
     */
    @Nonnull
    public HItemStack lores(@Nonnull List<String> lore) {
        List<String> lores = new ArrayList<>();
        for (String line : Objects.requireNonNull(lore, "lore cannot be null!"))
            lores.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(line, "lore cannot be null!")));
        this.meta.setLore(lores);
        return this.meta(this.meta);
    }


    /**
     * Gets amount of item stack.
     *
     * @return Amount of item stack.
     */
    @Override
    public int getAmount() {
        return super.getAmount();
    }

    /**
     * Sets amount of item stack.
     *
     * @param amount Amount of item stack.
     * @return This class.
     */
    @Nonnull
    public HItemStack amount(int amount) {
        super.setAmount(amount);
        return this;
    }


    /**
     * Gets durability of item stack.
     *
     * @return Durability of item stack.
     */
    @Override
    public short getDurability() {
        return super.getDurability();
    }

    /**
     * Sets durability of item stack.
     *
     * @param durability Durability of item stack.
     * @return This class.
     */
    @Nonnull
    public HItemStack durability(short durability) {
        super.setDurability(durability);
        return this;
    }


    /**
     * Gets data-value of item stack.
     *
     * @return Data-value of item stack.
     */
    @Nonnull
    @Override
    public MaterialData getData() {
        return super.getData();
    }

    /**
     * Sets data of item stack.
     *
     * @param data Data of item stack.
     * @return This class.
     */
    @Nonnull
    public HItemStack data(@Nonnull MaterialData data) {
        super.setData(Objects.requireNonNull(data, "material data cannot be null!"));
        return this;
    }


    /**
     * Checks item stack has any enchantment.
     *
     * @return If item stack has enchantment, returns true.
     */
    public boolean hasEnchants() {
        return this.meta.hasEnchants();
    }

    /**
     * Checks item stack has enchantment.
     *
     * @param enchantment Enchantment.
     * @return This class.
     */
    public boolean hasEnchant(@Nonnull Enchantment enchantment) {
        return this.meta.hasEnchant(Objects.requireNonNull(enchantment, "enchantment cannot be null!"));
    }

    /**
     * Gets enchantment list.
     *
     * @return Enchantments
     */
    @Nonnull
    public Map<Enchantment, Integer> getEnchants() {
        return this.meta.getEnchants();
    }

    /**
     * Gets level from enchantment.
     *
     * @param enchantment Enchantment.
     * @return Level of enchantment.
     */
    public int getEnchantLevel(@Nonnull Enchantment enchantment) {
        return this.meta.getEnchantLevel(Objects.requireNonNull(enchantment, "enchantment cannot be null!"));
    }

    /**
     * Adds enchantment to item stack.
     *
     * @param enchantment Enchantment.
     * @param level       Level.
     * @return This class.
     */
    @Nonnull
    public HItemStack addEnchant(@Nonnull Enchantment enchantment, int level) {
        this.meta.addEnchant(Objects.requireNonNull(enchantment, "enchantment cannot be null!"), level, true);
        return this.meta(this.meta);
    }

    /**
     * Removes enchantment from item stack.
     *
     * @param enchantment Enchantment.
     * @return This class.
     */
    @Nonnull
    public HItemStack removeEnchant(@Nonnull Enchantment enchantment) {
        this.meta.removeEnchant(Objects.requireNonNull(enchantment, "enchantment cannot be null!"));
        return this.meta(this.meta);
    }


    /**
     * Checks item stack has flag.
     *
     * @param flag Item flag.
     * @return If item stack has flag, returns true.
     */
    public boolean hasItemFlag(@Nonnull ItemFlag flag) {
        return this.meta.hasItemFlag(Objects.requireNonNull(flag, "item flag cannot be null!"));
    }

    /**
     * Gets item flag list.
     *
     * @return Item flag list.
     */
    @Nonnull
    public Set<ItemFlag> getItemFlags() {
        return this.meta.getItemFlags();
    }

    /**
     * Adds item flags to item stack.
     *
     * @param flags Item flags.
     * @return This class.
     */
    @Nonnull
    public HItemStack addItemFlags(@Nonnull ItemFlag... flags) {
        this.meta.addItemFlags(Objects.requireNonNull(flags, "item flags cannot be null!"));
        return this.meta(this.meta);
    }

    /**
     * Removes item flags from item stack.
     *
     * @param flags Item flags.
     * @return This class.
     */
    @Nonnull
    public HItemStack removeItemFlags(@Nonnull ItemFlag... flags) {
        this.meta.removeItemFlags(Objects.requireNonNull(flags, "item flags cannot be null!"));
        return this.meta(this.meta);
    }


    /**
     * Checks item is unbreakable.
     *
     * @return If item is unbreakable, returns true.
     */
    public boolean isUnbreakable() {
        return this.meta.spigot().isUnbreakable();
    }

    /**
     * Sets unbreakability of item stack.
     *
     * @param unbreakable Unbreakability.
     * @return This class.
     */
    @Nonnull
    public HItemStack unbreakable(boolean unbreakable) {
        this.meta.spigot().setUnbreakable(unbreakable);
        return this.meta(this.meta);
    }

    /**
     * Sets glow of item stack.
     *
     * @param glow Glow.
     * @return This class.
     */
    @Nonnull
    public HItemStack glow(boolean glow) {
        if (glow) {
            this.meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else if (this.glow) {
            this.meta.removeEnchant(Enchantment.ARROW_DAMAGE);
            this.meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        this.glow = glow;
        return this.meta(this.meta);
    }

    /**
     * Checks item stack has item meta.
     *
     * @return If item stack has item meta, returns true.
     */
    @Override
    public boolean hasItemMeta() {
        return true;
    }

    /**
     * Gets item meta of item stack.
     *
     * @return Item meta of item stack.
     */
    @Nonnull
    @Override
    public ItemMeta getItemMeta() {
        return super.getItemMeta();
    }

    /**
     * Sets item meta of item stack.
     *
     * @param meta Item meta.
     * @return This class.
     */
    @Nonnull
    public HItemStack meta(@Nonnull ItemMeta meta) {
        super.setItemMeta(Objects.requireNonNull(meta, "item meta cannot be null!"));
        return this;
    }
}