package com.hakan.core.proxy.client;

import com.hakan.core.proxy.client.connection.ConnectedServer;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;

/**
 * ClientProvider class to connect to a
 * server and send and receive messages.
 */
public final class ClientProvider {

    /**
     * Connects to the given server and register
     * the connections to connected server.
     *
     * @param ip   The ip of the server.
     * @param port The port of the server.
     */
    @Nonnull
    public static ConnectedServer connect(@Nonnull String name, @Nonnull String ip, int port) {
        try {
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(ip, "ip cannot be null");
            return ConnectedServer.connect(name, ip, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}