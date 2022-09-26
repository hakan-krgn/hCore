package com.hakan.core.ui.anvil;

import com.hakan.core.ui.Gui;
import com.hakan.core.ui.GuiHandler;
import com.hakan.core.ui.anvil.wrapper.AnvilWrapper;
import com.hakan.core.utils.ReflectionUtils;
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
public final class AnvilGui implements Gui {

    private final Player player;
    private final String title;
    private final String text;
    private final ItemStack leftItem;
    private final ItemStack rightItem;
    private final AnvilWrapper wrapper;

    private boolean closable;
    private Runnable openRunnable;
    private Runnable closeRunnable;
    private Consumer<String> inputConsumer;

    /**
     * Constructor.
     *
     * @param player    Player.
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
        this.wrapper = ReflectionUtils.newInstance("com.hakan.core.ui.anvil.wrapper.AnvilWrapper_%s", this);
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
     * Gets as bukkit inventory.
     *
     * @return Bukkit inventory.
     */
    @Nonnull
    public Inventory toInventory() {
        return this.wrapper.toInventory();
    }

    /**
     * Gets player.
     *
     * @return Player.
     */
    @Nonnull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets title of Gui.
     *
     * @return Title of Gui.
     */
    @Nonnull
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets text of input area.
     *
     * @return Text of input area.
     */
    @Nonnull
    public String getText() {
        return this.text;
    }

    /**
     * Gets item stack at left side.
     *
     * @return Item stack at left side.
     */
    @Nonnull
    public ItemStack getLeftItem() {
        return this.leftItem;
    }

    /**
     * Gets item stack at right side.
     *
     * @return Item stack at right side.
     */
    @Nullable
    public ItemStack getRightItem() {
        return this.rightItem;
    }

    /**
     * Gets if Gui is closable.
     *
     * @return If Gui is closable.
     */
    public boolean isClosable() {
        return this.closable;
    }

    /**
     * Sets if Gui is closable.
     *
     * @param closable If Gui is closable.
     * @return This class.
     */
    @Nonnull
    public AnvilGui setClosable(boolean closable) {
        this.closable = closable;
        return this;
    }

    /**
     * Runs when Gui is opened.
     *
     * @param openRunnable Callback.
     * @return This class.
     */
    @Nonnull
    public AnvilGui whenOpened(@Nonnull Runnable openRunnable) {
        this.openRunnable = Validate.notNull(openRunnable, "open callback cannot be null!");
        return this;
    }

    /**
     * Runs when input is received.
     * (When the player clicks the second slot on the anvil gui.)
     *
     * @param inputConsumer Callback.
     * @return This class.
     */
    @Nonnull
    public AnvilGui whenInputReceived(@Nonnull Consumer<String> inputConsumer) {
        this.inputConsumer = Validate.notNull(inputConsumer, "input callback cannot be null!");
        return this;
    }

    /**
     * Runs when Gui is closed.
     *
     * @param closeRunnable Callback.
     * @return This class.
     */
    @Nonnull
    public AnvilGui whenClosed(@Nonnull Runnable closeRunnable) {
        this.closeRunnable = Validate.notNull(closeRunnable, "close callback cannot be null!");
        return this;
    }

    /**
     * Opens gui to player.
     *
     * @return Instance of AnvilGui.
     */
    @Nonnull
    public AnvilGui open() {
        return this.open(true);
    }

    /**
     * Opens gui to player and if runnableActivity
     * is true, runs the open runnable.
     *
     * @param runnableActivity If true, runs whenOpened method.
     * @return Instance of AnvilGui.
     */
    @Nonnull
    public AnvilGui open(boolean runnableActivity) {
        if (runnableActivity && this.openRunnable != null)
            this.openRunnable.run();

        this.wrapper.open();
        GuiHandler.getContent().put(this.player.getUniqueId(), this);

        return this;
    }

    /**
     * Closes the Gui of player.
     *
     * @return Instance of AnvilGui.
     */
    @Nonnull
    public AnvilGui close() {
        if (this.closeRunnable != null)
            this.closeRunnable.run();

        this.closable = true;
        this.wrapper.close();
        GuiHandler.getContent().remove(this.player.getUniqueId());

        return this;
    }

    /**
     * Clones this Gui.
     *
     * @return Cloned Gui.
     */
    @Nonnull
    @Override
    public AnvilGui clone() {
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
}