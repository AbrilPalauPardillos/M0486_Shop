package model;

import javax.persistence.Column; 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id;
import javax.persistence.Table; 
import javax.persistence.Transient; 

@Entity 
@Table(name = "inventory")
public class Product {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "id") 
    private Integer id; 

    @Column(name = "name") 
    private String name;

    @Column(name = "wholesalerPrice") 
    private Double price; 

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "available") 
    private Boolean available;

    @Transient 
    private Amount publicPrice;

    @Transient 
    private Amount wholesalerPrice;

    public Product() {
    }

    public Product(int id, String name, Amount publicPrice, Amount wholesalerPrice, boolean available, int stock) {
        this.id = id;
        this.name = name;
        this.publicPrice = publicPrice;
        this.wholesalerPrice = wholesalerPrice;
        this.price = wholesalerPrice.getValue();
        this.available = available;
        this.stock = stock;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean isAvailable() {
        return available != null && available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }


    public Amount getPublicPrice() {
        return new Amount(this.price != null ? this.price * 1.5 : 0.0);
    }

    public void setPublicPrice(Amount publicPrice) {
        this.publicPrice = publicPrice;
    }

    public Amount getWholesalerPrice() {
        return new Amount(this.price != null ? this.price : 0.0);
    }

    public void setWholesalerPrice(Amount wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
        if (wholesalerPrice != null) {
            this.price = wholesalerPrice.getValue();
        }
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", wholesalerPrice=" + price + ", stock=" + stock + "]";
    }
}