public class Hole {
    private int baseReg;
    private int limitReg;
    private int size;

    public Hole(int baseReg, int limitReg,int size) {
        this.baseReg = baseReg; // hole base register
        this.limitReg = limitReg; // hole limit register
        this.size=size; // hole size 
    }

    public int getBaseReg() {
        return baseReg;
    }

    public void setBaseReg(int baseReg) {
        this.baseReg = baseReg;
    }

    public int getLimitReg() {
        return limitReg;
    }

    public void setLimitReg(int limitReg) {
        this.limitReg = limitReg;
    }

    
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Hole [baseReg=" + baseReg + ", limitReg=" + limitReg + ", size=" + size + "]";
	}

	
    
}
