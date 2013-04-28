package com.spacepirates.grumo.shared;

import java.util.ArrayList;


public class DataSet {

	private String mName;
	
	public ArrayList<DataPoint> contents;

	/**
	 * return the name of the dataSet as used by 
	 * @return
	 */
	public String getName(){
		return mName;
	}
	public DataSet(String name){
		mName = name;
		contents = new ArrayList<DataPoint>();
	}
	
}
