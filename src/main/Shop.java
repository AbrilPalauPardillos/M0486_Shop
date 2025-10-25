package main;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import dao.Dao;
import dao.DaoImplFile;
import model.*;
import exception.LimitLoginException;

public class Shop {

	private static final double TAX_RATE = 1.56;

	private Dao dao;
	private ArrayList<Product> inventory;
	private ArrayList<Sale> sales;
	private Amount cash;
	private int numberProducts;
	private int numberSales;

	public Shop() {
		dao = new DaoImplFile();
		inventory = new ArrayList<>();
		sales = new ArrayList<>();
		cash = new Amount(0.0);
		numberProducts = 0;
		numberSales = 0;
	}

	// MÉTODOS DE INVENTARIO

	// CARGAR
	public void loadInventory() {
		this.inventory = new ArrayList<>(dao.getInventory());
	}

	// EXPORTAR
	public boolean writeInventory() {
		return dao.writeInventory(inventory);
	}

	// AÑADIR
	public void addProduct() {
		if (isInventoryFull()) {
			System.out.println("No se pueden añadir más productos");
			return;
		}
		Scanner scanner = new Scanner(System.in);
		System.out.print("Nombre: ");
		String name = scanner.nextLine();
		System.out.print("Precio mayorista: ");
		double wholesalerPrice = scanner.nextDouble();
		System.out.print("Stock: ");
		int stock = scanner.nextInt();

		addProduct(new Product(name, new Amount(wholesalerPrice), true, stock));
	}

	public void addProduct(Product product) {
		if (isInventoryFull()) {
			System.out.println("No se pueden añadir más productos, se ha alcanzado el máximo de " + inventory.size());
			return;
		}
		inventory.add(product);
		numberProducts++;
	}

	// QUITAR
	public void removeProduct() {
		if (inventory.size() == 0) {
			System.out.println("Inventario vacío, no se puede eliminar nada");
			return;
		}
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);

		if (product != null) {
			if (inventory.remove(product)) {
				System.out.println("El producto " + name + " ha sido eliminado");
			} else {
				System.out.println("No se ha encontrado el producto " + name);
			}
		} else {
			System.out.println("No se ha encontrado el producto " + name);
		}
	}

	public void addStock() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);

		if (product != null) {
			System.out.print("Seleccione la cantidad a añadir: ");
			int stock = scanner.nextInt();
			product.setStock(product.getStock() + stock);
			System.out.println("Stock actualizado a " + product.getStock());
		} else {
			System.out.println("Producto no encontrado");
		}
	}

	public void showInventory() {
		System.out.println("Contenido actual de la tienda:");
		for (Product product : inventory) {
			if (product != null)
				System.out.println(product);
		}
	}

	public boolean isInventoryFull() {
		return numberProducts >= 10;
	}

	public Product findProduct(String name) {
		for (Product p : inventory) {
			if (p != null && p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	// MÉTODOS DE CAJA Y VENTAS

	public void showCash() {
		System.out.println("Dinero actual: " + cash);
	}

	public void sale() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Nombre cliente:");
		String nameClient = sc.nextLine();
		Client client = new Client(nameClient);

		ArrayList<Product> shoppingCart = new ArrayList<>();
		Amount totalAmount = new Amount(0.0);

		while (true) {
			System.out.println("Introduce nombre del producto (0 para terminar):");
			String name = sc.nextLine();
			if (name.equals("0"))
				break;

			Product product = findProduct(name);
			if (product != null && product.isAvailable()) {
				totalAmount.setValue(totalAmount.getValue() + product.getPublicPrice().getValue());
				product.setStock(product.getStock() - 1);
				shoppingCart.add(product);

				if (product.getStock() == 0)
					product.setAvailable(false);

				System.out.println("Producto añadido con éxito");
			} else {
				System.out.println("Producto no encontrado o sin stock");
			}
		}

		totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
		System.out.println("Venta total: " + totalAmount);

		if (!client.pay(totalAmount)) {
			System.out.println("Cliente debe: " + client.getBalance());
		}

		Sale sale = new Sale(client, shoppingCart, totalAmount);
		sales.add(sale);
		cash.setValue(cash.getValue() + totalAmount.getValue());
		numberSales++;
	}

	public void showSales() {
		System.out.println("Lista de ventas:");
		for (Sale sale : sales) {
			if (sale != null)
				System.out.println(sale);
		}
	}

	public void showSalesAmount() {
		Amount total = new Amount(0.0);
		for (Sale sale : sales) {
			if (sale != null)
				total.setValue(total.getValue() + sale.getAmount().getValue());
		}
		System.out.println("Total ventas: " + total);
	}

	// OTROS MÉTODOS

	public ArrayList<Product> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Product> inventory) {
		this.inventory = inventory;
	}

	public ArrayList<Sale> getSales() {
		return sales;
	}

	public Amount getCash() {
		return cash;
	}

	public int getNumberProducts() {
		return numberProducts;
	}

	public int getNumberSales() {
		return numberSales;
	}
}
