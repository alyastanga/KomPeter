package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.ItemDto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ItemDao {
    // CREATE
    // READ
    List<ItemDto> getAllItems() throws SQLException, IOException;

    List<ItemDto> getItemsById(int id) throws SQLException, IOException;

    // UPDATE
    // DELETE
}
