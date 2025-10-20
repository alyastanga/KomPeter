/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryDao;
import com.github.ragudos.kompeter.database.dto.inventory.ItemCategoryDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteItemCategoryDao implements ItemCategoryDao {
    @Override
    public int insertItemCategory(String name, String description) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_item_category", "items", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt =
                new NamedPreparedStatement(
                        SqliteFactoryDao.getInstance().getConnection(),
                        query,
                        Statement.RETURN_GENERATED_KEYS); ) {
            stmt.setString("name", name);
            stmt.setString("description", description);
            stmt.executeUpdate();

            var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public List<ItemCategoryDto> getAllCategories() throws SQLException, IOException {
        List<ItemCategoryDto> categoryList = new ArrayList<>();
        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_all_categories", "items", AbstractSqlQueryLoader.SqlQueryType.SELECT);

        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery(); ) {

            while (rs.next()) {
                ItemCategoryDto category =
                        new ItemCategoryDto(
                                rs.getInt("_item_category_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getString("name"),
                                rs.getString("description"));

                categoryList.add(category);
            }
        }
        return categoryList;
    }

    @Override
    public Optional<ItemCategoryDto> getCategoryById(int id) throws SQLException, IOException {
        Optional<ItemCategoryDto> categoryOpt = Optional.empty();
        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_category_by_id", "items", AbstractSqlQueryLoader.SqlQueryType.SELECT);

        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery(); ) {

            while (rs.next()) {
                ItemCategoryDto category =
                        new ItemCategoryDto(
                                rs.getInt("_item_category_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getString("name"),
                                rs.getString("description"));

                categoryOpt = Optional.of(category);
            }
        }
        return categoryOpt;
    }
}
