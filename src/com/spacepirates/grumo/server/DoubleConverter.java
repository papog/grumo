package com.spacepirates.grumo.server;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.NumberFormat;
public class DoubleConverter extends ValueConverter {

	private NumberFormat mFormat;
	
	public DoubleConverter(){
		mFormat = new DecimalFormat();
	}

	@Override
	public double parse(String s, ParsePosition position) {
		return  mFormat.parse(s, position).doubleValue();
	}



}
