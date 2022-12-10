package gui;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import communication.Client;
import environment.Direction;
import game.Game;

public class ClientGuiMain implements Observer {
	private JFrame frame = new JFrame("Client");
	private BoardJComponent boardGui;

	public ClientGuiMain() {
		super();
		Game.getInstance().addObserver(this);
		buildGui();
	}

	private void buildGui() {
		boardGui = new BoardJComponent();
		frame.add(boardGui);

		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLocationRelativeTo(null);
	}

	public void init()  {
		frame.setVisible(true);
		
		new Client(boardGui).startClient("localhost", 8080,
				KeyEvent.VK_W,
				KeyEvent.VK_S,
				KeyEvent.VK_A,
				KeyEvent.VK_D);
		
		new Client(boardGui).startClient("localhost", 8080,
				KeyEvent.VK_UP,
				KeyEvent.VK_DOWN,
				KeyEvent.VK_LEFT,
				KeyEvent.VK_RIGHT);
	}
	
	public Direction getLastDirection() {
		return boardGui.getLastPressedDirection();
	}
	
	public void clearLastPressedDirection() {
		boardGui.clearLastPressedDirection();
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) {
		ClientGuiMain game = new ClientGuiMain();
		game.init();
	}
}