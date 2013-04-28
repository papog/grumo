package com.spacepirates.grumo.client;

public class IntPair {

	public int x;
	public int y;
	
	public IntPair(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public double distance(IntPair pair){
		double deltaX = this.x - pair.x;
		double deltaY = this.y - pair.y;
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

	
	@Override
	public int hashCode() {
	
		
		return x + 10000 * y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntPair other = (IntPair) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
}
