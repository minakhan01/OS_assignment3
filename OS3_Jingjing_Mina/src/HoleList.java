import java.util.Arrays;

// http://www.cs.cmu.edu/~adamchik/15-121/lectures/Binary%20Heaps/code/Heap.java
public class HoleList {

	private Hole[] holeList;
	private Hole[] holeListPosSorted;
	HolePositionComparator hpc;

	public HoleList(int initialSize) {
		holeList = new Hole[1];
		holeList[0] = new Hole(0, initialSize);
		hpc = new HolePositionComparator();

	}

	/**
	 * Inserts a new item
	 */
	public void insert(Hole x) {
		Hole[] prev_holeList = holeList;
		holeList = new Hole[prev_holeList.length + 1];
		int index = bestFitIndex(x.getSize());
		int newIndex = 0;
		Hole[] first = Arrays.copyOfRange(prev_holeList, 0, index);
		for (int i = 0; i < first.length; i++) {
			holeList[newIndex] = first[i];
			newIndex++;
		}
		holeList[index] = x;
		newIndex++;
		if (index < prev_holeList.length - 1) {
			Hole[] second = Arrays.copyOfRange(prev_holeList, index,
					prev_holeList.length);
			for (int i = 0; i < second.length; i++) {
				holeList[newIndex] = second[i];
				newIndex++;
			}
		}
	}

	// Own method
	public int bestFitIndex(int requiredSize) {
		int currentIndex = holeList.length / 2;

		int currentSize;
		int leftSize;

		int leftIndex = currentIndex / 2;
		int rightIndex = leftIndex + currentIndex;

		while (leftIndex <= rightIndex) {
			currentSize = holeList[currentIndex].getSize();
			leftSize = holeList[leftIndex].getSize();
			if (currentSize == requiredSize)
				return currentIndex;
			else if (requiredSize < currentSize) {
				if (leftSize > requiredSize)
					currentIndex = leftIndex;
				else
					return currentIndex;
			} else {
				currentIndex = rightIndex;
			}
			leftIndex = currentIndex / 2;
			rightIndex = leftIndex + currentIndex;
		}
		return currentIndex;

	}

	public int find(Hole hole) {
		for (int i = 0; i < holeList.length; i++) {
			if (holeList[i].getStartingPos() == hole.getStartingPos())
				return i;
		}
		return -1;
	}

	public void remove(Hole hole) {
		int removeIndex = find(hole);
		removeByIndex(removeIndex);
	}

	public void removeByIndex(int index) {
		if (index < holeList.length) {
			Hole[] prev_holeList = holeList;
			holeList = new Hole[holeList.length - 1];
			int newIndex = 0;
			Hole[] first = Arrays.copyOfRange(prev_holeList, 0, index);
			for (int i = 0; i < first.length; i++) {
				holeList[newIndex] = first[i];
				newIndex++;
			}
			if (index < holeList.length) {
				Hole[] second = Arrays.copyOfRange(prev_holeList, index + 1,
						prev_holeList.length);
				for (int i = 0; i < second.length; i++) {
					holeList[newIndex] = second[i];
					newIndex++;
				}
			}
		}
	}

	public Hole reAllocateHole(int requiredSize) {
		int hole_index = bestFitIndex(requiredSize);
		Hole hole = holeList[hole_index];
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
		insert(hole);
		holeListPosSorted = Arrays.copyOfRange(holeList, 0, holeList.length);
		Arrays.sort(holeListPosSorted, hpc);
		int holeIndex = -1;
		for (int i = 0; i < holeListPosSorted.length - 1; i++) {
			if (holeListPosSorted[i].getStartingPos() == hole.getStartingPos())
				holeIndex = i;
		}
		if ((holeIndex + 1) < holeListPosSorted.length && holeIndex > 0) {
			if (shouldCombine(holeListPosSorted[holeIndex - 1],
					holeListPosSorted[holeIndex])
					&& shouldCombine(holeListPosSorted[holeIndex],
							holeListPosSorted[holeIndex + 1])) {
				remove(holeListPosSorted[holeIndex - 1]);
				remove(holeListPosSorted[holeIndex + 1]);
				remove(hole);
				insert(new Hole(
						holeListPosSorted[holeIndex - 1].getStartingPos(),
						holeListPosSorted[holeIndex - 1].getSize()
								+ hole.getSize()
								+ holeListPosSorted[holeIndex + 1].getSize()));
			}
		} else if (holeIndex > 0) {
			if (shouldCombine(holeListPosSorted[holeIndex - 1],
					holeListPosSorted[holeIndex])) {
				remove(holeListPosSorted[holeIndex - 1]);
				remove(holeListPosSorted[holeIndex]);
				insert(new Hole(
						holeListPosSorted[holeIndex - 1].getStartingPos(),
						holeListPosSorted[holeIndex - 1].getSize()
								+ holeListPosSorted[holeIndex].getSize()));
			}
		}

		else if (holeIndex + 1 < holeListPosSorted.length) {
			if (shouldCombine(holeListPosSorted[holeIndex],
					holeListPosSorted[holeIndex + 1])) {
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
