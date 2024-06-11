/*****************************************************************
 * File:  InventoryDao.java
 * Course materials CST 8277
 * @author Mike Norman
 *
 */
package com.pm.ProfManager.dao;

import java.util.List;

public interface ProfessorDao {

    // TODO - add more to this interface

    // Create
	void createInventory(Professor inv);
	
    // R - read all inventory for the specified region
    public List<Professor> readAllInventoryForRegion(String region);
    // R - read a specific inventory
    Professor readInventoryById(int invId);

    // Update
    void updateInventory(Professor inv);
    
    // Delete
    void deleteInventory(int invId);
    
}