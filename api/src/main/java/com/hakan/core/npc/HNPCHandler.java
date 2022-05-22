package com.hakan.core.npc;

import com.hakan.core.HCore;
import org.bukkit.Location;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HNPCHandler {

    private static final List<HNPC> npcList = new ArrayList<>();

    public static void initialize() {
        HCore.asyncScheduler().every(10).run(() -> {
            for (HNPC hnpc : npcList) {
                hnpc.renderer.render();
            }
        });
    }

    public static HNPC create(Location location) {
        try {
            Class<?> aClass = Class.forName("com.hakan.core.npc.wrapper.HNPC_" + HCore.getVersionString());
            Constructor<?> constructor = aClass.getDeclaredConstructor(String.class, Location.class, List.class);
            HNPC npc = (HNPC) constructor.newInstance(("n" + System.currentTimeMillis()), location, Arrays.asList("saa", "as"));

            npcList.add(npc);

            return npc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}