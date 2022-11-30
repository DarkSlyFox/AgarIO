package gui;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import communication.Client;
import communication.Server;
import environment.Direction;
import game.Game;
import game.Player;
import game.RealPlayer;

public class GameGuiMain implements Observer {
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;

	public GameGuiMain() {
		super();
		game = new Game();
		game.addObserver(this);

		buildGui();
	}

	private void buildGui() {
		boardGui = new BoardJComponent(game);
		frame.add(boardGui);

		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLocationRelativeTo(null);
	}

	public void init()  {
		frame.setVisible(true);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(Game.INITIAL_WAITING_TIME);
					game.loadPlayers();
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
		}).start();	
		
		try {
			new Server(8080, game).startServing();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public Direction getLastDirection() {
		return boardGui.getLastPressedDirection();
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) {
		GameGuiMain game = new GameGuiMain();
		game.init();
	}

}
