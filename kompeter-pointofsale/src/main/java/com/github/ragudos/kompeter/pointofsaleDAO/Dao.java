package com.github.ragudos.kompeter.pointofsaleDAO;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    T get(int productID) throws SQLException;
    
    List<T> getAll() throws SQLException;
    
    int save(T t) throws SQLException;
    int update(T t) throws SQLException;
    int delete(T t) throws SQLException;
    int add(T t) throws SQLException;
}

