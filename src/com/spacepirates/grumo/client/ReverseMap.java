package com.spacepirates.grumo.client;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gwt.dev.util.collect.HashMap;
import com.spacepirates.grumo.shared.DataPoint;

public class ReverseMap {

	private java.util.HashMap<IntPair, DataPoint> map;
	
	private static class ProximityComparator implements Comparator<IntPair>{

		IntPair mRef;
	
		
		public ProximityComparator(IntPair ref){
			mRef = ref;
		}
		
		@Override
		public int compare(IntPair arg0, IntPair arg1) {
			double d0 = mRef.distance(arg0);
			double d1 = mRef.distance(arg1);
			return ((d0 < d1 ) ? -1 : (d0 == d1 ? 0 : 1));
		}
		
		
	}
	public ReverseMap(){
		map = new java.util.HashMap<IntPair, DataPoint>();
	}
	
	public void put(int x, int y, DataPoint point){
		IntPair p = new IntPair(x,y);
		map.put(p, point);
	}
	public DataPoint get(int x, int y){
		IntPair p = new IntPair(x, y);
		return get(p);
	}
	public DataPoint get(IntPair location){
		return map.get(location);
	}
	/**
	 * look for datapoint starting at x,y in a square defined by diameter.
	 * get closest point
	 * @param x
	 * @param y
	 * @param diameter
	 * @return
	 */
	public DataPoint get(int x, int y, int diameter){
		ArrayList<IntPair> searchList = new ArrayList<IntPair>();
		DataPoint result = null;
		int deltaX = 0;
		int deltaY =0;
		int minDeltaX = 0;
		int maxDeltaX = 0;
		int minDeltaY = 0;
		int maxDeltaY = 0;
		
		
		for (int i = -diameter ; i <=  diameter ; i++ ){
			for (int j = -diameter ; j <= diameter ; j++ ){
				searchList.add(new IntPair(x+i, y+j));
			}
		}
		java.util.Collections.sort(searchList, new ProximityComparator(new IntPair(x,y)));
		for (IntPair location : searchList){
			result = get(location);
			if (result != null){
				return result;
			}
		}
		return null;
	}

	public int size() {
		return map.size();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (IntPair key: map.keySet()) {
			sb.append(" " + key.x + " " + key.y + "\n");
		}
		return sb.toString();		
	}
}
