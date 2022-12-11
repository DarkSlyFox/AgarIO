package game;

import java.io.Serializable;

public class ClientPlayer implements Serializable {

	private static final long serialVersionUID = 1L;
	public int id;
	public byte strength;
	public int x;
	public int y;
	public boolean isPlayer;
	public boolean hasPlayer;
	
	public ClientPlayer(int id, byte strength, int x, int y, boolean isPlayer, boolean hasPlayer) {
		this.id = id;
		this.strength = strength;
		this.x = x;
		this.y = y;
		this.isPlayer = isPlayer;
		this.hasPlayer = hasPlayer;
	}
	
	@Override
	public String toString() {
		return "Player:" + this.x + "," + this.y + " IsPlayer: " + this.isPlayer + " Strength: " + this.strength;
	}
}

