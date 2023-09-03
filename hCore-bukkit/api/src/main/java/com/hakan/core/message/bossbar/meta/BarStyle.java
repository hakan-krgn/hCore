package com.hakan.core.message.bossbar.meta;

/**
 * Bar style class.
 */
public enum BarStyle {

    SOLID,
    SEGMENTED_6,
    SEGMENTED_10,
    SEGMENTED_12,
    SEGMENTED_20,
    ;

    public org.bukkit.boss.BarStyle toBukkit() {
        return org.bukkit.boss.BarStyle.valueOf(this.name());
    }
}
