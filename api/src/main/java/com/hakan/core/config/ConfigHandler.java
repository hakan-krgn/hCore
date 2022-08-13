package com.hakan.core.config;

import com.hakan.core.HCore;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.ArrayList;
import java.util.List;

public final class ConfigHandler {

    private static final List<ConfigContainer<?>> configs = new ArrayList<>();

    public static void initialize() {
        HCore.registerEvent(PluginDisableEvent.class)
                .consume(event -> saveAll())
                .register();
    }

    public static void saveAll() {
        configs.forEach(ConfigContainer::save);
    }

    public static List<ConfigContainer<?>> getConfigs() {
        return configs;
    }

}