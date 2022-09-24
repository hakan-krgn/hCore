package com.hakan.core.ui.anvil;

import com.hakan.core.ui.Gui;
import com.hakan.core.ui.GuiHandler;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * AnvilGui is a class that
 * handles Anvil Gui.
 */
@SuppressWarnings({"UnusedReturnValue"})
public abstract class AnvilGui implements Gui {

    protected final Player player;
    protected final String title;
    protected final String text;
    protected final ItemStack leftItem;
    protected final ItemStack rightItem;

    protected boolean closable;
    protected Runnable openRunnable;
    protected Runnable closeRunnable;
    protected Consumer<String> inputConsumer;

    /**
     * Constructor.
     *
     * @param title     Title.
     * @param text      Text.
     * @param leftItem  Left item.
     * @param rightItem Right item.
     */
    public AnvilGui(@Nonnull Player player,
                    @Nonnull String title,
                    @Nonnull String text,
                    @Nonnull ItemStack leftItem,
                    @Nullable ItemStack rightItem) {
        this.player = Validate.notNull(player, "player cannot be null!");
        this.title = Validate.notNull(title, "title cannot be null!");
        this.text = Validate.notNull(text, "text cannot be null!");
        this.leftItem = Validate.notNull(leftItem, "leftItem cannot be null!");
        this.rightItem = rightItem;
        this.closable = true;

        ItemMeta meta = this.leftItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(this.text);
            this.leftItem.setItemMeta(meta);
        }
    }

    /**
     * Gets player.
     *
     * @return Player.
     */
    @Nonnull
    public final Player getPlayer() {
        return this.player;
    }

    /**
     * Gets title of Gui.
     */
    @Nonnull
    public final String getTitle() {
        return this.title;
    }

    /**
     * Gets text of input area.
     */
    @Nonnull
    public final String getText() {
        return this.text;
    }

    /**
     * Gets item stack at left side.
     */
    @Nonnull
    public final ItemStack getLeftItem() {
        return this.leftItem;
    }

    /**
     * Gets item stack at right side.
     */
    @Nullable
    public final ItemStack getRightItem() {
        return this.rightItem;
    }

    /**
     * Gets if Gui is closable.
     */
    public final boolean isClosable() {
        return this.closable;
    }

    /**
     * Sets if Gui is closable.
     */
    @Nonnull
    public final AnvilGui setClosable(boolean closable) {
        this.closable = closable;
        return this;
    }

    /**
     * Runs when Gui is opened.
     *
     * @param openRunnable Callback.
     */
    @Nonnull
    public final AnvilGui whenOpened(@Nonnull Runnable openRunnable) {
        this.openRunnable = Validate.notNull(openRunnable, "open callback cannot be null!");
        return this;
    }

    /**
     * Runs when input is received.
     * (When the player clicks the second slot on the Anvil Gui.)
     *
     * @param inputConsumer Callback.
     */
    @Nonnull
    public final AnvilGui whenInputReceived(@Nonnull Consumer<String> inputConsumer) {
        this.inputConsumer = Validate.notNull(inputConsumer, "input callback cannot be null!");
        return this;
    }

    /**
     * Runs when Gui is closed.
     *
     * @param closeRunnable Callback.
     */
    @Nonnull
    public final AnvilGui whenClosed(@Nonnull Runnable closeRunnable) {
        this.closeRunnable = Validate.notNull(closeRunnable, "close callback cannot be null!");
        return this;
    }

    /**
     * Opens Gui to player.
     *
     * @return Instance of AnvilGui.
     */
    @Nonnull
    public final AnvilGui open() {
        return this.open(true);
    }

    /**
     * Clones this Gui.
     *
     * @return Cloned Gui.
     */
    @Nonnull
    @Override
    public final AnvilGui clone() {
        return GuiHandler.anvilBuilder(this.player)
                .title(this.title)
                .text(this.text)
                .leftItem(this.leftItem)
                .rightItem(this.rightItem)
                .closable(this.closable)
                .whenOpened(this.openRunnable)
                .whenInputReceived(this.inputConsumer)
                .whenClosed(this.closeRunnable)
                .build();
    }


    /**
     * Gets as bukkit inventory.
     */
    @Nonnull
    public abstract Inventory toInventory();

    /**
     * Opens Gui to player.
     *
     * @param runnableActivity If true, runs whenOpened method.
     * @return Instance of AnvilGui.
     */
    @Nonnull
    public abstract AnvilGui open(boolean runnableActivity);

    /**
     * Closes the Gui of player.
     *
     * @return Instance of AnvilGui.
     */
    @Nonnull
    public abstract AnvilGui close();
}