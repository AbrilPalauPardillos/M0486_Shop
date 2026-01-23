package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import model.Product;
import model.ProductHistory;
import model.Employee;
import java.util.List;
import java.util.ArrayList;

public class DaoImplHibernate implements Dao { 
    private SessionFactory sessionFactory;
    private Session session;

    @Override
    public void connect() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        session = sessionFactory.openSession();
    }

    @Override
    public void disconnect() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    @Override
    public List<Product> getInventory() {
        return session.createQuery("FROM Product", Product.class).list();
    }

    @Override
    public void addProduct(Product product) { 
        Transaction tx = session.beginTransaction();
        session.save(product);
        tx.commit();
    }

    @Override
    public void updateProduct(Product product) {
        Transaction tx = session.beginTransaction();
        session.update(product);
        tx.commit();
    }

    @Override
    public void deleteProduct(int productId) { 
        Transaction tx = session.beginTransaction();
        Product p = session.get(Product.class, productId);
        if (p != null) session.delete(p);
        tx.commit();
    }

    @Override
    public boolean writeInventory(List<Product> inventory) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (Product p : inventory) {
                ProductHistory history = new ProductHistory(p);
                session.save(history);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        return session.createQuery("FROM Employee WHERE employeeId = :id AND password = :pass", Employee.class)
                      .setParameter("id", employeeId)
                      .setParameter("pass", password)
                      .uniqueResult();
    }

    @Override public Employee getEmployee(int employeeId) { return null; }
}