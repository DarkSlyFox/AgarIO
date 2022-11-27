package environment;

import game.Game;

public class Coordinate {
	public final int x;
	public final int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coordinate sumCoordinates(Coordinate directionCoordination) {

		int _x = Math.min(Game.DIMX - 1, Math.max(0, this.x + directionCoordination.x));
		int _y = Math.min(Game.DIMY - 1, Math.max(0, this.y + directionCoordination.y));
		
		return new Coordinate(_x, _y);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		Coordinate other = (Coordinate) obj;
		return other.x==x && other.y == y;
	}
	
	public double distanceTo(Coordinate other) {
		double dx = y - other.y;
		double dy = x - other.x;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public Coordinate translate(Coordinate vector) {
		return new Coordinate(x+vector.x, y+vector.y);
	}
}
