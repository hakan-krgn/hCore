package com.hakan.core.npc.types;

/**
 * HNPC slot types.
 */

public enum HNPCEquipmentType {

    HAND_ITEM(0),
    OFF_HAND(0),
    HELMET(3),
    CHESTPLATE(2),
    LEGGINGS(1),
    BOOTS(0);

    private final int slot;

    /**
     * Constructor with slot.
     *
     * @param slot Slot number.
     */
    HNPCEquipmentType(int slot) {
        this.slot = slot;
    }

    /**
     * Gets slot.
     *
     * @return slot.
     */
    public int getSlot() {
        return this.slot;
    }
}