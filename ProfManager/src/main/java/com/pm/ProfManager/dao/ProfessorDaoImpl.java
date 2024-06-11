/*****************************************************************
 * File:  InventoryDaoImpl.java
 * Course materials CST 8277
 * @author Mike Norman
 *
 */
package com.pm.ProfManager.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

@Named
@ApplicationScoped
public class ProfessorDaoImpl implements ProfessorDao, Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    public static final String GET_ALL_INVENTORY_FOR_REGION_SQL =
    		"SELECT * FROM REGIONAL_INVENTORY WHERE REGION = ?";
    public static final String INSERT_INVENTORY_SQL = 
    		"INSERT INTO REGIONAL_INVENTORY(INV_ID, RETAILER_NAME, CURR_INV_LEVEL, REGION, CREATED, UPDATED) VALUES (?, ?, ?, ?, now(), now());";
    public static final String GET_SPECIFIC_INVENTORY_BY_ID_SQL =
    		"SELECT * FROM REGIONAL_INVENTORY WHERE INV_ID = ?";
    public static final String UPDATE_SPECIFIC_INVENTORY_BY_ID_SQL =
    		"UPDATE REGIONAL_INVENTORY SET RETAILER_NAME = ?, CURR_INV_LEVEL = ?, REGION = ?, UPDATED = now(), VERSION = VERSION + 1 WHERE INV_ID = ?";
    public static final String DELETE_SPECIFIC_INVENTORY_BY_ID_SQL =
    		"DELETE FROM REGIONAL_INVENTORY WHERE INV_ID = ?";
    
    @Inject
    protected ServletContext ctx;
    private void logMsg(String msg) {
        ctx.log(msg);
    }

    @Resource(lookup="java:app/jdbc/regionalInventory")
    protected DataSource regionalInventoryDataSource;

    protected Connection conn;
    protected PreparedStatement getAllPstmt;
    protected PreparedStatement insertPstmt;
    protected PreparedStatement readInventoryByIdPstmt;
    protected PreparedStatement updateInventoryByIdPstmt;
    protected PreparedStatement deleteInventoryByIdPstmt;
    
    @PostConstruct
    protected void buildConnectionAndStatements() {
        try {
            logMsg("building connection ...");
            conn = regionalInventoryDataSource.getConnection();
            getAllPstmt = conn.prepareStatement(GET_ALL_INVENTORY_FOR_REGION_SQL);
            // build more statements for rest of C-R-U-D interface
            insertPstmt = conn.prepareStatement(INSERT_INVENTORY_SQL);
            readInventoryByIdPstmt = conn.prepareStatement(GET_SPECIFIC_INVENTORY_BY_ID_SQL);
            updateInventoryByIdPstmt = conn.prepareStatement(UPDATE_SPECIFIC_INVENTORY_BY_ID_SQL);
            deleteInventoryByIdPstmt = conn.prepareStatement(DELETE_SPECIFIC_INVENTORY_BY_ID_SQL);
        }
        catch (Exception e) {
            logMsg("something went wrong getting connection: " + e.getLocalizedMessage());
        }
    }

    @PreDestroy
    protected void closeConnectionAndStatements() {
        try {
            logMsg("closing stmts and connection ...");
            getAllPstmt.close();
            // close rest of statements ...
            insertPstmt.close();
            readInventoryByIdPstmt.close();
            updateInventoryByIdPstmt.close();
            deleteInventoryByIdPstmt.close();
            conn.close();
        }
        catch (Exception e) {
            logMsg("something went wrong closing connection: " + e.getLocalizedMessage());
        }
    }

    @Override
    public List<Professor> readAllInventoryForRegion(String region) {
        List<Professor> inventoryList = new ArrayList<>();
        try {
            getAllPstmt.setString(1, region);
            ResultSet rs = getAllPstmt.executeQuery();
            while (rs.next()) {
                Professor newInventory = new ProfessorDTO();
                newInventory.setId(rs.getInt("INV_ID"));
                newInventory.setProfessorName(rs.getString("RETAILER_NAME"));
                newInventory.setMajor(rs.getString("REGION"));
                newInventory.setAge(rs.getInt("CURR_INV_LEVEL"));
                inventoryList.add(newInventory);
            }
            try {
                rs.close();
            }
            catch (SQLException e) {
                logMsg("something went wrong closing resultSet: " + e.getLocalizedMessage());
            }
        }
        catch (SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
        }
        return inventoryList;
    }

    //TODO - fill in rest of C-R-U-D methods
    public void createInventory(Professor inv) {
    	// INV_ID, RETAILER_NAME, CURR_INV_LEVEL, REGION
    	try {
    		insertPstmt.setLong(1, inv.getId());
    		insertPstmt.setString(2, inv.getProfessorName());
    		insertPstmt.setLong(3, inv.getAge());
    		insertPstmt.setString(4, inv.getMajor());
    		insertPstmt.execute();
    	}
    	catch (SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
    	}
    }
    
    public Professor readInventoryById(int invId) {
    	Professor inv = null;
    	try {
    		readInventoryByIdPstmt.setLong(1, invId);
    		ResultSet rs = readInventoryByIdPstmt.executeQuery();
    		if (rs.next()) {
    			inv = new ProfessorDTO();
    			inv.setId(rs.getInt("INV_ID"));
    			inv.setAge(rs.getInt("CURR_INV_LEVEL"));
    			inv.setMajor(rs.getString("REGION"));
    			inv.setProfessorName(rs.getString("RETAILER_NAME"));
    		}
    	}
    	catch(SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
    	}
    	
    	return inv;
    }
    
    public void updateInventory(Professor inv) {
    	//UPDATE REGIONAL_INVENTORY SET RETAILER_NAME = ?, CURR_INV_LEVEL = ?, REGION = ?, UPDATED = now(), VERSION = VERSION + 1 WHERE INV_ID = ?
    	try {
    		updateInventoryByIdPstmt.setString(1, inv.getProfessorName());
    		updateInventoryByIdPstmt.setLong(2, inv.getAge());
    		updateInventoryByIdPstmt.setString(3, inv.getMajor());
    		updateInventoryByIdPstmt.setLong(4, inv.getId());
    		updateInventoryByIdPstmt.executeUpdate();
    	}
    	catch(SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
    	}
    }
    
    public void deleteInventory(int invId) {
    	try {
    		deleteInventoryByIdPstmt.setLong(1, invId);
    		deleteInventoryByIdPstmt.execute();
    	}
    	catch(SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
    	}
    }

}