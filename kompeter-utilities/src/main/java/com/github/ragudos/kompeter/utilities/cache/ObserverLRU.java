/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ObserverLRU<K, V> extends LRU<K, V> {
    private final List<Consumer<V>> observers;

    public ObserverLRU(int capacity) {
        super(capacity);

        observers = new ArrayList<>();
    }

    public void notifySubscribers(V value) {
        for (var subscriber : observers) {
            subscriber.accept(value);
        }
    }

    @Override
    public synchronized V remove(K key) {
        var val = super.remove(key);

        if (val != null) {
            notifySubscribers(val);
        }

        return val;
    }

    /**
     * Used for whenever a caller, that's also a subscriber, does not need to be updated since it's
     * contextually aware that the item it's trying to remove is, well... removed. Of course if {@code
     * V} is null, then the subscribers will also simply not be called.
     *
     * @param key
     * @param shouldPublishUpdate
     * @return
     */
    public synchronized V remove(K key, boolean shouldPublishUpdate) {
        var val = super.remove(key);

        if (val != null && shouldPublishUpdate) {
            notifySubscribers(val);
        }

        return val;
    }

    public void subscribe(Consumer<V> observer) {
        if (observers.contains(observer)) {
            return;
        }

        observers.add(observer);
    }

    @Override
    protected V trimCache() {
        var val = super.trimCache();

        if (val != null) {
            notifySubscribers(val);
        }

        return val;
    }

    public void unsubscribe(Consumer<V> observer) {
        if (!observers.contains(observer)) {
            return;
        }

        observers.remove(observer);
    }
}
