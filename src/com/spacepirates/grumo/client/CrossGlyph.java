package com.spacepirates.grumo.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class CrossGlyph implements Glyph {
	
	public CrossGlyph(){
	}
	
	@Override
	public void draw(Context2d context, double x, double y, double size, CssColor c) {
		context.setStrokeStyle(c);
		context.beginPath();
		context.moveTo(x - size, y);
		context.lineTo(x + size, y);
		context.moveTo(x, y - size);
		context.lineTo(x, y + size);
		context.closePath();
		context.stroke();
	}

	
}
