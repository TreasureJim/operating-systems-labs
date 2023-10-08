package src;

public class Stopwatch extends Thread {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Main thread. Waiting for stopwatch thread...");
		Stopwatch thread = new Stopwatch();
		thread.start();
		thread.join();
		System.out.println("Main thread. Finished stopwatch thread.");
	}

	public void run() {
		System.out.println("Stopwatch thread. Stopwatch starts now!");
		for (int i = 10; i <= 1000 * 60; i += 10) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.err.println("Stopwatch thread interrupted. ");
				return;
			}
			System.out.println("Stopwatch thread. Elapsed " + i / 1000.0 + " seconds.");
		}
	}
}