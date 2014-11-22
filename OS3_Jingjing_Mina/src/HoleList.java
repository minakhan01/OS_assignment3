import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class HoleList {

	private ArrayList<Hole> holeList;
	private Hole[] holeListPosSorted;
	HolePositionComparator hpc;

	public HoleList(int initialSize) {
		holeList = new ArrayList<Hole>();
		holeList.add(new Hole(0, initialSize));
		hpc = new HolePositionComparator();

	}

	/**
	 * Inserts a new item
	 */
	public void insert(Hole x) {
		System.out.println("inserting");
		int index = bestFitIndex(x.getSize());
		holeList.add(index, x);
		Collections.sort(holeList);
	}
	
	public int bestFitIndex(int requiredSize) {
		System.out.println("best fit");
		
		int lo = 0;
        int hi = holeList.size() - 1;
        
        
        int best_index=0;
        while (lo <= hi) {
        	if (lo == hi)
    			return lo;
        	
            int mid = lo + (hi - lo) / 2;
            if (requiredSize < holeList.get(mid).getSize()){
            	best_index=mid;
            	hi = mid - 1;}
            else if (requiredSize > holeList.get(mid).getSize()) 
            	lo = mid + 1;
            else 
            	return mid;
        }		
        return best_index;
	}

	public int find(Hole hole) {
		System.out.println("finding");
		for (int i = 0; i < holeList.size(); i++) {
			if (holeList.get(i).getStartingPos() == hole.getStartingPos())
				return i;
		}
		return -1;
	}

	public void remove(Hole hole) {
		System.out.println("removing");
		int removeIndex = find(hole);
		removeByIndex(removeIndex);
	}

	public void removeByIndex(int index) {
		System.out.println("removing by index");
		holeList.remove(index);
	}

	public Hole reAllocateHole(int requiredSize) {
		System.out.println("reallocating");
		int hole_index = bestFitIndex(requiredSize);
		Hole hole = holeList.get(hole_index);
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

	public void createCombineHoles(Hole[] createHole) {
		System.out.println("combining holes");
		if (shouldCombine(createHole[0], createHole[1])
				&& shouldCombine(createHole[1], createHole[2])) {
			insertHole(new Hole(createHole[0].getStartingPos(),
					createHole[0].getSize() + createHole[1].getSize()
							+ createHole[2].getSize()));
		} else if (shouldCombine(createHole[0], createHole[1])) {
			insertHole(new Hole(createHole[0].getStartingPos(),
					createHole[0].getSize() + createHole[1].getSize()));
			insertHole(createHole[2]);
		} else if (shouldCombine(createHole[1], createHole[2])) {
			insertHole(createHole[0]);
			insertHole(new Hole(createHole[1].getStartingPos(),
					createHole[2].getSize() + createHole[1].getSize()));
		} else {
			for (int i = 0; i < createHole.length; i++) {
				insertHole(createHole[i]);
			}
		}
	}

	public void insertHole(Hole hole) {
		System.out.println("inserting holes");
		insert(hole);
		holeListPosSorted=new Hole[holeList.size()];
		holeListPosSorted=holeList.toArray(holeListPosSorted);
		Arrays.sort(holeListPosSorted, hpc);
		
		int holeIndex = -1;
		for (int i = 0; i < holeListPosSorted.length - 1; i++) {
			if (holeListPosSorted[i].getStartingPos() == hole.getStartingPos())
				holeIndex = i;
		}
		if (holeIndex==-1)
			return;
		if ((holeIndex + 1) < holeListPosSorted.length && holeIndex > 0) 
		{
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
			else if (shouldCombine(holeListPosSorted[holeIndex - 1],
					holeListPosSorted[holeIndex])) {
				remove(holeListPosSorted[holeIndex - 1]);
				remove(holeListPosSorted[holeIndex]);
				insert(new Hole(
						holeListPosSorted[holeIndex - 1].getStartingPos(),
						holeListPosSorted[holeIndex - 1].getSize()
								+ holeListPosSorted[holeIndex].getSize()));
			}
			else if (shouldCombine(holeListPosSorted[holeIndex], holeListPosSorted[holeIndex + 1])) {
				remove(holeListPosSorted[holeIndex]);
				remove(holeListPosSorted[holeIndex + 1]);
				insert(new Hole(holeListPosSorted[holeIndex].getStartingPos(),
						holeListPosSorted[holeIndex].getSize()
								+ holeListPosSorted[holeIndex + 1].getSize()));
			}
		} else if (holeIndex > 0) 
		{
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
		else if (holeIndex + 1 < holeListPosSorted.length) 
		{
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
	
	private boolean shouldCombine(Hole hole1, Hole hole2) {
		if ((hole1.getStartingPos() + hole1.getSize()) == hole2
				.getStartingPos())
			return true;
		else
			return false;
	}

}
