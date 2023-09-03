package com.hakan.core.packet.utils;

import com.hakan.core.utils.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import javax.annotation.Nonnull;

/**
 * PacketUtils class.
 */
public final class PacketUtils {

    /**
     * Calls given event
     * even if its async.
     *
     * @param event Event.
     */
    public static void callEvent(@Nonnull Event event) {
        Validate.notNull(event, "event cannot be null!");

        HandlerList handlers = event.getHandlers();
        RegisteredListener[] listeners = handlers.getRegisteredListeners();

        for (RegisteredListener registration : listeners) {
            try {
                registration.callEvent(event);
            } catch (EventException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
