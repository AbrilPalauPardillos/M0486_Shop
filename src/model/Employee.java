package model;

import main.Logable;

import javax.persistence.Entity;
import javax.persistence.Transient;

import dao.*;

@Entity
public class Employee extends Person implements Logable{
	private int employeeId;
	private String password;
	
	@Transient
	private Dao dao = new DaoImplObjectDB();
	
	
	public Employee(String name) {
		super(name);
	}
	
	public Employee(int employeeId, String name, String password) {
		super(name);
		this.employeeId = employeeId;
		this.password = password;
	}
	
	public Employee() {
		super();
	}
	
	/**
	 * @return the employeeId
	 */
	public int getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param user from application, password from application
	 * @return true if credentials are correct or false if not
	 */
	@Override
	public boolean login(int user, String password) {
	    this.dao.connect();
	    Employee emp = dao.getEmployee(user, password); 
	    this.dao.disconnect(); 
	    return emp != null;
	}
}
