package com.hakan.core.npc;

/**
 * HNPC slot types.
 */

public enum HNPCSlotType {

    HAND_ITEM(0),
    OFF_HAND(0),
    HELMET(4),
    CHESTPLATE(3),
    LEGGINGS(2),
    BOOTS(1);

    private final int slot;

    /**
     * Sets slot.
     *
     * @param slot Slot number.
     */
    HNPCSlotType(int slot) {
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