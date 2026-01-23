package model;

import javax.persistence.*;
import java.util.Date;

@Entity // 
@Table(name = "historical_inventory")
public class ProductHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_product")
    private int idProduct;

    private String name;
    private int stock;
    private double price;
    private boolean available;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt; // [cite: 12]

    public ProductHistory() {}

    public ProductHistory(Product p) {
        this.idProduct = p.getId();
        this.name = p.getName();
        this.stock = p.getStock();
        this.price = p.getPrice();
        this.available = p.isAvailable();
        this.createdAt = new Date(); // Fecha actual [cite: 12]
    }
    
    // Getters y Setters...
}