package com.hakan.core.ui.inventory.builder;

import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * InventoryBuilder class to
 * build InventoryGui.
 */
public final class InventoryBuilder {

    private final String id;
    private int size;
    private String title;
    private InventoryType type;
    private Set<InventoryGui.Option> options;
    private Consumer<Player> openConsumer;
    private Consumer<Player> closeConsumer;

    /**
     * Creates new instance of this class.
     *
     * @param id ID.
     */
    public InventoryBuilder(@Nonnull String id) {
        this.id = Validate.notNull(id, "id cannot be null!");
        this.size = 6;
        this.title = "Inventory";
        this.type = InventoryType.CHEST;
        this.options = EnumSet.allOf(InventoryGui.Option.class);
    }

    /**
     * Sets title.
     *
     * @param title Title.
     * @return This class.
     */
    @Nonnull
    public InventoryBuilder title(@Nonnull String title) {
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
    public InventoryBuilder type(@Nonnull InventoryType type) {
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
    public InventoryBuilder size(int size) {
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
    public InventoryBuilder options(@Nonnull Set<InventoryGui.Option> options) {
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
    public InventoryBuilder options(@Nonnull InventoryGui.Option... options) {
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
    public InventoryBuilder addOption(@Nonnull InventoryGui.Option option) {
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
    public InventoryBuilder removeOption(@Nonnull InventoryGui.Option option) {
        this.options.remove(Validate.notNull(option, "option cannot be null!"));
        return this;
    }

    /**
     * Called when player opens the inventory.
     *
     * @param consumer Consumer.
     */
    @Nonnull
    public InventoryBuilder whenOpened(@Nonnull Consumer<Player> consumer) {
        this.openConsumer = Validate.notNull(consumer, "consumer cannot be null!");
        return this;
    }

    /**
     * Called when player closes the inventory.
     *
     * @param consumer Consumer.
     */
    @Nonnull
    public InventoryBuilder whenClosed(@Nonnull Consumer<Player> consumer) {
        this.closeConsumer = Validate.notNull(consumer, "consumer cannot be null!");
        return this;
    }

    /**
     * Builds InventoryGui.
     *
     * @return InventoryGui.
     */
    @Nonnull
    public InventoryGui build() {
        InventoryGui inventoryGui = new InventoryGui(this.id, this.title, this.size, this.type, this.options);

        if (this.openConsumer != null)
            inventoryGui.whenOpened(this.openConsumer);
        if (this.closeConsumer != null)
            inventoryGui.whenClosed(this.closeConsumer);

        return inventoryGui;
    }
}
