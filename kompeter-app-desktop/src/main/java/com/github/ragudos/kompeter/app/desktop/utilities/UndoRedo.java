/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.utilities;

import java.util.Iterator;
import java.util.Optional;
import java.util.Stack;

import org.jetbrains.annotations.NotNull;

public class UndoRedo<T> implements Iterable<T> {
    private final Stack<T> firstStack = new Stack<>();
    private RecentAction recentAction;

    private final Stack<T> secondStack = new Stack<>();

    public UndoRedo() {
    }

    public void add(@NotNull T item) {
        recentAction = null;
        firstStack.push(item);
        secondStack.clear();
    }

    public void clear() {
        firstStack.clear();
        secondStack.clear();
    }

    public void clearRedo() {
        secondStack.clear();
    }

    public @NotNull Optional<T> current() {
        if (firstStack.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(firstStack.getLast());
    }

    public boolean isRedoAble() {
        return !secondStack.isEmpty();
    }

    public boolean isUndoAble() {
        return firstStack.size() > 1;
    }

    @Override
    public Iterator<T> iterator() {
        return new UndoRedoIterator();
    }

    public RecentAction recentAction() {
        return recentAction;
    }

    public @NotNull Optional<T> redo() {
        if (secondStack.isEmpty()) {
            return Optional.empty();
        }

        T item = secondStack.pop();

        firstStack.push(item);
        recentAction = RecentAction.REDO;

        return Optional.of(item);
    }

    public @NotNull Optional<T> redoDry() {
        if (secondStack.isEmpty()) {
            return Optional.empty();
        }

        recentAction = RecentAction.REDO;
        return Optional.of(secondStack.getLast());
    }

    public @NotNull Optional<T> undo() {
        if (firstStack.size() <= 1) {
            return Optional.empty();
        }

        secondStack.push(firstStack.pop());
        recentAction = RecentAction.UNDO;

        return Optional.of(firstStack.getLast());
    }

    public @NotNull Optional<T> undoDry() {
        if (firstStack.size() <= 1) {
            return Optional.empty();
        }

        recentAction = RecentAction.UNDO;
        return Optional.of(firstStack.get(firstStack.size() - 2));
    }

    public enum RecentAction {
        REDO, UNDO;
    }

    private class UndoRedoIterator implements Iterator<T> {
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            int firstStackSize = firstStack.size();

            return cursor < firstStackSize || cursor < firstStackSize + secondStack.size();
        }

        @Override
        public T next() {
            int firstStackSize = firstStack.size();

            return cursor < firstStackSize ? firstStack.get(cursor++) : secondStack.get((cursor++) - firstStackSize);
        }
    }
}
