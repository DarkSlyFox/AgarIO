package game;

import environment.Coordinate;
import environment.Direction;

public class AutomaticPlayer extends Player {
			
	public AutomaticPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return false;
	}

	@Override
	public void run() {
		
		game.addPlayerToGame(this);
		
		while(this.canMove()) {
			try {
				Coordinate randomCoordinate = Direction.getRandomDirection();
				Coordinate newCoordinate = this.getCurrentCell().getPosition().sumCoordinates(randomCoordinate);

				game.movePlayer(this, newCoordinate);
				
				Thread.sleep(SLEEP_CYCLE);
			}
			catch (InterruptedException e) {
				System.out.println("Bateu nos bolos (1)");
			}
		}
	}
}