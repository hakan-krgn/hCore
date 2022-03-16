package com.hakan.core.worldborder.border;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;

public interface HWorldBorder {

    void show(Player player);

    void showAll();

    void hide(Player player);

    void hideAll();

    void delete();

    Set<Player> getShownViewers();

    Location getCenter();

    void setCenter(Location location);

    HBorderColor getColor();

    void setColor(HBorderColor hBorderColor);

    double getSize();

    void setSize(double size);

    double getDamageAmount();

    void setDamageAmount(double damageAmount);

    double getDamageBuffer();

    void setDamageBuffer(double damageBuffer);

    int getWarningDistance();

    void setWarningDistance(int warningDistance);

    int getWarningTime();

    void setWarningTime(int warningTime);

    void update();

}