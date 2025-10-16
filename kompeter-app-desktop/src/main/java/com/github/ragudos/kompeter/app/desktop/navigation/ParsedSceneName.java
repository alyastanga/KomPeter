/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

public class ParsedSceneName implements Iterable<String> {
    private class ParsedSceneNameIterator implements Iterator<String> {
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < fullPathString.size();
        }

        @Override
        public String next() {
            return fullPathString.get(cursor++);
        }
    }

    private ArrayList<String> fullPathString;
    private ParsedSceneName parent;

    public ParsedSceneName(@NotNull String fullPath) {
        this(fullPath, null);
    }

    public ParsedSceneName(@NotNull String fullPath, ParsedSceneName parent) {
        this.fullPathString = new ArrayList<String>(Arrays.asList(fullPath.split(SEPARATOR)));
        this.parent = parent;
    }

    /**
     * Appends to the root parent's full path string
     *
     * @param name
     */
    public void appendToFullPath(String name) {
        this.fullPathString.add(name);

        if (parent != null) {
            parent.appendToFullPath(name);
        }
    }

    /**
     * @return The root parent's full path string
     */
    public String fullPath() {
        if (parent == null) {
            return String.join(SEPARATOR, fullPathString);
        }

        return parent.fullPath();
    }

    public String thisFullPath() {
        return String.join(SEPARATOR, fullPathString);
    }

    public static final String SEPARATOR = "/";

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "ParsedSceneName { "
                + "\n\tfullPath="
                + thisFullPath()
                + "\n\tfullPathString="
                + fullPath()
                + "\n}";
    }

    @Override
    public Iterator<String> iterator() {
        return new ParsedSceneNameIterator();
    }
}
