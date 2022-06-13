package com.hakan.core.ui.sign;

import org.bukkit.Material;

/**
 * HSignType class to
 * set type of sign.
 */
public enum HSignType {

    NORMAL("OAK_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    OAK("OAK_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    ACACIA("ACACIA_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    BIRCH("BIRCH_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    DARK_OAK("DARK_OAK_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    JUNGLE("JUNGLE_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    SPRUCE("SPRUCE_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    ;


    private Material type;

    /**
     * Creates new instance of this class.
     *
     * @param types Types.
     */
    HSignType(String... types) {
        for (String type : types) {
            try {
                this.type = Material.valueOf(type);
                return;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Gets type of sign as material.
     *
     * @return Type of sign as material.
     */
    public Material asMaterial() {
        return this.type;
    }
}
