package com.hakan.core.item.nbt;

import com.hakan.core.utils.Validate;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class NbtManager_v1_17_R1 implements NbtManager {

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public ItemStack set(@Nonnull ItemStack itemStack, @Nonnull String key, @Nonnull String value) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        Validate.notNull(key, "key cannot be null");
        Validate.notNull(value, "value cannot be null");

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);

        if (itemStack.getType().equals(Material.AIR))
            return itemStack;
        else if (!nmsCopy.hasTag())
            nmsCopy.setTag(new NBTTagCompound());

        nmsCopy.getTag().setString(key, value);
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

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);

        if (itemStack.getType().equals(Material.AIR))
            return itemStack;
        else if (!nmsCopy.hasTag())
            nmsCopy.setTag(new NBTTagCompound());

        nmsCopy.getTag().a(this.parse(nbt));
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

        if (!nmsCopy.hasTag())
            nmsCopy.setTag(new NBTTagCompound());

        NBTTagCompound nbtTagCompound = nmsCopy.getTag();
        return nbtTagCompound.hasKey(key) ? nbtTagCompound.getString(key) : "{}";
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String get(@Nonnull ItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);

        if (!nmsCopy.hasTag())
            nmsCopy.setTag(new NBTTagCompound());

        return nmsCopy.getTag().toString();
    }

    private NBTTagCompound parse(String nbt) {
        try {
            return MojangsonParser.parse(nbt);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}