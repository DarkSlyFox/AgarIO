package communication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import environment.Cell;
import environment.Coordinate;
import game.ClientPlayer;
import game.Game;
import game.NetworkPayload;
import game.Player;

public class Client {

	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;
	private Game game;
	
	public Client(Game game) {
		this.game = game;
	}
	
	private class DealWithServer extends Thread {
		
		private String address;
		private int port;
		private int up;
		private int down;
		private int left;
		private int right;
		
		public DealWithServer(String address, int port, int up, int down, int left, int right) {
			this.address = address;
			this.port = port;
			this.up = up;
			this.down = down;
			this.left = left;
			this.right = right;
		}
		
		@Override
		public void run() {
			try {
				connectToServer(address, port);
				
				System.out.println("Inicio de cliente:");
				
				while(!socket.isClosed()) {
					try {
						receiveMessages();
						sendMessages();
						
						Thread.sleep(Game.REFRESH_INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			catch (IOException e)
			{
				System.out.println(e.getMessage());
			}
			finally {
				try {
					socket.close();
				}
				catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	public void startClient(String address, int port, int up, int down, int left, int right) {
		
		try {
			new DealWithServer(address, port, up, down, left, right).start();
		}
		catch(Exception e) {
			System.out.print(e.getMessage());
		}
	}

	private void connectToServer(String address, int port) throws IOException {
		InetAddress endereco = InetAddress.getByName(address);
		System.out.println("Endereco:" + endereco);
		socket = new Socket(endereco, port);
		System.out.println("Socket:" + socket);
		
		in = new ObjectInputStream(socket.getInputStream());

		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())), true);
	}
	
	private class MockClientPlayer extends Player {

		private boolean isHumanPlayer;
		
		public MockClientPlayer(int id, Game game, byte strength, boolean isHumanPlayer) {
			super(id, game, strength);
			this.isHumanPlayer = isHumanPlayer;
		}

		@Override
		public boolean isHumanPlayer() {
			return this.isHumanPlayer;
		}
	}

	private void receiveMessages() throws IOException {
		
		try {
			System.out.println("Mensagem recebida pelo servidor:");
			NetworkPayload net = (NetworkPayload)in.readObject();
			System.out.println(net);
			
			// testar uma thread autonoma para reduzir o overhead.
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					try {
//						Thread.sleep(Game.REFRESH_INTERVAL);
						
						for (int x = 0; x < Game.DIMX; x++) {
							for (int y = 0; y < Game.DIMY; y++) {
								Cell currentCell = game.getCell(new Coordinate(x, y));
								currentCell.clearPlayer();
							}
						}
//					} catch (InterruptedException e) {
//						System.out.println(e);
//					}
//				}
//			}).start();
			
			for (ClientPlayer cp : net.clientPlayers) {
				Cell currentCell = game.getCell(new Coordinate(cp.x, cp.y));
				currentCell.setPlayer(new MockClientPlayer(1, game, cp.strength, cp.isPlayer)); 
			}

			System.out.println("Fim de repopulação.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessages() throws IOException {

		System.out.println("Envio de mensagem por parte do cliente:");
		out.println("direção");
		out.flush();
	}
}