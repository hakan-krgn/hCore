package com.hakan.core.ui.sign.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.protocol.ProtocolVersion;
import com.hakan.core.ui.sign.SignGui;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;

/**
 * SignWrapper is a class that
 * handles nms methods of sign gui.
 */
public abstract class SignWrapper {

    protected static final int LOWEST_Y_AXIS = (HCore.getProtocolVersion().isNewerOrEqual(ProtocolVersion.v1_18_R1)) ? -64 : 0;



    protected final SignGui signGui;

    /**
     * Constructor of SignWrapper.
     *
     * @param signGui SignWrapper.
     */
    public SignWrapper(@Nonnull SignGui signGui) {
        this.signGui = Validate.notNull(signGui, "sign gui cannot be null!");
    }


    /**
     * Opens the gui to player.
     */
    public abstract void open();

    /**
     * Listens player packet if
     * packet is a sign packet.
     *
     * @param <T>    Packet type.
     * @param packet Packet.
     * @return List of lines.
     */
    @Nonnull
    public abstract <T> String[] inputReceive(@Nonnull T packet);
}