package com.spacepirates.grumo.server;

import java.text.ParsePosition;

public class LengthConverter implements SimpleConverter {

	@Override
	public double parse(String s) {
		return s.length();
	}

	@Override
	public double parse(String s, ParsePosition position) {
		position.setIndex(s.length());
		return parse(s.substring(position.getIndex()));
	}


}
