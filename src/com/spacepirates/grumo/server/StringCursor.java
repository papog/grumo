package com.spacepirates.grumo.server;

import java.text.ParsePosition;

/**
 * encapsulates a string and a cursor that points to the current character in the string.
 * 
 * @author pgaillard
 *
 */
public class StringCursor {

	private String mString;
	private ParsePosition mPosition;
	
	public StringCursor(String input){
		mPosition = new ParsePosition(0);
		mString = input;
	}
	
	public String getString(){
		return mString;
	}
	
	public ParsePosition getPosition(){
		return mPosition;
	}
	

}
