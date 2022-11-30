package game;

public class RealPlayer extends Player  {
	
	public RealPlayer(int id, Game game) {
		super(id, game, (byte)5);
	}

	public boolean isHumanPlayer() {
		return true;
	}

	@Override
	public void run() {
		game.addPlayerToGame(this);
		
		while(this.canMove()) {
			//TODO: Aqui na prática vamos obter a direção vinda da comunicação externa.
			
			
			

//			game.movePlayer(this, newCoordinate);
			
//			try {
//				Thread.sleep(this.SLEEP_CYCLE);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}