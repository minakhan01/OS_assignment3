// this class represent a segmented process
// the process has a text, data and heap segment
public class SegmentedProcess {
	
	private int id;
	private int size;
	private Segment[] segments;
	private int startTextIndex;
	private int startDataIndex;
	private int startHeapIndex;
	
	
	public SegmentedProcess(int id, int size, int text_size, int text_start, int data_size,
			int data_start, int heap_size, int heap_start) {
		this.id = id;
		this.size = size;
		segments=new Segment[3];
		startTextIndex=text_start;
		startDataIndex=data_start;
		startHeapIndex=heap_start;
		segments[0]=new Segment(id, SegmentType.TEXT, text_size, text_start);
		segments[1]=new Segment(id, SegmentType.STACK, data_size, data_start);
		segments[2]=new Segment(id, SegmentType.HEAP, heap_size, heap_start);
	}
		
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	
	public int getTextSize() {
		return segments[0].space;
	}
	
	public int getDataSize() {
		return segments[1].space;
	}
	
	public int getHeapSize() {
		return segments[2].space;
	}

	public void set_startTextIndex(int startTextIndex) {
		this.startTextIndex = startTextIndex;
	}
	
	public void set_startDataIndex(int startDataIndex) {
		this.startDataIndex = startDataIndex;
	}
	
	public void set_startHeapIndex(int startHeapIndex) {
		this.startHeapIndex = startHeapIndex;
	}
	
	public int get_startTextIndex() {
		return startTextIndex;
	}
	
	public int get_startDataIndex() {
		return startDataIndex;
	}
	
	public int get_startHeapIndex() {
		return startHeapIndex;
	}

}
