package com.hakan.core.ui.sign;

import com.hakan.core.ui.Gui;
import com.hakan.core.ui.GuiHandler;
import com.hakan.core.ui.sign.type.SignType;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * SignGui is a class that
 * handles nms methods of sign gui.
 */
public abstract class SignGui implements Gui {

    protected final Player player;
    protected SignType type;
    protected String[] lines;
    protected Runnable openRunnable;
    protected Consumer<String[]> inputConsumer;

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
    public final Player getPlayer() {
        return this.player;
    }

    /**
     * Gets type of sign.
     *
     * @return Type of sign.
     */
    @Nonnull
    public final SignType getType() {
        return this.type;
    }

    /**
     * Sets type of sign.
     *
     * @param type Type of sign.
     * @return This class.
     */
    @Nonnull
    public final SignGui setType(@Nonnull SignType type) {
        this.type = Validate.notNull(type, "type cannot be null!");
        return this;
    }

    /**
     * Gets line of sign.
     *
     * @return Lines of sign.
     */
    @Nonnull
    public final String[] getLines() {
        return this.lines;
    }

    /**
     * Sets lines of sign.
     *
     * @param lines Lines of sign.
     * @return This class.
     */
    @Nonnull
    public final SignGui setLines(@Nonnull String[] lines) {
        this.lines = Validate.notNull(lines, "lines cannot be null!");
        return this;
    }

    /**
     * Runnable will be triggered
     * when sign open to player.
     *
     * @param runnable Callback.
     * @return This class.
     */
    @Nonnull
    public final SignGui whenOpened(@Nonnull Runnable runnable) {
        this.openRunnable = Validate.notNull(runnable, "runnable cannot be null!");
        return this;
    }

    /**
     * Consumer will be triggered
     * when sign receive an input.
     *
     * @param consumer Callback.
     * @return This class.
     */
    @Nonnull
    public final SignGui whenInputReceived(@Nonnull Consumer<String[]> consumer) {
        this.inputConsumer = Validate.notNull(consumer, "complete consumer cannot be null!");
        return this;
    }

    /**
     * This should be run when
     * sign is opened.
     *
     * @return This class.
     */
    @Nonnull
    public final SignGui onOpen() {
        if (this.openRunnable != null)
            this.openRunnable.run();

        GuiHandler.getContent().put(this.player.getUniqueId(), this);
        return this;
    }

    /**
     * This should be run when
     * sign is closed or input
     * received.
     *
     * @return This class.
     */
    @Nonnull
    public final SignGui onInputReceive(@Nonnull String[] lines) {
        if (this.inputConsumer != null)
            this.inputConsumer.accept(Validate.notNull(lines, "lines cannot be null!"));

        GuiHandler.getContent().remove(this.player.getUniqueId());
        return this;
    }



    /**
     * Opens the sign gui
     * to the player.
     *
     * @return This class.
     */
    @Nonnull
    public abstract SignGui open();

    /**
     * Receives input from sign which
     * is sent by nms to player.
     *
     * @param packet Packet.
     * @param <T>    Type of packet.
     * @return This class.
     */
    @Nonnull
    public abstract <T> SignGui receiveInput(@Nonnull T packet);
}
