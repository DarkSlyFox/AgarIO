package game;

import java.io.Serializable;
import java.util.List;

public class NetworkPayload implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public final List<ClientPlayer> clientPlayers;
	
	public NetworkPayload(List<ClientPlayer> clientPlayers) {
		this.clientPlayers = clientPlayers;
	}
}