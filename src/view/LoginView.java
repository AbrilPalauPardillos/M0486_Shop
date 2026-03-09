package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import exception.LimitLoginException;
import model.Employee;
import utils.Constants;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class LoginView extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldEmployeeId;
	private JTextField textFieldPassword;
	private JButton btnLogin;
	private int counterErrorLogin = 0; 

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView frame = new LoginView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoginView() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblEmployeeId = new JLabel("Número de empleado");
		lblEmployeeId.setBounds(55, 50, 130, 14);
		contentPane.add(lblEmployeeId);

		textFieldEmployeeId = new JTextField();
		textFieldEmployeeId.setBounds(65, 75, 176, 20);
		contentPane.add(textFieldEmployeeId);
		textFieldEmployeeId.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(55, 115, 130, 14);
		contentPane.add(lblPassword);

		textFieldPassword = new JTextField();
		textFieldPassword.setBounds(65, 140, 176, 20);
		contentPane.add(textFieldPassword);
		textFieldPassword.setColumns(10);

		btnLogin = new JButton("Acceder");
		btnLogin.setBounds(308, 208, 89, 23);
		contentPane.add(btnLogin);
		btnLogin.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogin) {
			String employeeId = textFieldEmployeeId.getText();
			String password = textFieldPassword.getText();

			if (employeeId.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Usuario y contraseña son obligatorios", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				if (counterErrorLogin >= Constants.MAX_LOGIN_TIMES) {
					throw new LimitLoginException("Error login superado", counterErrorLogin);
				}

				Employee employee = new Employee();
				boolean logged = employee.login(Integer.parseInt(employeeId), password);

				if (logged) {
					ShopView shop = new ShopView();
					shop.setExtendedState(NORMAL);
					shop.setVisible(true);
					dispose();					
				} else {
					counterErrorLogin++;
					JOptionPane.showMessageDialog(null, "Usuario o password incorrectos", "Error",
							JOptionPane.ERROR_MESSAGE);
					
					textFieldEmployeeId.setText("");
					textFieldPassword.setText("");
				}

			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "El ID de empleado debe ser un número", "Error", 
						JOptionPane.ERROR_MESSAGE);
			} catch (LimitLoginException ex) {
				JOptionPane.showMessageDialog(null, "Error login, superados los " + counterErrorLogin + " intentos", "Error",
						JOptionPane.ERROR_MESSAGE);
				dispose();
			}
		}
	}
}