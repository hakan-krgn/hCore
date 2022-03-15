package com.hakan.core.ui.inventory.pagination;

import com.hakan.core.ui.inventory.item.ClickableItem;

import java.util.HashMap;
import java.util.Map;

public class Page {

    private final int number;
    private final Map<Integer, ClickableItem> items;

    public Page(int number) {
        this.number = number;
        this.items = new HashMap<>();
    }

    public int getNumber() {
        return this.number;
    }

    public Map<Integer, ClickableItem> getItems() {
        return this.items;
    }
}