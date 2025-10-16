/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.configurations;

import com.github.ragudos.kompeter.utilities.constants.Directories;
import java.io.File;

public abstract class AbstractFileSystemConfig extends AbstractConfig {
    @Override
    public String getFileDirectory() {
        return Directories.CONFIG_DIRECTORY + File.separator;
    }

    public String getHeaderComment() {
        return "A configuration file for Kompeter. Refer to the documentation for more information.";
    }
}
