package com.hakan.core.utils;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;

/**
 * ProtocolVersion class to get the current
 * protocol version of the server and compare
 * protocol versions.
 */
public enum ProtocolVersion {

    v1_8_R3("v1_8_R3"),
    v1_9_R1("v1_9_R1"),
    v1_9_R2("v1_9_R2"),
    v1_10_R1("v1_10_R1"),
    v1_11_R1("v1_11_R1"),
    v1_12_R1("v1_12_R1"),
    v1_13_R1("v1_13_R1"),
    v1_13_R2("v1_13_R2"),
    v1_14_R1("v1_14_R1"),
    v1_15_R1("v1_15_R1"),
    v1_16_R1("v1_16_R1"),
    v1_16_R2("v1_16_R2"),
    v1_16_R3("v1_16_R3"),
    v1_17_R1("v1_17_R1"),
    v1_18_R1("v1_18_R1"),
    v1_18_R2("v1_18_R2"),
    v1_19_R1("v1_19_R1"),
    v1_19_R1_2("v1_19_R1_2"),
    ;


    /**
     * Gets the current protocol version.
     *
     * @return The current protocol version.
     */
    @Nonnull
    public static ProtocolVersion getCurrentVersion() {
        if (Bukkit.getBukkitVersion().contains("1.19.1"))
            return v1_19_R1_2;

        String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        for (ProtocolVersion protocolVersion : ProtocolVersion.values())
            if (version.equals(protocolVersion.getKey()))
                return protocolVersion;
        throw new IllegalStateException("Unknown protocol version!");
    }


    private final String key;

    /**
     * Constructor.
     *
     * @param key The protocol version key.
     */
    ProtocolVersion(@Nonnull String key) {
        this.key = Validate.notNull(key, "key cannot be null!");
    }

    /**
     * Gets the protocol version key.
     *
     * @return The protocol version key.
     */
    @Nonnull
    public String getKey() {
        return this.key;
    }

    /**
     * Checks if the protocol version is newer than the given version.
     *
     * @param version The version to check.
     * @return True if the protocol version is newer or equal to the given version.
     */
    public boolean isNewer(@Nonnull ProtocolVersion version) {
        Validate.notNull(version, "version cannot be null!");
        return this.ordinal() > version.ordinal();
    }

    /**
     * Checks if the protocol version is newer than or equal to the given version.
     *
     * @param version The version to check.
     * @return True if the protocol version is older or equal to the given version.
     */
    public boolean isNewerOrEqual(@Nonnull ProtocolVersion version) {
        Validate.notNull(version, "version cannot be null!");
        return this.ordinal() >= version.ordinal();
    }

    /**
     * Checks if the protocol version is older the given version.
     *
     * @param version The version to check.
     * @return True if the protocol version is older or equal to the given version.
     */
    public boolean isOlder(@Nonnull ProtocolVersion version) {
        Validate.notNull(version, "version cannot be null!");
        return this.ordinal() < version.ordinal();
    }

    /**
     * Checks if the protocol version is older or equal to the given version.
     *
     * @param version The version to check.
     * @return True if the protocol version is older or equal to the given version.
     */
    public boolean isOlderOrEqual(@Nonnull ProtocolVersion version) {
        Validate.notNull(version, "version cannot be null!");
        return this.ordinal() <= version.ordinal();
    }
}