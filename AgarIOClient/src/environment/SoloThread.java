package environment;

public class SoloThread extends Thread {

	private Thread p;
	
	public SoloThread(Thread p) {
		this.p = p;
	}
	
	@Override
	public void run() {
		try {
//			System.out.println("Começou à espera 2s.");
			Thread.sleep(2000);
//			System.out.println("Acabaram os 2s.");
			
			
//			p.notifyAll();
			p.interrupt();
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}
}