import java.util.ArrayList;
import java.util.HashMap;

/**
 * This handles memory allocation in paging mode
 * @author Jingjing and Mina
 *
 */
public class PagingMemoryManager extends MemoryManager {

	// initial data structure for tracking, we can discuss what to use for the
	// algorithm we want
	private HashMap<Integer, Process> activeProcessesMap;

	// memory simulation
	private int memorySize;
	private int totalPageNumber;
	private Page[] pages;

	// keeping track of pages in use
	private ArrayList<Page> pagesInUse;

	// keeping track of errors
	private int totalInteralFragmentation = 0;
	private int noMemoryNumber = 0;

	// this should always be 0 since it is fixed size paging, there is no
	// external fragmentation
	private final int extFragNumber = 0;

	/**
	 * Constructor
	 * 
	 * @param bytes
	 *            total size of memory available
	 */
	public PagingMemoryManager(int bytes) {

		this.memorySize = bytes;

		// calculate maximum possible number of pages
		int leftOverSize = bytes % 32;
		totalInteralFragmentation += leftOverSize;
		this.totalPageNumber = (bytes - leftOverSize) / 32;

		// initialize pages
		this.pages = new Page[totalPageNumber];

		// initializing that all pages are not used
		for (int i = 0; i < totalPageNumber; i++) {
			Page p = new Page(i, 0);

			// set it to a process that is does not exists, this is for
			// preventing null access exception
			p.setAssignedProcess(new Process(-1, -1));

			pages[i] = p;
		}

		// so far no page is in use
		this.pagesInUse = new ArrayList<Page>();

		// and no process is active
		this.activeProcessesMap = new HashMap<Integer, Process>();
	}

	/**
	 * allocate pages to the given process
	 * 
	 */
	public int allocate(int bytes, int pid, int text_size, int data_size,
			int heap_size) {

		// allocate this many bytes to the process with this id
		Process newProcess = new Process(pid, bytes);

		System.out.println("allocating process" + pid + " size " + bytes);

		// allocate memoroy, MATH.ceil stuff behaving weird, using direct
		// calculation
		int sizeOfLastPage = bytes % 32;
		int sizeNeededToFill = (32 - sizeOfLastPage);
		int numPagesNeeded = 0;
		if (sizeOfLastPage != 0) {
			numPagesNeeded = (bytes + sizeNeededToFill) / 32;
		} else {
			numPagesNeeded = (bytes) / 32;
		}

		System.out.println("numPageNeeded = " + numPagesNeeded);

		// check if sum of used pages and needed pages is over limit
		if (pagesInUse.size() + numPagesNeeded <= totalPageNumber) {

			// all pages needed will be found after the above check
			activeProcessesMap.put(pid, newProcess);

			System.out.println("allocating pages for process : "
					+ newProcess.getId());

			// initialize the pages array for the process to record
			Page[] pagesForThisProcess = new Page[numPagesNeeded];

			// look for a page for each page needed
			for (int i = 0; i < numPagesNeeded; i++) {
				int index = findNextAvailablePage();

				// Found a page now
				System.out.println("found page " + index + "for process"
						+ newProcess.getId());
				Page p = pages[index];

				// if this is the last page, it might not be fully utilized
				if (i == numPagesNeeded - 1) {
					p.setUsedSize(sizeOfLastPage);
					totalInteralFragmentation += sizeNeededToFill;
					newProcess.setIntFragInPaging(sizeNeededToFill);
				} else {

					// previous pages should all be using 32 bytes
					p.setUsedSize(32);
				}

				// let the page remember it's new fields: process, physical
				// location, and virtual location
				p.setAssignedProcess(newProcess);
				p.setVirtualPosition(i);
				pagesForThisProcess[i] = p;

				// mark this page as in use
				pagesInUse.add(p);
			}

			// record the list of pages in the process
			newProcess.setPages(pagesForThisProcess);
			return 1;

		} else {

			// used page + pages needed > total pages, not enough memory for
			// this process
			System.out.println("Not enough pages");
			noMemoryNumber++;
			return -1;
		}
	}

	/**
	 * Find the first available page and return its index
	 * 
	 * @return the page position, -1 if not found
	 */
	private int findNextAvailablePage() {
		for (int i = 0; i < totalPageNumber; i++) {
			if (!pages[i].isUsed()) {
				return i;
			}
		}

		// if not returned at this point, then all pages are being used
		System.out
				.println("tried to find next available page but did not find one");
		return -1;
	}

	/**
	 * remove a process with a given ID and free up it's memory
	 * 
	 * @param int pid: the process's ID
	 * @return int: 1 if successful, -1 if otherwise.
	 */
	public int deallocate(int pid) {
		System.out.println("deleting process" + pid);
		boolean success = false;

		// check if this process is really assigned anything in memory
		if (activeProcessesMap.containsKey(pid)) {

			// gather info needed
			Process targetProcess = activeProcessesMap.remove(pid);
			Page[] targetPages = targetProcess.getPages();
			int intFragToBeFreed = targetProcess.getIntFragInPaging();

			// free all the pages this process is using
			for (int i = 0; i < targetPages.length; i++) {
				Page ithPage = pages[i];
				ithPage.free();
				success = pagesInUse.remove(ithPage);

				System.out.println("removed page"
						+ ithPage.getPhysicalPosition());
			}

			// then free up the internal fragmentation this process caused
			totalInteralFragmentation -= intFragToBeFreed;

			System.out.println("int frag is now " + totalInteralFragmentation
					+ ", after removing " + intFragToBeFreed);
		} else {

			// this process is not active in memory
			System.out.println("Trying to remove process" + pid
					+ ", this process is not active yet!");
			
			// return not successful
			return -1;
		}
		
		return success == true ? 1 : -1;
	}

	/**
	 * Print the current memory state
	 */
	public void printMemoryState() {

		// PAGING Example:
		// Memory size = 1024 bytes, total pages = 32
		System.out
				.println("============================Printing==================================");
		System.out.println("Meory size = " + memorySize + ", total pages = "
				+ totalPageNumber);
		// allocated pages = 6, free pages = 26
		System.out.println("allocated pages = " + pagesInUse.size()
				+ ", free pages = " + (totalPageNumber - pagesInUse.size()));
		// There are currently 3 active process
		System.out.println("There are currently " + activeProcessesMap.size()
				+ " active process(es)");

		System.out.println("Free Page list:");
		printFreePageNumbers();
		// 2, 6, 7, 8, 9, 10, 11, 12...
		// Process list:
		System.out.println("Process list:");
		printProcessListAndPageInfo();

		// Total Internal Fragmentation = 13 bytes
		System.out.println("Total Internal Fragmentation = "
				+ totalInteralFragmentation + " bytes");
		// Failed allocations (No memory) = 2
		System.out
				.println("Failed allocations (No memory) = " + noMemoryNumber);
		// Failed allocations (External Fragmentation) = 0
		System.out.println("Failed allocations (External Fragmentation) = "
				+ extFragNumber);
	}

	/**
	 * Helper for printing active processes info and it's memory usage info
	 */
	private void printProcessListAndPageInfo() {

		// for each active process in the map print process info, and page info
		for (int key : activeProcessesMap.keySet()) {
			Process p = activeProcessesMap.get(key);
			p.printPagesInfo();
		}
	}

	/**
	 * Helper for printing free page locations
	 */
	private void printFreePageNumbers() {
		String s = "";
		for (Page p : pages) {
			if (!p.isUsed()) {
				s += p.getPhysicalPosition() + ", ";
			}
		}
		System.out.println(s);
	}
}
