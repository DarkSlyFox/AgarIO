package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import environment.Coordinate;
import environment.Direction;
import game.ClientPlayer;
import game.Game;

/**
 * Creates a JComponent to display the game state.
 * At the same time, this is also a KeyListener for itself: when a key is pressed,
 * attribute lastPressedDirection is updated accordingly. This feature is a demo to
 * better understand how to deal with keys pressed, useful for the remote client.
 * This feature is not helpful for the main application and should be ignored.
 * This class does not need to be edited.
 * @author luismota
 *
 */
public class BoardJComponent extends JComponent implements KeyListener {
	
	private Image obstacleImage = new ImageIcon("obstacle.png").getImage();
	private Image humanPlayerImage= new ImageIcon("abstract-user-flat.png").getImage();
	private Direction lastPressedDirection=null;
	private int lastKeyPressed = -1;
	
	public BoardJComponent() {
		setFocusable(true);
		addKeyListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		double cellHeight=getHeight()/(double)Game.DIMY;
		double cellWidth=getWidth()/(double)Game.DIMX;
		
		for (int y = 1; y < Game.DIMY; y++) {
			g.drawLine(0, (int)(y * cellHeight), getWidth(), (int)(y* cellHeight));
		}
		
		for (int x = 1; x < Game.DIMX; x++) {
			g.drawLine( (int)(x * cellWidth),0, (int)(x* cellWidth), getHeight());
		}
		
		ArrayList<ClientPlayer> clientPlayers = Game.getInstance().getPlayers();
		
		if (clientPlayers != null) {
			// Vou buscar todos os players e percorrer, só preciso de pintar se tiver players.
			for(ClientPlayer player : clientPlayers) {
	
				// Fill yellow if there is a dead player
				if(player.strength == 0) {
					g.setColor(Color.YELLOW);
					g.fillRect((int)(player.x* cellWidth), 
							(int)(player.y * cellHeight),
							(int)(cellWidth),(int)(cellHeight));
					g.drawImage(obstacleImage, (int)(player.x * cellWidth), (int)(player.y*cellHeight), 
							(int)(cellWidth),(int)(cellHeight), null);
					// if player is dead, don'd draw anything else?
					continue;
				}
				// Fill green if it is a human player
				if(player.isPlayer) {
					g.setColor(Color.GREEN);
					g.fillRect((int)(player.x* cellWidth), 
							(int)(player.y * cellHeight),
							(int)(cellWidth),(int)(cellHeight));
					// Custom icon?
					g.drawImage(humanPlayerImage, (int)(player.x * cellWidth), (int)(player.y*cellHeight), 
							(int)(cellWidth),(int)(cellHeight), null);
				}
				g.setColor(new Color(player.id * 1000));
				((Graphics2D) g).setStroke(new BasicStroke(5));
				Font font = g.getFont().deriveFont( (float)cellHeight);
				g.setFont( font );
				String strengthMarking=(player.strength == 10? "X":""+player.strength);
				g.drawString(strengthMarking,
						(int) ((player.x + .2) * cellWidth),
						(int) ((player.y + .9) * cellHeight));
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		lastPressedDirection = Direction.translateDirection(e.getKeyCode());
		lastKeyPressed = e.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//ignore
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Ignored...
	}

	public Direction getLastPressedDirection() {
		return lastPressedDirection;
	}
	
	public int getLastKeyPressed() {
		return lastKeyPressed;	
	}

	public void clearLastPressedDirection() {
		lastPressedDirection=null;
		lastKeyPressed = -1;
	}
}
