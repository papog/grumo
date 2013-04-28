package com.spacepirates.grumo.client;

import java.util.ArrayList;

public class ObjectKeyer<Type> {

	ArrayList<Type> mArray;
	
	public ObjectKeyer(){
		mArray = new ArrayList<Type>();
	}
	
	public ObjectKeyer<Type> add(double value, Type element){
		mArray.add((int) value, element);
		return this;
	}
	
	public Type keyedObject(double value){
		Type result =  mArray.get((int) value);
		if (result == null){
			return mArray.get(0);
		}
		else {
			return result;
		}
	}
	
}
