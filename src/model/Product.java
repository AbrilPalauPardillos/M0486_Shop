package model;

import javax.persistence.*; 

@Entity 
@Table(name = "inventory") 
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "stock")
    private int stock;

    @Column(name = "available")
    private boolean available;

    @Transient 
    private Amount publicPrice;
    @Transient
    private Amount wholesalerPrice;

    public Product() {}

    public Product(int id, String name, Amount publicPrice, Amount wholesalerPrice, boolean available, int stock) {
        this.id = id;
        this.name = name;
        this.publicPrice = publicPrice;
        this.wholesalerPrice = wholesalerPrice;
        this.price = wholesalerPrice.getValue(); 
        this.available = available;
        this.stock = stock;
    }

    // Getters y Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public Amount getWholesalerPrice() { return new Amount(price); }
    public Amount getPublicPrice() { return new Amount(price * 1.5); }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + ", stock=" + stock + "]";
    }
}