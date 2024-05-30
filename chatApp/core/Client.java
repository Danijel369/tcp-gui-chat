package chatApp.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.EventListener;

import javax.swing.SwingUtilities;

import chatApp.controller.InputHandler;
import chatApp.view.ClientGUI;

public class Client implements Runnable {

	private Socket client;
	boolean done;
	BufferedReader in;
	private PrintWriter out;
	ClientGUI frame;
	String msg;
	Thread t;

	Client() {

		frame = new ClientGUI(this);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Socket client = new Socket("localhost", 9999);
			setOut(new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true));
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			InputHandler inHandler = new InputHandler(this, frame);
			t = new Thread(inHandler);
			String inMessage;
			while ((inMessage = in.readLine()) != null) {

				// System.out.println(inMessage);
				Reciever r = new Reciever(inMessage);
				Thread rt = new Thread(r);
				rt.start();

			}

		} catch (Exception e) {
			// TODO: handle exception
			shutdown();
		}
	}

	class Reciever implements Runnable {

		String msg;

		public Reciever(String inMsg) {
			// TODO Auto-generated constructor stub
			msg = inMsg;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			frame.recieveMessage(msg);
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Client client = new Client();
		client.run();

	}

	public void shutdown() {
		// TODO Auto-generated method stub
		done = true;
		try {
			in.close();
			getOut().close();
			if (!client.isClosed()) {
				client.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

}
