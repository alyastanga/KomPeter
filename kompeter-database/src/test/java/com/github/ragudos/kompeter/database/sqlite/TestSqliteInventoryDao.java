/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite;

import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteInventoryDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Peter M. Dela Cruz
 */
public class TestSqliteInventoryDao {
    @Test
    @DisplayName("Test Sqlite Inventory Dao")
    void testInventoryDao() {
        var dao =
                new SqliteInventoryDao(
                        AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE).getConnection());

        List<InventoryMetadataDto> inventory = null;
        try {
            inventory = dao.getAllData(null, null, null);
            for (InventoryMetadataDto item : inventory) {
                System.out.println("Item Name: " + item._itemId() + ", Quantity: " + item.quantity());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
