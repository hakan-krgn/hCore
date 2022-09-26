package com.hakan.core.ui.sign;

import com.hakan.core.ui.Gui;
import com.hakan.core.ui.GuiHandler;
import com.hakan.core.ui.sign.type.SignType;
import com.hakan.core.ui.sign.wrapper.SignWrapper;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * SignGui class to manage
 * and show sign to player.
 */
public final class SignGui implements Gui {

    private final Player player;
    private final SignWrapper wrapper;
    private SignType type;
    private String[] lines;
    private Consumer<String[]> consumer;

    /**
     * Creates new instance of this class.
     *
     * @param player Player.
     * @param type   Type of sign.
     * @param lines  Lines of sign.
     */
    public SignGui(@Nonnull Player player,
                   @Nonnull SignType type,
                   @Nonnull String... lines) {
        this.wrapper = ReflectionUtils.newInstance("com.hakan.core.ui.sign.wrapper.SignWrapper_%s", this);
        this.player = Validate.notNull(player, "player cannot be null!");
        this.type = Validate.notNull(type, "type cannot be null!");
        this.lines = Validate.notNull(lines, "lines cannot be null!");
    }

    /**
     * Gets the player.
     *
     * @return Player.
     */
    @Nonnull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets type of sign.
     *
     * @return Type of sign.
     */
    @Nonnull
    public SignType getType() {
        return this.type;
    }

    /**
     * Sets type of sign.
     *
     * @param type Type of sign.
     * @return This class.
     */
    @Nonnull
    public SignGui setType(@Nonnull SignType type) {
        this.type = Validate.notNull(type, "type cannot be null!");
        return this;
    }

    /**
     * Gets line of sign.
     *
     * @return Lines of sign.
     */
    @Nonnull
    public String[] getLines() {
        return this.lines;
    }

    /**
     * Sets lines of sign.
     *
     * @param lines Lines of sign.
     * @return This class.
     */
    @Nonnull
    public SignGui setLines(@Nonnull String[] lines) {
        this.lines = Validate.notNull(lines, "lines cannot be null!");
        return this;
    }

    /**
     * Runs when sign close.
     *
     * @param consumer Callback.
     * @return This class.
     */
    @Nonnull
    public SignGui whenInputReceived(@Nonnull Consumer<String[]> consumer) {
        this.consumer = Validate.notNull(consumer, "complete consumer cannot be null!");
        return this;
    }

    /**
     * Opens the gui to player.
     *
     * @return This class.
     */
    @Nonnull
    public SignGui open() {
        this.wrapper.open();
        GuiHandler.getContent().put(this.player.getUniqueId(), this);
        return this;
    }
}