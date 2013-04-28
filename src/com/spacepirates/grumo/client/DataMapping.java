package com.spacepirates.grumo.client;

import java.util.ArrayList;

import com.spacepirates.grumo.shared.DataPoint;

public class DataMapping {

	private String mTimeStampName;
	private ArrayList<String> mValueName;
	

	public DataMapping(String timeStampName, String valueName){
		mTimeStampName = timeStampName;
		mValueName = new ArrayList<String>();
		mValueName.add(valueName);
	}
	
	public double getTimeStamp(DataPoint p){
		return p.getAsDouble(mTimeStampName);
	}

	public double getValue(DataPoint p){
		return p.getAsDouble(mValueName.get(0));
	}
	
	public double getValue(DataPoint p, int vIndex){
		return p.getAsDouble(mValueName.get(vIndex));
	}

	/**
	 * add another mapping, column name that can be collected using getValue
	 * @param string
	 * @return
	 */
	public DataMapping add(String string) {
		mValueName.add(string);
		return this;
	}

}