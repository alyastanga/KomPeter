/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.seeder;

import java.sql.SQLException;

public interface Seeder {
    void seed() throws SQLException;
}
