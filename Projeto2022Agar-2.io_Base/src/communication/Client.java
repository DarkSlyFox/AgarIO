package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import game.NetworkPayload;

public class Client {

	private ObjectInputStream in;
	private ObjectOutputStream out;
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

	public void runClient() {
		try {
			connectToServer();
			
			System.out.println("Inicio de cliente:");
			
			while(!socket.isClosed()) {
//				sendMessages();
				receiveMessages();
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

	void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(address);
		System.out.println("Endereco:" + endereco);
		socket = new Socket(endereco, port);
		System.out.println("Socket:" + socket);
		
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}

	void receiveMessages() throws IOException {
		
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

	void sendMessages() throws IOException {
		
	
	}
}