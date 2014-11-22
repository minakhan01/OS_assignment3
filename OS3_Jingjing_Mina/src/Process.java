/**
 * The process object records information regarding a process
 * @author Jingjing and Mina
 *
 */
public class Process {

	// process's unique ID number
	private int id;
	
	// total memory that is process is taking
	private int size;
	
	// segments that are allocated to this
	private Segment[] segments;
	
	// pages that are allocated to this
	private Page[] pages;
	
	// amount of intFragmentation
	private int intFragInPaging;

	/**
	 * @return the pages
	 */
	public Page[] getPages() {
		return pages;
	}

	/**
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(Page[] pages) {
		this.pages = pages;
	}

	/**
	 * Constructor 
	 * @param id 
	 * @param size
	 */
	public Process(int id, int size) {
		this.id = id;
		this.size = size;
	}

	/**
	 * gets the segments
	 * @return segments
	 */
	public Segment[] getSegments() {
		return this.segments;
	}

	/**
	 * sets the segments
	 * @param segments array of Segment
	 */
	public void setSegments(Segment[] segments) {
		this.segments = segments;
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

	/**
	 * @param id
	 *            the id to set
	 */
	private void setId(int id) {
		this.id = id;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	private void setSize(int size) {
		this.size = size;
	}

	/**
	 * Process id size number of pages
	 * Virt Page 0 -> Phys Page 0 used: 32 bytes Virt Page 1 -> Phys Page 3
	 * used: 32 bytes Virt Page 2 -> Phys Page 4 used: 31 bytes
	 */
	public void printPagesInfo() {
		
		// print process info
		System.out.println("Process id = " + id + ", size = "
				+ size + ", number of pages"
				+ pages.length);

		// print each page info
		for (int i = 0; i < pages.length; i++) {
			Page p = pages[i];
			System.out.println("Virt Page " + i + " -> Phys Page"
					+ p.getPhysicalPosition() + " used: " + p.getUsedSize());
		}
	}

	/**
	 * @return the intFragInPaging
	 */
	public int getIntFragInPaging() {
		return intFragInPaging;
	}

	/**
	 * @param intFragInPaging
	 *            the intFragInPaging to set
	 */
	public void setIntFragInPaging(int intFragInPaging) {
		this.intFragInPaging = intFragInPaging;
	}
}
