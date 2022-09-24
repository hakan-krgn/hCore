package com.hakan.core.ui.inventory.builder;

import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.utils.Validate;
import org.bukkit.event.inventory.InventoryType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * InventoryGuiBuilder class to
 * build InventoryGui.
 */
public final class InventoryGuiBuilder {

    private int size;
    private String id;
    private String title;
    private InventoryType type;
    private Set<InventoryGui.Option> options;

    /**
     * Creates new instance of this class.
     *
     * @param id ID.
     */
    public InventoryGuiBuilder(@Nonnull String id) {
        this.id = Validate.notNull(id, "id cannot be null!");
        this.options(InventoryGui.Option.values());
    }

    /**
     * Sets id.
     *
     * @param id ID.
     * @return This class.
     */
    @Nonnull
    public InventoryGuiBuilder id(@Nonnull String id) {
        this.id = Validate.notNull(id, "id cannot be null!");
        return this;
    }

    /**
     * Sets title.
     *
     * @param title Title.
     * @return This class.
     */
    @Nonnull
    public InventoryGuiBuilder title(@Nonnull String title) {
        this.title = Validate.notNull(title, "title cannot be null!");
        return this;
    }

    /**
     * Sets inventory type.
     *
     * @param type Inventory type.
     * @return This class.
     */
    @Nonnull
    public InventoryGuiBuilder type(@Nonnull InventoryType type) {
        this.type = Validate.notNull(type, "inventory type cannot be null!");
        return this;
    }

    /**
     * Sets size.
     *
     * @param size Size.
     * @return This class.
     */
    @Nonnull
    public InventoryGuiBuilder size(int size) {
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
    public InventoryGuiBuilder options(@Nonnull Set<InventoryGui.Option> options) {
        this.options = Validate.notNull(options, "options cannot be null!");
        return this;
    }

    /**
     * Sets options.
     *
     * @param options Options.
     * @return This class.
     */
    @Nonnull
    public InventoryGuiBuilder options(@Nonnull InventoryGui.Option... options) {
        this.options = new HashSet<>(Arrays.asList(Validate.notNull(options, "options cannot be null!")));
        return this;
    }

    /**
     * Adds option.
     *
     * @param option Option.
     * @return This class.
     */
    @Nonnull
    public InventoryGuiBuilder addOption(@Nonnull InventoryGui.Option option) {
        this.options.add(Validate.notNull(option, "option cannot be null!"));
        return this;
    }

    /**
     * Removes option.
     *
     * @param option Option.
     * @return This class.
     */
    @Nonnull
    public InventoryGuiBuilder removeOption(@Nonnull InventoryGui.Option option) {
        this.options.remove(Validate.notNull(option, "option cannot be null!"));
        return this;
    }

    /**
     * Builds InventoryGui class.
     *
     * @return InventoryGui.
     */
    @Nonnull
    public InventoryGui build() {
        return new InventoryGui(this.id, this.title, this.size, this.type, this.options);
    }
}