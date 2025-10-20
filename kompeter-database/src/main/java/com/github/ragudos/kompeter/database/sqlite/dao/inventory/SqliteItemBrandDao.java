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
import com.github.ragudos.kompeter.database.dao.inventory.ItemBrandDao;
import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteItemBrandDao implements ItemBrandDao {

    @Override
    public int insertItemBrand(String name, String description) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_item_brand", "items", AbstractSqlQueryLoader.SqlQueryType.INSERT);
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
    public List<ItemBrandDto> getAllBrands() throws SQLException, IOException {
        List<ItemBrandDto> brandList = new ArrayList<>();
        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_all_brands", "items", AbstractSqlQueryLoader.SqlQueryType.SELECT);

        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery(); ) {

            while (rs.next()) {
                ItemBrandDto brand =
                        new ItemBrandDto(
                                rs.getInt("_item_brand_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getString("name"),
                                rs.getString("description"));

                brandList.add(brand);
            }
        }
        return brandList;
    }

    @Override
    public Optional<ItemBrandDto> getBrandById(int id) throws SQLException, IOException {
        Optional<ItemBrandDto> brandOpt = Optional.empty();
        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_all_brands", "items", AbstractSqlQueryLoader.SqlQueryType.SELECT);

        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery(); ) {

            while (rs.next()) {
                ItemBrandDto brand =
                        new ItemBrandDto(
                                rs.getInt("_item_brand_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getString("name"),
                                rs.getString("description"));

                brandOpt = Optional.of(brand);
            }
        }
        return brandOpt;
    }
}
