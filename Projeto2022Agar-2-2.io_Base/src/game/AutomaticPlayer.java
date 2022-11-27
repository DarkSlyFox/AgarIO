package game;

import environment.Coordinate;
import environment.Direction;

public class AutomaticPlayer extends Player {
	
	private Thread auto;
	
	public AutomaticPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return false;
	}

	@Override
	public void run() {
		game.addPlayerToGame(this);
		
		
		try {
			while(!this.isInterrupted()) {
				if (this.isPlayerAlive() && !this.hasMaxStrength()) {

					Coordinate randomCoordinate = Direction.getRandomDirection();
					Coordinate newCoordinate = this.getCurrentCell().getPosition().sumCoordinates(randomCoordinate);

					game.movePlayer(this, newCoordinate);
					
					Thread.sleep(this.getCurrentStrength() * Game.REFRESH_INTERVAL);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.print("Interuped");
//			try {
//				Thread.sleep(2000);
//				
//				
//				
//				
//				
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
		}
	}
}