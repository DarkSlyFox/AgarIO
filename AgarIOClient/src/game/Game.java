package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import environment.Coordinate;

public class Game extends Observable {

	public static int DIMY = 0;
	public static int DIMX = 0;
	
	private static Game INSTANCE = null;
	
	private ArrayList<ClientPlayer> players;
	
	public static Game getInstance() {
		
		if (INSTANCE == null) {
			INSTANCE = new Game();
		}
		return INSTANCE;
	}
	
	private Game() {}
	
	public void setGameSize(int x, int y) {
		
		if (DIMX == 0 && DIMY == 0) {
			Game.DIMX = x;
			Game.DIMY = y;
		}
	}

	public void loadPlayers(ArrayList<ClientPlayer> players) {
		this.players = players;
		notifyChange();
	}
	
	public ArrayList<ClientPlayer> getPlayers() {
		return this.players;
	}

	/**	
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}
}