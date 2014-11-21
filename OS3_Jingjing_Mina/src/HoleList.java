import java.util.Arrays;

// http://www.cs.cmu.edu/~adamchik/15-121/lectures/Binary%20Heaps/code/Heap.java
public class HoleList{
	
	private Hole[] holeList;
	   private static final int CAPACITY = 10;

	   private int size;            // Number of elements in holeList

	   public HoleList()
	   {
	      size = 0;
	      holeList = new Hole[CAPACITY];
	   }

	 /**
	  * Construct the binary holeList given an array of items.
	  */
	   public HoleList(Hole[] array)
	   {
	      size = array.length;
	      holeList = new Hole[array.length+1];

	      System.arraycopy(array, 0, holeList, 1, array.length);//we do not use 0 index

	      buildHeap();
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
	      size = array.length;
	      holeList = new Hole[size+1];
	      System.arraycopy(array, 0, holeList, 1, size);
	      buildHeap();

	      for (int i = size; i > 0; i--)
	      {
	    	 Hole tmp = holeList[i]; //move top item to the end of the holeList array
	         holeList[i] = holeList[1];
	         holeList[1] = tmp;
	         size--;
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
	      if(size == holeList.length - 1) doubleSize();

	      //Insert a new item to the end of the array
	      int pos = ++size;

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
//		   int rightSize;
		   
		   int leftChild = 2*currentIndex+1;
		   int rightChild = leftChild+1;
		   
		   while (leftChild<holeList.length){
			   currentSize=holeList[currentIndex].getSize();
			   leftSize=holeList[leftChild].getSize();
			   if (currentSize==requiredSize)
				   return currentIndex;
			   else if (requiredSize<currentSize && leftSize>requiredSize){
				   currentIndex=leftChild;
			   }
			   else if (rightChild<holeList.length){
//			   rightSize=holeList[rightChild].getSize();
				   currentIndex=rightChild;
			   }
			   leftChild = 2*currentIndex+1;
			   rightChild = leftChild+1;
		   }
		   return currentIndex;
		   
	   }
	   
	   public void removeIndex(int index){
		   if (index<size){
			   size--;
			   Hole[] prev_holeList=holeList;
			   holeList=new Hole[size];
			   int newIndex=0;
			   Hole[] first= Arrays.copyOfRange(prev_holeList,0 ,index);
			   Hole[] second= Arrays.copyOfRange(prev_holeList,index+1,prev_holeList.length);
			   for (int i=0; i<first.length;i++){
				   holeList[newIndex]=first[i];
				   newIndex++;
			   }
			   for (int i=0; i<second.length;i++){
				   holeList[newIndex]=second[i];
				   newIndex++;
			   }
			   buildHeap();
		   }
	   }
	   
	   private void doubleSize()
	   {
		  Hole [] old = holeList;
	      holeList = new Hole[holeList.length * 2];
	      System.arraycopy(old, 1, holeList, 1, size);
	   }

	   public String toString()
	   {
	      String out = "";
	      for(int k = 1; k <= size; k++) out += holeList[k]+" ";
	      return out;
	   }

}
