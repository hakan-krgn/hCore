package com.hakan.core.ui.sign.builder;

import com.hakan.core.ui.sign.SignGui;
import com.hakan.core.ui.sign.SignType;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * SignGuiBuilder class to build SignGui.
 */
public final class SignGuiBuilder {

    private Player player;
    private SignType type;
    private String[] lines;

    /**
     * Creates new instance of this class.
     */
    public SignGuiBuilder(@Nonnull Player player) {
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
    public SignGuiBuilder player(@Nonnull Player player) {
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
    public SignGuiBuilder type(@Nonnull SignType type) {
        this.type = Validate.notNull(type, "type cannot be null!");
        return this;
    }

    /**
     * Sets lines.
     *
     * @param lines Lines.
     * @return This class.
     */
    @Nonnull
    public SignGuiBuilder lines(@Nonnull String... lines) {
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
        return ReflectionUtils.newInstance("com.hakan.core.ui.sign.wrapper.Sign_%s",
                this.player, this.type, this.lines);
    }
}