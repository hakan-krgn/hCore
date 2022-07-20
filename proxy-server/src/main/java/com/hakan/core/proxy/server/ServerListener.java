package com.hakan.core.proxy.server;

import com.hakan.core.proxy.server.utils.Validate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * ServerListener class to
 * handle connection connections.
 */
public class ServerListener {

    protected final Map<String, SocketConnection> connections;
    protected final List<Consumer<SocketConnection>> connectConsumers;
    protected final List<Consumer<SocketConnection>> disconnectConsumers;
    protected final List<BiConsumer<SocketConnection, String>> messageConsumers;
    protected final List<BiConsumer<SocketConnection, Serializable>> objectConsumers;

    /**
     * Constructor to creates a new server listener.
     *
     * @param port The port to listen on.
     */
    public ServerListener(int port) {
        this.connections = new HashMap<>();
        this.connectConsumers = new ArrayList<>();
        this.disconnectConsumers = new ArrayList<>();
        this.messageConsumers = new ArrayList<>();
        this.objectConsumers = new ArrayList<>();
        this.listen(port);
    }

    /**
     * Gets all the connections.
     *
     * @return The connections.
     */
    @Nonnull
    public final Map<String, SocketConnection> getConnections() {
        return this.connections;
    }

    /**
     * Checks if the connection is connected.
     *
     * @param name The connection name.
     * @return True if the connection exists, false otherwise.
     */
    public final boolean hasConnectionByName(@Nonnull String name) {
        return this.findConnectionByName(name).isPresent();
    }

    /**
     * Checks if the connection is connected.
     *
     * @param ip The connection ip.
     * @return True if the connection exists, false otherwise.
     */
    public final boolean hasConnectionByIP(@Nonnull String ip) {
        return this.findConnectionByIP(ip).isPresent();
    }

    /**
     * Finds the connection by name.
     *
     * @param connection The connection name.
     * @return connection as optional.
     */
    @Nonnull
    public final Optional<SocketConnection> findConnectionByName(@Nonnull String connection) {
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
    public final SocketConnection getConnectionByName(@Nonnull String connectionName) {
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
    public final Optional<SocketConnection> findConnectionByIP(@Nonnull String ip) {
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
    public final SocketConnection getConnectionByIP(@Nonnull String ip) {
        Validate.notNull(ip, "ip name cannot be null");
        return this.findConnectionByIP(ip).orElseThrow(() -> new IllegalArgumentException("connection not found with ip: " + ip));
    }

    /**
     * Sends the message to the connection.
     *
     * @param connection The connection.
     * @param message    The message.
     */
    public final void send(@Nonnull SocketConnection connection, @Nonnull String message) {
        Validate.notNull(connection, "connection cannot be null").send(message);
    }

    /**
     * Sends the message to the connection.
     *
     * @param connectionName The connection name.
     * @param message        The message.
     */
    public final void send(@Nonnull String connectionName, @Nonnull String message) {
        this.getConnectionByName(connectionName).send(message);
    }

    /**
     * Sends the message to the connection.
     *
     * @param connection   The connection.
     * @param serializable The object.
     */
    public final void send(@Nonnull SocketConnection connection, @Nonnull Serializable serializable) {
        Validate.notNull(connection, "connection cannot be null").send(serializable);
    }

    /**
     * Sends the message to the connection.
     *
     * @param connectionName The connection name.
     * @param serializable   The object.
     */
    public final void send(@Nonnull String connectionName, @Nonnull Serializable serializable) {
        this.getConnectionByName(connectionName).send(serializable);
    }

    /**
     * Sends the message to all connections.
     *
     * @param message The message.
     */
    public final void publish(@Nonnull String message) {
        this.connections.values().forEach(connection -> connection.send(message));
    }

    /**
     * Sends the message to all connections.
     *
     * @param serializable The message.
     */
    public final void publish(@Nonnull Serializable serializable) {
        this.connections.values().forEach(connection -> connection.send(serializable));
    }

    /**
     * Calls when the connection connects.
     *
     * @param consumer The consumer.
     */
    public final void whenConnected(@Nonnull Consumer<SocketConnection> consumer) {
        this.connectConsumers.add(Validate.notNull(consumer, "consumer cannot be null"));
    }

    /**
     * Calls when the connection disconnects.
     *
     * @param consumer The consumer.
     */
    public final void whenDisconnected(@Nonnull Consumer<SocketConnection> consumer) {
        this.disconnectConsumers.add(Validate.notNull(consumer, "consumer cannot be null"));
    }

    /**
     * Calls when the receive
     * message from connection.
     *
     * @param consumer The consumer.
     */
    public final void whenMessageReceived(@Nonnull BiConsumer<SocketConnection, String> consumer) {
        this.messageConsumers.add(Validate.notNull(consumer, "consumer cannot be null"));
    }

    /**
     * Calls when the receive
     * message from connection.
     *
     * @param consumer The consumer.
     */
    public final void whenObjectReceived(@Nonnull BiConsumer<SocketConnection, Serializable> consumer) {
        this.objectConsumers.add(Validate.notNull(consumer, "consumer cannot be null"));
    }

    /**
     * Calls when the connection connect.
     *
     * @param connection The connection.
     */
    public void onConnect(@Nonnull SocketConnection connection) {
        Validate.notNull(connection, "connection cannot be null!");
        this.connectConsumers.forEach(consumer -> consumer.accept(connection));
    }

    /**
     * Calls when the connection disconnect.
     *
     * @param connection The connection.
     */
    public void onDisconnect(@Nonnull SocketConnection connection) {
        Validate.notNull(connection, "connection cannot be null!");
        this.disconnectConsumers.forEach(consumer -> consumer.accept(connection));
    }

    /**
     * Calls when the connection receive a message.
     *
     * @param connection The connection.
     * @param message    The message.
     */
    public void onMessageReceive(@Nonnull SocketConnection connection, @Nonnull String message) {
        Validate.notNull(connection, "connection cannot be null!");
        Validate.notNull(message, "message cannot be null!");
        this.messageConsumers.forEach(consumer -> consumer.accept(connection, message));
    }

    /**
     * Calls when the connection receive an object.
     *
     * @param connection   The connection.
     * @param serializable The object.
     */
    public void onObjectReceive(@Nonnull SocketConnection connection, @Nonnull Serializable serializable) {
        Validate.notNull(connection, "connection cannot be null!");
        Validate.notNull(serializable, "serializable cannot be null!");
        this.objectConsumers.forEach(consumer -> consumer.accept(connection, serializable));
    }

    /**
     * Registers the connection.
     *
     * @param connection The connection.
     */
    public final void register(@Nonnull SocketConnection connection) {
        Validate.notNull(connection, "connection cannot be null");
        this.connections.put(connection.getName(), connection);
        this.onConnect(connection);
    }

    /**
     * Unregisters the connection.
     *
     * @param connection The connection.
     */
    public final void unregister(@Nonnull SocketConnection connection) {
        Validate.notNull(connection, "connection cannot be null");
        this.connections.remove(connection.getName());
        this.onDisconnect(connection);
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