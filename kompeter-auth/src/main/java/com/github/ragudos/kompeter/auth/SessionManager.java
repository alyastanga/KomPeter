/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.auth;

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

    private SessionManager() {
        // Private constructor to prevent instantiation
    }

    public Session session() {
        return session;
    }

    public void removeSession() {
        session = null;
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
    }
}
