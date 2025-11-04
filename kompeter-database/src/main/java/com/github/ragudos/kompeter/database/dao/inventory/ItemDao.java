/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

public interface ItemDao {
    String[] getAllItemNames(Connection conn) throws SQLException, IOException;

    int insertItem(@NotNull Connection conn, @NotNull String name, @NotNull String description, String imagePath)
            throws SQLException, IOException;

    boolean itemExists(@NotNull Connection conn, @NotNull String itemName) throws IOException, SQLException;

    int updateItemNameById(@NotNull Connection conn, @NotNull String name, int id) throws SQLException, IOException;
}
