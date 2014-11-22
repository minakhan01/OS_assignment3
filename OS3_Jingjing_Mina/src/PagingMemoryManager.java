import java.util.ArrayList;
import java.util.List;

public class PagingMemoryManager extends MemoryManager {

	// initial data structure for tracking, we can discuss what to use for the
	// algorithm we want
	private ArrayList<Process> activeProcesses;
	
	// memory simulation
	private int memorySize;
	private int totalPageNumber;
	private Page[] pages;
	
	// keeping track of pages in use
	private ArrayList<Page> pagesInUse;

	// keeping track of errors
	private int totalInteralFragmentation = 0;
	private int noMemoryNumber = 0;
	
	// this should always be 0 since it is fixed size paging, there is no external fragmentation
	private int extFragNumber = 0;

	public PagingMemoryManager(int bytes) {
		
        this.memorySize = bytes;
		int leftOverSize = bytes % 32;
		totalInteralFragmentation += leftOverSize;
		this.totalPageNumber = (bytes - leftOverSize) / 32;
		this.pages = new Page[totalPageNumber];
		
		// initializing that all pages are not used
		for(int i = 0; i < totalPageNumber; i++)
		{
			Page p = new Page(i,0);
			// set it to a process that is does not exists 
			p.setAssignedProcess(new Process(-1, -1));
			pages[i] = p;			
		}
		
		// so far no page is in use
		this.pagesInUse = new ArrayList<>();
		
		// and no process is active
		this.activeProcesses = new ArrayList<>();	
	}

	/**
	 * allocate pages to the given process
	 * 
	 */
	public int allocate(int bytes, int pid, int text_size, int data_size,
			int heap_size) {

		// allocate this many bytes to the process with this id
		Process newProcess = new Process(pid, bytes);
		activeProcesses.add(newProcess);
		
		System.out.println("allocating process" + pid + " size " + bytes);

		// TODO allocate memoroy
		int sizeOfLastPage = bytes % 32;
		int sizeNeededToFill = 32 - sizeOfLastPage;
		int numPagesNeeded = (bytes + sizeNeededToFill)/32;
		System.out.println("numPageNeeded = " + numPagesNeeded);


		// check if sum of used pages and needed pages is over limit
		if (pagesInUse.size() + numPagesNeeded <= totalPageNumber) {
			
			System.out.println("allocating pages for process : " + newProcess.getId());
			// initialize the pages array for the process to record
			Page[] pagesForThisProcess = new Page[numPagesNeeded];
			
			// look for a page for each page needed
			for(int i = 0; i < numPagesNeeded; i++)
			{
				int index = findNextAvailablePage();
				if(index == -1)
				{
					System.out.println("something wrong either with the size check, or the find method..at this point pages should be enough but they are not");
					noMemoryNumber ++;
					return -1;
				}else
				{
					// Found a page now
					System.out.println("found page " + index + "for process" + newProcess.getId());
					Page p = pages[index];
					if( i == numPagesNeeded -1)
					{
					    p.setUsedSize(sizeOfLastPage);
					    totalInteralFragmentation += sizeNeededToFill;
					    newProcess.setIntFragInPaging(sizeNeededToFill);
					}else
					{
						p.setUsedSize(32);
					}
					
					p.setAssignedProcess(newProcess);
					p.setVirtualPosition(i);
					pagesForThisProcess[i] = p;		
					pagesInUse.add(p);
				}
				
			}
			newProcess.setPages(pagesForThisProcess);
			return 1;
			
		} else {
			System.out.println("Not enough pages");
			noMemoryNumber ++;
			return -1;
		}
	}
	
	/**
	 * Find the first available page and return its index
	 * @return the page postion, -1 if not found
	 */
	private int findNextAvailablePage()
	{
		for(int i = 0; i < totalPageNumber; i++)
		{
			if(!pages[i].isUsed())
			{
				return i;
			}
		}
		
		// if not returned at this point, then all pages are being used
		System.out.println("tried to find next available page but did not find one");
		return -1;
	}
	 

	public int deallocate(int pid) {
		System.out.println("deleting process" + pid);
		boolean success = false;
		int intFragToBeFreed = 0;
		for(int i = 0; i< pages.length; i++)
		{
			Page page = pages[i];
			Process process = page.getAssignedProcess();
			
			if(process.getId() == pid)
			{
				// this page needs to be freed
				page.free();
				pagesInUse.remove(page);
				System.out.println("removed page" + page.getPhysicalPosition());
				activeProcesses.remove(process);
				intFragToBeFreed = process.getIntFragInPaging();
			}
			
			
		}
		
		totalInteralFragmentation -= intFragToBeFreed;
		System.out.println("int frag is now " + totalInteralFragmentation + ", after removing " + intFragToBeFreed);
		return success == true ? 1 : -1;
		// deallocate memory allocated to this process
		// return 1 if successful, -1 otherwise with an error message
		// TODO How to return an int AND an error message -.-||| I can print out?

	}

	private void printFreePageNumbers() {
		String s = "";
		for (Page p : pages) {
			if (!p.isUsed()) {
				s += p.getPhysicalPosition() + ", ";
			}
		}
		System.out.println(s);
	}

	public void printMemoryState() {
		// print out current state of memory
		// the output will depend on the memory allocator being used.

		// PAGING Example:
		// Memory size = 1024 bytes, total pages = 32
		System.out.println("Meory size = " + memorySize + ", total pages = "
				+ totalPageNumber);
		// allocated pages = 6, free pages = 26
		System.out.println("allocated pages = " + pagesInUse.size()
				+ ", free pages = " + (totalPageNumber - pagesInUse.size()));
		// There are currently 3 active process
		System.out.println("There are currently " + activeProcesses.size()
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

	private void printProcessListAndPageInfo() {
		for (Process process : activeProcesses) {
			System.out.println("Process id = " + process.getId() + ", size = "
					+ process.getSize() + ", number of pages"
					+ process.getPages().length);

			process.printPagesInfo();
		}
	}
}
