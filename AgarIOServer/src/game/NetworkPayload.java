package game;

import java.io.Serializable;
import java.util.ArrayList;

public class NetworkPayload implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Tamanho do jogo.
	public final int x;
	public final int y;
	
	public final boolean gameOver;
	public final ArrayList<ClientPlayer> clientPlayers;
	
	public NetworkPayload(ArrayList<ClientPlayer> clientPlayers, int x, int y, boolean gameOver) {
		this.clientPlayers = clientPlayers;
		this.x = x;
		this.y = y;
		this.gameOver = gameOver;
	}
}