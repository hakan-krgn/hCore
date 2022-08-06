package com.hakan.core.npc.utils;

import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.entity.HNpcEntity;
import com.hakan.core.utils.ReflectionUtils;

import javax.annotation.Nonnull;

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
        return ReflectionUtils.newInstance("com.hakan.core.npc.entity.HNpcEntity_%s", new Class[]{HNPC.class}, new Object[]{hnpc});
    }
}