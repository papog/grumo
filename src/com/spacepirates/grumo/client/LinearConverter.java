package com.spacepirates.grumo.client;

public class LinearConverter {
	private double mTop;
	private double mBottom;
	private double mMinRange;
	private double mMaxRange;
	private double mDimension = 100.0;
	
	public LinearConverter setDimension(double dimension){
		mDimension = dimension;
		return this;
	}
	
	public LinearConverter setRange(double min, double max){
		mMinRange = min;
		mMaxRange = max;
		return this;
	}
	
	public LinearConverter setRatio(double bottom, double top){
		mTop = top;
		mBottom = bottom;
		return this;
	}

	public double getBottom(){
		return mBottom;
	}
	
	public double getTop(){
		return mTop;
	}
	
	public double getMaxRange(){
		return mMaxRange;
	}
	
	public double getMinRange(){
		return mMinRange;
	}

	
	public double convert(double value , double dimension){
		if (value < mMinRange){
			value = mMinRange;
		}
		else if (value > mMaxRange){
			value = mMaxRange;
		}
		return mBottom * dimension + (value - mMinRange) * dimension * (mTop - mBottom) / (mMaxRange - mMinRange);
	}
	
	public double convert(double value){
		return convert(value, mDimension);
	}
	


}
