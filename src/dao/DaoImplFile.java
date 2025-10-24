package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Amount;
import model.Employee;
import model.Product;


public class DaoImplFile implements Dao {

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getInventory() {
		List<Product> inventory = new ArrayList<>();
        File f = new File(System.getProperty("user.dir") + File.separator + "files/inputInventory.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] sections = line.split(";");
                String name = "";
                double wholesalerPrice = 0.0;
                int stock = 0;
                for (String section : sections) {
                    String[] data = section.split(":");
                    if (data[0].trim().equalsIgnoreCase("Product")) {
                        name = data[1];
                    } else if (data[0].trim().equalsIgnoreCase("Wholesaler Price")) {
                        wholesalerPrice = Double.parseDouble(data[1]);
                    } else if (data[0].trim().equalsIgnoreCase("Stock")) {
                        stock = Integer.parseInt(data[1]);
                    }
                }
                inventory.add(new Product(name, new Amount(wholesalerPrice), true, stock));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return inventory;
    }

	@Override
	public boolean writeInventory(List<Product> inventory) {
		LocalDate today = LocalDate.now();
        String fileName = "inventory_" + today + ".txt";
        File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            int counter = 1;
            for (Product p : inventory) {
                pw.println(counter + ";Product:" + p.getName() + ";Stock:" + p.getStock() + ";");
                counter++;
            }
            pw.println("Total number of products:" + inventory.size() + ";");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
  	}
	
}