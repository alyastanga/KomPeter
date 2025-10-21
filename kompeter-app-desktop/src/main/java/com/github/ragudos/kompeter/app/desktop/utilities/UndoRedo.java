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
    private final Stack<T> secondStack = new Stack<>();

    public UndoRedo() {
    }

    public void add(@NotNull T item) {
        firstStack.push(item);
        secondStack.clear();
    }

    public boolean canRedo() {
        return !secondStack.isEmpty();
    }

    public boolean canUndo() {
        return firstStack.size() > 1;
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

    @Override
    public Iterator<T> iterator() {
        return new UndoRedoIterator();
    }

    public @NotNull Optional<T> redo() {
        if (secondStack.isEmpty()) {
            return Optional.empty();
        }

        T item = secondStack.pop();

        firstStack.push(item);

        return Optional.of(item);
    }

    public @NotNull Optional<T> undo() {
        if (firstStack.size() <= 1) {
            return Optional.empty();
        }

        secondStack.push(firstStack.pop());

        return Optional.of(firstStack.getLast());
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
