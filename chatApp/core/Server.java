package chatApp.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private ArrayList<ConnectionHandler> connections;
	private ServerSocket server;
	private int cnt = 0; // client counter
	private boolean done;
	private ExecutorService pool;

	public Server() {
		connections = new ArrayList();
		done = false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			server = new ServerSocket(9999);
			System.out.println("The server is running...");
			pool = Executors.newCachedThreadPool();
			while (!done) {
				Socket client = server.accept();
				ConnectionHandler handler = new ConnectionHandler(client, cnt++);
				connections.add(handler);
				pool.execute(handler);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			shutdown();
		}

	}

	public void shutdown() {
		// TODO Auto-generated method stub
		done = true;
		if (!server.isClosed()) {
			try {
				for (ConnectionHandler ch : connections) {
					ch.shutdown();
				}
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

	}

	public void broadcast(String msg) {
		for (ConnectionHandler ch : connections) {
			if (ch != null) {
				ch.sendMsg(msg);

			}
		}
	}

	class ConnectionHandler implements Runnable {

		private Socket client;
		private BufferedReader in;
		private PrintWriter out;
		private int id;

		public ConnectionHandler(Socket client, int id) {
			this.client = client;
			this.id = id;
		}

		public void shutdown() {
			// TODO Auto-generated method stub
			try {
				in.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.close();
			if (!client.isClosed()) {
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
				broadcast("user #" + this.id + " CONNECTED");

				String message;
				while ((message = in.readLine()) != null) {

					if (message.equalsIgnoreCase("quit")) {

						broadcast("user #" + this.id + " DISCONNECTED");
						out.println(message);
						shutdown();
					} else {
						broadcast("#" + this.id + ": " + message);
					}

				}

			} catch (Exception e) {
				// TODO: handle exception
				shutdown();
			}

		}

		public void sendMsg(String msg) {
			out.println(msg);
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server s = new Server();
		s.run();
	}

}
