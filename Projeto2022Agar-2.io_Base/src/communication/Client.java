package communication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import game.Game;
import game.NetworkPayload;

public class Client {

	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;
	
	private String address;
	private int port;
	private int up;
	private int down;
	private int left;
	private int right;
	
	public Client(String address, int port, int up, int down, int left, int right) {
		this.address = address;
		this.port = port;
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
	}
	
	private class DealWithServer extends Thread {
		
		@Override
		public void run() {
			try {
				connectToServer();
				
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
	
	public void startClient() {
		
		try {
			new DealWithServer().start();
		}
		catch(Exception e) {
			System.out.print(e.getMessage());
		}
	}

	private void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(address);
		System.out.println("Endereco:" + endereco);
		socket = new Socket(endereco, port);
		System.out.println("Socket:" + socket);
		
		in = new ObjectInputStream(socket.getInputStream());

		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())), true);
	}

	private void receiveMessages() throws IOException {
		
		try {
			System.out.println("Mensagem recebida pelo servidor:");
			NetworkPayload net = (NetworkPayload)in.readObject();
			System.out.println(net);
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