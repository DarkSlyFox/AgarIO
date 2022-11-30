package game;


import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import environment.Cell;
import environment.Coordinate;

public class Game extends Observable {

	public static final int DIMY = 5;
	public static final int DIMX = 5;
	private static final int NUM_PLAYERS = 1;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME = 3;

	public static final long REFRESH_INTERVAL = 300;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 0;
	public static final int MAX_STRENGTH = 10;
	
	protected Cell[][] board;
	
	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];
	
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y),this);
	}
	
	/** 
	 * @param player 
	 */
	public void addPlayerToGame(Player player) {
		getRandomCell().setPlayer(player);		
	}
	
	public void movePlayer(Player player, Coordinate newCoordination) {
		this.getCell(newCoordination).movePlayer(player);
	}
	
	public void loadPlayers() {
		
//		try {
//			Thread.sleep(INITIAL_WAITING_TIME);
//		} catch (InterruptedException e) {}
		
		int i;
		
		for (i = 0; i != NUM_PLAYERS; i++) {
			new Thread(new AutomaticPlayer(i + 1, this, (byte)generateRandomNumberBetween(1,3))).start();
		}
		
		new Thread(new RealPlayer(i + 1, this)).start();
	}

	public synchronized Cell getCell(Coordinate at) {
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
