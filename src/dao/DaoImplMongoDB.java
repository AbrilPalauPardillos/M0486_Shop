package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.*;

public class DaoImplMongoDB implements Dao {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> inventoryColl;
    private MongoCollection<Document> usersColl;
    private MongoCollection<Document> historicalColl;

    @Override
    public void connect() {
        this.mongoClient = new MongoClient("localhost", 27017);
        this.database = mongoClient.getDatabase("shop"); 
        this.usersColl = database.getCollection("users"); 
        this.inventoryColl = database.getCollection("inventory");
        this.historicalColl = database.getCollection("historical_inventory");
    
    }

    @Override
    public void disconnect() {
        if (mongoClient != null) mongoClient.close();
    }

    @Override
    public List<Product> getInventory() {
        List<Product> products = new ArrayList<>();
        for (Document doc : inventoryColl.find()) { 
            products.add(documentToProduct(doc));
        }
        return products;
    }

    @Override
    public boolean writeInventory(List<Product> inventory) {
        try {
            for (Product p : inventory) {
                Document doc = productToDocument(p);
                doc.append("event", "MANUAL_EXPORT");
                doc.append("created_at", new java.util.Date()); // [cite: 37]
                
                historicalColl.insertOne(doc);
            }
            return true;
        } catch (Exception e) {
            return false; // [cite: 79]
        }
    }

    @Override
    public void addProduct(Product product) {
        Document doc = productToDocument(product);
        
        inventoryColl.insertOne(doc); 
        
        Document historyDoc = productToDocument(product);
        historyDoc.append("event", "INSERT");
        historyDoc.append("created_at", new java.util.Date()); 
        historicalColl.insertOne(historyDoc);
    }

    @Override
    public void updateProduct(Product product) {
        inventoryColl.updateOne(
            com.mongodb.client.model.Filters.eq("id", product.getId()),
            com.mongodb.client.model.Updates.set("stock", product.getStock())
        );
        Document historyDoc = productToDocument(product);
        historyDoc.append("event", "UPDATE_STOCK");
        historyDoc.append("created_at", new java.util.Date()); 
        historicalColl.insertOne(historyDoc);
    }

    @Override
    public void deleteProduct(int productId) {
        inventoryColl.deleteOne(com.mongodb.client.model.Filters.eq("id", productId));
        
        System.out.println("Producto " + productId + " eliminado de inventory, pero conservado en historical.");
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        System.out.println("Buscando en Mongo -> ID: " + employeeId + " PW: " + password);
        
        Document query = new Document("employeeId", employeeId)
                            .append("password", password);
        
        Document doc = usersColl.find(query).first();
        
        if (doc != null) {
            System.out.println("¡Usuario encontrado!");
            Employee emp = new Employee();
            emp.setEmployeeId(doc.getInteger("employeeId"));
            emp.setPassword(doc.getString("password"));
            return emp;
        }
        
        System.out.println("Usuario NO encontrado.");
        return null;
    }

    // --- MÉTODOS AUXILIARES DE CONVERSIÓN ---
    private Product documentToProduct(Document doc) {
        Product p = new Product();
        
        p.setId(doc.getInteger("id") != null ? doc.getInteger("id") : 0);
        p.setName(doc.getString("name") != null ? doc.getString("name") : "Sin nombre");
        
        Document priceObj = (Document) doc.get("wholesalerPrice");
        if (priceObj != null && priceObj.get("value") != null) {
            p.setPrice(((Number) priceObj.get("value")).doubleValue());
        } else {
            p.setPrice(0.0);
        }
        
        p.setStock(doc.getInteger("stock") != null ? doc.getInteger("stock") : 0);
        p.setAvailable(doc.getBoolean("available") != null ? doc.getBoolean("available") : false);
        
        return p;
    }

    private Document productToDocument(Product p) {
        Document price = new Document("value", p.getWholesalerPrice().getValue())
                            .append("currency", p.getWholesalerPrice().getCurrency());
        
        return new Document("id", p.getId())
                .append("name", p.getName())
                .append("wholesalerPrice", price)
                .append("available", p.isAvailable())
                .append("stock", p.getStock());
    }

	@Override
	public Employee getEmployee(int employeeId) {
		Document doc = usersColl.find(Filters.eq("employeeId", employeeId)).first();
	    if (doc != null) {
	        return new Employee(doc.getInteger("employeeId"), "Employee", doc.getString("password"));
	    }
	    return null;
	}

	}
