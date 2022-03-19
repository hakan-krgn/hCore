package com.hakan.core.utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Randomizer list.
 *
 * @param <T> Class type.
 */
public class RandomizerList<T> extends ArrayList<T> {

    /**
     * {@inheritDoc}
     */
    public RandomizerList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * {@inheritDoc}
     */
    public RandomizerList(@Nonnull List<T> list) {
        super(Objects.requireNonNull(list, "list cannot be null!"));
    }

    /**
     * {@inheritDoc}
     */
    public RandomizerList() {
        super();
    }

    /**
     * Gets random elements from list.
     *
     * @param elementCount Count that you want.
     * @return Random elements as list.
     */
    public List<T> getRandomElements(int elementCount) {
        List<T> copyList = new ArrayList<>(this);
        List<T> list = new ArrayList<>();

        Random random = new Random();
        IntStream.range(0, elementCount).map(i -> random.nextInt(copyList.size())).forEach(element -> {
            list.add(copyList.get(element));
            copyList.remove(element);
        });

        return list;
    }

    /**
     * Gets random element from list.
     *
     * @return Random element.
     */
    public T getRandomElement() {
        return this.getRandomElements(1).get(0);
    }
}