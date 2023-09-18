package readerwriter_Fixed;

public class RWLock {

	private int writing = 0;
	private int reading = 0;

	public synchronized void acquireRead() {
		while (writing > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		reading++;
	}

	public synchronized void releaseRead() {
		reading--;
		notifyAll();
	}

	public synchronized void acquireWrite() {
		while (writing > 0 || reading > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		writing++;
	}

	public synchronized void releaseWrite() {
		writing--;
		notifyAll();
	}

}
