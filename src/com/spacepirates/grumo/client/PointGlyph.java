package com.spacepirates.grumo.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class PointGlyph implements Glyph {

	
	public PointGlyph(){

	}
	
	
	@Override
	public void draw(Context2d context, double x, double y, double size, CssColor c) {
		context.setStrokeStyle(c);
		context.strokeRect(x, y, 1, 1);	
	}
	
	

}
