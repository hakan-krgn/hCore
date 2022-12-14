package com.hakan.core.ui.sign.type;

import org.bukkit.Material;

/**
 * SignType class to
 * set type of sign.
 */
public enum SignType {

    NORMAL("OAK_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    OAK("OAK_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    ACACIA("ACACIA_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    BIRCH("BIRCH_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    DARK_OAK("DARK_OAK_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    JUNGLE("JUNGLE_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    SPRUCE("SPRUCE_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    CRIMSON("CRIMSON_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    WARPED("WARPED_SIGN", "LEGACY_SIGN", "SIGN_POST"),
    MANGROVE("MANGROVE_SIGN", "LEGACY_SIGN", "SIGN_POST"),

    NORMAL_WALL("OAK_WALL_SIGN", "LEGACY_WALL_SIGN", "WALL_SIGN"),
    OAK_WALL("OAK_WALL_SIGN", "LEGACY_WALL_SIGN", "WALL_SIGN"),
    ACACIA_WALL("ACACIA_WALL_SIGN", "LEGACY_WALL_SIGN", "WALL_SIGN"),
    BIRCH_WALL("BIRCH_WALL_SIGN", "LEGACY_WALL_SIGN", "WALL_SIGN"),
    DARK_OAK_WALL("DARK_OAK_WALL_SIGN", "LEGACY_WALL_SIGN", "WALL_SIGN"),
    JUNGLE_WALL("JUNGLE_WALL_SIGN", "LEGACY_WALL_SIGN", "WALL_SIGN"),
    SPRUCE_WALL("SPRUCE_WALL_SIGN", "LEGACY_WALL_SIGN", "WALL_SIGN"),
    CRIMSON_WALL("CRIMSON_WALL_SIGN", "LEGACY_WALL_SIGN", "WALL_SIGN"),
    WARPED_WALL("WARPED_WALL_SIGN", "LEGACY_WALL_SIGN", "WALL_SIGN"),
    MANGROVE_WALL("MANGROVE_WALL_SIGN", "LEGACY_WALL_SIGN", "WALL_SIGN"),
    ;


    private Material type;

    /**
     * Creates new instance of this class.
     *
     * @param types Types.
     */
    SignType(String... types) {
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
