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
	protected long SLEEP_CYCLE;
	
	// TODO: get player position from data in game
	public Cell getCurrentCell() {
		return game.getCell(this.coordinate);
	}

	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game = game;
		this.currentStrength = strength;
		this.originalStrength = strength;
		this.SLEEP_CYCLE = strength * Game.REFRESH_INTERVAL;
	}
	
//	public void notifyPlayer() {
//		this.notifyAll();
//	}
	
	public abstract boolean isHumanPlayer();
	
	public void beginConflictWith(Player p) {
		byte _playerWhoWantsToMoveStrength = getCurrentStrength();
		byte _playerInCurrentCellStrength = p.getCurrentStrength();
		
		// Se o player que se quer mover tem + força que o que se encontra na célula.
		if (_playerWhoWantsToMoveStrength > _playerInCurrentCellStrength) {
			this.addStrength(_playerInCurrentCellStrength);
			p.currentStrength = 0;
		}
		else if (_playerWhoWantsToMoveStrength < _playerInCurrentCellStrength) {
			p.addStrength(_playerWhoWantsToMoveStrength);
			this.currentStrength = 0;
		}
		else {
			if(new Random().nextBoolean()) {
				this.addStrength(_playerInCurrentCellStrength);
				p.currentStrength = 0;
			}
			else {
				p.addStrength(_playerWhoWantsToMoveStrength);
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
	
	public boolean canMove() {
		return isPlayerAlive() && !hasMaxStrength() && !game.gameOver();
	}
	
	public boolean hasMaxStrength() {
		return this.currentStrength == Game.MAX_STRENGTH;
	}
	
	public byte getStrength() {
		return currentStrength;
	}
	
	private void addStrength(byte strength) {
		
		this.currentStrength = (byte)Math.min(Game.MAX_STRENGTH, strength + this.currentStrength);
		
		if (this.hasMaxStrength()) {
			game.addWinner();
		}
	}
	
	public String getPlayerName() {
		return "Player " + this.id;
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
