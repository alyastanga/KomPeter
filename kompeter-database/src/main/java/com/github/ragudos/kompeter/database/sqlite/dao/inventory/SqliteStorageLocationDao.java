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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.StorageLocationDao;
import com.github.ragudos.kompeter.database.dto.inventory.StorageLocationDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteStorageLocationDao implements StorageLocationDao {
    @Override
    public StorageLocationDto[] getAllStorageLocations(final Connection conn) throws SQLException, IOException {
        try (var stmnt = conn.createStatement()) {
            final ResultSet rs = stmnt.executeQuery(SqliteQueryLoader.getInstance().get("select_all_storage_locations",
                    "storage_locations", SqlQueryType.SELECT));

            final ArrayList<StorageLocationDto> locations = new ArrayList<>();

            while (rs.next()) {
                locations.add(StorageLocationDto.builder()._createdAt(rs.getTimestamp("_created_at"))
                        ._storageLocationId(rs.getInt("_storage_location_id")).description(rs.getString("description"))
                        .name(rs.getString("name")).build());
            }

            return locations.toArray(new StorageLocationDto[locations.size()]);
        }
    }

    @Override
    public int insertStorageLocation(final String setString, final String description)
            throws SQLException, IOException {
        final var query = SqliteQueryLoader.getInstance().get("insert_storage_location", "storage_locations",
                AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt = new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query,
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString("name", setString);
            stmt.setString("description", description);

            stmt.executeUpdate();
            final var rs = stmt.getPreparedStatement().getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }
}
