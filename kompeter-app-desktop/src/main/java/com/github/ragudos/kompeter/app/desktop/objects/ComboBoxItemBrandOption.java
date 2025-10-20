/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.ragudos.kompeter.utilities.StringUtils;

public final record ComboBoxItemBrandOption(@Range(from = 0, to = Integer.MAX_VALUE) int _itemBrandId,
        @NotNull String name) {
    @Override
    public String toString() {
        return StringUtils.upperCaseFirstLetters(name);
    }
}
