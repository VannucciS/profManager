/*****************************************************************
 * File:  Inventory.java
 * Course materials CST 8277
 * @author Mike Norman
 *
 */
package com.pm.ProfManager.dao;

public interface Professor {

    /**
     * @return the primary key from database
     */
    public int getId();
    public void setId(int id);

    /**
     * @return the name of the retailer
     */
    public String getProfessorName();
    /**
     * @param new name of the retailer
     */
    public void setProfessorName(String retailerName);

    /**
     * @return the name of retailer
     */
    public String getMajor();
    /**
     * @param retailer new name of retailer
     */
    public void setMajor(String firstName);

    /**
     * @return the inventory level
     */
    public int getAge();
    /**
     * @param new inventory level
     */
    public void setAge(int level);

}