package com.hakan.core.utils;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Optional;

/**
 * Serializer class to
 * serialize any object to string
 * and deserialize any string to object.
 */
@SuppressWarnings({"unchecked"})
public final class Serializer {

    /**
     * Serializes object.
     *
     * @param object Object.
     * @return Serialized string as optional.
     */
    @Nonnull
    public synchronized static Optional<String> serializeSafe(@Nonnull Object object) {
        try {
            Validate.notNull(object, "object cannot be null!");

            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

            os.writeObject(object);
            os.flush();

            return Optional.of(Base64.getEncoder().encodeToString(io.toByteArray()));
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Serializes object.
     *
     * @param object Object.
     * @return Serialized string.
     */
    @Nonnull
    public synchronized static String serialize(@Nonnull Object object) {
        return Serializer.serializeSafe(object).orElseThrow(() -> new NullPointerException("there is a problem with serializing!"));
    }

    /**
     * Deserializes object.
     *
     * @param serializedText Text that want to deserialize.
     * @param clazz          Object type class.
     * @param <T>            Object type.
     * @return Deserialized object as optional.
     */
    @Nonnull
    public synchronized static <T> Optional<T> deserializeSafe(@Nonnull String serializedText, @Nonnull Class<T> clazz) {
        try {
            Validate.notNull(serializedText, "serializeText cannot be null!");
            Validate.notNull(clazz, "object type cannot be null!");

            byte[] decoded_object = Base64.getDecoder().decode(serializedText);

            ByteArrayInputStream in = new ByteArrayInputStream(decoded_object);
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);

            return Optional.ofNullable((T) is.readObject());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Deserializes object.
     *
     * @param serializedText Text that want to deserialize.
     * @param clazz          Object type class.
     * @param <T>            Object type.
     * @return Deserialized object as optional.
     */
    @Nonnull
    public synchronized static <T> T deserialize(@Nonnull String serializedText, @Nonnull Class<T> clazz) {
        return Serializer.deserializeSafe(serializedText, clazz).orElseThrow(() -> new NullPointerException("there is a problem with deserializing!"));
    }
}
