package com.hakan.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomizerList<T> extends ArrayList<T> {

    public RandomizerList(int initialCapacity) {
        super(initialCapacity);
    }

    public RandomizerList(List<T> list) {
        super(list);
    }

    public RandomizerList() {
        super();
    }

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

    public T getRandomElement() {
        return this.getRandomElements(1).get(0);
    }
}