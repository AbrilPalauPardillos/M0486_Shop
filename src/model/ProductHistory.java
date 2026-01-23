package model;

import java.util.Date;
import javax.persistence.Column; 
import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity 
@Table(name = "historical_inventory") 
public class ProductHistory {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "id_product") 
    private int idProduct;

    @Column(name = "name") 
    private String name;

    @Column(name = "stock") 
    private int stock;

    @Column(name = "price") 
    private double price;

    @Column(name = "available") 
    private boolean available;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at") 
    private Date createdAt;

    public ProductHistory() {
    }

    public ProductHistory(Product p) {
        this.idProduct = p.getId(); 
        this.name = p.getName(); 
        this.stock = p.getStock(); 
        this.price = p.getPrice(); 
        this.available = p.isAvailable(); 
        this.createdAt = new Date(); 
    }

    // --- GETTERS Y SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ProductHistory [id=" + id + ", idProduct=" + idProduct + ", name=" + name + 
               ", stock=" + stock + ", price=" + price + ", createdAt=" + createdAt + "]";
    }
}