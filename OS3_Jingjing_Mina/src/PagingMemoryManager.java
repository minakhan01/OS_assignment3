import java.util.List;

public class PagingMemoryManager extends MemoryManager {

	// initial data structure for tracking, we can discuss what to use for the
	// algorithm we want
	private List<Process> activeProcesses;
	private int[] memory;
	private int memorySize;

	public PagingMemoryManager(int bytes) {
		// TODO Auto-generated constructor stub
	}

	public int allocate(int bytes, int pid, int text_size, int data_size,
			int heap_size) {
		
		// allocate this many bytes to the process with this id
		Process newProcess = new Process(pid, bytes);
		activeProcesses.add(newProcess);
		
		// TODO allocate memoroy
		
		// if using the paging allocator, simply ignore the segment size
		// variables
		// return 1 if successful
		// return -1 if unsuccessful; print an error indicating
		// whether there wasn't sufficient memory or whether
		// you ran into external fragmentation

		return 1;

	}

	public int deallocate(int pid) {
		// deallocate memory allocated to this process
		// return 1 if successful, -1 otherwise with an error message

	}

	public void printMemoryState() {
		// print out current state of memory
		// the output will depend on the memory allocator being used.

		// PAGING Example:
		// Memory size = 1024 bytes, total pages = 32
		// allocated pages = 6, free pages = 26
		// There are currently 3 active process
		// Free Page list:
		// 2, 6, 7, 8, 9, 10, 11, 12...
		// Process list:
		// Process id=34, size=95 bytes, number of pages=3
		// Virt Page 0 -> Phys Page 0 used: 32 bytes
		// Virt Page 1 -> Phys Page 3 used: 32 bytes
		// Virt Page 2 -> Phys Page 4 used: 31 bytes
		// Process id=39, size=55 bytes, number of pages=2
		// Virt Page 0 -> Phys Page 1 used: 32 bytes
		// Virt Page 1 -> Phys Page 13 used: 23 bytes
		// Process id=46, size=29 bytes, number of pages=1
		// Virt Page 0 -> Phys Page 5 used: 29 bytes
		//
		// Total Internal Fragmentation = 13 bytes
		// Failed allocations (No memory) = 2
		// Failed allocations (External Fragmentation) = 0
		//
	}

}
