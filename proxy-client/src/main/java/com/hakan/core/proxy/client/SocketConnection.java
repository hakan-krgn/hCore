package com.hakan.core.proxy.client;

import com.hakan.core.proxy.client.utils.Serializer;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * SocketConnection class to send and
 * receive messages from server.
 */
public final class SocketConnection {

    /**
     * Connects to the given server and register
     * itself to server-side.
     *
     * @param name The name of the client.
     * @param ip   The ip of the server.
     * @param port The port of the server.
     * @return The connected server.
     * @throws RuntimeException If an I/O error occurs.
     */
    @Nonnull
    public static SocketConnection create(@Nonnull String name, @Nonnull String ip, int port) {
        try {
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(ip, "ip cannot be null");

            SocketConnection socket = new SocketConnection(new Socket(ip, port));
            socket.outputStream.writeUTF(name);

            byte result = socket.inputStream.readByte();
            if (result == 1)
                throw new IllegalArgumentException("name already in use!");
            else if (result == 2)
                throw new IllegalArgumentException("input stream is broken!");

            socket.listen();
            return socket;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private Runnable disconnectRunnable;
    private Consumer<String> messageConsumer;
    private Consumer<Serializable> objectConsumer;

    /**
     * Constructor to creates a
     * new connected server.
     *
     * @param socket The socket to register.
     * @throws IOException If an I/O error occurs.
     */
    private SocketConnection(@Nonnull Socket socket) throws IOException {
        this.socket = Objects.requireNonNull(socket, "socket cannot be null");
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Gets the ip address of the client.
     *
     * @return The ip address of the client.
     */
    @Nonnull
    public String getIP() {
        return this.socket.getInetAddress().getHostAddress();
    }

    /**
     * Gets th socket.
     *
     * @return The socket.
     */
    @Nonnull
    public Socket getSocket() {
        return this.socket;
    }

    /**
     * Gets the input stream.
     *
     * @return The input stream.
     */
    @Nonnull
    public DataInputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * Gets the output stream.
     *
     * @return The output stream.
     */
    @Nonnull
    public DataOutputStream getOutputStream() {
        return this.outputStream;
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
     * Calls the given consumer when
     * a message is received.
     *
     * @param consumer The consumer to call.
     */
    public void whenObjectReceived(@Nonnull Consumer<Serializable> consumer) {
        this.objectConsumer = Objects.requireNonNull(consumer, "consumer cannot be null");
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
    public void send(@Nonnull String message) {
        try {
            Objects.requireNonNull(message, "message cannot be null");
            this.outputStream.writeUTF("message:" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a serializable object
     * to server.
     *
     * @param serializable The object to send.
     */
    public void send(@Nonnull Serializable serializable) {
        try {
            Objects.requireNonNull(serializable, "serializable object cannot be null");
            this.outputStream.writeUTF("object:" + Serializer.serialize(serializable));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Publishes a message to all clients which
     * connected to server.
     *
     * @param message The message to send.
     */
    public void publish(@Nonnull String message) {
        try {
            Objects.requireNonNull(message, "message cannot be null");
            this.outputStream.writeUTF("publish_message:" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Publishes an object to all clients which
     * connected to server.
     *
     * @param serializable The object to send.
     */
    public void publish(@Nonnull Serializable serializable) {
        try {
            Objects.requireNonNull(serializable, "serializable object cannot be null");
            this.outputStream.writeUTF("publish_object:" + Serializer.serialize(serializable));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the server.
     */
    public void close() {
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

                    if (message.startsWith("message:")) {
                        String pureMessage = message.substring("message:".length());
                        if (this.messageConsumer != null)
                            this.messageConsumer.accept(pureMessage);
                    } else if (message.startsWith("object:")) {
                        String pureMessage = message.substring("object:".length());
                        if (this.objectConsumer != null)
                            this.objectConsumer.accept(Serializer.deserialize(pureMessage, Serializable.class));
                    }
                } catch (IOException e) {
                    if (this.disconnectRunnable != null)
                        this.disconnectRunnable.run();
                    return;
                }
            }
        }).start();
    }
}