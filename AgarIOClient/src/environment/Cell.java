package environment;

import game.ClientPlayer;

public class Cell {
	private Coordinate position;
	private ClientPlayer player = null;

	public Cell(Coordinate position) {
		super();
		this.position = position;
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOcupied() {
		return player != null;
	}

	public ClientPlayer getPlayer() {
		return player;
	}

	public void setPlayer(ClientPlayer playerWhoWantsToMove) {
		this.player = playerWhoWantsToMove;
	}
	
	public void clearPlayer() {
		this.player = null;
	}
}
