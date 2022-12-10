package game;

import java.io.Serializable;
import java.util.List;

public class NetworkPayload implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public final int x;
	public final int y;
	public final List<ClientPlayer> clientPlayers;
	
	public NetworkPayload(List<ClientPlayer> clientPlayers, int x, int y) {
		this.clientPlayers = clientPlayers;
		this.x = x;
		this.y = y;
	}
}