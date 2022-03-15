package com.hakan.message.title;

public class HTitle {

    private String title;
    private String subtitle;
    private int stay;
    private int fadein;
    private int fadeout;

    public HTitle(String title, String subtitle, int stay, int fadein, int fadeout) {
        this.title = title;
        this.subtitle = subtitle;
        this.stay = stay;
        this.fadein = fadein;
        this.fadeout = fadeout;
    }

    public HTitle(String title, String subtitle, int stay) {
        this(title, subtitle, stay, 0, 0);
    }

    public HTitle(String title, String subtitle) {
        this(title, subtitle, 20, 0, 0);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getStay() {
        return this.stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    public int getFadeIn() {
        return this.fadein;
    }

    public void setFadeIn(int fadein) {
        this.fadein = fadein;
    }

    public int getFadeOut() {
        return this.fadeout;
    }

    public void setFadeOut(int fadeout) {
        this.fadeout = fadeout;
    }
}