package com.hakan.core.proxy.server;

import com.hakan.core.proxy.server.connection.ConnectedClient;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;

/**
 * ServerProvider class to listen to a
 * port and register connections.
 */
public final class ServerProvider {

    /**
     * Listens on the given port and register
     * the connections with the given listener.
     *
     * @param port     The port to listen on.
     * @param listener The listener to register the connections with.
     */
    public static void listen(int port, @Nonnull ServerListener listener) {
        Objects.requireNonNull(listener, "listener cannot be null");

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (!Thread.interrupted())
                    ConnectedClient.register(serverSocket.accept(), listener);
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}