import java.util.Arrays;

// http://www.cs.cmu.edu/~adamchik/15-121/lectures/Binary%20Heaps/code/Heap.java
public class HoleList{

	private Hole[] holeList;
	private Hole[] holeListPosSorted;
	HolePositionComparator hpc;

	private int size;            // Number of elements in holeList

	public HoleList(int initialSize)
	{
		size = 1;
		holeList = new Hole[1];
		holeList[0]=new Hole(0, initialSize);
		hpc=new HolePositionComparator();

	}

	/**
	 *   runs at O(size)
	 */
	private void buildHeap()
	{
		for (int k = size/2; k > 0; k--)
		{
			percolatingDown(k);
		}
	}

	private void percolatingDown(int k)
	{
		Hole tmp = holeList[k];
		int child;

		for(; 2*k <= size; k = child)
		{
			child = 2*k;

			if(child != size &&
					holeList[child].compareTo(holeList[child + 1]) > 0) child++;

			if(tmp.compareTo(holeList[child]) > 0)  holeList[k] = holeList[child];
			else
				break;
		}
		holeList[k] = tmp;
	}

	/**
	 *  Sorts a given array of items.
	 */
	 public void heapSort(Hole[] array)
	 {
		 int size_array = array.length;
		 holeList = new Hole[size_array+1];
		 System.arraycopy(array, 0, holeList, 1, size_array);
		 buildHeap();

		 for (int i = size_array; i > 0; i--)
		 {
			 Hole tmp = holeList[i]; //move top item to the end of the holeList array
			 holeList[i] = holeList[1];
			 holeList[1] = tmp;
			 size_array--;
			 percolatingDown(1);
		 }
		 for(int k = 0; k < holeList.length-1; k++)
			 array[k] = holeList[holeList.length - 1 - k];
		 
	 }

	 /**
	  * Deletes the top item
	  */
	 public Hole deleteMin() throws RuntimeException
	 {
		 if (size == 0) throw new RuntimeException();
		 Hole min = holeList[1];
		 holeList[1] = holeList[size--];
		 percolatingDown(1);
		 return min;
	 }

	 /**
	  * Inserts a new item
	  */
	 public void insert(Hole x)
	 {
		 if(size == holeList.length - 1) 
			 doubleSize();

		 //Insert a new item to the end of the array
		 int pos = size;
		 size++;
		 //Percolate up
		 for(; pos > 1 && x.compareTo(holeList[pos/2]) < 0; pos = pos/2 )
			 holeList[pos] = holeList[pos/2];

		 holeList[pos] = x;
	 }

	 // Own method
	 public int bestFitIndex(int requiredSize){
		 int currentIndex=0;

		 int currentSize;
		 int leftSize;

		 int leftIndex = 2*currentIndex+1;
		 int rightIndex = leftIndex+1;

		 while (leftIndex< size ){
			 currentSize=holeList[currentIndex].getSize();
			 leftSize=holeList[leftIndex].getSize();
			 if (currentSize==requiredSize)
				 return currentIndex;
			 else if (requiredSize<currentSize){
				 if (leftSize>requiredSize)
					 currentIndex=leftIndex;
				 else
					 return currentIndex;
			 }
			 else if (rightIndex<size){
				 currentIndex=rightIndex;
			 }
			 leftIndex = 2*currentIndex+1;
			 rightIndex = leftIndex+1;
		 }
		 return currentIndex;

	 }

	 public int find(Hole hole){
		 for (int i=0;i<size;i++){
			 if (holeList[i].getStartingPos()==hole.getStartingPos())
				 return i;
		 }
		 return -1;
	 }

	 public void remove(Hole hole){
		 int removeIndex=find(hole);
		 removeByIndex(removeIndex);
	 } 

	 public void removeByIndex(int index){
		 if (index<size){
			 size--;
			 Hole[] prev_holeList=holeList;
			 holeList=new Hole[size];
			 int newIndex=0;
			 Hole[] first= Arrays.copyOfRange(prev_holeList,0 ,index);
			 for (int i=0; i<first.length;i++){
				 holeList[newIndex]=first[i];
				 newIndex++;
			 }
			 if (index <size){
				 Hole[] second= Arrays.copyOfRange(prev_holeList,index+1,size+1);
				 for (int i=0; i<second.length;i++){
					 holeList[newIndex]=second[i];
					 newIndex++;
				 }
			 }
			 heapSort(holeList);
			 System.out.println(holeList);
		 }
	 }

	 public Hole reAllocateHole(int requiredSize){
		 int hole_index=bestFitIndex(requiredSize);
		 System.out.println(hole_index + "size "+ size);
		 System.out.println(holeList[hole_index]);
		 Hole hole=holeList[hole_index];
		 removeByIndex(hole_index);
		 if (hole.getSize()<requiredSize+16)
			 return hole;
		 else{
			 Hole newHole=new Hole(hole.getStartingPos()+requiredSize, hole.getSize()-requiredSize);
			 insertHole(newHole);
			 return new Hole(hole.getStartingPos(), requiredSize);
		 }

	 }

	 private void doubleSize()
	 {
		 Hole[] old = holeList;
		 holeList = new Hole[holeList.length +1];
		 for (int i=0;i < old.length;i++){
			 holeList[i]=old[i];
		 }
		 System.out.print("hello");
	 }

	 public String toString()
	 {
		 String out = "";
		 for(int k = 1; k <= size; k++) out += holeList[k]+" ";
		 return out;
	 }

	 public void createCombineHoles(Hole[] createHole) {
		 if (shouldCombine(createHole[0], createHole[1]) && shouldCombine(createHole[1], createHole[2])){
			 insertHole(new Hole(createHole[0].getStartingPos(), createHole[0].getSize()+createHole[1].getSize()+createHole[2].getSize()));
		 }
		 else if(shouldCombine(createHole[0], createHole[1])){
			 insertHole(new Hole(createHole[0].getStartingPos(), createHole[0].getSize()+createHole[1].getSize()));
			 insertHole(createHole[2]);
		 }
		 else if(shouldCombine(createHole[1], createHole[2])){
			 insertHole(createHole[0]);
			 insertHole(new Hole(createHole[1].getStartingPos(), createHole[2].getSize()+createHole[1].getSize()));
		 }
		 else{
			 for (int i=0;i<createHole.length; i++){
				 insertHole(createHole[i]);
			 }}

	 }

	 public void insertHole(Hole hole){
		 insert(hole);
		 holeListPosSorted=Arrays.copyOfRange(holeList,0, size);
		 Arrays.sort(holeListPosSorted, hpc);
		 int holeIndex=-1;
		 for (int i=0;i<holeListPosSorted.length-1;i++){
			 if (holeListPosSorted[i].getStartingPos()==hole.getStartingPos())
				 holeIndex=i;
		 }
		 if (holeIndex>=0)
		 {
		 if (holeIndex+1<size && holeIndex>0)
		 {
			 if (shouldCombine(holeListPosSorted[holeIndex-1], holeListPosSorted[holeIndex]) &&
					 shouldCombine(holeListPosSorted[holeIndex], holeListPosSorted[holeIndex+1])){
				 remove(holeListPosSorted[holeIndex-1]);
				 remove(holeListPosSorted[holeIndex+1]);
				 remove(hole);
				 insert(new Hole(holeListPosSorted[holeIndex-1].getStartingPos(), holeListPosSorted[holeIndex-1].getSize()+hole.getSize()+holeListPosSorted[holeIndex+1].getSize()));
			 }
		}
		 else if (holeIndex>0){
			 if (shouldCombine(holeListPosSorted[holeIndex-1], holeListPosSorted[holeIndex])){
				 remove(holeListPosSorted[holeIndex-1]);
				 remove(holeListPosSorted[holeIndex]);
				 insert(new Hole(holeListPosSorted[holeIndex-1].getStartingPos(), holeListPosSorted[holeIndex-1].getSize()+holeListPosSorted[holeIndex].getSize()));
			 }
		 }

		 else if (holeIndex+1<size){
			 if (shouldCombine(holeListPosSorted[holeIndex], holeListPosSorted[holeIndex+1])){
				 remove(holeListPosSorted[holeIndex]);
				 remove(holeListPosSorted[holeIndex+1]);
				 insert(new Hole(holeListPosSorted[holeIndex].getStartingPos(), holeListPosSorted[holeIndex].getSize()+holeListPosSorted[holeIndex+1].getSize()));
			 }
		 }	
		 }

	 }

	 private boolean shouldCombine(Hole hole1,Hole hole2){
		 if ((hole1.getStartingPos()+hole1.getSize())==hole2.getStartingPos())
			 return true;
		 else
			 return false;
	 }

}
