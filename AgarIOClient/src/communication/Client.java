package communication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import environment.Direction;
import game.Game;
import game.NetworkPayload;
import gui.BoardJComponent;

public class Client {

	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;
	private BoardJComponent boardJComponent;

	public Client(BoardJComponent boardJComponent) {
		this.boardJComponent = boardJComponent;
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

				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							// Vai correr a primeira vez e faz set ao jogo.
							NetworkPayload net = (NetworkPayload)in.readObject();
							Game.getInstance().setGameSize(net.x, net.y);
							
							receiveMessages();
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}).start();
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							while(!socket.isClosed()) {

								if (canCommunicate()) {
									sendMessages();
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			catch (IOException e)
			{
				System.out.println(e.getMessage());
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		private boolean canCommunicate() {
			int keyPressed = boardJComponent.getLastKeyPressed();
			return keyPressed == up || keyPressed == down || keyPressed == left || keyPressed == right;
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
		socket = new Socket(endereco, port);
		
		in = new ObjectInputStream(socket.getInputStream());

		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())), true);
	}

	private void receiveMessages() throws IOException {
		
		while(!socket.isClosed()) {
			try {
				NetworkPayload net = (NetworkPayload)in.readObject();
				Game.getInstance().loadPlayers(net.clientPlayers);
			} catch (ClassNotFoundException e) {
				socket.close();
			} catch (IOException e) {
				socket.close();
			}
		}
	}

	private void sendMessages() throws IOException {

		Direction d = boardJComponent.getLastPressedDirection();
		
		if (d != null) {
			out.println(d);
			out.flush();
			boardJComponent.clearLastPressedDirection();
		}
	}
}