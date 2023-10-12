package memory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;

public class MemoryManager {

	private int numberOfPages;
	private int pageSize; // In bytes
	private int numberOfFrames;
	private RandomAccessFile pageFile;
	private int pageFaultCounter = 0;
	private int myPageReplacementAlgorithm = 0;

	// The variables that we manage
	private int[] pageTable; // -1 if page is not in physical memory
	private byte[] ram; // physical memory RAM

	public MemoryManager(int numberOfPages, int pageSize, int numberOfFrames, String pageFile,
			int pageReplacementAlgorithm) {

		this.numberOfPages = numberOfPages;
		this.pageSize = pageSize;
		this.numberOfFrames = numberOfFrames;
		myPageReplacementAlgorithm = pageReplacementAlgorithm;

		initPageTable();
		ram = new byte[this.numberOfFrames * pageSize];

		try {

			this.pageFile = new RandomAccessFile(pageFile, "r");

		} catch (FileNotFoundException ex) {
			System.out.println("Can't open page file: " + ex.getMessage());
		}
	}

	private void initPageTable() {
		pageTable = new int[numberOfPages];
		for (int n = 0; n < numberOfPages; n++) {
			pageTable[n] = -1;
		}
	}

	public byte readFromMemory(int logicalAddress) {
		int pageNumber = getPageNumber(logicalAddress);
		int offset = getPageOffset(logicalAddress);

		if (pageTable[pageNumber] == -1) {
			pageFault(pageNumber);
		} else {
			// For LRU
			LRUUsePage(pageNumber);
		}

		int frame = pageTable[pageNumber];
		int physicalAddress = frame * pageSize + offset;
		byte data = ram[physicalAddress];

		System.out.print("Virtual address: " + logicalAddress);
		System.out.print(" Physical address: " + physicalAddress);
		System.out.println(" Value: " + data);
		return data;
	}

	private int getPageNumber(int logicalAddress) {
		return Math.floorDiv(logicalAddress, this.pageSize);
	}

	private int getPageOffset(int logicalAddress) {
		return logicalAddress % this.pageSize;
	}

	private void pageFault(int pageNumber) {
		this.pageFaultCounter++;

		if (myPageReplacementAlgorithm == Seminar3.NO_PAGE_REPLACEMENT)
			handlePageFault(pageNumber);

		if (myPageReplacementAlgorithm == Seminar3.FIFO_PAGE_REPLACEMENT)
			handlePageFaultFIFO(pageNumber);

		if (myPageReplacementAlgorithm == Seminar3.LRU_PAGE_REPLACEMENT)
			handlePageFaultLRU(pageNumber);

		readFromPageFileToMemory(pageNumber);
	}

	private void readFromPageFileToMemory(int pageNumber) {
		try {
			int frame = pageTable[pageNumber];
			this.pageFile.seek(pageNumber * pageSize);
			for (int b = 0; b < pageSize; b++)
				ram[frame * pageSize + b] = this.pageFile.readByte();
		} catch (IOException ex) {
		}
	}

	public int getNumberOfPageFaults() {
		return this.pageFaultCounter;
	}

	private void handlePageFault(int pageNumber) {
		/**
		 * Implement by student in task one
		 * This is the simple case where we assume same size of physical and logical
		 * memory.
		 * nextFreeFramePosition is used to point to next free frame position
		 */
		this.pageTable[pageNumber] = pageNumber;
	}

	private int initialFrameAllocator = 0;

	private Queue<Integer> FIFOQueue = new LinkedList<>();

	private void handlePageFaultFIFO(int pageNumber) {
		/**
		 * Implement by student in task two
		 * this solution allows different size of physical and logical memory
		 * page replacement using FIFO
		 */
		int availableFrame = -1;
		if (FIFOQueue.size() == this.numberOfFrames) {
			Integer oldPage = this.FIFOQueue.poll();
			availableFrame = this.pageTable[oldPage];
			this.pageTable[oldPage] = -1;
		} else {
			availableFrame = this.initialFrameAllocator++;
		}

		this.pageTable[pageNumber] = availableFrame;
		this.FIFOQueue.add(pageNumber);
	}

	private LinkedList<Integer> LRUPagePriorities = new LinkedList<>();

	private void LRUUsePage(int pageNumber) {
		/**
		 * If I were to implement this with efficiency in mind I definitely wouldn't use
		 * this function.
		 * I think the best way to implement this would be to replace the LinkedList
		 * with a doubly linked list and have an array storing the pointer to all of the
		 * page numbers.
		 * When I move the pageNumber to the beginning I can just remove it from the
		 * spot and assign the references of each pageNumber next to it in the doubly
		 * linked list to each other and then move it to the front.
		 */
		this.LRUPagePriorities.removeFirstOccurrence(pageNumber);
		this.LRUPagePriorities.addFirst(pageNumber);
	}

	private void handlePageFaultLRU(int pageNumber) {
		/**
		 * Implement by student in task three
		 * this solution allows different size of physical and logical memory
		 * page replacement using LRU (least recently used)
		 */
		int availableFrame;
		if (this.LRUPagePriorities.size() == this.numberOfFrames) {
			int oldPage = this.LRUPagePriorities.removeLast();
			availableFrame = this.pageTable[oldPage];
			this.pageTable[oldPage] = -1;
		} else {
			availableFrame = this.initialFrameAllocator++;
		}
		this.pageTable[pageNumber] = availableFrame;
		this.LRUPagePriorities.addFirst(pageNumber);
	}
}
