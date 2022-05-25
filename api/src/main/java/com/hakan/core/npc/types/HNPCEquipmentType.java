package com.hakan.core.npc.types;

/**
 * HNPC slot types.
 */

public enum HNPCEquipmentType {

    MAINHAND(0, "mainhand"),
    OFFHAND(0, "offhand"),
    FEET(4, "feet"),
    LEGS(3, "legs"),
    CHEST(2, "chest"),
    HEAD(1, "head"),
    ;

    private final int slot;
    private final String value;

    /**
     * Constructor with slot.
     *
     * @param slot Slot number.
     */
    HNPCEquipmentType(int slot, String value) {
        this.slot = slot;
        this.value = value;
    }

    /**
     * Gets slot.
     *
     * @return slot.
     */
    public int getSlot() {
        return this.slot;
    }

    /**
     * Gets value for higher versions.
     *
     * @return value.
     */
    public String getValue() {
        return this.value;
    }
}