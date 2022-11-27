package game;



import java.util.Random;

import environment.Cell;
import environment.Coordinate;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread {

	protected Game game;

	private int id;

	private byte currentStrength;
	protected byte originalStrength;
	
	private Coordinate coordinate;

	// TODO: get player position from data in game
	public synchronized Cell getCurrentCell() {
		return game.getCell(this.coordinate);
	}

	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game = game;
		currentStrength = strength;
		originalStrength = strength;
	}
	
	public void notifyPlayer() {
		this.notifyAll();
	}
	
	public abstract boolean isHumanPlayer();
	
	public void beginConflictWith(Player p) {
		int _playerWhoWantsToMoveStrength = getCurrentStrength();
		int _playerInCurrentCellStrength = p.getCurrentStrength();
		
		if (_playerWhoWantsToMoveStrength > _playerInCurrentCellStrength) {
			this.currentStrength += _playerInCurrentCellStrength;
			p.currentStrength = 0;
		}
		else if (_playerWhoWantsToMoveStrength < _playerInCurrentCellStrength) {
			p.currentStrength += _playerWhoWantsToMoveStrength;
			this.currentStrength = 0;
		}
		else {
			if(new Random().nextBoolean()) {
				this.currentStrength += _playerInCurrentCellStrength;
				p.currentStrength = 0;
			}
			else {
				p.currentStrength += _playerWhoWantsToMoveStrength;
				this.currentStrength = 0;
			}
		}
	}
	
	public boolean isPlayerAlive() {
		return this.currentStrength != 0;
	}
	
	public boolean isDead() {
		return this.currentStrength == 0;
	}
	
	public boolean hasMaxStrength() {
		return this.currentStrength == Game.MAX_STRENGTH;
	}
	
	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
		+ "]";
	}
	
	public void setCoordinate(Coordinate newCoordinate) {
		this.coordinate = newCoordinate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}

	public int getIdentification() {
		return id;
	}
}
