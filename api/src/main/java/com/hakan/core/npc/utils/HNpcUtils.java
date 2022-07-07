package com.hakan.core.npc.utils;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.entity.HNpcEntity;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;

/**
 * HNpcUtils class to
 * create npc entity.
 */
public final class HNpcUtils {

    /**
     * Creates new HNpcEntity.
     *
     * @param hnpc HNPC instance.
     * @return HNpcEntity object.
     */
    @Nonnull
    public static HNpcEntity createEntity(HNPC hnpc) {
        try {
            Class<?> wrapper = Class.forName("com.hakan.core.npc.wrapper.HNpcEntity_" + HCore.getVersionString());
            Constructor<?> constructor = wrapper.getDeclaredConstructor(HNPC.class);
            return (HNpcEntity) constructor.newInstance(hnpc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}