package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import game.Game;
import game.NetworkPayload;

public class Server {

	public final int Port;

	public Server(int port) {
		this.Port = port;
	}
	
	public class DealWithClient extends Thread {
		
		public DealWithClient(Socket socket) throws IOException {
			doConnections(socket);
		}
		
		private ObjectInputStream in;
		
		@Override
		public void run() {
			try {
				serve();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private ObjectOutputStream out;

		void doConnections(Socket socket) throws IOException {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		
		private void serve() throws IOException {
			while (true) {
				receiveMessages();
				sendMessages();
			}
		}
		
		public void receiveMessages() throws IOException {
			System.out.println("Mensagem recebida servidor vinda do cliente:");
		}
		
		public void sendMessages() throws IOException {
			try {
				NetworkPayload payload = new NetworkPayload(2, "ola");
				out.writeObject(payload);
				
				Thread.sleep(Game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startServing() throws IOException {
		ServerSocket ss = new ServerSocket(this.Port);
		
		try {
			while(true) {
				Socket socket = ss.accept();
				new DealWithClient(socket).start();
				System.out.println("Servidor iniciou.");
			}
		}
		catch(Exception e) {
			System.out.print(e.getMessage());
		}
		finally {
			ss.close();
		}
	}
}