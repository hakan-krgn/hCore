package com.hakan.core.message.title;

import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;

/**
 * Title class to send
 * title to player.
 */
public final class Title {

    private String title;
    private String subtitle;
    private int stay;
    private int fadein;
    private int fadeout;

    /**
     * Creates new instance of this class.
     *
     * @param title    Title.
     * @param subtitle Subtitle.
     * @param stay     Stay time as tick.
     * @param fadein   Fade in time as tick.
     * @param fadeout  Fade out time as tick.
     */
    public Title(@Nonnull String title, @Nonnull String subtitle, int stay, int fadein, int fadeout) {
        this.title = Validate.notNull(title, "title cannot be null!");
        this.subtitle = Validate.notNull(subtitle, "subtitle cannot be null!");
        this.stay = stay;
        this.fadein = fadein;
        this.fadeout = fadeout;
    }

    /**
     * Creates new instance of this class.
     *
     * @param title    Title.
     * @param subtitle Subtitle.
     * @param stay     Stay time as tick.
     */
    public Title(@Nonnull String title, @Nonnull String subtitle, int stay) {
        this(title, subtitle, stay, 4, 4);
    }

    /**
     * Creates new instance of this class.
     *
     * @param title    Title.
     * @param subtitle Subtitle.
     */
    public Title(@Nonnull String title, @Nonnull String subtitle) {
        this(title, subtitle, 60, 4, 4);
    }

    /**
     * Gets title.
     *
     * @return Title.
     */
    @Nonnull
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets title.
     *
     * @param title Title.
     */
    public void setTitle(@Nonnull String title) {
        this.title = Validate.notNull(title, "title cannot be null!");
    }

    /**
     * Gets subtitle.
     *
     * @return Subtitle.
     */
    @Nonnull
    public String getSubtitle() {
        return this.subtitle;
    }

    /**
     * Set subtitle.
     *
     * @param subtitle Subtitle.
     */
    public void setSubtitle(@Nonnull String subtitle) {
        this.subtitle = Validate.notNull(subtitle, "sub title cannot be null!");
    }

    /**
     * Gets stay time of title.
     *
     * @return Stay time of title.
     */
    public int getStay() {
        return this.stay;
    }

    /**
     * Sets stay time of title.
     *
     * @param stay Stay time of title.
     */
    public void setStay(int stay) {
        this.stay = stay;
    }

    /**
     * Gets fade in time of title.
     *
     * @return Fade in time of title.
     */
    public int getFadeIn() {
        return this.fadein;
    }

    /**
     * Sets fade in time of title.
     *
     * @param fadein Fade in time of title.
     */
    public void setFadeIn(int fadein) {
        this.fadein = fadein;
    }

    /**
     * Gets fade out time of title.
     *
     * @return Fade out time of title.
     */
    public int getFadeOut() {
        return this.fadeout;
    }

    /**
     * Sets fade out time of title.
     *
     * @param fadeout Fade out time of title.
     */
    public void setFadeOut(int fadeout) {
        this.fadeout = fadeout;
    }
}
