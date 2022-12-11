package environment;

public class CountDownLatch {

	private int count;
	
	public CountDownLatch(int count) {
		this.count = count;
	}

	public synchronized void await() throws InterruptedException {
		while(count > 0) wait();
	}
	
	public synchronized void countDown() {
		count--;
		if (count == 0) notifyAll();
	}
}