import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class SegmentationMemoryManager extends MemoryManager {

	// currently active processes in memory, containing id/size/segments start
	private HashMap<Integer, SegmentedProcess> activeProcesses;
	
	// TODO need to decide what data structure we want for tracking
	private Hashtable<Integer, Hole[]> memoryUsed;
	
	// total memory size available
	private int memorySize;
	int noEnoughMemory=0;
	int externalFrag=0;
	
	HoleList holelist;

	public SegmentationMemoryManager(int bytes) {
		// initialize active processes, memory used and hole list
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
		
		// check for lack of memory 
		// if there isn't enough memory, increment noEnoughMemory
		if (totalmemoryAllocated()+bytes>memorySize){
			noEnoughMemory++;
			return -1;}
		
		// get the holes/memory chunks alloted to each of the segment
		Hole text_hole=holelist.reAllocateHole(text_size);
		Hole data_hole=holelist.reAllocateHole(data_size);
		Hole heap_hole=holelist.reAllocateHole(heap_size);
		
		// if each segment can be alloted a memory chunk
		// create a segmented process and 
		// insert in active processes and memory used
		// memory used knows the size of block alloted to each segment which may
		//  be different from the size of each segment
		if(text_hole != null && data_hole != null && heap_hole != null)
		{
		
		Hole[] segmentHoles=new Hole[3];
		
		segmentHoles[0]=text_hole;
		segmentHoles[1]=data_hole;
		segmentHoles[2]=heap_hole;
		SegmentedProcess newProcess = new SegmentedProcess(pid, bytes, text_size, text_hole.getStartingPos(), data_size, data_hole.getStartingPos(), heap_size, heap_hole.getStartingPos());
		activeProcesses.put(Integer.valueOf(pid), newProcess);
		memoryUsed.put(Integer.valueOf(pid), segmentHoles);
		return 1;
		}
		else{
			// increment external fragmentation counter when can 
			// find hole for at least one segment of process
			externalFrag++;
			return -1;
			}
	}

	// deallocate
	public int deallocate(int pid) {
		// do nothing if can't find the process
		if (!activeProcesses.containsKey(Integer.valueOf(pid))){
			System.out.print("no such process");
			return -1;}
		else{
			// remove from active processes and memory used and
			//  create appropriate hole using createCombineHoles
			SegmentedProcess removeProcess=activeProcesses.get(Integer.valueOf(pid));
			activeProcesses.remove(Integer.valueOf(pid));
			Hole[] createHole=memoryUsed.get(Integer.valueOf(pid));
			memoryUsed.remove(Integer.valueOf(pid));
			holelist.createCombineHoles(createHole);
		}
		// deallocate memory allocated to this process
		// return 1 if successful, -1 otherwise with an error message
		return 1;
	}
	
	// calculate total memory allocated in memory used
	private int totalmemoryAllocated(){
		int memoryAllocated=0;
		Iterator<Integer> memoryUsedIter=memoryUsed.keySet().iterator();
		while (memoryUsedIter.hasNext()){
			Integer pid= memoryUsedIter.next();
			Hole[] memoryUsedSegments=memoryUsed.get(pid);
			for (Hole hole: memoryUsedSegments){
				memoryAllocated += hole.getSize();
			}
		}
		return memoryAllocated;
	}
	
	public void printMemoryState() {
		int internalFragmentation=0;
		// print out current state of memory
		// the output will depend on the memory allocator being used.
		
		// SEGMENTATION Example:
		// Memory size = 1024 bytes, allocated bytes = 179, free = 845
		System.out.println("Memory size = "+memorySize+" bytes, allocated bytes = "+totalmemoryAllocated()+", free = "+holelist.totalFreeMemory());
		
		// There are currently 10 holes and 3 active process
		System.out.println("There are currently "+holelist.numHole()+" holes and "+activeProcesses.size()+" active process");
		
		// Hole list:
		System.out.println("Hole list:");
		
		// hole 1: start location = 0, size = 202
		System.out.println(holelist.toString());
		
		// ...
		// Process list:
		System.out.println("Process list:");
		
		// process id=34, size=95 allocation=95
		Set<Integer> processSet=activeProcesses.keySet();
		Integer[] processArray=new Integer[processSet.size()];
		processArray=processSet.toArray(processArray);
//		processArray=Arrays.sort(processArray, ); // SORT IT?
		for (int i=0; i<processArray.length;i++){
			
			SegmentedProcess sp=activeProcesses.get(processArray[i]);
			Hole[] holes=memoryUsed.get(processArray[i]);
			int allocation=0;
			for (Hole hole: holes){
				allocation += hole.getSize();
			}
			internalFragmentation+=allocation-sp.getSize();
		System.out.println("process id="+sp.getId()+", size="+sp.getSize()+" allocation="+allocation);
		System.out.println("text start="+sp.get_startTextIndex()+", size="+sp.getTextSize());
		System.out.println("data start="+sp.get_startDataIndex()+", size="+sp.getDataSize());
		System.out.println("heap start="+sp.get_startHeapIndex()+", size="+sp.getHeapSize());
		// text start=202, size=25
		// data start=356, size=16
		// heap start=587, size=54}
		// process id=39, size=55 allocation=65
		// ...
		}
		System.out.println("");
		
		// Total Internal Fragmentation = 10 bytes
		System.out.println("Total Internal Fragmentation = "+internalFragmentation+" bytes");

		// Failed allocations (No memory) = 2
		System.out.println("Failed allocations (No memory) = "+noEnoughMemory);
		
		// Failed allocations (External Fragmentation) = 7
		System.out.println("Failed allocations (External Fragmentation) = "+externalFrag);
		//
		System.out.println("");
		System.out.println("------------End Print-------------");
		System.out.println("");
	}

}
