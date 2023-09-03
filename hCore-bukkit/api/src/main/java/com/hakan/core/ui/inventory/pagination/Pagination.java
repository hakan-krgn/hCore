package com.hakan.core.ui.inventory.pagination;

import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.ui.inventory.item.ClickableItem;
import com.hakan.core.utils.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Pagination class.
 */
public final class Pagination {

    private final InventoryGui inventory;

    private int currentPage;
    private List<Integer> slots;
    private List<ClickableItem> items;
    private List<Page> pages;

    /**
     * Creates new instance of this class.
     *
     * @param inventory Parent inventory.
     */
    public Pagination(@Nonnull InventoryGui inventory) {
        this.inventory = Validate.notNull(inventory, "inventory cannot be null!");
        this.currentPage = 0;
        this.slots = new ArrayList<>();
        this.items = new ArrayList<>();
        this.pages = new LinkedList<>();
        this.pages.add(new Page(0));
    }

    /**
     * Gets parent inventory.
     *
     * @return Parent inventory.
     */
    @Nonnull
    public InventoryGui getInventory() {
        return this.inventory;
    }

    /**
     * Gets page list as safe.
     *
     * @return Page list.
     */
    @Nonnull
    public List<Page> getPagesSafe() {
        return new ArrayList<>(this.pages);
    }

    /**
     * Gets item list as safe.
     *
     * @return Item list.
     */
    @Nonnull
    public List<ClickableItem> getItemsSafe() {
        return new ArrayList<>(this.items);
    }

    /**
     * Sets clickable item list.
     *
     * @param clickableItems Clickable item list
     */
    public void setItems(@Nonnull List<ClickableItem> clickableItems) {
        this.items = Validate.notNull(clickableItems, "clickable items cannot be null!");
        this.pages = this.createPages();
    }

    /**
     * Gets slots.
     *
     * @return Slots.
     */
    @Nonnull
    public List<Integer> getSlots() {
        return this.slots;
    }

    /**
     * Sets slots min to max.
     *
     * @param min First.
     * @param max Last.
     */
    public void setSlots(int min, int max) {
        this.setSlots(IntStream.rangeClosed(min, max).boxed().collect(Collectors.toList()));
    }

    /**
     * Sets slots.
     *
     * @param ints Slots.
     */
    public void setSlots(@Nonnull List<Integer> ints) {
        this.slots = Validate.notNull(ints, "slots cannot be null!");
        this.pages = this.createPages();
    }

    /**
     * Finds page from page number.
     *
     * @param page Page as number.
     * @return Page.
     */
    @Nonnull
    public Optional<Page> findPage(int page) {
        return Optional.ofNullable(this.pages.get(page));
    }

    /**
     * Gets page from page number.
     *
     * @param page Page number.
     * @return Page.
     */
    @Nonnull
    public Page getPage(int page) {
        return this.findPage(page).orElseThrow(() -> new NullPointerException("there is no page at number(" + page + ")!"));
    }

    /**
     * Gets current page number.
     *
     * @return Current page number.
     */
    public int getCurrentPage() {
        return this.currentPage;
    }

    /**
     * Sets current page number.
     *
     * @param page Current page number.
     */
    public void setCurrentPage(int page) {
        this.setCurrentPage(this.getPage(page));
    }

    /**
     * Changes current page and
     * fill the inventory.
     *
     * @param page Page.
     */
    public void setCurrentPage(@Nonnull Page page) {
        this.currentPage = Validate.notNull(page, "page cannot be null!").getNumber();
        page.getItems().forEach(this.inventory::setItem);
    }

    /**
     * Gets next page number.
     *
     * @return Next page number.
     */
    public int getNextPage() {
        return this.currentPage + 1;
    }

    /**
     * Gets previous page number.
     *
     * @return Previous page number.
     */
    public int getPreviousPage() {
        return this.currentPage - 1;
    }

    /**
     * Gets first page number.
     *
     * @return First page number
     */
    public int getFirstPage() {
        return 0;
    }

    /**
     * Gets last page number.
     *
     * @return Last page number.
     */
    public int getLastPage() {
        double itemSize = this.items.size();
        if (itemSize == 0) return 0;

        double slotSize = this.slots.size();
        if (slotSize == 0) return 0;

        return (int) (Math.ceil(itemSize / slotSize) - 1);
    }

    /**
     * Checks the current page is first page.
     *
     * @return If the current page is first page, returns true.
     */
    public boolean isFirstPage() {
        return this.currentPage == this.getFirstPage();
    }

    /**
     * Checks the current page is last page.
     *
     * @return If the current page is last page, returns true.
     */
    public boolean isLastPage() {
        return this.currentPage == this.getLastPage();
    }

    /**
     * Creates pages from this.items list.
     *
     * @return Pages as list.
     */
    @Nonnull
    private List<Page> createPages() {
        int clickableItemSize = this.items.size();
        int itemSlotSize = this.slots.size();
        int lastPage = this.getLastPage();

        List<Page> pages = new LinkedList<>();
        for (int pageNum = 0; pageNum <= lastPage; pageNum++) {
            Page page = new Page(pageNum);
            pages.add(page);

            int first = pageNum * itemSlotSize;
            int last = (pageNum + 1) * itemSlotSize;
            for (int i = first; i < last; i++) {
                ClickableItem clickableItem = (clickableItemSize > i) ? this.items.get(i) : new ClickableItem(new ItemStack(Material.AIR), null);
                page.getItems().put(this.slots.get(i - first), clickableItem);
            }
        }

        return pages;
    }
}
