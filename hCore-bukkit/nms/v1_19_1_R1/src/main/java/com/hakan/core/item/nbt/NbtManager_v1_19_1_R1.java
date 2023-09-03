package com.hakan.core.item.nbt;

import com.hakan.core.utils.Validate;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class NbtManager_v1_19_1_R1 implements NbtManager {

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

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        nmsCopy.v().a(key, value);
        return CraftItemStack.asBukkitCopy(nmsCopy);
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

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        nmsCopy.v().a(this.parse(nbt));
        return CraftItemStack.asBukkitCopy(nmsCopy);
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

        NBTTagCompound nbtTagCompound = nmsCopy.v();
        return nbtTagCompound.e(key) ? nbtTagCompound.l(key) : "{}";
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

        return nmsCopy.v().toString();
    }

    private NBTTagCompound parse(String nbt) {
        try {
            return MojangsonParser.a(nbt);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
