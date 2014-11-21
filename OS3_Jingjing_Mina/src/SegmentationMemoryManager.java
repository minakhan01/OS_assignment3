import java.util.HashMap;

public class SegmentationMemoryManager extends MemoryManager {

	// currently active processes in memory, containing id/size/segments start
	private HashMap<Integer, SegmentedProcess> activeProcesses;
	
	// TODO need to decide what data structure we want for tracking
	private int[] memory;
	
	// total memory size available
	private int memorySize;
	
	HoleList holelist;

	public SegmentationMemoryManager(int bytes) {
		activeProcesses=new HashMap<Integer, SegmentedProcess>();
		this.memorySize = bytes;
		this.memory = new int[memorySize];
		holelist=new HoleList(bytes);
	}

	/**
	 * allocate this many bytes to the process with this id
	 * verify input sizes are valid
	 * 
	 */
	public int allocate(int bytes, int pid, int text_size, int data_size,
			int heap_size) {
 
		SegmentedProcess newProcess = new SegmentedProcess(pid, bytes, text_size, data_size, heap_size);
		activeProcesses.put(Integer.valueOf(pid), newProcess);
		int text_hole=holelist.bestFitIndex(text_size);
		// TODO allocate memoroy
		// TODO do sth with the segments

		if (!(text_size + data_size + heap_size == bytes)) {
			System.out.println("input sizes of segments wrong!!!!!!!! ");
			return -1;
		}

		return 1;
	}

	public int deallocate(int pid) {
		// deallocate memory allocated to this process
		// return 1 if successful, -1 otherwise with an error message

	}

	public void printMemoryState() {
		// print out current state of memory
		// the output will depend on the memory allocator being used.

		// SEGMENTATION Example:
		// Memory size = 1024 bytes, allocated bytes = 179, free = 845
		// There are currently 10 holes and 3 active process
		// Hole list:
		// hole 1: start location = 0, size = 202
		// ...
		// Process list:
		// process id=34, size=95 allocation=95
		// text start=202, size=25
		// data start=356, size=16
		// heap start=587, size=54
		// process id=39, size=55 allocation=65
		// ...
		// Total Internal Fragmentation = 10 bytes
		// Failed allocations (No memory) = 2
		// Failed allocations (External Fragmentation) = 7
		//
	}

}
