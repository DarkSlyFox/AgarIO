package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import game.Game;
import game.NetworkPayload;
import game.RealPlayer;

public class Server {

	private final int port;
	private final Game game;

	public Server(int port, Game g) {
		this.port = port;
		this.game = g;
	}
	
	private class DealWithClient extends Thread {
		
		private final int clientPort;
		
		public DealWithClient(Socket socket) throws IOException {
			this.clientPort = Integer.parseInt(socket.getRemoteSocketAddress().toString().split(":")[1]);
			doConnections(socket);
		}
		
		private BufferedReader in;
		
		@Override
		public void run() {
			try {
				serve();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private ObjectOutputStream out;

		private void doConnections(Socket socket) throws IOException {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		
		private void serve() throws IOException {
			
			new RealPlayer(this.clientPort, game).start();
			
			while (true) {
				sendMessages();
				receiveMessages();
			}
		}
		
		private void receiveMessages() throws IOException {
			if (in.ready()) {
				System.out.println("Mensagem recebida servidor vinda do cliente: " + in.readLine());
				
			}
		}
		
		private void sendMessages() throws IOException {
			try {
				NetworkPayload payload = new NetworkPayload(2, "ola");
				out.writeObject(payload);
				out.flush();
				
				System.out.println("Mensagem enviada pelo servidor: ");
				
				Thread.sleep(Game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startServing() throws IOException {
		ServerSocket ss = new ServerSocket(this.port);

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