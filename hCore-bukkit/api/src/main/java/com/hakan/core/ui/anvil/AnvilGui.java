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
     *
     * @return Title of Gui.
     */
    @Nonnull
    public final String getTitle() {
        return this.title;
    }

    /**
     * Gets text of input area.
     *
     * @return Text of input area.
     */
    @Nonnull
    public final String getText() {
        return this.text;
    }

    /**
     * Gets item stack at left side.
     *
     * @return Item stack at left side.
     */
    @Nonnull
    public final ItemStack getLeftItem() {
        return this.leftItem;
    }

    /**
     * Gets item stack at right side.
     *
     * @return Item stack at right side.
     */
    @Nullable
    public final ItemStack getRightItem() {
        return this.rightItem;
    }

    /**
     * Gets if Gui is closable.
     *
     * @return If Gui is closable.
     */
    public final boolean isClosable() {
        return this.closable;
    }

    /**
     * Sets if Gui is closable.
     *
     * @param closable If Gui is closable.
     * @return This class.
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
     * @return This class.
     */
    @Nonnull
    public final AnvilGui whenOpened(@Nonnull Runnable openRunnable) {
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
    public final AnvilGui whenInputReceived(@Nonnull Consumer<String> inputConsumer) {
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
    public final AnvilGui whenClosed(@Nonnull Runnable closeRunnable) {
        this.closeRunnable = Validate.notNull(closeRunnable, "close callback cannot be null!");
        return this;
    }

    /**
     * Triggers open runnable and
     * change variables.
     *
     * @param runnableActivity If true, open runnable will be triggered.
     * @return This class.
     */
    @Nonnull
    public final AnvilGui onOpen(boolean runnableActivity) {
        if (runnableActivity && this.openRunnable != null)
            this.openRunnable.run();

        GuiHandler.getContent().put(this.player.getUniqueId(), this);
        return this;
    }

    /**
     * Triggers input consumer and
     * change variables.
     *
     * @param input Input.
     * @return This class.
     */
    @Nonnull
    public final AnvilGui onInputReceive(@Nonnull String input) {
        if (this.inputConsumer != null)
            this.inputConsumer.accept(Validate.notNull(input, "input cannot be null!"));
        return this;
    }

    /**
     * Triggers close runnable and
     * change variables.
     *
     * @param runnableActivity If true, close runnable will be triggered.
     * @return This class.
     */
    @Nonnull
    public final AnvilGui onClose(boolean runnableActivity) {
        if (runnableActivity && this.closeRunnable != null)
            this.closeRunnable.run();

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
     *
     * @return Bukkit inventory.
     */
    @Nonnull
    public abstract Inventory toInventory();

    /**
     * Opens gui to player.
     *
     * @return Instance of AnvilGui.
     */
    @Nonnull
    public final AnvilGui open() {
        return this.open(true);
    }

    /**
     * Opens gui to player.
     *
     * @return Instance of AnvilGui.
     */
    @Nonnull
    public final AnvilGui close() {
        return this.close(true);
    }

    /**
     * Opens anvil gui to player.
     *
     * @param runnableActivity if it's true, open runnable will run.
     * @return Instance of AnvilGui.
     */
    public abstract AnvilGui open(boolean runnableActivity);

    /**
     * Closes the anvil gui of player.
     *
     * @param runnableActivity if it's true, close runnable will run.
     * @return Instance of AnvilGui.
     */
    public abstract AnvilGui close(boolean runnableActivity);
}
