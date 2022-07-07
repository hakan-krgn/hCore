package com.hakan.core.proxy.server;

import com.hakan.core.utils.Serializer;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Objects;

/**
 * SocketConnection class to
 * send and receive messages.
 */
public final class SocketConnection {

    /**
     * Registers the given socket
     * to the given listener.
     *
     * @param socket   The socket to register.
     * @param listener The listener to register the socket with.
     * @throws IOException If an I/O error occurs.
     */
    static void register(@Nonnull Socket socket, @Nonnull ServerListener listener) throws IOException {
        Objects.requireNonNull(socket, "socket cannot be null");
        Objects.requireNonNull(listener, "listener cannot be null");

        SocketConnection client = new SocketConnection(socket, listener);

        if (listener.hasClientByName(client.name)) {
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
     * new socket connection.
     *
     * @param socket   The socket to register.
     * @param listener The listener to register the socket with.
     * @throws IOException If an I/O error occurs.
     */
    private SocketConnection(@Nonnull Socket socket, @Nonnull ServerListener listener) throws IOException {
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
     * Gets the ip address of the client.
     *
     * @return The ip address of the client.
     */
    @Nonnull
    public String getIP() {
        return this.socket.getInetAddress().getHostAddress();
    }

    /**
     * Gets the socket of the client.
     *
     * @return The socket of the client.
     */
    @Nonnull
    public Socket getSocket() {
        return this.socket;
    }

    /**
     * Gets the input stream of the client.
     *
     * @return The input stream of the client.
     */
    @Nonnull
    public DataInputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * Gets the output stream of the client.
     *
     * @return The output stream of the client.
     */
    @Nonnull
    public DataOutputStream getOutputStream() {
        return this.outputStream;
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
     * Closes the client.
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
     * from the client.
     */
    private void listen() {
        new Thread(() -> {
            this.listener.register(this);

            while (!Thread.interrupted()) {
                try {
                    String message = this.inputStream.readUTF();

                    if (message.startsWith("message:")) {
                        String pureMessage = message.substring("message:".length());
                        if (this.listener.messageConsumer != null)
                            this.listener.messageConsumer.accept(this, pureMessage);
                    } else if (message.startsWith("object:")) {
                        String pureMessage = message.substring("object:".length());
                        if (this.listener.objectConsumer != null)
                            this.listener.objectConsumer.accept(this, Serializer.deserialize(pureMessage, Serializable.class));
                    } else if (message.startsWith("publish_message:")) {
                        String pureMessage = message.substring("publish_message:".length());
                        this.listener.publish(pureMessage);
                    } else if (message.startsWith("publish_object:")) {
                        String pureMessage = message.substring("publish_object:".length());
                        this.listener.publish(Serializer.deserialize(pureMessage, Serializable.class));
                    }
                } catch (IOException e) {
                    break;
                }
            }

            this.listener.unregister(this);
        }).start();
    }
}