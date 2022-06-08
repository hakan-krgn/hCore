package com.hakan.core.item.nbt;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;

public class HNbtManager_v1_19_R1 implements HNbtManager {

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public ItemStack set(@Nonnull ItemStack itemStack, @Nonnull String key, @Nonnull String value) {
        Objects.requireNonNull(itemStack, "itemStack cannot be null");
        Objects.requireNonNull(key, "key cannot be null");
        Objects.requireNonNull(value, "value cannot be null");

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);

        if (nmsCopy.t() == null)
            nmsCopy.c(new NBTTagCompound());

        nmsCopy.t().a(key, value);
        return CraftItemStack.asBukkitCopy(nmsCopy);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public ItemStack set(@Nonnull ItemStack itemStack, @Nonnull String nbt) {
        Objects.requireNonNull(itemStack, "itemStack cannot be null");
        Objects.requireNonNull(nbt, "nbt cannot be null");

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);

        if (nmsCopy.t() == null)
            nmsCopy.c(new NBTTagCompound());

        nmsCopy.t().a(this.parse(nbt));
        return CraftItemStack.asBukkitCopy(nmsCopy);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String get(@Nonnull ItemStack itemStack, @Nonnull String key) {
        Objects.requireNonNull(itemStack, "itemStack cannot be null");
        Objects.requireNonNull(key, "key cannot be null");

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);

        if (nmsCopy.t() == null)
            nmsCopy.c(new NBTTagCompound());

        NBTTagCompound nbtTagCompound = nmsCopy.t();
        return nbtTagCompound.e(key) ? nbtTagCompound.l(key) : "{}";
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String get(@Nonnull ItemStack itemStack) {
        Objects.requireNonNull(itemStack, "itemStack cannot be null");

        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);

        if (nmsCopy.t() == null)
            nmsCopy.c(new NBTTagCompound());

        return nmsCopy.t().toString();
    }

    private NBTTagCompound parse(String nbt) {
        try {
            return MojangsonParser.a(nbt);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}