package communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

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

	public void runClient() {
		try {
			connectToServer();
			
			System.out.println("Inicio de cliente:");
			
			while(!socket.isClosed()) {
				receiveMessages();
				sendMessages();
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

	private void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(address);
		System.out.println("Endereco:" + endereco);
		socket = new Socket(endereco, port);
		System.out.println("Socket:" + socket);
		
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
		in = new ObjectInputStream(socket.getInputStream());
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
		out.write("direção");
	}
}