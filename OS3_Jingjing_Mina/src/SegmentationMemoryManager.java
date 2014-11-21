import java.util.HashMap;
import java.util.Hashtable;

public class SegmentationMemoryManager extends MemoryManager {

	// currently active processes in memory, containing id/size/segments start
	private HashMap<Integer, SegmentedProcess> activeProcesses;
	
	// TODO need to decide what data structure we want for tracking
	private Hashtable<Integer, Hole[]> memoryUsed;
	
	// total memory size available
	private int memorySize;
	
	HoleList holelist;

	public SegmentationMemoryManager(int bytes) {
		activeProcesses=new HashMap<Integer, SegmentedProcess>();
		this.memorySize = bytes;
		this.memoryUsed = new Hashtable<Integer, Hole[]>();
		holelist=new HoleList(bytes);
	}

	/**
	 * allocate this many bytes to the process with this id
	 * verify input sizes are valid
	 * 
	 */
	public int allocate(int bytes, int pid, int text_size, int data_size,
			int heap_size) {
 
		if (!(text_size + data_size + heap_size == bytes)) {
			System.out.println("input sizes of segments wrong!!!!!!!! ");
			return -1;
		}
		
		SegmentedProcess newProcess = new SegmentedProcess(pid, bytes, text_size, data_size, heap_size);
		activeProcesses.put(Integer.valueOf(pid), newProcess);
		Hole text_hole=holelist.reAllocateHole(text_size);
		Hole data_hole=holelist.reAllocateHole(data_size);
		Hole heap_hole=holelist.reAllocateHole(heap_size);
		Hole[] segmentHoles=new Hole[3];
		segmentHoles[0]=text_hole;
		segmentHoles[1]=data_hole;
		segmentHoles[2]=heap_hole;
		memoryUsed.put(Integer.valueOf(pid), segmentHoles);

		return 1;
	}

	public int deallocate(int pid) {
		if (!activeProcesses.containsKey(Integer.valueOf(pid)))
			return -1;
		else{
			
			SegmentedProcess removeProcess=activeProcesses.get(Integer.valueOf(pid));
			activeProcesses.remove(Integer.valueOf(pid));
			Hole[] createHole=memoryUsed.get(Integer.valueOf(pid));
			holelist.createCombineHoles(createHole);
		}
		// deallocate memory allocated to this process
		// return 1 if successful, -1 otherwise with an error message
		return 1;
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
