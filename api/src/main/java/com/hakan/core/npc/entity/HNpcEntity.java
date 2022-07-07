package com.hakan.core.npc.entity;

import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.skin.HNPCSkin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public interface HNpcEntity {

    /**
     * Get the id of nms entity.
     *
     * @return Entity id.
     */
    int getID();

    /**
     * Moves NPC.
     *
     * @param to    Destination location.
     * @param speed Speed.
     */
    void walk(@Nonnull Location to, double speed, @Nonnull Runnable runnable);

    /**
     * Sets location.
     *
     * @param location Location.
     */
    void setLocation(@Nonnull Location location);

    /**
     * Sets skin on NPC.
     *
     * @param skin Skin.
     */
    void setSkin(@Nonnull HNPCSkin skin);

    /**
     * Equips NPC with items.
     *
     * @param slotType  Slot type. Ex: HAND_ITEM, LEGGINGS,
     * @param itemStack Item.
     */
    void setEquipment(@Nonnull HNPC.EquipmentType slotType, @Nonnull ItemStack itemStack);

    /**
     * Who sees NPC?
     *
     * @param players Player list.
     */
    void show(@Nonnull List<Player> players);

    /**
     * From whom should this NPC be hidden?
     *
     * @param players Player list.
     */
    void hide(@Nonnull List<Player> players);
}