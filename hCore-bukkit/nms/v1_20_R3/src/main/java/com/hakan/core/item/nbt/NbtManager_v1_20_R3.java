package com.hakan.core.item.nbt;

import com.hakan.core.utils.Validate;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class NbtManager_v1_20_R3 implements NbtManager {

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public ItemStack set(@Nonnull ItemStack itemStack, @Nonnull String key, @Nonnull String value) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        Validate.notNull(key, "key cannot be null");
        Validate.notNull(value, "value cannot be null");

        if (itemStack.getType().equals(Material.AIR))
            return itemStack;

        try {
            net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
            nmsCopy.w().a(key, value);
            return CraftItemStack.asBukkitCopy(nmsCopy);
        } catch (Exception e) {
            return itemStack;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public ItemStack set(@Nonnull ItemStack itemStack, @Nonnull String nbt) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        Validate.notNull(nbt, "nbt cannot be null");

        if (itemStack.getType().equals(Material.AIR))
            return itemStack;

        try {
            net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
            nmsCopy.w().a(this.parse(nbt));
            return CraftItemStack.asBukkitCopy(nmsCopy);
        } catch (Exception e) {
            return itemStack;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String get(@Nonnull ItemStack itemStack, @Nonnull String key) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        Validate.notNull(key, "key cannot be null");

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        if (nmsCopy == null)
            return "{}";

        try {
            NBTTagCompound nbtTagCompound = nmsCopy.w();
            return nbtTagCompound.e(key) ? nbtTagCompound.l(key) : "{}";
        } catch (Exception e) {
            return "{}";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String get(@Nonnull ItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        if (nmsCopy == null)
            return "{}";

        try {
            return nmsCopy.w().toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    private NBTTagCompound parse(String nbt) {
        try {
            return MojangsonParser.a(nbt);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
