package com.hakan.core.ui.inventory.builder;

import com.hakan.core.ui.inventory.HInventory;
import org.bukkit.event.inventory.InventoryType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class HInventoryBuilder {

    private int size;
    private String id;
    private String title;
    private InventoryType type;
    private Set<HInventory.Option> options;

    public HInventoryBuilder(String id) {
        this.id = id;
        this.options(HInventory.Option.values());
    }

    @Nonnull
    public HInventoryBuilder id(@Nonnull String id) {
        this.id = id;
        return this;
    }

    @Nonnull
    public HInventoryBuilder title(@Nonnull String title) {
        this.title = title;
        return this;
    }

    @Nonnull
    public HInventoryBuilder type(@Nonnull InventoryType type) {
        this.type = type;
        return this;
    }

    @Nonnull
    public HInventoryBuilder size(int size) {
        this.size = size;
        return this;
    }

    @Nonnull
    public HInventoryBuilder options(Set<HInventory.Option> options) {
        this.options = options;
        return this;
    }

    @Nonnull
    public HInventoryBuilder options(HInventory.Option... options) {
        this.options = new HashSet<>(Arrays.asList(options));
        return this;
    }

    @Nonnull
    public HInventoryBuilder addOption(HInventory.Option option) {
        this.options.add(option);
        return this;
    }

    @Nonnull
    public HInventoryBuilder removeOption(HInventory.Option option) {
        this.options.remove(option);
        return this;
    }

    @Nonnull
    public HInventory build() {
        return new HInventory(this.id, this.title, this.size, this.type, this.options);
    }
}