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
	
	protected Cell[][] board;
	
	private boolean gameOver = false;
	
	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];
	
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y));
	}
	
	public void addPlayerToGame(ClientPlayer player) {
		getCell(new Coordinate(player.x, player.y)).setPlayer(player);
	}
	
	public void cleanAllBoard() {
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y));
	}
	
	public boolean gameOver() {
		return gameOver;
	}

	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}

	/**	
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}
}
