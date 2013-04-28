package com.spacepirates.grumo.server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Date;

public class DateStringConverter extends ValueConverter {

	final static String number = "(\\d+)";
	final static Pattern timeStampPattern = Pattern.compile(number + "-" + number + "-" + number + " " + number + ":" + number + ":" + number + "," + number );
	public static final String MONTH_FORMAT = "MMM dd HH:mm:ss";
	private Matcher mMatcher;
	public static String COMPLETE_FORMAT = "yyyy-MM-dd HH:mm:ss,S";
	public static String SIMPLE_TIME_FORMAT = "HH:mm:ss";
	private DateFormat mFormat;
	
	public DateStringConverter(String format){
		mFormat = new SimpleDateFormat(format);
	}

	
	@Override
	public double parse(String line, ParsePosition position){
		Date date;
		date = mFormat.parse(line, position);
		if (date == null) {
			return -666; // Can't parse
		}
		return date.getTime() / 1000.0;					
	}






}
