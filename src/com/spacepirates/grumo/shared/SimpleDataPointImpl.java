package com.spacepirates.grumo.shared;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;

public class SimpleDataPointImpl implements DataPoint {

	private HashMap<String, Double> values;
	private DataSet mDataSet; // data set containing the point. There can only be one.
	
	public SimpleDataPointImpl(DataSet dataSet){
		values = new HashMap<String, Double>();
		mDataSet = dataSet;
	}
	@Override
	public double getAsDouble(String name) {
		return values.get(name); 
	}

	public void set(String name, double value){
		//GWT.log("data point:" + name + "value:" + value );
		values.put(name, value);
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (String key: values.keySet()){
			sb.append(key + ":" + values.get(key) + "\n");
		}
		return sb.toString();
	}
	@Override
	public DataSet getDataSet() {
		// TODO Auto-generated method stub
		return mDataSet;
	}
}
