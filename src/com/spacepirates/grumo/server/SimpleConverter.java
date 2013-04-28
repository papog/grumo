package com.spacepirates.grumo.server;

import java.text.ParsePosition;

public interface SimpleConverter {

	public class ParsingFailureException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = -1130286502556408941L;
		
	}
	
	public double parse(String s);
	
	public double parse(String s, ParsePosition position);
	
	
	
}
