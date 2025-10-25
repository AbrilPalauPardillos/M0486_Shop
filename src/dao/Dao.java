package dao;

import java.util.List;
import model.Employee;
import model.Product;

public interface Dao {

	public void connect();

	public void disconnect();

	public Employee getEmployee(int employeeId);

	public List<Product> getInventory();

	public boolean writeInventory(List<Product> inventory);

	Employee getEmployee(int employeeId, String password);
}