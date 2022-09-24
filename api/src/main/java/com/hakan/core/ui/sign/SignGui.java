package com.hakan.core.ui.sign;

import com.hakan.core.HCore;
import com.hakan.core.protocol.ProtocolVersion;
import com.hakan.core.ui.Gui;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * SignGui class to manage
 * and show sign to player.
 */
public abstract class SignGui implements Gui {

    protected static final int LOWEST_Y_AXIS = (HCore.getProtocolVersion().isNewerOrEqual(ProtocolVersion.v1_18_R1)) ? -64 : 0;


    protected final Player player;
    protected SignType type;
    protected String[] lines;
    protected Consumer<String[]> consumer;

    /**
     * Creates new instance of this class.
     *
     * @param player Player.
     * @param type   Type of sign.
     * @param lines  Lines of sign.
     */
    public SignGui(@Nonnull Player player, @Nonnull SignType type, @Nonnull String... lines) {
        this.player = Validate.notNull(player, "player cannot be null!");
        this.type = Validate.notNull(type, "type cannot be null!");
        this.lines = Validate.notNull(lines, "lines cannot be null!");
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
     * Runs when sign close.
     *
     * @param consumer Callback.
     * @return This class.
     */
    @Nonnull
    public final SignGui whenInputReceived(@Nonnull Consumer<String[]> consumer) {
        this.consumer = Validate.notNull(consumer, "complete consumer cannot be null!");
        return this;
    }


    /**
     * Opens the gui to player.
     */
    public abstract void open();

    /**
     * Listens player packet if
     * packet is a sign packet.
     *
     * @param <T>    Packet type.
     * @param packet Packet.
     */
    public abstract <T> void listen(@Nonnull T packet);
}