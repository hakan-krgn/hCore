package com.hakan.core.message.bossbar.meta;

/**
 * Bar flag class.
 */
public enum BarFlag {

    DARKEN_SKY,
    PLAY_BOSS_MUSIC,
    CREATE_FOG,
    ;

    public org.bukkit.boss.BarFlag toBukkit() {
        return org.bukkit.boss.BarFlag.valueOf(this.name());
    }
}
