package com.hakan.core.ui.inventory.pagination;

import com.hakan.core.ui.inventory.item.ClickableItem;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Page class.
 */
public final class Page {

    private final int number;
    private final Map<Integer, ClickableItem> items;

    /**
     * Creates new instance of this class.
     *
     * @param number Number of page.
     */
    public Page(int number) {
        this.number = number;
        this.items = new HashMap<>();
    }

    /**
     * Gets number of page.
     *
     * @return Number of page.
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Gets item list in page.
     *
     * @return Item list in page.
     */
    @Nonnull
    public Map<Integer, ClickableItem> getItems() {
        return this.items;
    }
}
