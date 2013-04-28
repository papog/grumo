package com.spacepirates.grumo.client;

import com.spacepirates.grumo.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Grumo implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	    
        GraphCanvas gc = new GraphCanvas();
	    RootPanel.get().add(gc);
	    gc.render();
	}
	

}
