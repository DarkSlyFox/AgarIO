package game;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import environment.Cell;
import environment.Coordinate;

public class Game extends Observable {

	public static final int DIMY = 10;
	public static final int DIMX = 10;
	private static final int NUM_PLAYERS = 20;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME = 3;

	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 5000;
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
	
	public List<ClientPlayer> getClientPlayers() {

		List<ClientPlayer> clientPlayers = new ArrayList<>();
		
		for (int x = 0; x < Game.DIMX; x++) {
			for (int y = 0; y < Game.DIMY; y++) {
				Player p = board[x][y].getPlayer();
				Coordinate coordinate = board[x][y].getPosition();
				
				if (board[x][y].isOcupied()) {
					clientPlayers.add(new ClientPlayer(p.getStrength(),
							coordinate.x, coordinate.y,
							p.isHumanPlayer(), board[x][y].isOcupied()));
				}
				else {
//					clientPlayers.add(new ClientPlayer((byte)0,
//							coordinate.x, coordinate.y,
//							false, board[x][y].isOcupied()));
				}
			}
		}
		
		return clientPlayers;
	}
	
	public void loadPlayers() {
		
		int i;
		
		for (i = 0; i != NUM_PLAYERS; i++) {
			new Thread(new AutomaticPlayer(i + 1, this, (byte)generateRandomNumberBetween(1,3))).start();
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
