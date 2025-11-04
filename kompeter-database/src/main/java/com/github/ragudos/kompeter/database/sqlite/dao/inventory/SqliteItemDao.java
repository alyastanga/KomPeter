/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.ItemDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteItemDao implements ItemDao {
    @Override
    public String[] getAllItemNames(Connection conn) throws SQLException, IOException {
        try (Statement stmnt = conn.createStatement();
                ResultSet rs = stmnt.executeQuery(
                        SqliteQueryLoader.getInstance().get("select_all_item_names", "items", SqlQueryType.SELECT))) {
            ArrayList<String> names = new ArrayList<>();

            while (rs.next()) {
                names.add(rs.getString("name"));
            }

            return names.toArray(new String[names.size()]);
        }
    }

    @Override
    public int insertItem(Connection conn, final String name, final String description, String imagePath)
            throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn, SqliteQueryLoader.getInstance().get("insert_item", "items",
                SqlQueryType.INSERT),
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString("name", name);
            stmt.setString("description", description);
            stmt.setString("display_image", imagePath);
            stmt.executeUpdate();

            final var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public boolean itemExists(@NotNull final Connection conn, @NotNull final String itemName)
            throws IOException, SQLException {
        try (PreparedStatement stmnt = conn.prepareStatement(
                SqliteQueryLoader.getInstance().get("select_name_exists", "items", SqlQueryType.SELECT))) {
            stmnt.setString(1, itemName);

            final ResultSet rs = stmnt.executeQuery();

            return rs.next() && rs.getInt(1) != 0;
        }
    }

    @Override
    public int updateItemNameById(Connection conn, final String name, final int id) throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("update_item_name", "items",
                        SqlQueryType.UPDATE))) {
            stmt.setString("name", name);
            stmt.setInt("_item_id", id);
            return stmt.executeUpdate();
        }
    }
}
