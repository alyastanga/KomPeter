/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.configurations;

public abstract class AbstractResourceConfig extends AbstractConfig {
    @Override
    public String getFileDirectory() {
        return "";
    }

    public abstract Class<? extends AbstractResourceConfig> getResourceClass();
}
