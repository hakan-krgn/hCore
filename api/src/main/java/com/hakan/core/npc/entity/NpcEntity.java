package com.hakan.core.npc.entity;

import com.hakan.core.npc.Npc;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public interface NpcEntity {

    /**
     * Gets the id of nms entity.
     *
     * @return Entity id.
     */
    int getID();

    /**
     * Moves NPC to given location.
     *
     * @param speed    Speed.
     * @param to       Destination location.
     * @param runnable Runnable to run when npc reached the destination.
     */
    void walk(double speed,
              @Nonnull Location to,
              @Nonnull Runnable runnable);

    /**
     * Plays animation to given players.
     *
     * @param players   Players.
     * @param animation Animation.
     */
    void playAnimation(@Nonnull List<Player> players,
                       @Nonnull Npc.Animation animation);

    /**
     * Updates location.
     *
     * @param players Player list.
     */
    void updateLocation(@Nonnull List<Player> players);

    /**
     * Updates head rotation.
     *
     * @param players Player list.
     */
    void updateHeadRotation(@Nonnull List<Player> players);

    /**
     * Updates skin on NPC.
     *
     * @param players Player list.
     */
    void updateSkin(@Nonnull List<Player> players);

    /**
     * Equips NPC with items.
     *
     * @param players Player list.
     */
    void updateEquipments(@Nonnull List<Player> players);

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