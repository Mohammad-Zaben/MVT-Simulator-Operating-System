import java.util.LinkedList;

public class Memory {
	private int memory_size = 2048; // the size of memory
	private int OS_size = 512; // the size of Operating System
	private int base_reg = OS_size;
	private LinkedList<Object> memory = new LinkedList<>(); // this is memory list, to store the processes and holes
	private LinkedList<PCB> ready;
	private LinkedList<PCB> jobs;
	private LinkedList<PCB> finish = new LinkedList<>();
	private static int OverAllTime;

	public Memory(LinkedList<PCB> ready, LinkedList<PCB> jobs) {
		this.ready = ready;
		this.jobs = jobs;
		AllocatReady();

	}

	private void AllocatReady() { // this first method in the program, to allocate the process from ready Q to
									// memory

		for (PCB process : ready) {
			if (process.getSize() < (memory_size - base_reg)) {
				process.setBaseRegister(base_reg);
				process.setLimitRegister(base_reg + process.getSize());
				base_reg = base_reg + process.getSize();
				memory.add(process);
			}
		}
		int basehole = ((PCB) (memory.getLast())).getLimitRegister();
		int limithole = memory_size;
		int size = limithole - basehole;
		Hole hole = new Hole(basehole, limithole, size);
		memory.add(hole);

		System.out.println(memory.toString());

	}

	public void creatHole() { // this method to create hole by remove the lees process time
		int mintime = Integer.MAX_VALUE;
		int index = 0;
		if (cheack()) { // after create hole ,check if the memory has any free available hole?
			if (jobs.size() > 0)
				for (int i = 0; i < memory.size(); i++) {
					if (memory.get(i) instanceof PCB) {
						if (((PCB) (memory.get(i))).getTimeInMemory() < mintime) {
							mintime = ((PCB) (memory.get(i))).getTimeInMemory();
							index = i; // to get the index from the process that has a minimum time from the memory
						}
					}
				}
			PCB f = ((PCB) (memory.remove(index)));
			OverAllTime += f.getTimeInMemory(); // to calculate the over all time in the processing the process.
			finish.add(f); // add the finish job to finish queue
			Hole hole = new Hole(f.getBaseRegister(), f.getLimitRegister(), f.getSize());
			memory.add(index, hole);

			for (int i = 0; i < memory.size(); i++) {
				if (memory.get(i) instanceof PCB) {
					int time = ((PCB) (memory.get(i))).getTimeInMemory();
					((PCB) (memory.get(i))).setTimeInMemory(time - f.getTimeInMemory());
				}
			}
		}

	}

	public boolean AllocatNextProcess() { // to get the job from memory in FCFS with skip

		for (int i = 0; i < jobs.size(); i++) {
			for (int j = 0; j < memory.size(); j++) {
				if (memory.get(j) instanceof Hole) {
					if ((((Hole) (memory.get(j))).getSize()) >= jobs.get(i).getSize()) { // if the process/job is
																							// available in the hole or
																							// not
						Hole hole = ((Hole) (memory.get(j)));
						PCB job = jobs.remove(i);
						job.setBaseRegister(hole.getBaseReg());
						job.setLimitRegister(job.getSize() + job.getBaseRegister());
						((Hole) (memory.get(j))).setSize(hole.getSize() - job.getSize());
						((Hole) (memory.get(j))).setBaseReg(job.getLimitRegister());
						if (((Hole) (memory.get(j))).getSize() == 0) {
							memory.remove(j);
						}
						memory.add(j, job);

						return true;
					}
				}
			}
		}
		return false;

	}

	private boolean cheack() { // check if the memory need create hole or no
		for (int i = 0; i < jobs.size(); i++) {
			for (int j = 0; j < memory.size(); j++) {
				if (memory.get(j) instanceof Hole) {
					if (jobs.get(i).getSize() < ((Hole) (memory.get(j))).getSize()) {
						return false;
					}
				}
			}

		}
		return true;

	}

	public void memoryString() {
		System.out.println(memory.toString());

	}

	public LinkedList<Object> getMemory() {
		return memory;
	}

	public void setMemory(LinkedList<Object> memory) {
		this.memory = memory;
	}

	public int getOS_size() {
		return OS_size;
	}

	public void setOS_size(int oS_size) {
		OS_size = oS_size;
	}

	public LinkedList<PCB> getJobs() {
		return jobs;
	}

	public LinkedList<PCB> getFinish() {
		return finish;
	}

	public int getOverAllTime() {
		return OverAllTime;
	}

}
