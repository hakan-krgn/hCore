package com.hakan.core.ui.inventory;

import com.hakan.core.ui.GUI;
import com.hakan.core.ui.GUIHandler;
import com.hakan.core.ui.inventory.item.ClickableItem;
import com.hakan.core.ui.inventory.pagination.Page;
import com.hakan.core.ui.inventory.pagination.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * HInventory class to create
 * inventories easily.
 */
@SuppressWarnings({"unchecked"})
public class HInventory implements GUI {

    private final String id;
    private final String title;
    private final Inventory inventory;
    protected final Pagination pagination;
    private final Set<Option> options;
    private final Map<Integer, ClickableItem> items;
    private Consumer<Player> openConsumer;
    private Consumer<Player> closeConsumer;

    /**
     * Creates new instance of this class.
     *
     * @param id      ID of inventory.
     * @param title   Title.
     * @param size    Size.
     * @param type    Inventory type.
     * @param options Options.
     */
    public HInventory(@Nonnull String id, @Nonnull String title, int size, @Nonnull InventoryType type, @Nonnull Set<Option> options) {
        this.id = id;
        this.title = title;
        this.options = options;
        this.items = new HashMap<>();
        this.pagination = new Pagination(this);
        this.inventory = (type == InventoryType.CHEST) ?
                Bukkit.createInventory(null, size * 9, title) :
                Bukkit.createInventory(null, type, title);

        this.fillAir(false);
    }

    /**
     * Creates new instance of this class
     * with all options.
     *
     * @param id    ID of inventory.
     * @param title Title.
     * @param size  Size.
     * @param type  Inventory type.
     */
    public HInventory(@Nonnull String id, @Nonnull String title, int size, @Nonnull InventoryType type) {
        this(id, title, size, type, EnumSet.allOf(Option.class));
    }

    /**
     * Gets bukkit inventory.
     *
     * @return bukkit inventory.
     */
    @Nonnull
    public final Inventory toInventory() {
        return this.inventory;
    }

    /**
     * Gets id.
     *
     * @return id.
     */
    @Nonnull
    public final String getId() {
        return this.id;
    }

    /**
     * Gets title of inventory.
     *
     * @return Title of inventory.
     */
    @Nonnull
    public final String getTitle() {
        return this.title;
    }

    /**
     * Gets type of inventory.
     *
     * @return enum from InventoryType.
     */
    @Nonnull
    public final InventoryType getInventoryType() {
        return this.inventory.getType();
    }

    /**
     * Gets size of inventory.
     *
     * @return Size of inventory.
     */
    public final int getSize() {
        return this.inventory.getSize() / 9;
    }

    /**
     * Checks UI contains option.
     *
     * @param option enum HInventory.Option.
     * @return If UI contains option, returns true.
     */
    public final boolean hasOption(@Nonnull Option option) {
        return this.options.contains(Objects.requireNonNull(option, "option cannot be null!"));
    }

    /**
     * Adds option to UI.
     *
     * @param <T>    HInventory type.
     * @param option enum HInventory.Option.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T addOption(@Nonnull Option option) {
        this.options.add(Objects.requireNonNull(option, "option cannot be null!"));
        return (T) this;
    }

    /**
     * Removes option from UI.
     *
     * @param <T>    HInventory type.
     * @param option enum HInventory.Option.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T removeOption(@Nonnull Option option) {
        this.options.remove(Objects.requireNonNull(option, "option cannot be null!"));
        return (T) this;
    }

    /**
     * If you want to use pagination, you
     * need pagination instance.
     *
     * @return Pagination instance.
     */
    @Nonnull
    public final Pagination getPagination() {
        return this.pagination;
    }

    /**
     * Finds clickable item from slot that used parameter.
     *
     * @param slot Slot.
     * @return Clickable item.
     */
    @Nonnull
    public final Optional<ClickableItem> findItem(int slot) {
        return Optional.ofNullable(this.items.getOrDefault(slot, null));
    }

    /**
     * Gets clickable item from slot that used parameter.
     *
     * @param slot Slot.
     * @return Clickable item.
     */
    @Nonnull
    public final ClickableItem getItem(int slot) {
        return this.findItem(slot).orElseThrow(() -> new NullPointerException("item(" + slot + ") cannot be null!"));
    }

    /**
     * Checks there is a clickable item at slot.
     *
     * @param slot Slot.
     * @return return if there is item in slot.
     */
    public final boolean hasItem(int slot) {
        return this.items.containsKey(slot);
    }

    /**
     * Sets clickable item to slot.
     *
     * @param <T>           HInventory type.
     * @param slot          Slot.
     * @param clickableItem Clickable item.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T setItem(int slot, @Nullable ClickableItem clickableItem) {
        this.items.put(slot, clickableItem);
        this.inventory.setItem(slot, (clickableItem != null) ? clickableItem.getItem() : new ItemStack(Material.AIR));
        return (T) this;
    }

    /**
     * Sets clickable item to slot.
     *
     * @param <T>       HInventory type.
     * @param slot      Slot.
     * @param itemStack Itemstack.
     * @param consumer  What will happen when player click on item.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T setItem(int slot, @Nonnull ItemStack itemStack, @Nullable Consumer<InventoryClickEvent> consumer) {
        return this.setItem(slot, new ClickableItem(itemStack, consumer));
    }

    /**
     * Sets item to slot.
     *
     * @param <T>       HInventory type.
     * @param slot      Slot.
     * @param itemStack Itemstack.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T setItem(int slot, @Nonnull ItemStack itemStack) {
        return this.setItem(slot, itemStack, null);
    }

    /**
     * Sets item to slot.
     *
     * @param <T>  HInventory type.
     * @param page Page.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fillPage(@Nonnull Page page) {
        this.pagination.setCurrentPage(Objects.requireNonNull(page, "page cannot be null!").getNumber());
        return (T) this;
    }

    /**
     * Sets item to slot.
     *
     * @param <T>     HInventory type.
     * @param pageNum Page number.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fillPage(int pageNum) {
        this.pagination.setCurrentPage(pageNum);
        return (T) this;
    }

    /**
     * Removes item from slot.
     *
     * @param <T>  HInventory type.
     * @param slot Slot.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T removeItem(int slot) {
        this.items.remove(slot);
        this.inventory.setItem(slot, null);
        return (T) this;
    }

    /**
     * Sets the slots with clickable item.
     *
     * @param <T>              HInventory type.
     * @param clickableItem    ClickableItem object.
     * @param replaceWithItems If this param is true, it replaces all items with clickable item.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fill(@Nullable ClickableItem clickableItem, boolean replaceWithItems) {
        IntStream.range(0, this.getSize() * 9).filter(slot -> !this.hasItem(slot) || replaceWithItems).forEach(slot -> {
            if (clickableItem != null) this.setItem(slot, clickableItem);
            else this.removeItem(slot);
        });
        return (T) this;
    }

    /**
     * Sets all the slots with clickable item.
     *
     * @param <T>           HInventory type.
     * @param clickableItem clickable item.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fill(@Nullable ClickableItem clickableItem) {
        return this.fill(clickableItem, true);
    }

    /**
     * Sets the slots with itemstack.
     *
     * @param <T>              HInventory type.
     * @param itemStack        Itemstack that you want.
     * @param replaceWithItems If this param is true, it replaces all items with itemstack.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fill(@Nonnull ItemStack itemStack, boolean replaceWithItems) {
        return this.fill(new ClickableItem(itemStack, null), replaceWithItems);
    }

    /**
     * Sets all the slots with itemstack.
     *
     * @param <T>       HInventory type.
     * @param itemStack Itemstack that you want.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fill(@Nonnull ItemStack itemStack) {
        return this.fill(itemStack, true);
    }

    /**
     * Sets the slots with material.
     *
     * @param <T>              HInventory type.
     * @param material         Item type that you want.
     * @param replaceWithItems If this param is true, it replaces all items with material.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fillMaterial(@Nonnull Material material, boolean replaceWithItems) {
        return this.fill(new ItemStack(Objects.requireNonNull(material, "material cannot be null!")), replaceWithItems);
    }

    /**
     * Sets all the slots with material.
     *
     * @param <T>      HInventory type.
     * @param material Item type that you want.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fillMaterial(@Nonnull Material material) {
        return this.fillMaterial(material, true);
    }

    /**
     * Sets the slots with air.
     *
     * @param <T>              HInventory type.
     * @param replaceWithItems If this param is true, it replaces all items with air.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fillAir(boolean replaceWithItems) {
        return this.fill(new ItemStack(Material.AIR), replaceWithItems);
    }

    /**
     * Sets all the slots with air.
     *
     * @param <T> HInventory type.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fillAir() {
        return this.fillAir(true);
    }

    /**
     * Clears inventory.
     *
     * @param <T>              HInventory type.
     * @param replaceWithItems If this param is true, it replaces all items with null.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fillNull(boolean replaceWithItems) {
        return this.fill((ClickableItem) null, replaceWithItems);
    }

    /**
     * Clears inventory.
     *
     * @param <T> HInventory type.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T fillNull() {
        return this.fillNull(true);
    }

    /**
     * Reopens the inventory for player.
     *
     * @param player Player.
     * @param <T>    HInventory type.
     * @return instance of this class.
     */
    @Nonnull
    public final <T extends HInventory> T refresh(@Nonnull Player player) {
        this.onOpen(Objects.requireNonNull(player, "player cannot be null!"));
        return (T) this;
    }

    /**
     * Open inventory to player also put player
     * to hInventoryMap and triggers onOpen method.
     *
     * @param player Player.
     */
    public final void open(@Nonnull Player player) {
        HInventory hInventory = GUIHandler.findInventoryByPlayer(Objects.requireNonNull(player, "player cannot be null!")).orElse(null);

        this.onOpen(player);

        if (hInventory == null || !hInventory.equals(this)) {
            player.openInventory(this.inventory);
            GUIHandler.getContent().put(player.getUniqueId(), this);
        }
    }

    /**
     * Close inventory of player also if UI is not locked
     * removes from hInventoryMap and triggers onClose method.
     *
     * @param player Player.
     */
    public final void close(@Nonnull Player player) {
        Objects.requireNonNull(player, "player cannot be null!");

        if (this.hasOption(Option.CLOSABLE)) {
            player.closeInventory();
        } else {
            this.addOption(Option.CLOSABLE);
            player.closeInventory();
            this.removeOption(Option.CLOSABLE);
        }
    }

    /**
     * Called when player opens the inventory.
     *
     * @param consumer Consumer.
     */
    public void whenOpened(@Nonnull Consumer<Player> consumer) {
        this.openConsumer = Objects.requireNonNull(consumer, "consumer cannot be null!");
    }

    /**
     * Called when player closes the inventory.
     *
     * @param consumer Consumer.
     */
    public void whenClosed(@Nonnull Consumer<Player> consumer) {
        this.closeConsumer = Objects.requireNonNull(consumer, "consumer cannot be null!");
    }

    /**
     * This method run when inventory
     * opened to any player.
     *
     * @param player Player.
     */
    protected void onOpen(@Nonnull Player player) {
        if (this.openConsumer != null)
            this.openConsumer.accept(player);
    }

    /**
     * This method run when inventory
     * closed from any player.
     *
     * @param player Player.
     */
    protected void onClose(@Nonnull Player player) {
        if (this.closeConsumer != null)
            this.closeConsumer.accept(player);
    }


    /**
     * Option enums for UI.
     */
    public enum Option {
        CLOSABLE,
        CANCEL_TOP_CLICK,
        CANCEL_DOWN_CLICK
    }
}