package com.spacepirates.grumo.client;

import com.google.gwt.canvas.dom.client.CssColor;

/**
 * Represents the characteristics of a plot
 * * mapping to extract data
 * * glyph used to represent data
 * * range of representation on screen @TODO top, bottom as percentage of screen height 
 * * log scale @TODO
 * * saturation @TODO min value, max value, saturate or clip
 * * autoscale @TODO
 * 
 *  How to convert a value to an on screen position in an area of a given dimension.
 *  bottom * dimension + (value - minValue) * dimension * (top - bottom) / (maxValue - minValue)  
 * @author pgaillard
 *
 */
public class PlotDefinition {

	private Glyph mGlyph;
	private DataMapping mMapping;
	private ObjectKeyer<CssColor> mColorKeyer;
	private LinearConverter mYConverter;
	private LinearConverter mSizeConverter;
	
	
	public PlotDefinition( DataMapping mapping, Glyph glyph, LinearConverter yConverter, ObjectKeyer<CssColor> colorKeyer, LinearConverter sizeConverter){
		mGlyph = glyph;
		mMapping = mapping;
		mColorKeyer = colorKeyer;
		mYConverter = yConverter;
		mSizeConverter = sizeConverter;
	}
	
	
	public DataMapping getMapping(){
		return mMapping;
	}
	
	
	public Glyph getGlyph(){
		return mGlyph;
	}

	public double convert(double value , double dimension){
		return mYConverter.convert(value, dimension);
	}
	
	public CssColor convertColor(double value) {
		return mColorKeyer.keyedObject(value);
	}
	
	public double convertSize(double value){
		return mSizeConverter.convert(value);
	}
}
