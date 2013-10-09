package view;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.*;

@SuppressWarnings("serial")
public class Username extends JDialog {
	private JPanel panel = new JPanel();
	private JLabel label = new JLabel("Username:");
	private JTextField username = new JTextField();
	private JButton proceed = new JButton("Proceed");
	
	public Username() {
		decorateWindow();
		createInterface();
	}
	
	private void decorateWindow() {
		setIcon();
		setModal(true);
		setSize(322, 106);
		setResizable(false);
		setTitle("Enter Username");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private void setIcon() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("images/icon.png");
		setIconImage(image);
	}
	
	private void createInterface() {
		label.setBounds(14, 14, 55, 15);
		username.setBounds(79, 8, 222, 25);
		proceed.setBounds(94, 42, 131, 25);

		panel.setBounds(0, 0, 322, 106);
		panel.setLayout(null);
		
		panel.add(label);
		panel.add(username);
		panel.add(proceed);
		add(panel);
	}
	
	public JTextField getUsername() {
		return username;
	}
	
	public JButton getProceed() {
		return proceed;
	}

}
