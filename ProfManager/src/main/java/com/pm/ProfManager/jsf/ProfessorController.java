
package com.pm.ProfManager.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.SessionMap;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.pm.ProfManager.dao.Professor;
import com.pm.ProfManager.dao.ProfessorDTO;
import com.pm.ProfManager.dao.ProfessorDao;

/**
* JSF Controller class for Professor Manager
*
*/
@Named // used to be @javax.faces.bean.ManagedBean from JSF spec, but as of Java EE 7 (JSF 2.2),
       // switched to use CDI (Contexts and Dependency Injection) javax.inject
       // Note: cannot 'mix' JSF and CDI scope annotations
@SessionScoped // this scope (like @RequestScoped or @ApplicationScoped) is about *how many* instances there are
   // and *how long* they are in scope:
   //  @RequestScoped - 1 for every (HTTP) request, goes out-of-scope when request finishes
   //  @SessionScoped - 1 for every session (ie. browser session), goes out-of-scope when session finishes (browser closes)
   //  @ApplicationScoped - 1 for the application, goes out-of-scope when app terminates/is un-deployed
public class ProfessorController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    @SessionMap
    private Map<String, Object> session;
    
   
    //Modified version of the map, now switched by an arraylist
    private static List<String> regions;
   
    protected String region = "CA";
    protected ProfessorDao theDao;
    protected List<Professor> theInventoryList;

    @Inject
    public ProfessorController(ProfessorDao theDao) {
        this.theDao = theDao;
        loadRegions();
    }

    private void loadRegions() {
		regions=theDao.getRegions();
		
	}

	public void loadInventory() {
        theInventoryList = theDao.readAllInventoryForRegion(region);
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getRegions() {
        return regions;
    }

    public List<Professor> getInventory() {
        return theInventoryList;
    }
    public void setInventory(List<Professor> theInventoryList) {
        this.theInventoryList = theInventoryList;
    }

    
    
    //value change event listener
    public void valueChangeMethod(ValueChangeEvent e) {
        Object newChangedValue = e.getNewValue();
        if (newChangedValue != null) {
          setRegion(newChangedValue.toString());
          loadInventory();
        }
    }
    
    public String goToAddInventory() {
    	session.put("newInventory", new ProfessorDTO());
    	return "add-inventory.xhtml?faces-redirect=true";
    }

    public String submitNewInventory(Professor inv) {
    	theDao.createInventory(inv);
    	return "index.xhtml?faces-redirect=true";
    }
    
    public String goToEditInventory(int invId) {
    	session.put("existingInventory", theDao.readInventoryById(invId));
    	return "edit-inventory.xhtml?faces-redirect=true";
    }
    
    public String submitExistingInventory(Professor inv) {
    	theDao.updateInventory(inv);
    	return "index.xhtml?faces-redirect=true";
    }
    
    public String deleteInventory(int invId) {
    	theDao.deleteInventory(invId);
    	return "index.xhtml?faces-redirect=true";
    }
    
}