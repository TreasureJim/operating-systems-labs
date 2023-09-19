package philosophers;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher implements Runnable {
	private int id;
	private Table table;

	public Philosopher(int id, Table table) {
		this.id = id;
		this.table = table;
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			try {
				System.out.println("Philosopher " + id + " thinks. Iteration " + i);
				Thread.sleep((int) (Math.random() * 100));

				table.getLeftChopstick(id);

				Thread.sleep((int) (Math.random() * 10));

				table.getRightChopstick(id);

				System.out.println("Philosopher " + id + " eats. Iteration " + i);
				Thread.sleep((int) (Math.random() * 100));

				table.releaseLeftChopstick(id);
				Thread.sleep((int) (Math.random() * 10));

				table.releaseRightChopstick(id);
				Thread.sleep((int) (Math.random() * 10));

			} catch (InterruptedException ex) {
				Logger.getLogger(Philosopher.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
