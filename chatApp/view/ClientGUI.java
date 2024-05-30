package chatApp.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import chatApp.controller.InputHandler;
import chatApp.core.Client;

public class ClientGUI extends JFrame {

	private JPanel contentPane;
	private JPanel panel;
	private JTextArea textArea;
	private JTextField textField;

	private Client client;
	public JButton btnSend;
	public boolean sendFlag = false;
	ClientGUI frame = this;
	private ExecutorService pool;

	/**
	 * Launch the application. public static void main(String[] args) {
	 * SwingUtilities.invokeLater(new Runnable() { public void run() { try {
	 * ClientGUI frame = new ClientGUI(null); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

	public ClientGUI(Client c) {

		this.client = c;
		setTitle("TCP chat application");
		getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBounds(25, 12, 414, 243);
		getContentPane().add(panel);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
		panel.add(textArea);

		JScrollPane scrollPane = new JScrollPane(textArea);
		panel.add(scrollPane);

		btnSend = new JButton("send");

		btnSend.setBounds(350, 266, 89, 20);
		getContentPane().add(btnSend);

		textField = new JTextField();
		textField.setBounds(24, 266, 321, 20);
		getContentPane().add(textField);
		textField.setColumns(10);

		setActions();
		setDefaultLookAndFeelDecorated(true);
		pack();
		setVisible(true);
		setSize(480, 337);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pool = Executors.newCachedThreadPool();
		
		
		
	}

	public JTextField getTextField() {
		return textField;
	}

	private void setActions() {

		frame.getRootPane().setDefaultButton(btnSend);

		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				String msg = textField.getText();
				InputHandler input = new InputHandler(client, frame);
				pool.execute(input);

				/*
				 * if (!client.t.isAlive()) { client.t.start(); } else {
				 * 
				 * }
				 */
			}

		});
		
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosed(WindowEvent event) {
		        clientExitProcedure();
		    }

			private void clientExitProcedure() {
				// TODO Auto-generated method stub
				client.shutdown();
			}
		});
		
	}

	public String getMessage() {
		// TODO Auto-generated method stub
		return textField.getText();
	}

	public void recieveMessage(String msg) {
		// TODO Auto-generated method stub
		textArea.setText(textArea.getText().concat(msg).concat("\n"));
	}

}
