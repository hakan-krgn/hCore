package com.hakan.core.database;

import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * DatabaseHandler class to
 * handle database operations.
 */
@SuppressWarnings({"unchecked"})
public final class DatabaseHandler {

    private static final Map<Class<?>, DatabaseProvider<?>> providers = new HashMap<>();

    /**
     * Registers database provider.
     *
     * @param objectClass Database object class.
     * @param provider    Database provider.
     * @param <T>         Database object class.
     */
    @Nonnull
    public static <T extends DatabaseObject> DatabaseProvider<T> registerProvider(@Nonnull Class<T> objectClass, @Nonnull DatabaseProvider<T> provider) {
        Validate.notNull(objectClass, "objectClass cannot be null!");
        Validate.notNull(provider, "provider cannot be null!");
        providers.put(objectClass, provider);
        return provider;
    }

    /**
     * Finds database provider.
     *
     * @param objectClass Database object class.
     * @param <T>         Database object class.
     * @return Database provider.
     */
    @Nonnull
    public static <T extends DatabaseObject> Optional<DatabaseProvider<T>> findProvider(@Nonnull Class<T> objectClass) {
        Validate.notNull(objectClass, "objectClass cannot be null!");
        return providers.containsKey(objectClass) ? Optional.of((DatabaseProvider<T>) providers.get(objectClass)) : Optional.empty();
    }

    /**
     * Gets database provider.
     *
     * @param objectClass Database object class.
     * @param <T>         Database object class.
     * @return Database provider.
     */
    @Nonnull
    public static <T extends DatabaseObject> DatabaseProvider<T> getProvider(@Nonnull Class<T> objectClass) {
        Validate.notNull(objectClass, "objectClass cannot be null!");
        return findProvider(objectClass).orElseThrow(() -> new IllegalArgumentException("no provider found for " + objectClass.getName()));
    }
}