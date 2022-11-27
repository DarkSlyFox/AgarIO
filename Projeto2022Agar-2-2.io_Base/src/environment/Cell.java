package environment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.Game;
import game.Player;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player = null;
	
	public Lock lock = new ReentrantLock();
	public Condition isPositionOccupied = lock.newCondition();
	
	public Cell(Coordinate position, Game g) {
		super();
		this.position = position;
		this.game = g;
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOcupied() {
		return player != null;
	}

	public Player getPlayer() {
		return player;
	}

	// TODO: Should not be used like this in the initial state: cell might be occupied, must coordinate this operation
	public void setPlayer(Player playerWhoWantsToMove) {
		
		lock.lock();
		
		try {
			
			while (this.isOcupied()) {
				System.out.println(playerWhoWantsToMove.toString());
				game.notifyChange();
				isPositionOccupied.await();
			}
			
			playerWhoWantsToMove.setCoordinate(this.position);
			this.player = playerWhoWantsToMove;
			
			game.notifyChange();
			isPositionOccupied.signalAll();
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			lock.unlock();
		}
	}
	
	public void movePlayer(Player playerWhoWantsToMove) {
	
		Cell _oldCell = playerWhoWantsToMove.getCurrentCell();

		_oldCell.lock.lock();
		lock.lock();
		
		try {
			while (this.isOcupied() && !this.player.equals(playerWhoWantsToMove)) {
				System.out.println(playerWhoWantsToMove.toString());
				playerWhoWantsToMove.beginConflictWith(player);

				
				player.interrupt();
				playerWhoWantsToMove.interrupt();
				
				game.notifyChange();
												
				_oldCell.isPositionOccupied.await();
				isPositionOccupied.await();
			}
			
			if (this.player == null) {

				playerWhoWantsToMove.setCoordinate(this.position);				

				this.player = playerWhoWantsToMove;
					
				_oldCell.clearPlayer();
				
				game.notifyChange();

				isPositionOccupied.signalAll();
				_oldCell.isPositionOccupied.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			_oldCell.lock.unlock();
			lock.unlock();
		}
	}
	
	public void clearPlayer() {
		this.player = null;
	}
}
