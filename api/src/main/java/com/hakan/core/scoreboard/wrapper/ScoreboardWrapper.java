package com.hakan.core.scoreboard.wrapper;

import com.hakan.core.scoreboard.Scoreboard;
import com.hakan.core.utils.ColorUtil;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;

/**
 * Sco
 */
public abstract class ScoreboardWrapper {

    protected final Scoreboard scoreboard;

    /**
     * Creates new Instance of this class.
     *
     * @param scoreboard Scoreboard.
     */
    public ScoreboardWrapper(@Nonnull Scoreboard scoreboard) {
        this.scoreboard = Validate.notNull(scoreboard, "scoreboard cannot be null!");
    }

    /**
     * Splits text into 3 different parts
     * for prefix, middle and suffix.
     *
     * @param text Text.
     * @param len1 Length of prefix.
     * @param len2 Length of suffix.
     * @return 3 parts.
     */
    @Nonnull
    protected final String[] splitLine(int line, @Nonnull String text, int len1, int len2) {
        Validate.notNull(text, "text cannot be null!");

        String color = (line >= 10) ? "§" + new String[]{"a", "b", "c", "d", "e", "f"}[line - 10] : "§" + line;
        text += new String(new char[len1 + len2 - text.length()]).replace("\0", "‼");

        String prefix = text.substring(0, len1);
        String suffix = text.substring(len1, len1 + len2);
        String middle = color + ColorUtil.getLastColors(prefix);

        return new String[]{
                prefix.replace("‼", ""),
                middle.replace("‼", ""),
                suffix.replace("‼", "")
        };
    }


    /**
     * Shows the scoreboard to player.
     */
    public abstract void show();

    /**
     * Deletes scoreboard.
     */
    public abstract void delete();
}