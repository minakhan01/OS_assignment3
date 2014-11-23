public class Page {

	private int virtualPosition;
	private final int physicalPosition;
	private int usedSize;
	private Process assignedProcess;
	
	public boolean isUsed()
	{
		return (usedSize != 0);
	}

	/**
	 * @return the assignedProcess
	 */
	public Process getAssignedProcess() {
		return assignedProcess;
	}

	/**
	 * @param assignedProcess the assignedProcess to set
	 */
	public void setAssignedProcess(Process assignedProcess) {
		this.assignedProcess = assignedProcess;
	}

	public Page(int physicalPosition, int usedSize) {
		this.physicalPosition = physicalPosition;
		this.usedSize = usedSize;
	}
	
	public Page(int physicalPosition) {
		this.physicalPosition = physicalPosition;
	}

	/**
	 * @return the virtualPosition
	 */
	public int getVirtualPosition() {
		return virtualPosition;
	}

	/**
	 * @return the physicalPosition
	 */
	public int getPhysicalPosition() {
		return physicalPosition;
	}

	/**
	 * @return the usedSize
	 */
	public int getUsedSize() {
		return usedSize;
	}

	/**
	 * @param virtualPosition
	 *            the virtualPosition to set
	 */
	public void setVirtualPosition(int virtualPosition) {
		this.virtualPosition = virtualPosition;
	}


	/**
	 * @param usedSize
	 *            the usedSize to set
	 */
	public void setUsedSize(int usedSize) {
		this.usedSize = usedSize;
	}
	
	public void free()
	{
		this.assignedProcess = new Process(-1,-1);
		this.usedSize = 0;
		this.virtualPosition = 0;
	}

}
