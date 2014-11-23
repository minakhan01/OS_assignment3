// class representing a segment
// segment has type, it's process id, space (size) and start position
public class Segment {
	SegmentType segmentType;
	int space;
	int id;
	int start;
	
	public Segment(int id, SegmentType segmentType, int space, int start){
		this.segmentType=segmentType;
		this.space=space;
		this.id=id;
		this.start=start;
	}

}
