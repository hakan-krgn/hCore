package com.hakan.core.utils;

import org.bukkit.Bukkit;

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
    ;


    /**
     * Gets the current protocol version.
     *
     * @return The current protocol version.
     */
    public static ProtocolVersion getCurrentVersion() {
        String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        for (ProtocolVersion protocolVersion : ProtocolVersion.values())
            if (version.equals(protocolVersion.getKey()))
                return protocolVersion;
        return null;
    }


    private final String key;

    /**
     * Constructor.
     *
     * @param key The protocol version key.
     */
    ProtocolVersion(String key) {
        this.key = key;
    }

    /**
     * Gets the protocol version key.
     *
     * @return The protocol version key.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Checks if the protocol version is newer than the given version.
     *
     * @param version The version to check.
     * @return True if the protocol version is newer or equal to the given version.
     */
    public boolean isNewer(ProtocolVersion version) {
        return this.ordinal() > version.ordinal();
    }

    /**
     * Checks if the protocol version is newer than or equal to the given version.
     *
     * @param version The version to check.
     * @return True if the protocol version is older or equal to the given version.
     */
    public boolean isNewerOrEqual(ProtocolVersion version) {
        return this.ordinal() >= version.ordinal();
    }

    /**
     * Checks if the protocol version is older the given version.
     *
     * @param version The version to check.
     * @return True if the protocol version is older or equal to the given version.
     */
    public boolean isOlder(ProtocolVersion version) {
        return this.ordinal() < version.ordinal();
    }

    /**
     * Checks if the protocol version is older or equal to the given version.
     *
     * @param version The version to check.
     * @return True if the protocol version is older or equal to the given version.
     */
    public boolean isOlderOrEqual(ProtocolVersion version) {
        return this.ordinal() <= version.ordinal();
    }
}