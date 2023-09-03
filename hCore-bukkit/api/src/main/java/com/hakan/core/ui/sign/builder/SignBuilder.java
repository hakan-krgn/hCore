package com.hakan.core.ui.sign.builder;

import com.hakan.core.ui.sign.SignGui;
import com.hakan.core.ui.sign.type.SignType;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * SignBuilder class to build SignGui.
 */
public final class SignBuilder {

    private Player player;
    private SignType type;
    private String[] lines;
    private Consumer<String[]> consumer;

    /**
     * Creates new instance of this class.
     */
    public SignBuilder(@Nonnull Player player) {
        this.player = player;
        this.lines = new String[]{"", "", "", ""};
        this.type = SignType.NORMAL;
    }

    /**
     * Sets the owner of this sign.
     *
     * @param player Player.
     * @return This class.
     */
    @Nonnull
    public SignBuilder player(@Nonnull Player player) {
        this.player = Validate.notNull(player, "type cannot be null!");
        return this;
    }

    /**
     * Sets type.
     *
     * @param type Type.
     * @return This class.
     */
    @Nonnull
    public SignBuilder type(@Nonnull SignType type) {
        this.type = Validate.notNull(type, "type cannot be null!");
        return this;
    }

    /**
     * Runs when sign close.
     *
     * @param consumer Callback.
     * @return This class.
     */
    @Nonnull
    public SignBuilder whenInputReceived(@Nonnull Consumer<String[]> consumer) {
        this.consumer = Validate.notNull(consumer, "complete consumer cannot be null!");
        return this;
    }

    /**
     * Sets lines.
     *
     * @param lines Lines.
     * @return This class.
     */
    @Nonnull
    public SignBuilder lines(@Nonnull String... lines) {
        Validate.notNull(lines, "lines cannot be null!");
        if (lines.length != 4)
            throw new IllegalArgumentException("lines length must be 4!");

        this.lines = lines;
        return this;
    }

    /**
     * Builds SignGui.
     *
     * @return SignGui.
     */
    @Nonnull
    public SignGui build() {
        SignGui signGui = ReflectionUtils.newInstance("com.hakan.core.ui.sign.versions.SignGui_%s",
                new Class[]{Player.class, SignType.class, String[].class},
                new Object[]{this.player, this.type, this.lines});

        if (this.consumer != null)
            signGui.whenInputReceived(this.consumer);

        return signGui;
    }
}
