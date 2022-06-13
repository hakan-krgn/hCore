package com.hakan.core.ui.sign.builder;

import com.hakan.core.HCore;
import com.hakan.core.ui.sign.HSign;
import com.hakan.core.ui.sign.HSignType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * HSignBuilder class to build HSign.
 */
public final class HSignBuilder {

    private Player player;
    private HSignType type;
    private String[] lines;

    /**
     * Creates new instance of this class.
     */
    public HSignBuilder(@Nonnull Player player) {
        this.player = player;
        this.lines = new String[]{"", "", "", ""};
        this.type = HSignType.NORMAL;
    }

    /**
     * Sets the owner of this sign.
     *
     * @param player Player.
     * @return This class.
     */
    @Nonnull
    public HSignBuilder player(@Nonnull Player player) {
        this.player = Objects.requireNonNull(player, "type cannot be null!");
        return this;
    }

    /**
     * Sets type.
     *
     * @param type Type.
     * @return This class.
     */
    @Nonnull
    public HSignBuilder type(@Nonnull HSignType type) {
        this.type = Objects.requireNonNull(type, "type cannot be null!");
        return this;
    }

    /**
     * Sets lines.
     *
     * @param lines Lines.
     * @return This class.
     */
    @Nonnull
    public HSignBuilder lines(@Nonnull String... lines) {
        Objects.requireNonNull(lines, "lines cannot be null!");
        if (lines.length != 4)
            throw new IllegalArgumentException("lines length must be 4!");

        this.lines = lines;
        return this;
    }

    /**
     * Builds HSign.
     *
     * @return HSign.
     */
    @Nonnull
    public HSign build() {
        try {
            return (HSign) Class.forName("com.hakan.core.ui.sign.wrapper.HSign_" + HCore.getVersionString())
                    .getConstructor(Player.class, HSignType.class, String[].class)
                    .newInstance(this.player, this.type, this.lines);
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}