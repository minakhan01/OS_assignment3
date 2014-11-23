import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

// this class keeps track of all the memory holes in the memory
public class HoleList {
	
	// a list of holes in ascending order of size
	private ArrayList<Hole> holeList;
	
	// a list of holes in ascending order of starting position
	private Hole[] holeListPosSorted;
	
	// comparator based on starting position
	HolePositionComparator hpc;

	public HoleList(int initialSize) {
		// initialize holelist with one hole that has initial size
		holeList = new ArrayList<Hole>();
		holeList.add(new Hole(0, initialSize));
		hpc = new HolePositionComparator();

	}
	
	// get total number of holes
	public int numHole(){
		return holeList.size();
	}
	
	// string representation for holes
	public String toString(){
		String holeListString="";
		for (int i=0; i<holeList.size(); i++){
			Hole currentHole=holeList.get(i);
			holeListString += "hole : "+i +" start location = "+currentHole.getStartingPos()+", size = "+currentHole.getSize();
			holeListString +="\n";
		}
		return holeListString;
	}
	
	// Inserts a new item and sorts the list again
	private void insert(Hole x) {
		holeList.add(x);
		Collections.sort(holeList);
	}

	// binary search to find the best fit hole
	// returns -1 if there is no fitting hole
	private int bestFitIndex(int requiredSize) {
		
		// we keep a dynamic window on the array using lo and high
		// this window shrinks exponentially since we are doing binary search
		int lo = 0;
        int hi = holeList.size() - 1;
        
        int best_index=-1;
        while (lo <= hi) {
        	
        	// if there just one hole left
        	// check if that hole can fit the required
        	// if it doesn't, return -1
        	if (lo == hi){
        		if (requiredSize<=holeList.get(lo).getSize())
        			return lo;
        		else
        			return -1;
        	}
    			
        	
            int mid = lo + (hi - lo) / 2;
            if (requiredSize < holeList.get(mid).getSize()){
            	// so far the best hole is mid
            	// update the upper limit of the window
            	best_index=mid;
            	hi = mid - 1;}
            else if (requiredSize > holeList.get(mid).getSize()){
            	// if required size is bigger than the size of mid hole,
            	// then we have not found the hole
            	// update the lower limit of the window
            	lo = mid + 1;
            }
            else 
            	return mid;
        }		
        return best_index;
	}

	// helper method
	private int find(Hole hole) {
		// we can't do binary search here because holeList is sorted by
		// size and not starting position
		// this is linear search
		for (int i = 0; i < holeList.size(); i++) {
			if (holeList.get(i).getStartingPos() == hole.getStartingPos())
				return i;
		}
		return -1;
	}
	
	// remove a particular hole
	public void remove(Hole hole) {
		int removeIndex = find(hole);
		removeByIndex(removeIndex);
	}
	
	// remove the index
	// no need to sort again
	public void removeByIndex(int index) {
		holeList.remove(index);
//		Collections.sort(holeList);
	}

	public Hole reAllocateHole(int requiredSize) {
		
		// find best fit hole index in terms of size
		int hole_index = bestFitIndex(requiredSize);
		
		// if there is no appropriate hole, return null
		if (hole_index==-1)
			return null;
		
		// get the hole in that index
		Hole hole = holeList.get(hole_index);
		
		// update the hole by first removing the hole
		// and adding another hole of less size if the size of 
		// the hole is more than 16 bigger than required size
		removeByIndex(hole_index);
		if (hole.getSize() < requiredSize + 16)
			return hole;
		else {
			Hole newHole = new Hole(hole.getStartingPos() + requiredSize,
					hole.getSize() - requiredSize);
			insertHole(newHole);
			return new Hole(hole.getStartingPos(), requiredSize);
		}

	}
	
	// this is used when we are deallocating processes
	public void createCombineHoles(Hole[] createHole) {
		
		//createHole would have length 3 corresponding to the
		// three segments in a process
		
		// combine all the three holes
		if (shouldCombine(createHole[0], createHole[1])
				&& shouldCombine(createHole[1], createHole[2])) {
			insertHole(new Hole(createHole[0].getStartingPos(),
					createHole[0].getSize() + createHole[1].getSize()
							+ createHole[2].getSize()));
		}
		// combine only the first two
		else if (shouldCombine(createHole[0], createHole[1])) {
			insertHole(new Hole(createHole[0].getStartingPos(),
					createHole[0].getSize() + createHole[1].getSize()));
			insertHole(createHole[2]);
		}
		// combine only the last two
		else if (shouldCombine(createHole[1], createHole[2])) {
			insertHole(createHole[0]);
			insertHole(new Hole(createHole[1].getStartingPos(),
					createHole[2].getSize() + createHole[1].getSize()));
		}
		// combine none
		else {
			for (int i = 0; i < createHole.length; i++) {
				insertHole(createHole[i]);
			}
		}
	}

	public void insertHole(Hole hole) {
		// insert hole
		insert(hole);
		
		// get a position sorted array for holes
		// positions would be meaning in combining holes
		// since we can only combine the current hole 
		// with the next hole in terms of position
		// and with the previous hole in terms of position
		holeListPosSorted=new Hole[holeList.size()];
		holeListPosSorted=holeList.toArray(holeListPosSorted);
		Arrays.sort(holeListPosSorted, hpc);
		
		int holeIndex = -1;
		// find index of the inserted hole in the position sorted holes array
		for (int i = 0; i < holeListPosSorted.length - 1; i++) {
			if (holeListPosSorted[i].getStartingPos() == hole.getStartingPos())
				holeIndex = i;
		}
		
		// if hole is not found, do nothing
		if (holeIndex==-1)
			return;
		
		// hole has one hole before it and one hole after it in terms of position
		if ((holeIndex + 1) < holeListPosSorted.length && holeIndex > 0) 
		{
			// if all three holes should be combined
			if (shouldCombine(holeListPosSorted[holeIndex - 1], holeListPosSorted[holeIndex])
					&& shouldCombine(holeListPosSorted[holeIndex],holeListPosSorted[holeIndex + 1])) 
			{
				remove(holeListPosSorted[holeIndex - 1]);
				remove(holeListPosSorted[holeIndex + 1]);
				remove(hole);
				insert(new Hole(
						holeListPosSorted[holeIndex - 1].getStartingPos(),
						holeListPosSorted[holeIndex - 1].getSize()
								+ hole.getSize()
								+ holeListPosSorted[holeIndex + 1].getSize()));
			}
			// combine with only the previous hole
			else if (shouldCombine(holeListPosSorted[holeIndex - 1],
					holeListPosSorted[holeIndex])) {
				remove(holeListPosSorted[holeIndex - 1]);
				remove(holeListPosSorted[holeIndex]);
				insert(new Hole(
						holeListPosSorted[holeIndex - 1].getStartingPos(),
						holeListPosSorted[holeIndex - 1].getSize()
								+ holeListPosSorted[holeIndex].getSize()));
			}
			// combine with only the next hole
			else if (shouldCombine(holeListPosSorted[holeIndex], holeListPosSorted[holeIndex + 1])) {
				remove(holeListPosSorted[holeIndex]);
				remove(holeListPosSorted[holeIndex + 1]);
				insert(new Hole(holeListPosSorted[holeIndex].getStartingPos(),
						holeListPosSorted[holeIndex].getSize()
								+ holeListPosSorted[holeIndex + 1].getSize()));
			}
		}
		// hole only has one hole before it in terms of position
		else if (holeIndex > 0) 
		{
			// combine with only the previous hole
			if (shouldCombine(holeListPosSorted[holeIndex - 1],
					holeListPosSorted[holeIndex])) 
			{
				remove(holeListPosSorted[holeIndex - 1]);
				remove(holeListPosSorted[holeIndex]);
				insert(new Hole(
						holeListPosSorted[holeIndex - 1].getStartingPos(),
						holeListPosSorted[holeIndex - 1].getSize()
								+ holeListPosSorted[holeIndex].getSize()));
			}
		}
		// hole only has one hole after it in terms of position
		else if (holeIndex + 1 < holeListPosSorted.length) 
		{
			// combine with only the next hole
			if (shouldCombine(holeListPosSorted[holeIndex], holeListPosSorted[holeIndex + 1])) 
			{
				remove(holeListPosSorted[holeIndex]);
				remove(holeListPosSorted[holeIndex + 1]);
				insert(new Hole(holeListPosSorted[holeIndex].getStartingPos(),
						holeListPosSorted[holeIndex].getSize()
								+ holeListPosSorted[holeIndex + 1].getSize()));
			}
		}

	}
	
	// method of check if two adjacent hole should be combined
	private boolean shouldCombine(Hole hole1, Hole hole2) {
		if ((hole1.getStartingPos() + hole1.getSize()) == hole2
				.getStartingPos())
			return true;
		else
			return false;
	}
	
	// add up the sizes of all the holes
	public int totalFreeMemory(){
		int freeMemory=0;
		Iterator<Hole> iter=holeList.iterator();
		while(iter.hasNext()){
			freeMemory+=iter.next().getSize();
		}
		return freeMemory;
	}

}
