package philosophers;

import java.util.concurrent.locks.ReentrantLock;

public class Table {

	public int nbrOfChopsticks;
	private boolean chopstick[]; // true if chopstick[i] is available
	public ReentrantLock pickingUpLock = new ReentrantLock(true);

	public Table(int nbrOfSticks) {
		nbrOfChopsticks = nbrOfSticks;
		chopstick = new boolean[nbrOfChopsticks];
		for (int i = 0; i < nbrOfChopsticks; i++) {
			chopstick[i] = true;
		}
	}

	private synchronized boolean pickupChopstick(int n) throws InterruptedException {
		while (!chopstick[n])
			wait();
		chopstick[n] = false;
		return true;
	}

	public boolean getLeftChopstick(int n) throws InterruptedException {
		return pickupChopstick(n);
	}

	public boolean getRightChopstick(int n) throws InterruptedException {
		return pickupChopstick((n + 1) % nbrOfChopsticks);
	}

	public synchronized void releaseLeftChopstick(int n) {
		chopstick[n] = true;
		notifyAll();
	}

	public synchronized void releaseRightChopstick(int n) {
		chopstick[(n + 1) % nbrOfChopsticks] = true;
		notifyAll();
	}
}
