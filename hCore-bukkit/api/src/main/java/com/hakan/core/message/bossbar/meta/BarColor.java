package com.hakan.core.message.bossbar.meta;

/**
 * Bar color class.
 */
public enum BarColor {

    PINK,
    BLUE,
    RED,
    GREEN,
    YELLOW,
    PURPLE,
    WHITE,
    ;

    public org.bukkit.boss.BarColor toBukkit() {
        return org.bukkit.boss.BarColor.valueOf(this.name());
    }
}
