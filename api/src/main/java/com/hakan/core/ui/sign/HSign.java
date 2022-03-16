package com.hakan.core.ui.sign;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public abstract class HSign {

    protected Material type;
    protected String[] lines;
    protected Consumer<String[]> consumer;

    public HSign(Material type, String... lines) {
        this.type = type;
        this.lines = lines;
    }

    public final Material getType() {
        return this.type;
    }

    public final HSign setType(Material type) {
        this.type = type;
        return this;
    }

    public final String[] getLines() {
        return this.lines;
    }

    public final HSign setLines(String[] lines) {
        this.lines = lines;
        return this;
    }

    public final HSign onComplete(Consumer<String[]> consumer) {
        this.consumer = consumer;
        return this;
    }

    public abstract void open(Player player);

    public abstract <T> void listen(Player player, T packet);
}