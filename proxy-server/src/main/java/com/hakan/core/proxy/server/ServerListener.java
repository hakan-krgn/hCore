package com.hakan.core.proxy.server;

import com.hakan.core.proxy.server.connection.ConnectedClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * ServerListener class to
 * handle client connections.
 */
public abstract class ServerListener {

    private final Map<String, ConnectedClient> clients = new HashMap<>();

    /**
     * Gets all the clients.
     *
     * @return The clients.
     */
    @Nonnull
    public final Map<String, ConnectedClient> getClients() {
        return clients;
    }

    /**
     * Checks if the client is connected.
     *
     * @param name The client name.
     * @return True if the client exists, false otherwise.
     */
    public final boolean hasClient(@Nonnull String name) {
        Objects.requireNonNull(name, "name cannot be null");
        return this.clients.containsKey(name);
    }

    /**
     * Finds the client by name.
     *
     * @param client The client name.
     * @return Client as optional.
     */
    @Nonnull
    public final Optional<ConnectedClient> findClientByName(@Nonnull String client) {
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
    public final ConnectedClient getClientByName(@Nonnull String client) {
        Objects.requireNonNull(client, "client name cannot be null");
        return this.findClientByName(client).orElseThrow(() -> new IllegalArgumentException("client not found with name: " + client));
    }

    /**
     * Registers the client.
     *
     * @param client The client.
     */
    public final void register(@Nonnull ConnectedClient client) {
        Objects.requireNonNull(client, "client cannot be null");
        this.clients.put(client.getName(), client);
        this.onConnect(client);
    }

    /**
     * Unregisters the client.
     *
     * @param client The client.
     */
    public final void unregister(@Nonnull ConnectedClient client) {
        Objects.requireNonNull(client, "client cannot be null");
        this.clients.remove(client.getName());
        this.onDisconnect(client);
    }

    /**
     * Sends the message to the client.
     *
     * @param client  The client.
     * @param message The message.
     */
    public final void send(@Nonnull ConnectedClient client, @Nonnull String message) {
        Objects.requireNonNull(client, "client cannot be null");
        Objects.requireNonNull(message, "message cannot be null");
        client.send(message);
    }

    /**
     * Sends the message to the client.
     *
     * @param client  The client name.
     * @param message The message.
     */
    public final void send(@Nonnull String client, @Nonnull String message) {
        this.getClientByName(client).send(message);
    }

    /**
     * Sends the message to all clients.
     *
     * @param message The message.
     */
    public final void sendAll(@Nonnull String message) {
        Objects.requireNonNull(message, "message cannot be null");
        this.clients.values().forEach(client -> client.send(message));
    }


    /**
     * Calls when the client connects.
     *
     * @param client The client.
     */
    public void onConnect(@Nonnull ConnectedClient client) {

    }

    /**
     * Calls when the client disconnects.
     *
     * @param client The client.
     */
    public void onDisconnect(@Nonnull ConnectedClient client) {

    }

    /**
     * Calls when the receive
     * message from client.
     *
     * @param client  The client.
     * @param message The message.
     */
    @Nullable
    public String onMessageReceive(@Nonnull ConnectedClient client, @Nonnull String message) {
        return null;
    }
}