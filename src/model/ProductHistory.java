package model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "historical_inventory")
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_product")
    private Integer idProduct;

    private String name;
    private Integer stock;

    @Column(name = "wholesalerPrice") // Coincide con tu SQL
    private Double price;

    private Boolean available;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    public ProductHistory() {}

    public ProductHistory(Product p) {
        this.idProduct = p.getId();
        this.name = p.getName();
        this.stock = p.getStock();
        this.price = p.getPrice();
        this.available = p.isAvailable();
        this.createdAt = new Date();
    }
    
    // Getters y Setters...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getIdProduct() { return idProduct; }
    public void setIdProduct(Integer idProduct) { this.idProduct = idProduct; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}