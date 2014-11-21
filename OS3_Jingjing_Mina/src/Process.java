
public class Process {
	
	private int id;
	private int size;
	private Segment[] segments;
	
	
	public Process(int id, int size) {
		this.id = id;
		this.size = size;
	}
		
	/**
	 * @return the id
	 */
	private int getId() {
		return id;
	}
	/**
	 * @return the size
	 */
	private int getSize() {
		return size;
	}
	/**
	 * @param id the id to set
	 */
	private void setId(int id) {
		this.id = id;
	}
	/**
	 * @param size the size to set
	 */
	private void setSize(int size) {
		this.size = size;
	}
}
