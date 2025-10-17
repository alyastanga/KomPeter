/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface InventoryDao {
    //    public enum Location {
    //        KOMPETER_DISPLAY_FLOOR,
    //        WAREHOUSE_A,
    //        WAREHOUSE_B,
    //        RECEIVING_DOCK;
    //
    //        @Override
    //        public String toString() {
    //            return switch (this) {
    //                case KOMPETER_DISPLAY_FLOOR -> "Kompeter Display Floor";
    //                case WAREHOUSE_A -> "Warehouse A";
    //                case WAREHOUSE_B -> "Warehouse B";
    //                case RECEIVING_DOCK -> "Receiving Dock";
    //            };
    //        }
    //    }

    public enum OrderBy {
        ITEM_NAME,
        BRAND_NAME,
        CATEGORY_NAME,
        QUANTITY,
        DATE,
        PRICE;

        @Override
        public String toString() {
            return switch (this) {
                case ITEM_NAME -> "i.name";
                case BRAND_NAME -> "ib.name";
                case CATEGORY_NAME -> "ic.name";
                case QUANTITY -> "issl.quantity";
                case DATE -> "i._created_at";
                case PRICE -> "ist.unit_price_php";
            };
        }
    }

    public enum Direction {
        ASC,
        DESC;
    }

    List<InventoryMetadataDto> getAllData(String search, OrderBy orderBy, Direction direction)
            throws SQLException, IOException;
    //    List<InventoryMetadataDto> find(OrderBy orderBy, Direction direction)
    //            throws SQLException, IOException;
}
