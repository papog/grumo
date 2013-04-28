package com.spacepirates.grumo.server;

import java.text.ParsePosition;

public abstract class ValueConverter implements SimpleConverter {

	@Override
	public double parse(String s) {
		return this.parse(s, new ParsePosition(0));
	}

	@Override
	public abstract double parse(String s, ParsePosition position);



}
