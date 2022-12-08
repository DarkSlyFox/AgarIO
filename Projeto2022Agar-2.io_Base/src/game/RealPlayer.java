package game;

import environment.Coordinate;
import environment.Direction;

public class RealPlayer extends Player  {
	
	private Direction lastDirection;
	
	public RealPlayer(int id, Game game) {
		super(id, game, (byte)5);
	}

	public boolean isHumanPlayer() {
		return true;
	}
	
	public void setDirection(Direction d) {
		lastDirection = d;
	}
	
	@Override
	public void run() {
		game.addPlayerToGame(this);
				
		while(this.canMove()) {
			
			try {
				if (lastDirection != null) {
					Coordinate nextCoordinate = lastDirection.getVector();
					Coordinate newCoordinate = this.getCurrentCell().getPosition().sumCoordinates(nextCoordinate);
	
					game.movePlayer(this, newCoordinate);
					
					// Limpamos a última direção para ele ficar novamente em espera.
					lastDirection = null;
				}
				Thread.sleep(Game.REFRESH_INTERVAL);
			}
			catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}