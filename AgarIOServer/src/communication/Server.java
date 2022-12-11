package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import environment.Coordinate;
import environment.Direction;
import game.ClientPlayer;
import game.Game;
import game.NetworkPayload;
import game.Player;
import game.RealPlayer;

public class Server {

	private final int port;
	private final Game game;

	public Server(int port, Game game) {
		this.port = port;
		this.game = game;
	}
	
	private class DealWithClient extends Thread {
		
		private final int clientPort;
		private final RealPlayer realPlayer;
		private final Socket socket;
		
		public DealWithClient(Socket socket) throws IOException {
			this.socket = socket;
			
			// Só para lhe dar um ID.
			this.clientPort = Integer.parseInt(socket.getRemoteSocketAddress().toString().split(":")[1]);
			this.realPlayer =  new RealPlayer(this.clientPort, game);
			this.realPlayer.start();
			
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
						
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						sendMessages();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						receiveMessages();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		private void receiveMessages() throws IOException {
				
			while(!socket.isClosed()) {
				if (in.ready()) {
					this.realPlayer.setDirection(Direction.translateDirection(in.readLine()));
				}
			}
		}
		
		private void sendMessages() throws IOException {
			
			while(!socket.isClosed()) {
				try {
					ArrayList<ClientPlayer> clientPlayers = game.getClientPlayers();
					NetworkPayload payload = new NetworkPayload(clientPlayers, Game.DIMX, Game.DIMY, game.getGameOver());
					 
					out.writeObject(payload);
					out.flush();

					closeConnectionIfNeeded();
					
					Thread.sleep(Game.REFRESH_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void closeConnectionIfNeeded() {
			if (game.getGameOver()) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void startServing() throws IOException {
		ServerSocket ss = new ServerSocket(this.port);

		try {
			while(true) {
				Socket socket = ss.accept();
				new DealWithClient(socket).start();
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