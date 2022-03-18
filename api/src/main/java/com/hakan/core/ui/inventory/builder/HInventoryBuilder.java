package com.hakan.core.ui.inventory.builder;

import com.hakan.core.ui.inventory.HInventory;
import org.bukkit.event.inventory.InventoryType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * HInventoryBuilder class to
 * build HInventory.
 */
public final class HInventoryBuilder {

    private int size;
    private String id;
    private String title;
    private InventoryType type;
    private Set<HInventory.Option> options;

    /**
     * Creates new instance of this class.
     *
     * @param id ID.
     */
    public HInventoryBuilder(@Nonnull String id) {
        this.id = Objects.requireNonNull(id, "id cannot be null!");
        this.options(HInventory.Option.values());
    }

    /**
     * Sets id.
     *
     * @param id ID.
     * @return This class.
     */
    @Nonnull
    public HInventoryBuilder id(@Nonnull String id) {
        this.id = Objects.requireNonNull(id, "id cannot be null!");
        return this;
    }

    /**
     * Sets title.
     *
     * @param title Title.
     * @return This class.
     */
    @Nonnull
    public HInventoryBuilder title(@Nonnull String title) {
        this.title = Objects.requireNonNull(title, "title cannot be null!");
        return this;
    }

    /**
     * Sets inventory type.
     *
     * @param type Inventory type.
     * @return This class.
     */
    @Nonnull
    public HInventoryBuilder type(@Nonnull InventoryType type) {
        this.type = Objects.requireNonNull(type, "inventory type cannot be null!");
        return this;
    }

    /**
     * Sets size.
     *
     * @param size Size.
     * @return This class.
     */
    @Nonnull
    public HInventoryBuilder size(int size) {
        this.size = size;
        return this;
    }

    /**
     * Sets options.
     *
     * @param options Options.
     * @return This class.
     */
    @Nonnull
    public HInventoryBuilder options(@Nonnull Set<HInventory.Option> options) {
        this.options = Objects.requireNonNull(options, "options cannot be null!");
        return this;
    }

    /**
     * Sets options.
     *
     * @param options Options.
     * @return This class.
     */
    @Nonnull
    public HInventoryBuilder options(@Nonnull HInventory.Option... options) {
        this.options = new HashSet<>(Arrays.asList(Objects.requireNonNull(options, "options cannot be null!")));
        return this;
    }

    /**
     * Adds option.
     *
     * @param option Option.
     * @return This class.
     */
    @Nonnull
    public HInventoryBuilder addOption(@Nonnull HInventory.Option option) {
        this.options.add(Objects.requireNonNull(option, "option cannot be null!"));
        return this;
    }

    /**
     * Removes option.
     *
     * @param option Option.
     * @return This class.
     */
    @Nonnull
    public HInventoryBuilder removeOption(@Nonnull HInventory.Option option) {
        this.options.remove(Objects.requireNonNull(option, "option cannot be null!"));
        return this;
    }

    /**
     * Builds HInventory class.
     *
     * @return HInventory.
     */
    @Nonnull
    public HInventory build() {
        return new HInventory(this.id, this.title, this.size, this.type, this.options);
    }
}