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
import java.util.Optional;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.ItemBrandDao;
import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteItemBrandDao implements ItemBrandDao {
    @Override
    public ItemBrandDto[] getAllBrands(final Connection conn) throws SQLException, IOException {
        try (var stmt = conn.createStatement();
                var rs = stmt.executeQuery(SqliteQueryLoader.getInstance().get("select_all_item_brands", "item_brands",
                        SqlQueryType.SELECT));) {
            final ArrayList<ItemBrandDto> brandList = new ArrayList<>();

            while (rs.next()) {
                brandList.add(ItemBrandDto.builder()._itemBrandId(rs.getInt("_item_brand_id"))
                        ._createdAt(rs.getTimestamp("_created_at")).name(rs.getString("name"))
                        .description(rs.getString("description")).build());
            }

            return brandList.toArray(new ItemBrandDto[brandList.size()]);
        }
    }

    @Override
    public Optional<ItemBrandDto> getBrandById(final Connection conn, final int _brandId)
            throws SQLException, IOException {
        try (PreparedStatement stmnt = conn.prepareStatement(
                SqliteQueryLoader.getInstance().get("select_brand_by_brand_id", "item_brands", SqlQueryType.SELECT));
                ResultSet rs = stmnt.executeQuery();) {
            return rs.next()
                    ? Optional.of(ItemBrandDto.builder()._createdAt(rs.getTimestamp("_created_at"))
                            ._itemBrandId(rs.getInt("_item_brand_id")).name(rs.getString("name"))
                            .description(rs.getString("description")).build())
                    : Optional.empty();
        }
    }

    @Override
    public int insertItemBrand(final Connection conn, final String name, final String description)
            throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("insert_item_brand", "item_brands", SqlQueryType.INSERT),
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString("name", name);
            stmt.setString("description", description);
            stmt.executeUpdate();

            final var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }
}
