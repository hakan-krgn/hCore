package com.hakan.core.npc.entity;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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
     * Moves NPC to given location.
     *
     * @param speed Speed.
     * @param to    Destination location.
     */
    void walk(double speed, @Nonnull Location to, @Nonnull Runnable runnable);

    /**
     * Sets location.
     */
    void updateLocation();

    /**
     * Sets skin on NPC.
     */
    void updateSkin();

    /**
     * Equips NPC with items.
     */
    void updateEquipments();

    /**
     * Shows NPC to players.
     *
     * @param players Player list.
     */
    void show(@Nonnull List<Player> players);

    /**
     * Hides NPC to players.
     *
     * @param players Player list.
     */
    void hide(@Nonnull List<Player> players);
}