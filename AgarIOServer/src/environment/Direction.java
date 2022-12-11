package environment;

import java.awt.event.KeyEvent;
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
	
	public static Direction translateDirection(String key) {
		return Direction.valueOf(key.toUpperCase());
	}
	
	public static Direction translateDirection(int keyCode) {
		switch(keyCode) {
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				return environment.Direction.LEFT;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				return environment.Direction.RIGHT;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				return environment.Direction.UP;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				return environment.Direction.DOWN;
		}
		throw new UnsupportedOperationException("Não existe configurado.");
	}
}