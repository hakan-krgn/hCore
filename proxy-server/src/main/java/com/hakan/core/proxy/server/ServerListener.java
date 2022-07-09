package com.hakan.core.proxy.server;

import com.hakan.core.proxy.server.utils.Validate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * ServerListener class to
 * handle connection connections.
 */
public final class ServerListener {

    private final Map<String, SocketConnection> connections;
    private Consumer<SocketConnection> connectConsumer;
    private Consumer<SocketConnection> disconnectConsumer;
    BiConsumer<SocketConnection, String> messageConsumer;
    BiConsumer<SocketConnection, Serializable> objectConsumer;

    /**
     * Constructor to creates a new server listener.
     *
     * @param port The port to listen on.
     */
    public ServerListener(int port) {
        this.connections = new HashMap<>();
        this.listen(port);
    }

    /**
     * Gets all the connections.
     *
     * @return The connections.
     */
    @Nonnull
    public Map<String, SocketConnection> getConnections() {
        return this.connections;
    }

    /**
     * Checks if the connection is connected.
     *
     * @param name The connection name.
     * @return True if the connection exists, false otherwise.
     */
    public boolean hasConnectionByName(@Nonnull String name) {
        return this.findConnectionByName(name).isPresent();
    }

    /**
     * Checks if the connection is connected.
     *
     * @param ip The connection ip.
     * @return True if the connection exists, false otherwise.
     */
    public boolean hasConnectionByIP(@Nonnull String ip) {
        return this.findConnectionByIP(ip).isPresent();
    }

    /**
     * Finds the connection by name.
     *
     * @param connection The connection name.
     * @return connection as optional.
     */
    @Nonnull
    public Optional<SocketConnection> findConnectionByName(@Nonnull String connection) {
        Validate.notNull(connection, "connection name cannot be null");
        return Optional.ofNullable(this.connections.get(connection));
    }

    /**
     * Gets the connection by name.
     *
     * @param connectionName The connection name.
     * @return connection.
     */
    @Nonnull
    public SocketConnection getConnectionByName(@Nonnull String connectionName) {
        Validate.notNull(connectionName, "connection name cannot be null");
        return this.findConnectionByName(connectionName).orElseThrow(() -> new IllegalArgumentException("connection not found with name: " + connectionName));
    }

    /**
     * Finds the connection by ip.
     *
     * @param ip The connection ip.
     * @return connection as optional.
     */
    @Nonnull
    public Optional<SocketConnection> findConnectionByIP(@Nonnull String ip) {
        Validate.notNull(ip, "ip name cannot be null");
        return this.connections.values().stream().filter(c -> c.getIP().equals(ip)).findFirst();
    }

    /**
     * Gets the connection by ip.
     *
     * @param ip The connection ip.
     * @return Connection.
     */
    @Nonnull
    public SocketConnection getConnectionByIP(@Nonnull String ip) {
        Validate.notNull(ip, "ip name cannot be null");
        return this.findConnectionByIP(ip).orElseThrow(() -> new IllegalArgumentException("connection not found with ip: " + ip));
    }

    /**
     * Sends the message to the connection.
     *
     * @param connection The connection.
     * @param message    The message.
     */
    public void send(@Nonnull SocketConnection connection, @Nonnull String message) {
        Validate.notNull(connection, "connection cannot be null").send(message);
    }

    /**
     * Sends the message to the connection.
     *
     * @param connectionName The connection name.
     * @param message        The message.
     */
    public void send(@Nonnull String connectionName, @Nonnull String message) {
        this.getConnectionByName(connectionName).send(message);
    }

    /**
     * Sends the message to the connection.
     *
     * @param connection   The connection.
     * @param serializable The object.
     */
    public void send(@Nonnull SocketConnection connection, @Nonnull Serializable serializable) {
        Validate.notNull(connection, "connection cannot be null").send(serializable);
    }

    /**
     * Sends the message to the connection.
     *
     * @param connectionName The connection name.
     * @param serializable   The object.
     */
    public void send(@Nonnull String connectionName, @Nonnull Serializable serializable) {
        this.getConnectionByName(connectionName).send(serializable);
    }

    /**
     * Sends the message to all connections.
     *
     * @param message The message.
     */
    public void publish(@Nonnull String message) {
        this.connections.values().forEach(connection -> connection.send(message));
    }

    /**
     * Sends the message to all connections.
     *
     * @param serializable The message.
     */
    public void publish(@Nonnull Serializable serializable) {
        this.connections.values().forEach(connection -> connection.send(serializable));
    }

    /**
     * Calls when the connection connects.
     *
     * @param consumer The consumer.
     */
    public void whenConnected(@Nonnull Consumer<SocketConnection> consumer) {
        this.connectConsumer = Validate.notNull(consumer, "consumer cannot be null");
    }

    /**
     * Calls when the connection disconnects.
     *
     * @param consumer The consumer.
     */
    public void whenDisconnected(@Nonnull Consumer<SocketConnection> consumer) {
        this.disconnectConsumer = Validate.notNull(consumer, "consumer cannot be null");
    }

    /**
     * Calls when the receive
     * message from connection.
     *
     * @param consumer The consumer.
     */
    public void whenMessageReceived(@Nonnull BiConsumer<SocketConnection, String> consumer) {
        this.messageConsumer = Validate.notNull(consumer, "consumer cannot be null");
    }

    /**
     * Calls when the receive
     * message from connection.
     *
     * @param consumer The consumer.
     */
    public void whenObjectReceived(@Nonnull BiConsumer<SocketConnection, Serializable> consumer) {
        this.objectConsumer = Validate.notNull(consumer, "consumer cannot be null");
    }

    /**
     * Registers the connection.
     *
     * @param connection The connection.
     */
    public void register(@Nonnull SocketConnection connection) {
        Validate.notNull(connection, "connection cannot be null");
        this.connections.put(connection.getName(), connection);

        if (this.connectConsumer != null)
            this.connectConsumer.accept(connection);
    }

    /**
     * Unregisters the connection.
     *
     * @param connection The connection.
     */
    public void unregister(@Nonnull SocketConnection connection) {
        Validate.notNull(connection, "connection cannot be null");
        this.connections.remove(connection.getName());

        if (this.disconnectConsumer != null)
            this.disconnectConsumer.accept(connection);
    }


    /*
    START LISTEN
     */

    /**
     * Starts listening to the connections.
     */
    private void listen(int port) {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (!Thread.interrupted())
                    SocketConnection.register(serverSocket.accept(), this);
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}