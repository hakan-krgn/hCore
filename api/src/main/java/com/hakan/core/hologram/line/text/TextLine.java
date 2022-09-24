package com.hakan.core.hologram.line.text;

import com.hakan.core.hologram.Hologram;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;

import javax.annotation.Nonnull;

/**
 * Text line class.
 */
public interface TextLine extends HologramLine {

    /**
     * Creates new text line.
     *
     * @param hologram Hologram of line.
     * @param text     Text to display.
     * @return Text line.
     */
    @Nonnull
    static TextLine create(@Nonnull Hologram hologram, @Nonnull String text) {
        Validate.notNull(hologram, "hologram cannot be null!");
        Validate.notNull(text, "text cannot be null!");

        TextLine line = ReflectionUtils.newInstance("com.hakan.core.hologram.line.text.TextLine_%s",
                hologram, hologram.getLocation());
        line.setText(text);

        return line;
    }

    /**
     * Creates new text line.
     *
     * @param hologram Hologram of line.
     * @param location Location of line.
     * @param text     Text to display.
     * @return Text line.
     */
    @Nonnull
    static TextLine create(@Nonnull Hologram hologram, @Nonnull Location location, @Nonnull String text) {
        Validate.notNull(hologram, "hologram cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(text, "text cannot be null!");

        TextLine line = TextLine.create(hologram, text);
        line.setLocation(location);

        return line;
    }


    /**
     * Gets text from line.
     *
     * @return Text of line.
     */
    @Nonnull
    String getText();

    /**
     * Sets text of line.
     *
     * @param text text of line.
     */
    void setText(@Nonnull String text);
}