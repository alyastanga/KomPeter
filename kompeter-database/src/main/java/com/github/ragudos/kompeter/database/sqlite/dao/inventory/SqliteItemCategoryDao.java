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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryDao;
import com.github.ragudos.kompeter.database.dto.inventory.ItemCategoryDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteItemCategoryDao implements ItemCategoryDao {
    @Override
    public List<ItemCategoryDto> getAllCategories(final Connection conn) throws SQLException, IOException {
        final List<ItemCategoryDto> categoryList = new ArrayList<>();
        try (PreparedStatement stmt = conn
                .prepareStatement(SqliteQueryLoader.getInstance().get("select_all_categories", "items",
                        AbstractSqlQueryLoader.SqlQueryType.SELECT));
                var rs = stmt.executeQuery();) {

            while (rs.next()) {
                final ItemCategoryDto category = new ItemCategoryDto(rs.getInt("_item_category_id"),
                        rs.getTimestamp("_created_at"), rs.getString("name"), rs.getString("description"));

                categoryList.add(category);
            }
        }

        return categoryList;
    }

    @Override
    public Optional<ItemCategoryDto> getCategoryById(final Connection conn, final int id)
            throws SQLException, IOException {
        Optional<ItemCategoryDto> categoryOpt = Optional.empty();
        try (PreparedStatement stmt = conn
                .prepareStatement(SqliteQueryLoader.getInstance().get("select_category_by_id", "items",
                        AbstractSqlQueryLoader.SqlQueryType.SELECT));
                var rs = stmt.executeQuery();) {

            while (rs.next()) {
                final ItemCategoryDto category = new ItemCategoryDto(rs.getInt("_item_category_id"),
                        rs.getTimestamp("_created_at"), rs.getString("name"), rs.getString("description"));

                categoryOpt = Optional.of(category);
            }
        }
        return categoryOpt;
    }

    @Override
    public int insertItemCategory(final Connection conn, final String name, final String description)
            throws SQLException, IOException {
        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("insert_item_category", "items",
                        AbstractSqlQueryLoader.SqlQueryType.INSERT),
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString("name", name);
            stmt.setString("description", description);
            stmt.executeUpdate();

            final var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }
}
