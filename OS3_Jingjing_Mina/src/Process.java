public class Process {

	private int id;
	private int size;
	private Segment[] segments;
	private Page[] pages;
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

	public Process(int id, int size) {
		this.id = id;
		this.size = size;
	}

	public Segment[] getSegments() {
		return this.segments;
	}

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
	 * Virt Page 0 -> Phys Page 0 used: 32 bytes Virt Page 1 -> Phys Page 3
	 * used: 32 bytes Virt Page 2 -> Phys Page 4 used: 31 bytes
	 */
	public void printPagesInfo() {

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
