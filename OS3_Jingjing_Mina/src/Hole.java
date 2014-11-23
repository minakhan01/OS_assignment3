// Hole represents a chunk of memory with a starting position
// and starting size. I have named it hole because we are
// mostly using as an empty memory chunk
public class Hole implements Comparable<Hole>{
	private int size;
	private int startingPos;
	
	public Hole(int startingPos, int size){
		this.size=size;
		this.startingPos=startingPos;
	}
	
	public int getSize(){
		return size;
	}
	
	public int getStartingPos(){
		return startingPos;
	}
	
	//we compare based on hole size, not starting position
	@Override
	public int compareTo(Hole hole2) {
		Integer  size1=Integer.valueOf(this.size);
		Integer  size2=Integer.valueOf(hole2.getSize());
		return size1.compareTo(size2);
	}
	
	@Override
	public String toString(){
		return String.valueOf("startingPos: "+startingPos+" size: "+size);
	}

}
