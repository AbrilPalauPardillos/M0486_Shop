package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import main.Shop;
import utils.Constants;

public class ShopView extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private Shop shop;

	private JPanel contentPane;
	private JButton btnShowCash;
	private JButton btnAddProduct;
	private JButton btnAddStock;
	private JButton btnRemoveProduct;
	private JButton btnExportInventory; 

	public ShopView() {
		setTitle("MiTenda.com - Menú principal");
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		// CERAR TIENDA
		shop = new Shop();
		shop.loadInventory();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("Seleccione o pulse una opción:");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTitle.setBounds(57, 20, 300, 20);
		contentPane.add(lblTitle);

		// 1. CONTAR CAJA
		btnShowCash = new JButton("1. Contar caja");
		btnShowCash.setHorizontalAlignment(SwingConstants.LEFT);
		btnShowCash.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnShowCash.setBounds(99, 90, 236, 40);
		btnShowCash.addActionListener(this);
		contentPane.add(btnShowCash);

		// 2. AÑADIR PRODUCTO
		btnAddProduct = new JButton("2. Añadir producto");
		btnAddProduct.setHorizontalAlignment(SwingConstants.LEFT);
		btnAddProduct.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddProduct.setBounds(99, 140, 236, 40);
		btnAddProduct.addActionListener(this);
		contentPane.add(btnAddProduct);

		// 3. AÑADIR STOCK
		btnAddStock = new JButton("3. Añadir stock");
		btnAddStock.setHorizontalAlignment(SwingConstants.LEFT);
		btnAddStock.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddStock.setBounds(99, 190, 236, 40);
		btnAddStock.addActionListener(this);
		contentPane.add(btnAddStock);

		// 9. ELIMINAR PRODUCTO
		btnRemoveProduct = new JButton("9. Eliminar producto");
		btnRemoveProduct.setHorizontalAlignment(SwingConstants.LEFT);
		btnRemoveProduct.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRemoveProduct.setBounds(99, 240, 236, 40);
		btnRemoveProduct.addActionListener(this);
		contentPane.add(btnRemoveProduct);

		// 0. EXPORTAR INVENTORIO
		btnExportInventory = new JButton("0. Exportar inventario");
		btnExportInventory.setHorizontalAlignment(SwingConstants.LEFT);
		btnExportInventory.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnExportInventory.setBounds(99, 40, 236, 40);
		btnExportInventory.addActionListener(this);
		contentPane.add(btnExportInventory);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnExportInventory) {
			exportInventory();
		}
		if (e.getSource() == btnShowCash) {
			this.openCashView();						
		}
		if (e.getSource() == btnAddProduct) {
			this.openProductView(Constants.OPTION_ADD_PRODUCT);						
		}
		if (e.getSource() == btnAddStock) {
			this.openProductView(Constants.OPTION_ADD_STOCK);				
		}
		if (e.getSource() == btnRemoveProduct) {
			this.openProductView(Constants.OPTION_REMOVE_PRODUCT);				
		}
		
	}

	private void exportInventory() {
	    boolean ok = shop.writeInventory();
	    if (ok) {
	        StringBuilder resumen = new StringBuilder("Inventario exportado correctamente.\n\n");
	        int i = 1;
	        for (var p : shop.getInventory()) {
	            resumen.append(i)
	                   .append(". ")
	                   .append(p.getName())
	                   .append(" - Stock: ")
	                   .append(p.getStock())
	                   .append("\n");
	            i++;
	        }

	        JOptionPane.showMessageDialog(
	            this,
	            resumen.toString(),
	            "Éxito",
	            JOptionPane.INFORMATION_MESSAGE
	        );
	    } else {
	        JOptionPane.showMessageDialog(
	            this,
	            "Error al exportar el inventario.",
	            "Error",
	            JOptionPane.ERROR_MESSAGE
	        );
	    }
	}


	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == '0') {
			exportInventory();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ShopView frame = new ShopView();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * open dialog to show shop cash
	 */
	public void openCashView() {
		// create a dialog Box
        CashView dialog = new CashView(shop);  
        // setsize of dialog
        dialog.setSize(400, 400);
        // set visibility of dialog
        dialog.setModal(true);
        dialog.setVisible(true);
	}
	
	/**
	 * open dialog to add/remove/stock product
	 */
	public void openProductView(int option) {
		// create a dialog Box
		ProductView dialog = new ProductView(shop, option);  
        // setsize of dialog
        dialog.setSize(400, 400);
        // set visibility of dialog
        dialog.setModal(true);
        dialog.setVisible(true);
	}
}
