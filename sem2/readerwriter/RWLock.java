package readerwriter;

public class RWLock {

	private int writeRequests = 0;
	private int writing = 0;
	private int reading = 0;

	public synchronized void acquireRead() {
		while (writing > 0 || writeRequests > 0) {
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
		writeRequests++;
		while (writing > 0 || reading > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		writeRequests--;
		writing++;
	}

	public synchronized void releaseWrite() {
		writing--;
		notifyAll();
	}

}
