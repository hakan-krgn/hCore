package com.hakan.core.ui.inventory;

import com.hakan.core.ui.inventory.item.ClickableItem;
import com.hakan.core.ui.inventory.pagination.Page;
import com.hakan.core.ui.inventory.pagination.Pagination;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
public class HInventory {

    private final String id;
    private final String title;
    private final Inventory inventory;
    protected final Pagination pagination;

    private final Set<Option> options;
    private final Map<Integer, ClickableItem> items;

    public HInventory(String id, String title, int size, InventoryType type, Set<Option> options) {
        this.id = id;
        this.title = title;
        this.options = options;
        this.items = new HashMap<>();
        this.pagination = new Pagination(this);
        this.inventory = (type == InventoryType.CHEST) ?
                Bukkit.createInventory(null, size * 9, title) :
                Bukkit.createInventory(null, type, title);

        this.fillAir(false);
        this.onCreate(this);
    }

    public HInventory(String id, String title, int size, InventoryType type) {
        this(id, title, size, type, new HashSet<>(Arrays.asList(Option.values())));
    }

    /**
     * Gets bukkit inventory
     *
     * @return bukkit inventory
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
     * Gets title of inventory
     *
     * @return Title of inventory
     */
    @Nonnull
    public final String getTitle() {
        return this.title;
    }

    /**
     * Gets type of inventory
     *
     * @return enum from InventoryType
     */
    @Nonnull
    public final InventoryType getInventoryType() {
        return this.inventory.getType();
    }

    /**
     * Gets size of inventory
     *
     * @return Size of inventory
     */
    public final int getSize() {
        return this.inventory.getSize() / 9;
    }

    /**
     * Checks UI contains option
     *
     * @param option enum HInventory.Option
     * @return If UI contains option, returns true
     */
    public final boolean hasOption(@Nonnull Option option) {
        Validate.notNull(option, "option cannot be null");
        return this.options.contains(option);
    }

    /**
     * Adds option to UI
     *
     * @param option enum HInventory.Option
     * @return instance of this class
     */
    public final <T extends HInventory> T addOption(@Nonnull Option option) {
        Validate.notNull(option, "option cannot be null");
        this.options.add(option);
        return (T) this;
    }

    /**
     * Removes option from UI
     *
     * @param option enum HInventory.Option
     * @return instance of this class
     */
    public final <T extends HInventory> T removeOption(@Nonnull Option option) {
        Validate.notNull(option, "option cannot be null");
        this.options.remove(option);
        return (T) this;
    }

    /**
     * If you want to use pagination, you need
     * VezorPagination instance
     *
     * @return VezorPagination instance
     */
    @Nonnull
    public final Pagination getPagination() {
        return this.pagination;
    }

    /**
     * Finds clickable item from slot that used parameter
     *
     * @param slot Sot
     * @return Clickable item
     */
    @Nonnull
    public final Optional<ClickableItem> findItem(int slot) {
        return Optional.ofNullable(this.items.getOrDefault(slot, null));
    }

    /**
     * Gets clickable item from slot that used parameter
     *
     * @param slot Sot
     * @return Clickable item
     */
    @Nonnull
    public final ClickableItem getItem(int slot) {
        return this.findItem(slot).orElseThrow(() -> new NullPointerException("item(" + slot + ") cannot be null!"));
    }

    /**
     * Checks there is a clickable item at slot
     *
     * @param slot Sot
     * @return return if there is item in slot
     */
    public final boolean hasItem(int slot) {
        return this.items.containsKey(slot);
    }

    /**
     * Sets clickable item to slot
     *
     * @param slot          Slot
     * @param clickableItem Clickable item
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T setItem(int slot, @Nullable ClickableItem clickableItem) {
        this.items.put(slot, clickableItem);
        this.inventory.setItem(slot, (clickableItem != null) ? clickableItem.getItem() : new ItemStack(Material.AIR));
        return (T) this;
    }

    /**
     * Sets clickable item to slot
     *
     * @param slot      Slot
     * @param itemStack Itemstack
     * @param consumer  What will happen when player click on item
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T setItem(int slot, @Nonnull ItemStack itemStack, @Nullable Consumer<InventoryClickEvent> consumer) {
        Validate.notNull(itemStack, "itemstack cannot be null");
        return this.setItem(slot, new ClickableItem(itemStack, consumer));
    }

    /**
     * Sets item to slot
     *
     * @param slot      Slot
     * @param itemStack Itemstack
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T setItem(int slot, @Nonnull ItemStack itemStack) {
        return this.setItem(slot, itemStack, null);
    }

    /**
     * Sets item to slot
     *
     * @param page Page
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T fillPage(Page page) {
        this.pagination.setCurrentPage(page.getNumber());
        return (T) this;
    }

    /**
     * Sets item to slot
     *
     * @param pageNum Page number
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T fillPage(int pageNum) {
        this.pagination.setCurrentPage(pageNum);
        return (T) this;
    }

    /**
     * Removes item from slot
     *
     * @param slot Slot
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T removeItem(int slot) {
        this.items.remove(slot);
        this.inventory.setItem(slot, null);
        return (T) this;
    }

    /**
     * Sets the slots with clickable item
     *
     * @param clickableItem    VezorClickableItem object
     * @param replaceWithItems If this param is true, it replaces all items with clickable item
     * @return instance of this class
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
     * Sets all the slots with clickable item
     *
     * @param clickableItem clickable item
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T fill(@Nullable ClickableItem clickableItem) {
        return this.fill(clickableItem, true);
    }

    /**
     * Sets the slots with itemstack
     *
     * @param itemStack        Itemstack that you want
     * @param replaceWithItems If this param is true, it replaces all items with itemstack
     */
    @Nonnull
    public final <T extends HInventory> T fill(@Nonnull ItemStack itemStack, boolean replaceWithItems) {
        return this.fill(new ClickableItem(itemStack, null), replaceWithItems);
    }

    /**
     * Sets all the slots with itemstack
     *
     * @param itemStack Itemstack that you want
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T fill(@Nonnull ItemStack itemStack) {
        return this.fill(new ClickableItem(itemStack, null), true);
    }

    /**
     * Sets the slots with material
     *
     * @param material         Item type that you want
     * @param replaceWithItems If this param is true, it replaces all items with material
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T fillMaterial(@Nonnull Material material, boolean replaceWithItems) {
        return this.fill(new ItemStack(material), replaceWithItems);
    }

    /**
     * Sets all the slots with material
     *
     * @param material Item type that you want
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T fillMaterial(@Nonnull Material material) {
        return this.fill(new ItemStack(material), true);
    }

    /**
     * Sets the slots with air
     *
     * @param replaceWithItems If this param is true, it replaces all items with air
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T fillAir(boolean replaceWithItems) {
        return this.fill(new ItemStack(Material.AIR), replaceWithItems);
    }

    /**
     * Sets all the slots with air
     *
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T fillAir() {
        return this.fill(new ItemStack(Material.AIR), true);
    }

    /**
     * Clears inventory
     *
     * @param replaceWithItems If this param is true, it replaces all items with null
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T fillNull(boolean replaceWithItems) {
        return this.fill((ClickableItem) null, replaceWithItems);
    }

    /**
     * Clears inventory
     *
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T fillNull() {
        return this.fill((ClickableItem) null, true);
    }

    /**
     * Reopens the inventory for player
     *
     * @return instance of this class
     */
    @Nonnull
    public <T extends HInventory> T refresh(Player player) {
        this.onOpen(this, player);
        return (T) this;
    }

    /**
     * Open inventory to player also put player
     * to hInventoryMap and triggers onOpen method
     *
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T open(Player player) {
        HInventory hInventory = HInventoryHandler.findByPlayer(player).orElse(null);

        if (hInventory == null || !hInventory.equals(this)) {
            player.openInventory(this.inventory);
            HInventoryHandler.getContent().put(player.getUniqueId(), this);
        }

        this.onOpen(this, player);
        return (T) this;
    }

    /**
     * Close inventory of player also if UI is not locked
     * removes from hInventoryMap and triggers onClose method
     *
     * @return instance of this class
     */
    @Nonnull
    public final <T extends HInventory> T close(Player player) {
        this.addOption(Option.CLOSABLE);
        player.closeInventory();
        return (T) this;
    }

    public void onCreate(HInventory hInventory) {

    }

    public void onOpen(HInventory hInventory, Player player) {

    }

    public void onClose(HInventory hInventory, Player player) {

    }


    /**
     * Option enums for UI
     */
    public enum Option {

        CLOSABLE,
        CANCEL_TOP_CLICK,
        CANCEL_DOWN_CLICK;

        Option() {
        }
    }
}