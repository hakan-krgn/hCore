package com.hakan.core.proxy.client;

import com.hakan.core.proxy.client.utils.Serializer;
import com.hakan.core.proxy.client.utils.Validate;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * SocketConnection class to send and
 * receive messages from server.
 */
public final class SocketConnection {

    /**
     * Connects to the given server and register
     * itself to side of server.
     *
     * @param name The name of the connection.
     * @param ip   The ip of the server.
     * @param port The port of the server.
     * @return The connected server.
     * @throws RuntimeException If an I/O error occurs.
     */
    @Nonnull
    public static SocketConnection create(@Nonnull String name, @Nonnull String ip, int port) {
        try {
            Validate.notNull(name, "name cannot be null");
            Validate.notNull(ip, "ip cannot be null");

            SocketConnection connection = new SocketConnection(new Socket(ip, port));
            connection.outputStream.writeUTF(name);

            byte result = connection.inputStream.readByte();
            Validate.isTrue(result == 1, "name already in use!");
            Validate.isTrue(result == 2, "input stream is broken!");

            connection.listen();
            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final List<Runnable> disconnectRunnable;
    private final List<Consumer<String>> messageConsumer;
    private final List<Consumer<Serializable>> objectConsumer;

    /**
     * Constructor to creates a
     * new connected server.
     *
     * @param socket The socket to register.
     * @throws IOException If an I/O error occurs.
     */
    private SocketConnection(@Nonnull Socket socket) throws IOException {
        this.socket = Validate.notNull(socket, "socket cannot be null");
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.disconnectRunnable = new ArrayList<>();
        this.messageConsumer = new ArrayList<>();
        this.objectConsumer = new ArrayList<>();
    }

    /**
     * Gets the ip address of the connection.
     *
     * @return The ip address of the connection.
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
        this.messageConsumer.add(Validate.notNull(consumer, "consumer cannot be null"));
    }

    /**
     * Calls the given consumer when
     * a message is received.
     *
     * @param consumer The consumer to call.
     */
    public void whenObjectReceived(@Nonnull Consumer<Serializable> consumer) {
        this.objectConsumer.add(Validate.notNull(consumer, "consumer cannot be null"));
    }

    /**
     * Calls the given runnable when
     * the connection is disconnected.
     *
     * @param runnable The runnable to call.
     */
    public void whenDisconnected(@Nonnull Runnable runnable) {
        this.disconnectRunnable.add(Validate.notNull(runnable, "runnable cannot be null"));
    }

    /**
     * Calls the consumers when
     * a message is received.
     *
     * @param message The message to receive.
     */
    public void onMessageReceive(@Nonnull String message) {
        Validate.notNull(message, "message cannot be null!");
        this.messageConsumer.forEach(consumer -> consumer.accept(message));
    }

    /**
     * Calls the consumers when
     * a message is received.
     *
     * @param object The object to receive.
     */
    public void onObjectReceive(@Nonnull Serializable object) {
        Validate.notNull(object, "object cannot be null!");
        this.objectConsumer.forEach(consumer -> consumer.accept(object));
    }

    /**
     * Calls the consumers when
     * the connection is disconnected.
     */
    public void onDisconnect() {
        this.disconnectRunnable.forEach(Runnable::run);
    }

    /**
     * Sends a message to the server.
     *
     * @param message The message to send.
     */
    public void send(@Nonnull String message) {
        try {
            Validate.notNull(message, "message cannot be null");
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
            Validate.notNull(serializable, "serializable object cannot be null");
            this.outputStream.writeUTF("object:" + Serializer.serialize(serializable));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Publishes a message to all connections which
     * connected to server.
     *
     * @param message The message to send.
     */
    public void publish(@Nonnull String message) {
        try {
            Validate.notNull(message, "message cannot be null");
            this.outputStream.writeUTF("publish_message:" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Publishes an object to all connections which
     * connected to server.
     *
     * @param serializable The object to send.
     */
    public void publish(@Nonnull Serializable serializable) {
        try {
            Validate.notNull(serializable, "serializable object cannot be null");
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
                        this.onMessageReceive(pureMessage);
                    } else if (message.startsWith("object:")) {
                        String pureMessage = message.substring("object:".length());
                        this.onObjectReceive(Serializer.deserialize(pureMessage, Serializable.class));
                    }
                } catch (IOException e) {
                    this.onDisconnect();
                    return;
                }
            }
        }).start();
    }
}