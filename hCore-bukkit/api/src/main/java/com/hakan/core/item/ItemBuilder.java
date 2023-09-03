package com.hakan.core.item;

import com.hakan.core.HCore;
import com.hakan.core.item.nbt.NbtManager;
import com.hakan.core.item.skull.SkullBuilder;
import com.hakan.core.protocol.ProtocolVersion;
import com.hakan.core.utils.ColorUtil;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ItemBuilder class to create and
 * manage item stacks easily.
 */
@SuppressWarnings({"unchecked"})
public class ItemBuilder {

    private static Enchantment glowEnchantment;
    private static NbtManager nbtManager;

    /**
     * initialize method of HItemStack class.
     */
    public static void initialize() {
        SkullBuilder.initialize();

        nbtManager = ReflectionUtils.newInstance("com.hakan.core.item.nbt.NbtManager_%s");
        glowEnchantment = ReflectionUtils.newInstance("com.hakan.core.item.enchantment.EnchantmentGlow_%s",
                new Class[]{int.class}, new Object[]{152634});

        if (Arrays.asList(Enchantment.values()).contains(glowEnchantment))
            return;

        ReflectionUtils.setField(Enchantment.class, "acceptingNew", true);
        Enchantment.registerEnchantment(glowEnchantment);
    }

    /**
     * Gets Glow enchantment.
     *
     * @return Glow enchantment.
     */
    @Nonnull
    public static Enchantment getGlowEnchantment() {
        return glowEnchantment;
    }

    /**
     * Gets NbtManager object.
     *
     * @return NbtManager object.
     */
    @Nonnull
    public static NbtManager getNbtManager() {
        return nbtManager;
    }


    private Material type;
    private String nbt;
    private String name;
    private int amount;
    private short durability;
    private boolean glow;
    private boolean unbreakable;
    private List<String> lore;
    private Set<ItemFlag> flags;
    private Map<Enchantment, Integer> enchantments;

    /**
     * Creates new instance of this class.
     *
     * @param type Material type.
     */
    public ItemBuilder(@Nonnull Material type) {
        this(type, 1);
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Material type.
     * @param amount Amount.
     */
    public ItemBuilder(@Nonnull Material type, int amount) {
        this(type, amount, (short) 0);
    }

    /**
     * Creates new instance of this class.
     *
     * @param type       Material type.
     * @param amount     Amount.
     * @param durability Datavalue.
     */
    public ItemBuilder(@Nonnull Material type, int amount, short durability) {
        this.type = Validate.notNull(type, "type cannot be null!");
        this.nbt = "{}";
        this.name = "";
        this.amount = amount;
        this.durability = durability;
        this.glow = false;
        this.unbreakable = false;
        this.lore = new ArrayList<>();
        this.flags = new HashSet<>();
        this.enchantments = new HashMap<>();
    }

    /**
     * Creates new instance of this class from item builder.
     *
     * @param builder Item builder.
     */
    public ItemBuilder(@Nonnull ItemBuilder builder) {
        Validate.notNull(builder, "builder cannot be null!");
        this.type = builder.type;
        this.nbt = builder.nbt;
        this.name = builder.name;
        this.amount = builder.amount;
        this.durability = builder.durability;
        this.glow = builder.glow;
        this.unbreakable = builder.unbreakable;
        this.lore = new ArrayList<>(builder.lore);
        this.flags = new HashSet<>(builder.flags);
        this.enchantments = new HashMap<>(builder.enchantments);
    }

    /**
     * Creates new instance of this class from item stack.
     *
     * @param stack Item stack.
     */
    public ItemBuilder(@Nonnull ItemStack stack) {
        this(stack.getType(), stack.getAmount(), stack.getDurability());
        this.nbt = nbtManager.get(stack);

        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            this.name = meta.getDisplayName();
            this.flags = meta.getItemFlags();
            this.lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            this.enchantments = meta.hasEnchants() ? meta.getEnchants() : new HashMap<>();
            this.glow = meta.hasEnchants() && meta.getEnchants().containsKey(glowEnchantment);
            this.unbreakable = false;
        }
    }

    /**
     * Gets type of item stack.
     *
     * @return Type of item stack.
     */
    @Nonnull
    public Material getType() {
        return this.type;
    }

    /**
     * Sets type of item stack.
     *
     * @param type Material type.
     * @param <T>  This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T type(@Nonnull Material type) {
        this.type = Validate.notNull(type, "type cannot be null!");
        return (T) this;
    }


    /**
     * Gets name of item stack.
     *
     * @return Name of item stack.
     */
    @Nonnull
    public String getName() {
        return this.name;
    }

    /**
     * Sets name of item stack.
     *
     * @param name Name of item stack.
     * @param <T>  This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T name(@Nonnull String name) {
        return this.name(true, name);
    }

    /**
     * Sets name of item stack.
     *
     * @param colored Name is colored.
     * @param name    Name of item stack.
     * @param <T>     This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T name(boolean colored, @Nonnull String name) {
        this.name = Validate.notNull(name, "name cannot be null!");
        if (colored) this.name = ColorUtil.colored(this.name);
        return (T) this;
    }


    /**
     * Gets lore of item stack.
     *
     * @return Lore of item stack.
     */
    @Nonnull
    public List<String> getLore() {
        return this.lore;
    }

    /**
     * Sets lore of item stack.
     *
     * @param lore Lore of item stack.
     * @param <T>  This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T lores(@Nonnull List<String> lore) {
        return this.lores(true, lore);
    }

    /**
     * Adds lore to item stack.
     *
     * @param lines Lines to add.
     * @param <T>   This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T appendLore(@Nonnull String... lines) {
        return this.appendLore(true, lines);
    }

    /**
     * Adds lore to item stack.
     *
     * @param lines Lines to add.
     * @param <T>   This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T appendLore(@Nonnull List<String> lines) {
        return this.appendLore(true, lines);
    }

    /**
     * Sets lore of item stack.
     *
     * @param colored Lore is colored.
     * @param lore    Lore of item stack.
     * @param <T>     This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T lores(boolean colored, @Nonnull List<String> lore) {
        this.lore.clear();
        return this.appendLore(colored, lore);
    }

    /**
     * Adds lore to item stack.
     *
     * @param colored Lore is colored.
     * @param lines   Lines to add.
     * @param <T>     This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T appendLore(boolean colored, @Nonnull String... lines) {
        return this.appendLore(colored, Arrays.asList(lines));
    }

    /**
     * Adds lore to item stack.
     *
     * @param colored Lore is colored.
     * @param lines   Lines to add.
     * @param <T>     This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T appendLore(boolean colored, @Nonnull List<String> lines) {
        for (String _line : Validate.notNull(lines, "lines cannot be null!")) {
            String line = Validate.notNull(_line, "lore cannot be null!");
            if (colored) line = ColorUtil.colored(line);
            this.lore.add(line);
        }

        return (T) this;
    }

    /**
     * Gets amount of item stack.
     *
     * @return Amount of item stack.
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Sets amount of item stack.
     *
     * @param amount Amount of item stack.
     * @param <T>    This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T amount(int amount) {
        this.amount = amount;
        return (T) this;
    }


    /**
     * Gets durability of item stack.
     *
     * @return Durability of item stack.
     */
    public short getDurability() {
        return this.durability;
    }

    /**
     * Sets durability of item stack.
     *
     * @param durability Durability of item stack.
     * @param <T>        This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T durability(short durability) {
        this.durability = durability;
        return (T) this;
    }

    /**
     * Checks item stack has any enchantment.
     *
     * @return If item stack has enchantment, returns true.
     */
    public boolean hasEnchants() {
        return this.enchantments.size() > 0;
    }

    /**
     * Checks item stack has enchantment.
     *
     * @param enchantment Enchantment.
     * @return This class.
     */
    public boolean hasEnchant(@Nonnull Enchantment enchantment) {
        return this.enchantments.containsKey(Validate.notNull(enchantment, "enchantment cannot be null!"));
    }

    /**
     * Gets enchantment list.
     *
     * @return Enchantments.
     */
    @Nonnull
    public Map<Enchantment, Integer> getEnchants() {
        return this.enchantments;
    }

    /**
     * Gets level from enchantment.
     *
     * @param enchantment Enchantment.
     * @return Level of enchantment.
     */
    public int getEnchantLevel(@Nonnull Enchantment enchantment) {
        return this.enchantments.get(Validate.notNull(enchantment, "enchantment cannot be null!"));
    }

    /**
     * Adds enchantment to item stack.
     *
     * @param enchantment Enchantment.
     * @param level       Level.
     * @param <T>         This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T addEnchant(@Nonnull Enchantment enchantment, int level) {
        this.enchantments.put(Validate.notNull(enchantment, "enchantment cannot be null!"), level);
        return (T) this;
    }

    /**
     * Removes enchantment from item stack.
     *
     * @param enchantment Enchantment.
     * @param <T>         This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T removeEnchant(@Nonnull Enchantment enchantment) {
        this.enchantments.remove(Validate.notNull(enchantment, "enchantment cannot be null!"));
        return (T) this;
    }


    /**
     * Checks item stack has flag.
     *
     * @param flag Item flag.
     * @return If item stack has flag, returns true.
     */
    public boolean hasItemFlag(@Nonnull ItemFlag flag) {
        return this.flags.contains(Validate.notNull(flag, "item flag cannot be null!"));
    }

    /**
     * Gets item flag list.
     *
     * @return Item flag list.
     */
    @Nonnull
    public Set<ItemFlag> getItemFlags() {
        return this.flags;
    }

    /**
     * Adds item flags to item stack.
     *
     * @param flags Item flags.
     * @param <T>   This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T addItemFlags(@Nonnull ItemFlag... flags) {
        for (ItemFlag flag : Validate.notNull(flags, "item flags cannot be null!"))
            this.flags.add(Validate.notNull(flag, "item flag cannot be null!"));
        return (T) this;
    }

    /**
     * Removes item flags from item stack.
     *
     * @param flags Item flags.
     * @param <T>   This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T removeItemFlags(@Nonnull ItemFlag... flags) {
        for (ItemFlag flag : Validate.notNull(flags, "item flags cannot be null!"))
            this.flags.remove(Validate.notNull(flag, "item flag cannot be null!"));
        return (T) this;
    }

    /**
     * Gets glow of item stack.
     *
     * @return Returns true, if item is glowing.
     */
    public boolean isGlow() {
        return this.glow;
    }

    /**
     * Sets glow of item stack.
     *
     * @param glow Glow.
     * @param <T>  This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T glow(boolean glow) {
        this.glow = glow;
        return (T) this;
    }

    /**
     * Gets nbt tag of item stack.
     *
     * @return Nbt tag.
     */
    public String getNbt() {
        return this.nbt;
    }

    /**
     * Sets nbt of item stack.
     *
     * @param nbt NBT.
     * @param <T> This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T nbt(@Nonnull String nbt) {
        this.nbt = Validate.notNull(nbt, "nbt cannot be null!");
        return (T) this;
    }

    /**
     * Sets unbreakability of item stack.
     *
     * @param unbreakable Unbreakability.
     * @param <T>         This class type.
     * @return This class.
     */
    @Nonnull
    public <T extends ItemBuilder> T unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return (T) this;
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
     * Builds item stack.
     *
     * @return Item stack.
     */
    @Nonnull
    public ItemStack build() {
        ItemStack stack = new ItemStack(this.type, this.amount, this.durability);
        stack = nbtManager.set(stack, this.nbt);

        ItemMeta meta = stack.getItemMeta();

        if (meta != null) {
            if (this.unbreakable) {
                if (HCore.getProtocolVersion().isOlder(ProtocolVersion.v1_11_R1)) {
                    Object spigot = ReflectionUtils.invoke(meta, "spigot");
                    ReflectionUtils.invoke(Validate.notNull(spigot), "setUnbreakable", true);
                } else {
                    meta.setUnbreakable(true);
                }
            }

            meta.setDisplayName(this.name);
            meta.setLore(this.lore);
            this.flags.forEach(meta::addItemFlags);
            this.enchantments.forEach((key, value) -> meta.addEnchant(key, value, true));
            if (this.glow) meta.addEnchant(glowEnchantment, 0, true);
            stack.setItemMeta(meta);
        }

        return stack;
    }
}
