package com.spacepirates.grumo.shared;


public interface DataPoint {

	/**
	 * find attribute called name and return its contents as a double
	 * strings can be converted automatically. 
	 * @param name
	 * @return
	 */
	public double getAsDouble(String name);
	
	/**
	 * set attributed called name.
	 * @param name
	 * @param value
	 */
	public void set(String name, double value);
	
	/**
	 * return the DataSet that contains the point.
	 * @return
	 */
	public DataSet getDataSet();
	
}
