package com.hakan.core.proxy.server;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * ServerListener class to
 * handle client connections.
 */
public class ServerListener {

    private final Map<String, SocketConnection> clients;
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
        this.clients = new HashMap<>();
        this.listen(port);
    }

    /**
     * Gets all the clients.
     *
     * @return The clients.
     */
    @Nonnull
    public final Map<String, SocketConnection> getClients() {
        return this.clients;
    }

    /**
     * Checks if the client is connected.
     *
     * @param name The client name.
     * @return True if the client exists, false otherwise.
     */
    public final boolean hasClientByName(@Nonnull String name) {
        return this.findClientByName(name).isPresent();
    }

    /**
     * Checks if the client is connected.
     *
     * @param ip The client ip.
     * @return True if the client exists, false otherwise.
     */
    public final boolean hasClientByIP(@Nonnull String ip) {
        return this.findClientByIP(ip).isPresent();
    }

    /**
     * Finds the client by name.
     *
     * @param client The client name.
     * @return Client as optional.
     */
    @Nonnull
    public final Optional<SocketConnection> findClientByName(@Nonnull String client) {
        Objects.requireNonNull(client, "client name cannot be null");
        return Optional.ofNullable(this.clients.get(client));
    }

    /**
     * Gets the client by name.
     *
     * @param client The client name.
     * @return Client.
     */
    @Nonnull
    public final SocketConnection getClientByName(@Nonnull String client) {
        Objects.requireNonNull(client, "client name cannot be null");
        return this.findClientByName(client).orElseThrow(() -> new IllegalArgumentException("client not found with name: " + client));
    }

    /**
     * Finds the client by name.
     *
     * @param ip The client ip.
     * @return Client as optional.
     */
    @Nonnull
    public final Optional<SocketConnection> findClientByIP(@Nonnull String ip) {
        Objects.requireNonNull(ip, "ip name cannot be null");
        return this.clients.values().stream().filter(c -> c.getIP().equals(ip)).findFirst();
    }

    /**
     * Gets the client by name.
     *
     * @param ip The client ip.
     * @return Client.
     */
    @Nonnull
    public final SocketConnection getClientByIP(@Nonnull String ip) {
        Objects.requireNonNull(ip, "ip name cannot be null");
        return this.findClientByIP(ip).orElseThrow(() -> new IllegalArgumentException("client not found with ip: " + ip));
    }

    /**
     * Sends the message to the client.
     *
     * @param client  The client.
     * @param message The message.
     */
    public final void send(@Nonnull SocketConnection client, @Nonnull String message) {
        Objects.requireNonNull(client, "client cannot be null").send(message);
    }

    /**
     * Sends the message to the client.
     *
     * @param clientName The client name.
     * @param message    The message.
     */
    public final void send(@Nonnull String clientName, @Nonnull String message) {
        this.getClientByName(clientName).send(message);
    }

    /**
     * Sends the message to the client.
     *
     * @param client       The client.
     * @param serializable The object.
     */
    public final void send(@Nonnull SocketConnection client, @Nonnull Serializable serializable) {
        Objects.requireNonNull(client, "client cannot be null").send(serializable);
    }

    /**
     * Sends the message to the client.
     *
     * @param clientName   The client name.
     * @param serializable The object.
     */
    public final void send(@Nonnull String clientName, @Nonnull Serializable serializable) {
        this.getClientByName(clientName).send(serializable);
    }

    /**
     * Sends the message to all clients.
     *
     * @param message The message.
     */
    public final void publish(@Nonnull String message) {
        this.clients.values().forEach(client -> client.send(message));
    }

    /**
     * Sends the message to all clients.
     *
     * @param serializable The message.
     */
    public final void publish(@Nonnull Serializable serializable) {
        this.clients.values().forEach(client -> client.send(serializable));
    }

    /**
     * Calls when the client connects.
     *
     * @param consumer The consumer.
     */
    public void whenConnected(@Nonnull Consumer<SocketConnection> consumer) {
        this.connectConsumer = Objects.requireNonNull(consumer, "consumer cannot be null");
    }

    /**
     * Calls when the client disconnects.
     *
     * @param consumer The consumer.
     */
    public void whenDisconnected(@Nonnull Consumer<SocketConnection> consumer) {
        this.disconnectConsumer = Objects.requireNonNull(consumer, "consumer cannot be null");
    }

    /**
     * Calls when the receive
     * message from client.
     *
     * @param consumer The consumer.
     */
    public void whenMessageReceived(@Nonnull BiConsumer<SocketConnection, String> consumer) {
        this.messageConsumer = Objects.requireNonNull(consumer, "consumer cannot be null");
    }

    /**
     * Calls when the receive
     * message from client.
     *
     * @param consumer The consumer.
     */
    public void whenObjectReceived(@Nonnull BiConsumer<SocketConnection, Serializable> consumer) {
        this.objectConsumer = Objects.requireNonNull(consumer, "consumer cannot be null");
    }

    /**
     * Registers the client.
     *
     * @param client The client.
     */
    public final void register(@Nonnull SocketConnection client) {
        Objects.requireNonNull(client, "client cannot be null");
        this.clients.put(client.getName(), client);

        if (this.connectConsumer != null)
            this.connectConsumer.accept(client);
    }

    /**
     * Unregisters the client.
     *
     * @param client The client.
     */
    public final void unregister(@Nonnull SocketConnection client) {
        Objects.requireNonNull(client, "client cannot be null");
        this.clients.remove(client.getName());

        if (this.disconnectConsumer != null)
            this.disconnectConsumer.accept(client);
    }


    /*
    START LISTEN
     */

    /**
     * Starts listening to the clients.
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