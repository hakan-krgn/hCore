package com.hakan.core.ui.anvil.wrapper;

import com.hakan.core.ui.anvil.AnvilGui;
import com.hakan.core.utils.Validate;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

/**
 * AnvilGui is a class that
 * handles nms methods of anvil gui.
 */
public abstract class AnvilWrapper {

    protected final AnvilGui anvilGui;

    /**
     * Constructor of AnvilGui.
     *
     * @param anvilGui AnvilGui.
     */
    public AnvilWrapper(@Nonnull AnvilGui anvilGui) {
        this.anvilGui = Validate.notNull(anvilGui, "anvil gui cannot be null!");
    }


    /**
     * Gets as bukkit inventory.
     *
     * @return Bukkit inventory.
     */
    @Nonnull
    public abstract Inventory toInventory();

    /**
     * Opens anvil gui to player.
     */
    public abstract void open();

    /**
     * Closes the anvil gui of player.
     */
    public abstract void close();
}