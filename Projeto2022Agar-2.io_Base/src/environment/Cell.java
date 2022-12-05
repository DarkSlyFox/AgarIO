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

	public void setPlayer(Player playerWhoWantsToMove) {
		
		lock.lock();
		
		try {
			while (this.isOcupied()) {
				System.out.println("Jogador que pretende se mover " + playerWhoWantsToMove.getPlayerName());
				System.out.println("Jogador que ocupa o lugar " + this.player);
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
	
	public synchronized void movePlayer(Player playerWhoWantsToMove) {
	
		Cell _oldCell = playerWhoWantsToMove.getCurrentCell();

		_oldCell.lock.lock();
		lock.lock();
		
		try {
			// Em caso de não estar ocupado.
			if (!this.isOcupied()) {

				playerWhoWantsToMove.setCoordinate(this.position);				
				this.player = playerWhoWantsToMove;
				_oldCell.clearPlayer();
				_oldCell.isPositionOccupied.signalAll();
				game.notifyChange();
			}
			
			else if (!this.player.equals(playerWhoWantsToMove)) {
				
				System.out.println("Jogador OldPosition: " + playerWhoWantsToMove);
				System.out.println("Jogador NewPosition: " + this.player);
				
				if (this.player.isPlayerAlive() && !this.player.hasMaxStrength()) {
					System.out.println("Inicio de conflito entre jogadores");
					playerWhoWantsToMove.beginConflictWith(player);	
					game.notifyChange();
				}
				
				else if (this.player.isDead() && !playerWhoWantsToMove.isHumanPlayer()) {
					// Thread que vai colocar o player outra vez a mover-se.
					new SoloThread(Thread.currentThread()).start();
					
					try {
						wait();
					} catch (InterruptedException e) {
						System.out.println("acabou espera wait");
						return;
					}
				}
			}
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
