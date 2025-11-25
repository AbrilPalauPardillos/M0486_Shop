package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao {
	Connection connection;

	@Override
	public void connect() {
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1,employeeId);
    	  	ps.setString(2,password);
            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            		employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
            	}
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
    	return employee;
	}

	@Override
	public List<Product> getInventory() {
		List<Product> inventory = new ArrayList<>();
		String query = "SELECT id, name, public_price, wholesalerPrice, available, stock FROM inventory";

		try (Statement stmt = this.connection.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				int id = rs.getInt("id"); 
				String name = rs.getString("name");
				double publicPriceDouble = rs.getDouble("public_price");
				double wholesalerPriceDouble = rs.getDouble("wholesalerPrice");
				Amount publicPrice = new Amount(publicPriceDouble);
				Amount wholesalerPrice = new Amount(wholesalerPriceDouble);
				boolean available = rs.getBoolean("available");
				int stock = rs.getInt("stock");

				Product product = new Product(id, name, publicPrice, wholesalerPrice, available, stock);
				inventory.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inventory;
	}
	
	@Override
	public boolean writeInventory(List<Product> inventory) {
	    String insertQuery = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
	    boolean success = true;
	    
	    try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
	        for (Product p : inventory) {
	            ps.setInt(1, p.getId());
	            ps.setString(2, p.getName());
	            ps.setDouble(3, p.getWholesalerPrice().getValue());
	            ps.setBoolean(4, p.isAvailable());
	            ps.setInt(5, p.getStock());
	            ps.addBatch();
	        }
	        
	        int[] results = ps.executeBatch(); 
	        for (int result : results) {
	            if (result == Statement.EXECUTE_FAILED) {
	                success = false;
	                break;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        success = false;
	    }
	    return success;
	}

	@Override
	public void addProduct(Product product) {
		String query = "INSERT INTO inventory (name, public_price, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, product.getName());
			ps.setDouble(2, product.getPublicPrice().getValue());
			ps.setDouble(3, product.getWholesalerPrice().getValue());
			ps.setBoolean(4, product.isAvailable());
			ps.setInt(5, product.getStock());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateProduct(Product product) {
		String query = "UPDATE inventory SET name = ?, public_price = ?, wholesalerPrice = ?, available = ?, stock = ? WHERE id = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, product.getName());
			ps.setDouble(2, product.getPublicPrice().getValue());
			ps.setDouble(3, product.getWholesalerPrice().getValue());
			ps.setBoolean(4, product.isAvailable());
			ps.setInt(5, product.getStock());
			ps.setInt(6, product.getId()); 
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteProduct(int productId) {
		String query = "DELETE FROM inventory WHERE id = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, productId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Employee getEmployee(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}
}