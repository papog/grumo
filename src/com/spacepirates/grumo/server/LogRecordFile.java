package com.spacepirates.grumo.server;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogRecordFile extends RecordFile {

	DateStringConverter mDateParser;
	
	
	public LogRecordFile(String path, LogFormat logFormat) throws FileNotFoundException {
		super(path);
		mDateParser = new DateStringConverter(logFormat.dateFormat);

	}
	
	final static int timestamp_id = 0;
	final static int line_length_id = 1;
	final static int nb_lines_id = 2;
	final static int address_id = 3;
	final static int log_level_id = 4;
	final static String word = "(\\w+)";
	final static Pattern logLevelPattern = Pattern.compile(": " + word + " -");

	@Override
	public String[] getColumns() {
		String [] columns = new String[5];
		columns[timestamp_id] = "timestamp";
		columns[line_length_id] = "line_length";
		columns[nb_lines_id] = "nb_lines";
 		columns[address_id] = "address";
 		columns[log_level_id] = "log_level";
		return columns;
	}


	@Override
	public double[] parseRecordText(ArrayList<String> tmpBuffer, long offset) {
		
		
		double values[] = new double[getColumns().length]; 
		String line = tmpBuffer.get(0);
		double timestamp = mDateParser.parse(line);
		if (timestamp > 0) {
			values[timestamp_id]  = timestamp;
			values[line_length_id] = line.length();
			values[address_id] = offset; //TODO range limit issue.
			values[nb_lines_id] = tmpBuffer.size();
			Matcher levelMatcher = logLevelPattern.matcher(line);
			if (levelMatcher.find()){
				values[log_level_id] = logLevelToNumber(levelMatcher.group(1));
			}
			return values;
		}
		return null; // Nothing could be parsed.
	}


	private double logLevelToNumber(String group) {
		if (group.equals("FATAL"))
		{
			return 4;
		}
		if (group.equals("ERROR"))
		{
			return 3;
		}
		
		if (group.equals("WARNING"))
		{
			return 2;
		}

		if (group.equals("INFO"))
		{
			return 1;
		}
		
		return 5; // UNKNOWN is worse than FATAL ;-)
	}


	@Override
	public boolean isRecordStart(String line) {
		{
			double timestamp = mDateParser.parse(line);

			//return (line.length() > 1) && line.charAt(0) <= '9' && line.charAt(0) >= '0';
			return (line.length() > 5) && timestamp > 0;
		}
	}
}
