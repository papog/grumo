package com.spacepirates.grumo.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.spacepirates.grumo.shared.DataBlock;

/**
 * The async counterpart of <code>GetDataService</code>.
 */
public interface GetDataServiceAsync {

	void getBlock(String setName, double startTime, double endTime,
			AsyncCallback<DataBlock> callback);

	void getText(String setName, long address, AsyncCallback<String> callback);

	void listAvailableSets(AsyncCallback<ArrayList<String>> callback);

;
}
