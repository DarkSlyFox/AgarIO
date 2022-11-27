package environment;

import java.util.Random;

public enum Direction {
	
	UP(0,-1),DOWN(0,1),LEFT(-1,0),RIGHT(1,0);
	
	private Coordinate vector;
	
	private static Direction[] directions = Direction.values();
	
	Direction(int x, int y) {
		vector = new Coordinate(x, y);
	}
	
	public Coordinate getVector() {
		return vector;
	}
	
	public static Coordinate getRandomDirection()  {
		return directions[new Random().nextInt(directions.length)].vector;
    }
}