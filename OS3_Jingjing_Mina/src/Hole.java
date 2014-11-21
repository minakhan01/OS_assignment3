
public class Hole implements Comparable<Hole>{
	private int size;
	
	public Hole(int size){
		this.size=size;
	}
	
	public int getSize(){
		return size;
	}

	@Override
	public int compareTo(Hole hole2) {
		Integer  size1=Integer.valueOf(this.size);
		Integer  size2=Integer.valueOf(hole2.getSize());
		return size1.compareTo(size2);
	}
	
	@Override
	public String toString(){
		return String.valueOf(size);
	}

}
