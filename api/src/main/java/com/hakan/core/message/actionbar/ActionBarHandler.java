package com.hakan.core.message.actionbar;

import org.bukkit.entity.Player;

public interface ActionBarHandler {

    void send(Player player, String text);
}