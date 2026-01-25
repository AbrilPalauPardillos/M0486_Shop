package model;

import javax.persistence.*;

@Entity
@Table(name = "inventory")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id; // Cambiado a Integer para aceptar nulos

    @Column(name = "name")
    private String name;

    @Column(name = "wholesalerPrice") // Nombre exacto de tu tabla SQL
    private Double price; // Cambiado a Double para evitar el error de la imagen

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "available")
    private Boolean available;

    @Transient
    private Amount publicPrice;
    @Transient
    private Amount wholesalerPrice;

    public Product() {}

    public Product(int id, String name, Amount publicPrice, Amount wholesalerPrice, boolean available, int stock) {
        this.id = id;
        this.name = name;
        this.price = wholesalerPrice.getValue();
        this.available = available;
        this.stock = stock;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Boolean isAvailable() { return available != null && available; }
    public void setAvailable(Boolean available) { this.available = available; }

    // Compatibilidad con la lógica de Amount
    public Amount getWholesalerPrice() { return new Amount(this.price != null ? this.price : 0.0); }
    public Amount getPublicPrice() { return new Amount((this.price != null ? this.price : 0.0) * 1.5); }
}