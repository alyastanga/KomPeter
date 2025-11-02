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
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.ItemDao;
import com.github.ragudos.kompeter.database.dto.inventory.ItemDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteItemDao implements ItemDao {
    @Override
    public int deleteItemById(final int id) throws SQLException, IOException {
        final var query = SqliteQueryLoader.getInstance().get("delete_item_by_id", "items",
                AbstractSqlQueryLoader.SqlQueryType.DELETE);
        try (var conn = SqliteFactoryDao.getInstance().getConnection(); var stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            final var rs = stmt.executeUpdate();
            return rs;
        }
    }

    @Override
    public List<ItemDto> getAllItems() throws SQLException, IOException {
        final List<ItemDto> items = new ArrayList<>();
        final var query = SqliteQueryLoader.getInstance().get("select_all_items", "items", SqlQueryType.SELECT);

        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery();) {

            while (rs.next()) {
                final ItemDto item = new ItemDto(rs.getInt("_item_id"), rs.getTimestamp("_created_at"),
                        rs.getString("name"), rs.getString("description"));

                items.add(item);
            }
        }
        return items;
    }

    @Override
    public Optional<ItemDto> getItemsById(final int id) throws SQLException, IOException {
        Optional<ItemDto> items = Optional.empty();

        final var query = SqliteQueryLoader.getInstance().get("select_item_by_id", "items", SqlQueryType.SELECT);

        try (var conn = SqliteFactoryDao.getInstance().getConnection(); var stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, id);
            final var rs = stmt.executeQuery();

            while (rs.next()) {
                final ItemDto item = new ItemDto(rs.getInt("_item_id"), rs.getTimestamp("_created_at"),
                        rs.getString("name"), rs.getString("description"));

                items = Optional.of(item);
            }
        }
        return items;
    }

    @Override
    public int insertItem(final String name, final String description) throws SQLException, IOException {
        final var query = SqliteQueryLoader.getInstance().get("insert_item", "items",
                AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt = new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query,
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString("name", name);
            stmt.setString("description", description);
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
    public int updateItemNameById(final String name, final int id) throws SQLException, IOException {
        final var query = SqliteQueryLoader.getInstance().get("update_item_name", "items",
                AbstractSqlQueryLoader.SqlQueryType.UPDATE);
        try (var stmt = new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query)) {
            stmt.setString("name", name);
            stmt.setInt("_item_id", id);
            return stmt.executeUpdate();
        }
    }
}
