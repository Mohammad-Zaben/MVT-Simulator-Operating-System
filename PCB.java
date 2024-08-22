public class PCB {
	// Attributes of the PCB
	private int processId; // Process ID
	private int size; // Size of the process
	private int timeInMemory; // Time in memory
	private int baseRegister; // Base register
	private int limitRegister; // Limit register
	private int staticTime;

	// Constructor
	public PCB(int processId, int size, int timeInMemory, int baseRegister, int limitRegister) {
		this.processId = processId;
		this.size = size;
		this.timeInMemory = timeInMemory;
		this.baseRegister = baseRegister;
		this.limitRegister = limitRegister;
		staticTime=timeInMemory;
	}

	public PCB(int processId, int size, int timeInMemory) {
		this.processId = processId;
		this.size = size;
		this.timeInMemory = timeInMemory;
		staticTime=timeInMemory;

	}

	// Getters
	public int getProcessId() {
		return processId;
	}

	public int getSize() {
		return size;
	}

	public int getTimeInMemory() {
		return timeInMemory;
	}

	public int getBaseRegister() {
		return baseRegister;
	}

	public int getLimitRegister() {
		return limitRegister;
	}

	// Setters
	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setTimeInMemory(int timeInMemory) {
		this.timeInMemory = timeInMemory;
	}

	public void setBaseRegister(int baseRegister) {
		this.baseRegister = baseRegister;
	}

	public void setLimitRegister(int limitRegister) {
		this.limitRegister = limitRegister;
	}

	public int getStaticTime() {
		return staticTime;
	}

	// toString method to display PCB details
	@Override
	public String toString() {
		return "PCB{" + "processId=" + processId + ", size=" + size + ", timeInMemory=" + timeInMemory
				+ ", baseRegister=" + baseRegister + ", limitRegister=" + limitRegister + '}';
	}
}
