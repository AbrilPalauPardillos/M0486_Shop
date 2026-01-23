package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import dao.Dao;
import dao.DaoImplHibernate; 
import model.*;

public class Shop {

    private static final double TAX_RATE = 1.56;
    private Dao dao; //
    private ArrayList<Product> inventory;
    private ArrayList<Sale> sales;
    private Amount cash;
    private int numberProducts;
    private int numberSales;

    public Shop() {
        this.dao = new DaoImplHibernate(); 
        this.inventory = new ArrayList<>();
        this.sales = new ArrayList<>();
        this.cash = new Amount(0.0);
        this.numberProducts = 0;
        this.numberSales = 0;
    }

    public void loadInventory() {
        dao.connect();
        List<Product> loadedInventory = dao.getInventory(); // [cite: 49]
        if (loadedInventory != null) {
            this.inventory = new ArrayList<>(loadedInventory);
            this.numberProducts = this.inventory.size();
        }
        dao.disconnect();
    }

    public boolean writeInventory() {
        dao.connect();
        boolean result = dao.writeInventory(inventory); 
        dao.disconnect();
        return result;
    }

    public void addProduct(Product product) {
        if (isInventoryFull()) {
            System.out.println("No se pueden añadir más productos");
            return;
        }
        
        inventory.add(product);
        numberProducts++;
        
        dao.connect();
        dao.addProduct(product);
        dao.disconnect();
    }
    
    public void updateProduct(Product product) {
        dao.connect();
        dao.updateProduct(product); // [cite: 51]
        dao.disconnect();
    }

    public void deleteProduct(Product product) {
        if (inventory.remove(product)) {
            numberProducts--;
            dao.connect();
            dao.deleteProduct(product.getId()); //
            dao.disconnect();
        } else {
            System.out.println("Error al eliminar producto de la lista en memoria.");
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
            
            updateProduct(product); 
            
            System.out.println("Stock actualizado a " + product.getStock());
        } else {
            System.out.println("Producto no encontrado");
        }
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
            if (name.equals("0")) break;

            Product product = findProduct(name);
            if (product != null && product.isAvailable() && product.getStock() > 0) {
                totalAmount.setValue(totalAmount.getValue() + product.getPublicPrice().getValue());
                product.setStock(product.getStock() - 1);
                
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }
                
                updateProduct(product); 
                
                shoppingCart.add(product);
                System.out.println("Producto añadido con éxito");
            } else {
                System.out.println("Producto no encontrado o sin stock");
            }
        }

        totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
        if (!client.pay(totalAmount)) {
            System.out.println("Cliente debe: " + client.getBalance());
        }

        Sale sale = new Sale(client, shoppingCart, totalAmount);
        sales.add(sale);
        cash.setValue(cash.getValue() + totalAmount.getValue());
        numberSales++;
    }


    public Product findProduct(String name) {
        for (Product p : inventory) {
            if (p != null && p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public boolean isInventoryFull() {
        return numberProducts >= 100; 
    }

    public void showInventory() {
        System.out.println("Contenido actual de la tienda:");
        for (Product product : inventory) {
            if (product != null) System.out.println(product);
        }
    }

    public ArrayList<Product> getInventory() { return inventory; }
    public Amount getCash() { return cash; }
}