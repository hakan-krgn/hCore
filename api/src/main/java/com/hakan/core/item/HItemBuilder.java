package com.hakan.core.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import javax.annotation.Nonnull;
import java.util.*;

public class HItemBuilder {

    private ItemStack stack;
    private final ItemMeta meta;

    /**
     * Creates new instance of this class.
     *
     * @param type Material type.
     */
    public HItemBuilder(@Nonnull Material type) {
        this.stack = new ItemStack(type);
        this.meta = this.stack.getItemMeta();
        this.meta.setLore(new ArrayList<>());
        this.stack.setItemMeta(this.meta);
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Material type.
     * @param amount Amount.
     */
    public HItemBuilder(@Nonnull Material type, int amount) {
        this.stack = new ItemStack(type, amount);
        this.meta = this.stack.getItemMeta();
        this.meta.setLore(new ArrayList<>());
        this.stack.setItemMeta(this.meta);
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Material type.
     * @param amount Amount.
     * @param damage Datavalue.
     */
    public HItemBuilder(@Nonnull Material type, int amount, short damage) {
        this.stack = new ItemStack(type, amount, damage);
        this.meta = this.stack.getItemMeta();
        this.meta.setLore(new ArrayList<>());
        this.stack.setItemMeta(this.meta);
    }

    /**
     * Creates new instance of this class from item stack.
     *
     * @param stack Item stack.
     */
    public HItemBuilder(@Nonnull ItemStack stack) {
        this.stack = new ItemStack(stack);
        this.meta = this.stack.getItemMeta();
        this.meta.setLore(this.meta.hasLore() ? this.meta.getLore() : new ArrayList<>());
        this.stack.setItemMeta(this.meta);
    }

    /**
     * Gets type of item stack.
     *
     * @return Type of item stack.
     */
    @Nonnull
    public Material getType() {
        return this.stack.getType();
    }

    /**
     * Sets type of item stack.
     *
     * @param type Material type.
     * @return This class.
     */
    @Nonnull
    public HItemBuilder type(@Nonnull Material type) {
        this.stack.setType(Objects.requireNonNull(type, "type cannot be null!"));
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
    public HItemBuilder name(@Nonnull String name) {
        this.meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(name, "name cannot be null!")));
        return this;
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
    public HItemBuilder lores(@Nonnull List<String> lore) {
        List<String> lores = new ArrayList<>();
        for (String line : Objects.requireNonNull(lore, "lore cannot be null!"))
            lores.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(line, "lore cannot be null!")));
        this.meta.setLore(lores);
        return this;
    }

    /**
     * Adds lore to item stack.
     *
     * @param lines Lines to add.
     * @return This class.
     */
    @Nonnull
    public HItemBuilder appendLore(@Nonnull String... lines) {
        List<String> lore = this.getLore();
        for (String line : Objects.requireNonNull(lines, "lines cannot be null!"))
            lore.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(line, "line cannot be null!")));

        this.meta.setLore(lore);
        return this;
    }

    /**
     * Gets amount of item stack.
     *
     * @return Amount of item stack.
     */
    public int getAmount() {
        return this.stack.getAmount();
    }

    /**
     * Sets amount of item stack.
     *
     * @param amount Amount of item stack.
     * @return This class.
     */
    @Nonnull
    public HItemBuilder amount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }


    /**
     * Gets durability of item stack.
     *
     * @return Durability of item stack.
     */
    public short getDurability() {
        return this.stack.getDurability();
    }

    /**
     * Sets durability of item stack.
     *
     * @param durability Durability of item stack.
     * @return This class.
     */
    @Nonnull
    public HItemBuilder durability(short durability) {
        this.stack.setDurability(durability);
        return this;
    }


    /**
     * Gets data-value of item stack.
     *
     * @return Data-value of item stack.
     */
    @Nonnull
    public MaterialData getData() {
        return this.stack.getData();
    }

    /**
     * Sets data of item stack.
     *
     * @param data Data of item stack.
     * @return This class.
     */
    @Nonnull
    public HItemBuilder data(@Nonnull MaterialData data) {
        this.stack.setData(Objects.requireNonNull(data, "material data cannot be null!"));
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
    public HItemBuilder addEnchant(@Nonnull Enchantment enchantment, int level) {
        this.meta.addEnchant(Objects.requireNonNull(enchantment, "enchantment cannot be null!"), level, true);
        return this;
    }

    /**
     * Removes enchantment from item stack.
     *
     * @param enchantment Enchantment.
     * @return This class.
     */
    @Nonnull
    public HItemBuilder removeEnchant(@Nonnull Enchantment enchantment) {
        this.meta.removeEnchant(Objects.requireNonNull(enchantment, "enchantment cannot be null!"));
        return this;
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
    public HItemBuilder addItemFlags(@Nonnull ItemFlag... flags) {
        this.meta.addItemFlags(Objects.requireNonNull(flags, "item flags cannot be null!"));
        return this;
    }

    /**
     * Removes item flags from item stack.
     *
     * @param flags Item flags.
     * @return This class.
     */
    @Nonnull
    public HItemBuilder removeItemFlags(@Nonnull ItemFlag... flags) {
        this.meta.removeItemFlags(Objects.requireNonNull(flags, "item flags cannot be null!"));
        return this;
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
    public HItemBuilder unbreakable(boolean unbreakable) {
        this.meta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Sets glow of item stack.
     *
     * @param glow Glow.
     * @return This class.
     */
    @Nonnull
    public HItemBuilder glow(boolean glow) {
        if (glow) this.meta.addEnchant(HItemStack.getGlowEnchantment(), 0, true);
        else this.meta.removeEnchant(HItemStack.getGlowEnchantment());
        return this;
    }

    /**
     * Sets nbt of item stack.
     *
     * @param nbt NBT.
     */
    @Nonnull
    public HItemBuilder nbt(@Nonnull String nbt) {
        this.stack = HItemStack.getNbtManager().set(this.stack,
                Objects.requireNonNull(nbt, "nbt cannot be null!"));
        return this;
    }

    /**
     * Sets nbt of item stack.
     *
     * @param key   Key of nbt.
     * @param value Value of nbt.
     */
    @Nonnull
    public HItemBuilder nbt(@Nonnull String key, @Nonnull String value) {
        this.stack = HItemStack.getNbtManager().set(this.stack,
                Objects.requireNonNull(key, "key cannot be null!"),
                Objects.requireNonNull(value, "value cannot be null!"));
        return this;
    }

    /**
     * Checks item stack has item meta.
     *
     * @return If item stack has item meta, returns true.
     */
    public boolean hasItemMeta() {
        return true;
    }

    /**
     * Gets item meta of item stack.
     *
     * @return Item meta of item stack.
     */
    @Nonnull
    public ItemMeta getItemMeta() {
        return this.meta;
    }

    /**
     * Sets item meta of item stack.
     *
     * @param meta Item meta.
     * @return This class.
     */
    @Nonnull
    public HItemBuilder meta(@Nonnull ItemMeta meta) {
        this.stack.setItemMeta(Objects.requireNonNull(meta, "item meta cannot be null!"));
        return this;
    }

    /**
     * Builds item stack.
     *
     * @return Item stack.
     */
    @Nonnull
    public ItemStack build() {
        this.stack.setItemMeta(meta);
        return this.stack;
    }
}