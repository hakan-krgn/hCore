package com.hakan.core.ui.inventory.item;

import com.hakan.core.utils.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * ClickableItem class.
 */
public final class ClickableItem {

    private final ItemStack item;
    private final Consumer<InventoryClickEvent> click;

    /**
     * Creates new instance of this class.
     *
     * @param item  ItemStack.
     * @param click Click consumer.
     */
    public ClickableItem(@Nonnull ItemStack item, @Nullable Consumer<InventoryClickEvent> click) {
        this.item = Validate.notNull(item, "item cannot be null!");
        this.click = click;
    }

    /**
     * Gets item stack.
     *
     * @return Item stack.
     */
    @Nonnull
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Gets click consumer.
     *
     * @return Click consumer.
     */
    @Nullable
    public Consumer<InventoryClickEvent> getClick() {
        return this.click;
    }
}
