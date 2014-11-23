
public class SegmentedProcess {
	
	private int id;
	private int size;
	private Segment[] segments;
	private int startTextIndex;
	private int startDataIndex;
	private int startHeapIndex;
	
	
	public SegmentedProcess(int id, int size, int text_size, int data_size,
			int heap_size) {
		this.id = id;
		this.size = size;
		segments=new Segment[3];
		segments[0]=new Segment(id, SegmentType.TEXT, text_size);
		segments[1]=new Segment(id, SegmentType.STACK, data_size);
		segments[2]=new Segment(id, SegmentType.HEAP, heap_size);
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
