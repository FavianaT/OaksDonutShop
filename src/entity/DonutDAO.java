/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import core.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author FavianaT
 */
public class DonutDAO implements DAO<Donut>
{
    public DonutDAO() {

    }
    List<Donut> donuts;
    /**
     * Get a single customer entity as a customer object
     * @param id
     * @return
     */
    @Override
    public Optional<Donut> get(int id) {
        DB db = DB.getInstance();
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM OD_Donut WHERE Donut_ID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            Donut donut = null;
            while (rs.next()) {
                donut = new Donut(rs.getInt("Donut_ID"), rs.getString("Donut_Name"), rs.getDouble("Donut_Price"));
            }
            return Optional.ofNullable(donut);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }

    /**
     * Get all customer entities as a List
     * @return
     */
    @Override
    public List<Donut> getAll() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        donuts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM OD_Donut";
            rs = db.executeQuery(sql);
            Donut donut = null;
            while (rs.next()) {
                donut = new Donut(rs.getInt("Donut_ID"), rs.getString("Donut_Name"), rs.getDouble("Donut_Price"));
                donuts.add(donut);
            }
            return donuts;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }

    /**
     * Insert a customer object into customer table
     * @param donut
     */
    @Override
    public void insert(Donut donut)
    {
        DB db = DB.getInstance();
        try {
            String sql = "INSERT INTO OD_Donut(Donut_ID, Donut_Name, Donut_Price) VALUES (?, ?, ?)";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, donut.getID());
            stmt.setString(2, donut.getName());
            stmt.setDouble(3, donut.getPrice());
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("A new donut was inserted successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Update a customer entity in database if it exists using a customer object
     * @param donut
     */
    @Override
    public void update(Donut donut) {
        DB db = DB.getInstance();
        try {
            String sql = "UPDATE OD_Donut SET Donut_Name=?, Donut_Price=? WHERE Donut_ID=?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setString(1, donut.getName());
            stmt.setDouble(2, donut.getPrice());
            stmt.setInt(4, donut.getID());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("An existing donut was updated successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Delete a customer from customer table if the entity exists
     * @param donut
     */
    @Override
    public void delete(Donut donut) {
        DB db = DB.getInstance();
        try {
            String sql = "DELETE FROM OD_Donut WHERE Donut_ID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, donut.getID());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A donut was deleted successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Get all column names in a list array
     * @return
     */
    @Override
    public List<String> getColumnNames() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        List<String> headers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM OD_Donut WHERE Donut_ID = -1";//We just need this sql query to get the column headers
            rs = db.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            //Get number of columns in the result set
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                headers.add(rsmd.getColumnLabel(i));//Add column headers to the list
            }
            return headers;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }
}
