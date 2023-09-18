package philosophers;

public class Main {

	private static final int NUMBER_OF_PHILOSOPHERS = 5;

	public static void main(String[] args) {
		setupTableAndPhilosophers(NUMBER_OF_PHILOSOPHERS);
	}

	private static void setupTableAndPhilosophers(int numberOfPhilosophers) {
		Table table = new Table(numberOfPhilosophers);
		for (int i = 0; i < numberOfPhilosophers; i++) {
			Thread thread = new Thread(new Philosopher(i, table));
			thread.start();
		}

	}
}
