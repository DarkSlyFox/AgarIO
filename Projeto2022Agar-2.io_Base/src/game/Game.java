package game;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import environment.Cell;
import environment.Coordinate;
import environment.CountDownLatch;

public class Game extends Observable {

	public static final int DIMY = 30;
	public static final int DIMX = 30;
	private static final int NUM_PLAYERS = 80;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME = 3;

	public static final long REFRESH_INTERVAL = 400;
	public static final byte MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;
	public static final int MAX_STRENGTH = 10;
	
	protected Cell[][] board;
	
	private CountDownLatch counter;
	private ArrayList<Thread> threads;
	private boolean gameOver = false;
	
	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];
	
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y),this);
		
		counter = new CountDownLatch(NUM_FINISHED_PLAYERS_TO_END_GAME);
		threads = new ArrayList<>();
	}
	
	public void init() {
		
		loadPlayers();
		
		try {
			counter.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		gameOver = true;
		
		for(Thread e : threads) {
			e.interrupt();
		}
	}
	
	public boolean getGameOver() {
		return gameOver;
	}
	
	public void addPlayerToGame(Player player) {
		getRandomCell().setPlayer(player);	
		threads.add(player);
	}
	
	public void movePlayer(Player player, Coordinate newCoordination) {
		this.getCell(newCoordination).movePlayer(player);
	}
	
	public void addWinner() {
		counter.countDown();
	}
	
	public ArrayList<ClientPlayer> getClientPlayers() {

		ArrayList<ClientPlayer> clientPlayers = new ArrayList<>();
		
		for (int x = 0; x < Game.DIMX; x++) {
			for (int y = 0; y < Game.DIMY; y++) {
				Player p = board[x][y].getPlayer();
				Coordinate coordinate = board[x][y].getPosition();
				
				if (board[x][y].isOcupied()) {
					clientPlayers.add(new ClientPlayer(p.getIdentification(), p.getStrength(),
							coordinate.x, coordinate.y,
							p.isHumanPlayer(), board[x][y].isOcupied()));
				}
			}
		}
		
		return clientPlayers;
	}
	
	public void loadPlayers() {
		for (int i = 0; i != NUM_PLAYERS; i++) {
			new AutomaticPlayer(i + 1, this, (byte)generateRandomNumberBetween(1,MAX_INITIAL_STRENGTH)).start();
		}
	}

	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}
	
	public int generateRandomNumberBetween(int min, int max) {
		return new Random().nextInt(max - min) + min;
	}

	/**	
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public Cell getRandomCell() {	
		return getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY))); 
	}
}
