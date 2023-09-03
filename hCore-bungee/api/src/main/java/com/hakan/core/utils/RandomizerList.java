package com.hakan.core.utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Randomizer list.
 *
 * @param <T> Class type.
 */
public final class RandomizerList<T> extends ArrayList<T> {

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
        super(list);
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
    @Nonnull
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
    @Nonnull
    public T getRandomElement() {
        return this.getRandomElements(1).get(0);
    }
}
