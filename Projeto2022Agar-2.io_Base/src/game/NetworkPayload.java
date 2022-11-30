package game;

import java.io.Serializable;

public class NetworkPayload implements Serializable {

	public int Ola;
	public String Teste;
	
	public NetworkPayload(int ola, String teste) {
		this.Ola = ola;
		this.Teste = teste;
	}
}