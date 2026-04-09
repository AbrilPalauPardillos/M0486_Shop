package dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import model.Employee;
import model.Product;

public class DaoImplObjectDB implements Dao {
    
    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void connect() {
        try {
            emf = Persistence.createEntityManagerFactory("objects/shop.odb");
            em = emf.createEntityManager();
        } catch (Exception e) {
            System.err.println("Error al conectar con ObjectDB: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        Employee employee = null;
        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.employeeId = :id AND e.password = :pw", 
                Employee.class);
            query.setParameter("id", employeeId);
            query.setParameter("pw", password);
            
            employee = query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }

    @Override
    public List<Product> getInventory() {
        return new ArrayList<>();
    }

    @Override
    public boolean writeInventory(List<Product> inventory) {
        return false;
    }

    @Override
    public void addProduct(Product product) {}

    @Override
    public void updateProduct(Product product) {}

    @Override
    public void deleteProduct(int productId) {}

    @Override
    public Employee getEmployee(int employeeId) {
        return em.find(Employee.class, employeeId);
    }
}