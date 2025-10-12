package com.github.ragudos.kompeter.pointofsaleDAO;

import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.pointofsale.CartItem;
import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PosDaoImpl implements PosDao{
    PreparedStatement ps;
    ResultSet rs;

    @Override
    public CartItem get(int productID) throws SQLException {
        Connection con = SqliteFactoryDao.getInstance().getConnection();
        CartItem cartitem = null;
        List<String> sales = new ArrayList<>();
                
        String sql = "SELECT i._item_id, i.name, issl.quantity, iss.unit_price_php \n" +
                    "FROM items AS i\n" +
                    "INNER JOIN item_stocks As iss On i._item_id = iss._item_id\n" +
                    "INNER JOIN item_stock_storage_locations AS issl on iss._item_stock_id = iss._item_stock_id WHERE i._item_id = ?";
        
        ps = con.prepareStatement(sql);
        
        ps.setInt(1, productID);
        
        rs = ps.executeQuery();
        while(rs.next()) {
            cartitem = new CartItem( 
            rs.getInt("_item_id"),
            rs.getString("name"), 
            0, 
            rs.getDouble("unit_price_php")
            );
        }
        return cartitem;
    }

    @Override
    public List<CartItem> getAll() throws SQLException {
         Connection con = SqliteFactoryDao.getInstance().getConnection();
         List<CartItem> items = new ArrayList<>();
         
         String sql = "SELECT i._item_id, i.name, iss.unit_price_php \n" +
                    "FROM items AS i\n" +
                    "INNER JOIN item_stocks As iss On i._item_id = iss._item_id";
                 
         ps = con.prepareStatement(sql);
         rs = ps.executeQuery();
         
         while(rs.next()) {
             items.add(new CartItem(
             rs.getInt("_item_id"),
             rs.getString("name"),
             0,
             rs.getDouble("unit_price_php")
             ));
         }
         return items;
    }

    @Override
    public int save(CartItem cartitem) throws SQLException {
         Connection con = SqliteFactoryDao.getInstance().getConnection();
         return 0;
    }

    @Override
    public int update(CartItem t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int delete(CartItem t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int add(CartItem t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
