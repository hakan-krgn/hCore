package com.hakan.core.proxy.server.connection;

import com.hakan.core.proxy.server.ServerListener;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

/**
 * ConnectedClient class to
 * send and receive messages.
 */
public final class ConnectedClient {

    /**
     * Registers the given socket
     * to the given listener.
     *
     * @param socket   The socket to register.
     * @param listener The listener to register the socket with.
     * @throws IOException If an I/O error occurs.
     */
    public static void register(@Nonnull Socket socket, @Nonnull ServerListener listener) throws IOException {
        Objects.requireNonNull(socket, "socket cannot be null");
        Objects.requireNonNull(listener, "listener cannot be null");

        ConnectedClient client = new ConnectedClient(socket, listener);

        if (listener.hasClient(client.name)) {
            client.outputStream.writeByte(1);
            return;
        } else if (client.socket.isInputShutdown()) {
            client.outputStream.writeByte(2);
            return;
        }

        client.outputStream.writeByte(0);
        client.listen();
    }


    private final String name;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final ServerListener listener;

    /**
     * Constructor to creates a
     * new connected client.
     *
     * @param socket   The socket to register.
     * @param listener The listener to register the socket with.
     * @throws IOException If an I/O error occurs.
     */
    private ConnectedClient(@Nonnull Socket socket, @Nonnull ServerListener listener) throws IOException {
        this.socket = Objects.requireNonNull(socket, "socket cannot be null");
        this.listener = Objects.requireNonNull(listener, "listener cannot be null");
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.name = this.inputStream.readUTF();
    }

    /**
     * Gets the name of the client.
     *
     * @return The name of the client.
     */
    @Nonnull
    public String getName() {
        return this.name;
    }

    /**
     * Sends a message to the client.
     *
     * @param message The message to send.
     */
    public void send(@Nonnull String message) {
        try {
            Objects.requireNonNull(message, "message cannot be null");
            this.outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the client.
     */
    public void disconnect() {
        try {
            this.inputStream.close();
            this.outputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    LISTENER
     */

    /**
     * Listens for messages
     * from the client.
     */
    private void listen() {
        new Thread(() -> {
            this.listener.register(this);
            while (!Thread.interrupted()) {
                try {
                    String message = this.inputStream.readUTF();
                    String result = this.listener.onMessageReceive(this, message);
                    this.outputStream.writeUTF("%%proxy_message_result: " + result);
                } catch (IOException e) {
                    break;
                }
            }
            this.listener.unregister(this);
        }).start();
    }
}