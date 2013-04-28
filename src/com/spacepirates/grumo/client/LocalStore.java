package com.spacepirates.grumo.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.spacepirates.grumo.shared.DataBlock;
import com.spacepirates.grumo.shared.DataSet;
import com.spacepirates.grumo.shared.SimpleDataPointImpl;

public class LocalStore {

	public static interface DataSetListener{
		public void onSuccess(String dataSetName);
		public void onFailure(String dataSetName, Throwable caught);
	}
	private ArrayList<DataSet> mDataSets;

	
	private HashMap<String, DataSetListener> mDataListeners;
	
	public LocalStore(){
		mDataSets = new ArrayList<DataSet>();
		mDataListeners = new HashMap<String, LocalStore.DataSetListener>();
	}
	
    public void createDataSet(String dataSetName){
    	mDataSets.add(new DataSet(dataSetName));
    }
    
	public DataSet getDataSet(String dataSetName){
		for (DataSet dataSet : mDataSets){
			if (dataSet.getName().equals(dataSetName)){
				return dataSet;
			}
		}
		return null; // not found
	}

	public synchronized void  fillData(String dataSetName, DataBlock block){
		DataSet dataSet = getDataSet(dataSetName);

		for (int i = 0 ; i < 800 ; i++){
			SimpleDataPointImpl p = new SimpleDataPointImpl(dataSet);
			for (int j = 0 ; j < block.columns.length; j++){
				String col = block.columns[j];
				p.set(col, block.values.get(i)[j]);
			}
			dataSet.contents.add(p);
		}

	}
	
	public double getMinDataTime(String dataSetName, DataMapping mapping){
		DataSet dataSet = getDataSet(dataSetName);
		return mapping.getTimeStamp(dataSet.contents.get(0));
	}

	public double getMaxDataTime(String dataSetName, DataMapping mapping){
		DataSet dataSet = getDataSet(dataSetName);
		return mapping.getTimeStamp(dataSet.contents.get(dataSet.contents.size() - 1));
	}
	
	public void addListener(String dataSetName, DataSetListener dataSetListener){
		mDataListeners.put(dataSetName, dataSetListener);		
	}
	public void loadDataSet(final String dataSetName, DataSetListener dataSetListener){
		if (getDataSet(dataSetName) == null){
			createDataSet(dataSetName);
		}
		
		addListener(dataSetName, dataSetListener);
		
		GetDataServiceAsync dataService = GWT.create(GetDataService.class);
		 dataService.getBlock(dataSetName, 0.0, 400.0, (AsyncCallback<DataBlock>) new AsyncCallback<DataBlock>() {

			 
			@Override
			public void onSuccess(DataBlock result) {
				DataSetListener listener = mDataListeners.get(dataSetName);
				fillData(dataSetName,result);
				listener.onSuccess(dataSetName);
			}

			
			@Override
			public void onFailure(Throwable caught) {
				DataSetListener listener = mDataListeners.get(dataSetName);
				listener.onFailure(dataSetName, caught);
			}
		});

	}

	public ArrayList<DataSet> getAvailableSets() {
		return mDataSets;
	}
}
