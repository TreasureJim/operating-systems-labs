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

	public synchronized void printChopsticks() {
		for (int i = 0; i < nbrOfChopsticks; i++) {
			if (chopstick[i])
				System.out.print("T");
			else
				System.out.print("F");
		}
		System.out.println();
	}

	public synchronized void getLeftChopstick(int n) throws InterruptedException {
		while (!chopstick[n])
			wait();
		chopstick[n] = false;
		System.out.println("Philosopher " + n + " take left : chopstick " + n);
		this.printChopsticks();
	}

	public synchronized void getRightChopstick(int n) throws InterruptedException {
		int n1 = (n + 1) % nbrOfChopsticks;
		while (!chopstick[n1])
			wait();
		chopstick[n1] = false;
		System.out.println(
				"Philosopher " + n + " take right : chopstick " + ((n + 1) % this.nbrOfChopsticks));
		this.printChopsticks();
	}

	public synchronized void releaseLeftChopstick(int n) {
		chopstick[n] = true;
		notifyAll();
		System.out.println("Philosopher " + n + " drop left : chopstick " + n);
		this.printChopsticks();
	}

	public synchronized void releaseRightChopstick(int n) {
		chopstick[(n + 1) % nbrOfChopsticks] = true;
		notifyAll();
		System.out
				.println("Philosopher " + n + " drop right : chopstick " + ((n + 1) % this.nbrOfChopsticks));
		this.printChopsticks();
	}
}
