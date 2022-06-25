package com.hakan.core.proxy.client.connection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * ConnectedServer class to send and
 * receive messages from server.
 */
public final class ConnectedServer {

    /**
     * Connects to the given server and register
     * itself to server-side.
     *
     * @param name The name of the client.
     * @param ip   The ip of the server.
     * @param port The port of the server.
     * @return The connected server.
     * @throws IOException If an I/O error occurs.
     */
    public static ConnectedServer connect(@Nonnull String name, @Nonnull String ip, int port) throws IOException {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(ip, "ip cannot be null");

        ConnectedServer server = new ConnectedServer(new Socket(ip, port));
        server.outputStream.writeUTF(name);

        byte result = server.inputStream.readByte();
        if (result == 1)
            throw new IllegalArgumentException("name already in use!");
        else if (result == 2)
            throw new IllegalArgumentException("input stream is broken!");

        server.listen();
        return server;
    }


    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    private Runnable disconnectRunnable;
    private Consumer<String> messageConsumer;
    private String result;

    /**
     * Constructor to creates a
     * new connected server.
     *
     * @param socket The socket to register.
     * @throws IOException If an I/O error occurs.
     */
    private ConnectedServer(@Nonnull Socket socket) throws IOException {
        this.socket = Objects.requireNonNull(socket, "socket cannot be null");
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Calls the given consumer when
     * a message is received.
     *
     * @param consumer The consumer to call.
     */
    public void whenMessageReceived(@Nonnull Consumer<String> consumer) {
        this.messageConsumer = Objects.requireNonNull(consumer, "consumer cannot be null");
    }

    /**
     * Calls the given runnable when
     * the client is disconnected.
     *
     * @param runnable The runnable to call.
     */
    public void whenDisconnected(@Nonnull Runnable runnable) {
        this.disconnectRunnable = Objects.requireNonNull(runnable, "runnable cannot be null");
    }

    /**
     * Sends a message to the server.
     *
     * @param message The message to send.
     */
    @Nullable
    public String send(@Nonnull String message) {
        try {
            Objects.requireNonNull(message, "message cannot be null");
            this.outputStream.writeUTF(message);

            while (this.result == null)
                Thread.sleep(1);
            String result = this.result;
            this.result = null;

            return result.equals("null") ? null : result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Closes the server.
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
     */
    private void listen() {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    String message = this.inputStream.readUTF();
                    if (message.startsWith("%%proxy_message_result:"))
                        this.result = message.substring(23);
                    else if (this.messageConsumer != null)
                        this.messageConsumer.accept(message);
                } catch (IOException e) {
                    if (this.disconnectRunnable != null)
                        this.disconnectRunnable.run();
                    return;
                }
            }
        }).start();
    }
}