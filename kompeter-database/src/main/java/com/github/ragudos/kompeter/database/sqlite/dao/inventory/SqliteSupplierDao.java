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
import com.github.ragudos.kompeter.database.dao.inventory.SupplierDao;
import com.github.ragudos.kompeter.database.dto.inventory.SupplierDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteSupplierDao implements SupplierDao {

    @Override
    public int insertSupplier(
            String name,
            String email,
            String street,
            String city,
            String state,
            String postalCode,
            String country)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_supplier", "supplier", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt =
                new NamedPreparedStatement(
                        SqliteFactoryDao.getInstance().getConnection(),
                        query,
                        Statement.RETURN_GENERATED_KEYS); ) {
            stmt.setString("name", name);
            stmt.setString("email", email);
            stmt.setString("street", street);
            stmt.setString("city", city);
            stmt.setString("state", state);
            stmt.setString("postal_code", postalCode);
            stmt.setString("country", country);

            stmt.executeUpdate();

            var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public List<SupplierDto> getAllSuppliers() throws SQLException, IOException {
        List<SupplierDto> supplierList = new ArrayList<>();
        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_all_supplier", "supplier", AbstractSqlQueryLoader.SqlQueryType.SELECT);

        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery(); ) {

            while (rs.next()) {
                SupplierDto supplier =
                        new SupplierDto(
                                rs.getInt("_supplier_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("street"),
                                rs.getString("city"),
                                rs.getString("state"),
                                rs.getString("postal_code"),
                                rs.getString("country"));

                supplierList.add(supplier);
            }
        }
        return supplierList;
    }

    @Override
    public Optional<SupplierDto> getSupplierByID(int id) throws SQLException, IOException {
        Optional<SupplierDto> supplierOpt = Optional.empty();

        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_supplier_by_id", "supplier", AbstractSqlQueryLoader.SqlQueryType.SELECT);

        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query); ) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                SupplierDto item =
                        new SupplierDto(
                                rs.getInt("_supplier_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("street"),
                                rs.getString("city"),
                                rs.getString("state"),
                                rs.getString("postal_code"),
                                rs.getString("country"));

                supplierOpt = Optional.of(item);
            }
        }
        return supplierOpt;
    }
}
