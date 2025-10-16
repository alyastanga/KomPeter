/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A simple LRU (Least Recently Used) cache implementation.
 *
 * @param <K> The type of the keys in the cache.
 * @param <V> The type of the values in the cache.
 */
public class LRU<K, V> {
    private int length;
    private int capacity;

    private Node<V> tail;
    private Node<V> head;

    private HashMap<K, Node<V>> lookup;
    private HashMap<Node<V>, K> reverseLookup;

    public LRU(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        this.capacity = capacity;

        lookup = new HashMap<>();
        reverseLookup = new HashMap<>();
    }

    public synchronized void clear() {
        while (length > 0) {
            trimCache();
        }
    }

    public synchronized boolean containsKey(K key) {
        return lookup.containsKey(key);
    }

    private void detach(Node<V> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        }

        if (head == node) {
            head = head.next;
        }

        if (tail == node) {
            tail = tail.prev;
        }

        node.next = null;
        node.prev = null;
    }

    public synchronized Set<Entry<K, V>> entrySet() {
        var result = new HashMap<K, V>();

        for (var entry : lookup.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue().value;

            result.put(key, value);
        }

        return result.entrySet();
    }

    /**
     * Returns the value associated with the key in the cache. If the key is not present, it returns
     * null.
     *
     * @param key The key to look up.
     * @return The value associated with the key, or null if not found.
     */
    public synchronized V get(K key) {
        var node = lookup.get(key);

        if (node == null) {
            return null;
        }

        detach(node);
        prepend(node);

        return node.value;
    }

    public synchronized HashMap<K, V> getCopy() {
        var result = new HashMap<K, V>();

        for (var entry : lookup.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue().value;

            result.put(key, value);
        }

        return result;
    }

    private void prepend(Node<V> node) {
        if (head == null) {
            head = tail = node;
            return;
        }

        node.next = head;
        head.prev = node;
        head = node;
    }

    public synchronized V remove(K key) {
        var node = lookup.get(key);

        if (node == null) {
            return null;
        }

        detach(node);

        lookup.remove(key);
        reverseLookup.remove(node);
        length -= 1;

        return node.value;
    }

    protected V trimCache() {
        if (length <= capacity) {
            return null;
        }

        var referenceToTail = tail;

        detach(tail);

        var key = reverseLookup.get(referenceToTail);

        lookup.remove(key);
        reverseLookup.remove(referenceToTail);
        length -= 1;

        return referenceToTail.value;
    }

    /**
     * Updates the cache with the given key and value. If the key already exists, it updates the value
     * and moves the node to the front of the cache.
     *
     * @param key The key to update.
     * @param value The value to associate with the key.
     */
    public synchronized void update(K key, V value) {
        var node = lookup.get(key);

        if (node == null) {
            node = Node.createNode(value);

            length += 1;
            prepend(node);
            trimCache();

            lookup.put(key, node);
            reverseLookup.put(node, key);
        } else {
            detach(node);
            prepend(node);

            node.value = value;
        }
    }

    public synchronized Collection<V> values() {
        var result = new ArrayList<V>();

        for (var entry : lookup.entrySet()) {
            var value = entry.getValue().value;

            result.add(value);
        }

        return result;
    }
}

class Node<T> {
    public static <T> Node<T> createNode(T data) {
        return new Node<>(data);
    }

    T value;
    Node<T> prev;
    Node<T> next;

    public Node(T value) {
        this.value = value;
        prev = null;
        next = null;
    }
}
