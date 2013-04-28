package com.spacepirates.grumo.shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * a possible data block implementation to represent simplified data AFTER retrieval, parsing and summarization.
 * This is what the client takes to draw graphs.
 * 
 * @author pgaillard
 *
 */
public class DataBlock implements Serializable {
	/**
	 * Eclipse generated static version id.
	 */
	private static final long serialVersionUID = -1755627162693262642L;
	public String columns[];
	public int references[];
	public ArrayList<double[]> values;

}
