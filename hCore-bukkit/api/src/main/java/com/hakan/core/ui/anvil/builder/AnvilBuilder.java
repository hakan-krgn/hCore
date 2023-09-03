package com.hakan.core.ui.anvil.builder;

import com.hakan.core.ui.anvil.AnvilGui;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * AnvilBuilder class to build AnvilGui.
 */
public final class AnvilBuilder {

    private Player player;
    private String title;
    private String text;
    private ItemStack leftItem;
    private ItemStack rightItem;
    private boolean closable;

    private Runnable openRunnable;
    private Runnable closeRunnable;
    private Consumer<String> inputConsumer;

    /**
     * Constructor.
     *
     * @param player Player.
     */
    public AnvilBuilder(@Nonnull Player player) {
        this.player = Validate.notNull(player, "player cannot be null!");
        this.title = "";
        this.text = "";
        this.leftItem = new ItemStack(Material.DIAMOND);
        this.rightItem = null;
        this.closable = true;
    }

    /**
     * Sets the owner of this anvil.
     *
     * @param player Player.
     * @return This class.
     */
    @Nonnull
    public AnvilBuilder player(@Nonnull Player player) {
        this.player = Validate.notNull(player, "player cannot be null!");
        return this;
    }

    /**
     * Sets title.
     *
     * @param title Title.
     * @return This class.
     */
    @Nonnull
    public AnvilBuilder title(@Nonnull String title) {
        this.title = Validate.notNull(title, "title cannot be null!");
        return this;
    }

    /**
     * Sets text at input area.
     *
     * @param text Text.
     * @return This class.
     */
    @Nonnull
    public AnvilBuilder text(@Nonnull String text) {
        this.text = Validate.notNull(text, "text cannot be null!");
        return this;
    }

    /**
     * Sets item stack at left side of anvil.
     *
     * @param leftItem Item stack.
     * @return This class.
     */
    @Nonnull
    public AnvilBuilder leftItem(@Nonnull ItemStack leftItem) {
        this.leftItem = Validate.notNull(leftItem, "leftItem cannot be null!");
        return this;
    }

    /**
     * Sets item stack at right side of anvil.
     *
     * @param rightItem Item stack.
     * @return This class.
     */
    @Nonnull
    public AnvilBuilder rightItem(@Nullable ItemStack rightItem) {
        this.rightItem = rightItem;
        return this;
    }

    /**
     * Sets closable.
     *
     * @param closable Closable.
     * @return This class.
     */
    @Nonnull
    public AnvilBuilder closable(boolean closable) {
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
    public AnvilBuilder whenOpened(@Nonnull Runnable openRunnable) {
        this.openRunnable = Validate.notNull(openRunnable, "open callback cannot be null!");
        return this;
    }

    /**
     * Runs when input is received.
     * (When the player clicks the second slot on the Anvil Gui.)
     *
     * @param inputConsumer Callback.
     * @return This class.
     */
    @Nonnull
    public AnvilBuilder whenInputReceived(@Nonnull Consumer<String> inputConsumer) {
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
    public AnvilBuilder whenClosed(@Nonnull Runnable closeRunnable) {
        this.closeRunnable = Validate.notNull(closeRunnable, "close callback cannot be null!");
        return this;
    }

    /**
     * Builds AnvilGui.
     *
     * @return AnvilGui.
     */
    @Nonnull
    public AnvilGui build() {
        AnvilGui anvil = ReflectionUtils.newInstance("com.hakan.core.ui.anvil.versions.AnvilGui_%s",
                new Class[]{Player.class, String.class, String.class, ItemStack.class, ItemStack.class},
                new Object[]{this.player, this.title, this.text, this.leftItem, this.rightItem});
        anvil.setClosable(this.closable);

        if (this.openRunnable != null)
            anvil.whenOpened(this.openRunnable);
        if (this.closeRunnable != null)
            anvil.whenClosed(this.closeRunnable);
        if (this.inputConsumer != null)
            anvil.whenInputReceived(this.inputConsumer);

        return anvil;
    }
}
