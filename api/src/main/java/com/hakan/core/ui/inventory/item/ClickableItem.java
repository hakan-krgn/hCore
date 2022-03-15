package com.hakan.core.ui.inventory.item;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public final class ClickableItem {

    private final ItemStack item;
    private final Consumer<InventoryClickEvent> click;

    public ClickableItem(@Nonnull ItemStack item, @Nullable Consumer<InventoryClickEvent> click) {
        this.item = Objects.requireNonNull(item, "item cannot be null!");
        this.click = click;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public Consumer<InventoryClickEvent> getClick() {
        return this.click;
    }
}