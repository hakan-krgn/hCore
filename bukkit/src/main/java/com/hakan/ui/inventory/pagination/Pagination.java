package com.hakan.ui.inventory.pagination;

import com.hakan.ui.inventory.HInventory;
import com.hakan.ui.inventory.item.ClickableItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Pagination {

    private final HInventory inventory;

    private int currentPage;
    private List<Integer> slots;
    private List<ClickableItem> items;
    private List<Page> pages;

    public Pagination(HInventory inventory) {
        this.currentPage = 0;
        this.inventory = inventory;
        this.slots = new ArrayList<>();
        this.items = new ArrayList<>();
        this.pages = new LinkedList<>();
        this.pages.add(new Page(0));
    }

    public HInventory getInventory() {
        return this.inventory;
    }

    public List<Page> getPagesSafe() {
        return new ArrayList<>(this.pages);
    }

    public List<ClickableItem> getItemsSafe() {
        return new ArrayList<>(this.items);
    }

    public void setItems(List<ClickableItem> clickableItems) {
        this.items = clickableItems;
        this.pages = this.createPages();
    }

    public List<Integer> getSlots() {
        return this.slots;
    }

    public void setSlots(int min, int max) {
        this.setSlots(IntStream.rangeClosed(min, max).boxed().collect(Collectors.toList()));
    }

    public void setSlots(List<Integer> ints) {
        this.slots = ints;
        this.pages = this.createPages();
    }

    public Optional<Page> findPage(int page) {
        return Optional.ofNullable(this.pages.get(page));
    }

    public Page getPage(int page) {
        return this.findPage(page).orElseThrow(() -> new NullPointerException("there is no page at number(" + page + ")!"));
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int page) {
        this.setCurrentPage(this.getPage(page));
    }

    public void setCurrentPage(Page page) {
        this.currentPage = page.getNumber();
        page.getItems().forEach(this.inventory::setItem);
    }

    public int getNextPage() {
        return this.currentPage + 1;
    }

    public int getPreviousPage() {
        return this.currentPage - 1;
    }

    public int getFirstPage() {
        return 0;
    }

    public int getLastPage() {
        double itemSize = this.items.size();
        if (itemSize == 0) return 0;

        double slotSize = this.slots.size();
        if (slotSize == 0) return 0;

        return (int) (Math.ceil(itemSize / slotSize) - 1);
    }

    public boolean isFirstPage() {
        return this.currentPage == this.getFirstPage();
    }

    public boolean isLastPage() {
        return this.currentPage == this.getLastPage();
    }

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