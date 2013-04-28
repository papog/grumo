package com.spacepirates.grumo.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

/**
 * Glyph represents an item that can be drawn and has variable drawing parameters used to represent different input values.
 * Each parameter can be constant or keyed to some value (formula or table)
 * General parameters:
 *  * y position at which the glyph is drawn
 *  * color used to draw
 *  * size
 * @author pgaillard
 *
 */
public interface Glyph {

	void draw(Context2d context, double x, double y, double size, CssColor c);
	
}
