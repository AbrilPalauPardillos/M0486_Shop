package dao;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Product;
import model.Amount;
import model.Employee;

public class DaoImplFile implements Dao {

	@Override
	public void addProduct(Product product) {
	}

	@Override
	public void updateProduct(Product product) {
	}

	@Override
	public void deleteProduct(int productId) {
	}
	
	@Override
	public List<Product> getInventory() {
		List<Product> inventory = new ArrayList<>();
		File file = new File(
				System.getProperty("user.dir") + File.separator + "files" + File.separator + "inputInventory.txt");

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] sections = line.split(";");
				String name = "";
				double price = 0.0;
				int stock = 0;

				for (String section : sections) {
					String[] data = section.split(":");
					if (data[0].contains("Product"))
						name = data[1];
					if (data[0].contains("Wholesaler Price"))
						price = Double.parseDouble(data[1]);
					if (data[0].contains("Stock"))
						stock = Integer.parseInt(data[1]);
				}
				inventory.add(new Product(name, new Amount(price), true, stock)); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return inventory;
	}

	@Override
	public boolean writeInventory(List<Product> inventory) {
        LocalDate today = LocalDate.now();
        String fileName = "inventory_" + today.toString() + ".txt";
        File file = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            int index = 1;
            for (Product product : inventory) {
                pw.println(index + ";Product:" + product.getName() + ";Stock:" + product.getStock() + ";");
                index++;
            }
            pw.println("Total number of products:" + inventory.size() + ";");
            return true;
        } catch (IOException e) {
            System.err.println("Error al exportar inventario: " + e.getMessage());
            return false;
        }
    }

	@Override
	public void connect() {
	}

	@Override
	public void disconnect() {
	}

	@Override
	public model.Employee getEmployee(int employeeId) {
		return null;
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}
}