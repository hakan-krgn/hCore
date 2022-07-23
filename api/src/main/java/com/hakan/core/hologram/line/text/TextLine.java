package com.hakan.core.hologram.line.text;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.hologram.util.HHologramUtils;
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
    static TextLine create(@Nonnull HHologram hologram, @Nonnull String text) {
        Validate.notNull(hologram, "hologram cannot be null!");
        Validate.notNull(text, "text cannot be null!");

        String path = "com.hakan.core.hologram.line.text.TextLine_" + HCore.getVersionString();
        Class<?>[] classes = new Class[]{HHologram.class, Location.class};
        Object[] objects = new Object[]{hologram, hologram.getLocation()};

        TextLine line = HHologramUtils.createNewInstance(path, classes, objects);
        line.setText(text);

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