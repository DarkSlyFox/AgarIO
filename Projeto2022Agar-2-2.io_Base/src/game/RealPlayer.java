package game;

import environment.Coordinate;
import environment.Direction;

public class RealPlayer extends Player {
	
	public RealPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return true;
	}

	@Override
	public void run() {
		game.addPlayerToGame(this);
		
		while(!Thread.interrupted()) {
			if (this.isPlayerAlive() && !this.hasMaxStrength()) {

				//TODO: Aqui na prática vamos obter a direção vinda da comunicação externa.
				Coordinate randomCoordinate = Direction.getRandomDirection();
				Coordinate newCoordinate = this.getCurrentCell().getPosition().sumCoordinates(randomCoordinate);
	
				game.movePlayer(this, newCoordinate);
				
				try {
					Thread.sleep(this.getCurrentStrength() * Game.REFRESH_INTERVAL);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}