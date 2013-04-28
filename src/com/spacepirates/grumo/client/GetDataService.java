package com.spacepirates.grumo.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.spacepirates.grumo.shared.DataBlock;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("getdata")
public interface GetDataService extends RemoteService {
	
	ArrayList<String> listAvailableSets() throws IllegalArgumentException;
	
	DataBlock getBlock(String setName, double startTime, double endTime) throws IllegalArgumentException;

	String getText(String setName, long address) throws IllegalArgumentException;
	
}
