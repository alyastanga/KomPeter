/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public final class SessionManager {
    private static SessionManager instance;

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }

        return instance;
    }

    private Session session;
    private List<Consumer<Void>> subscribers = new ArrayList<>();

    private SessionManager() {
        // Private constructor to prevent instantiation
    }

    public Session session() {
        return session;
    }

    public void removeSession() {
        session = null;

        subscribers.forEach(
                (subscriber) -> {
                    subscriber.accept(null);
                });
    }

    /**
     * @throws IllegalArgumentException if session is null.
     */
    public void setSession(@NotNull Session session)
            throws IllegalArgumentException, IllegalStateException {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }

        if (session.isExpired()) {
            throw new IllegalArgumentException("Session is expired.");
        }

        if (this.session != null) {
            throw new IllegalStateException("Session already exists.");
        }

        this.session = session;

        subscribers.forEach(
                (subscriber) -> {
                    subscriber.accept(null);
                });
    }

    public void subscribe(Consumer<Void> subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(Consumer<Void> subscriber) {
        subscribers.remove(subscriber);
    }
}
