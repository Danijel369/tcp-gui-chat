package chatApp.controller;

import chatApp.core.Client;
import chatApp.view.ClientGUI;

public class InputHandler implements Runnable {

	private Client client;
	ClientGUI gui;

	public InputHandler(Client c, ClientGUI g) {
		this.client = c;
		this.gui = g;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {

			String msg = gui.getMessage();

			if (msg.equalsIgnoreCase("quit")) {
				client.getOut().println(msg);
				client.shutdown();
			} else {
				client.getOut().println(msg);
			}
			gui.getTextField().setText("");

		} catch (Exception e) {
			// TODO: handle exception
			client.shutdown();
		}

	}
}
