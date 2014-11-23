import java.util.Comparator;

// comparator based on hole position
public class HolePositionComparator implements Comparator<Hole>{
	
	@Override
	public int compare(Hole hole1, Hole hole2) {
		// TODO Auto-generated method stub
		Integer position1=Integer.valueOf(hole1.getStartingPos());
		Integer position2=Integer.valueOf(hole2.getStartingPos());
		return position1.compareTo(position2);
	}

}
