package com.spacepirates.grumo.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.spacepirates.grumo.client.GetDataService;

import com.spacepirates.grumo.shared.DataBlock;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the GetDataService. It is in charge of providing data for different data sets, identified by a name.
 * The road map is to implement multiple data sources that can be parsed and accessed dynamically.
 * The service should be able to extract Abstract Records from the original source.
 * For each Abstract Record, a timestamp should be available. That's the 
 * Information representing the Abstract Records and intended to be graphed is provided by getBlock for all record in a time interval. 
 * Also, an address allowing to find more details about the actual entity behind an Abstract Record is required for each item.
 * 
 * The following data sources are expected:
 *  * text files with a block structure (Log4J, GC logs from IBM and Sun, strace with timestamp, CSV-like log files)
 *  * output of monitoring commands
 *  * SQL query results
 *  * NoSQL query results
 *  * Carbon store which is Graphite's storage part
 * Indirect data sources should log events as CSV, NoSQL or SQL entries into a temporary DB, this avoids reinventing an internal DB.
 * Ideas for additional sources:
 *  * Monitoring file system activity (see file updates, log event about it: timestamp=modif time, size of file, file path as string, checksum of file path, owner, permissions as int)
 *  * top/prstat: processes activity (run command listing process activity, log event about it: timestamp, PID, PPID, memory size, CPU activity, command)
 *  * tailing unstructured files that lack timestamps etc. By looking at them regulary, recreate timestamps (e.g. log file without time stamps, purify log)
 *  * all Unix commands and observations (lsof, netstat -a, netstat -s , vmstat, nmon, sar, pstack, pargs, pfiles)
 * For each source, we need to specify:
 * * a URI to find the source, for instance file:, http:, mysql: etc.
 * * a parser/interpreter for the data from the source. This produces dictionary-like entries where values are text or typed values if coming from a more semantic source
 * * a summarizer that takes dictionary-like entries and projects everything to scalars that can be managed by the GUI
 * * a key : one of the dictionary values should be available for all entries, translated into the summary and there should be a mechanism to quickly retrieve the first entry in the source 
 *   that maches a given value of that key. This is typically the timestamp.
 */
@SuppressWarnings("serial")
public class GetDataServiceImpl extends RemoteServiceServlet implements
GetDataService {

	private RecordFile getRecordFileForSetName(String setName) throws FileNotFoundException, IOException {
		
		// Source => Interpreter (Parser, converter) => GUI
		// 
		// CSV :
		//  * separator
		//  * column names
		//  * conversions to double for use in the GUI: conversion method and column id/name, new name
		//  * name/id of the timestamp
		// Block structured log:
		//  * Regexp for the header
		//  * convention to end block: implicit or Extra Regexp
		//  * conversions to double for use in the GUI same as in CSV
		//  * name/id of the timestamp
		
		if (setName.endsWith(".log")){
			if (setName.endsWith("messages.log")){
				return new LogRecordFile(setName, new LogFormat(DateStringConverter.MONTH_FORMAT));
			}
			else {
				return new LogRecordFile(setName, new LogFormat(DateStringConverter.COMPLETE_FORMAT));
			}
		}
		else if (setName.endsWith(".csv")){
			ArrayList<SimpleConverter> parsers = new ArrayList<SimpleConverter>();
			parsers.add(new DateStringConverter(DateStringConverter.SIMPLE_TIME_FORMAT));
			parsers.add(new LengthConverter());
			parsers.add(new LengthConverter());
			parsers.add(new DoubleConverter());
			parsers.add(new DoubleConverter());
			parsers.add(new LengthConverter());
			return new CSVFile(setName, "\t").setParsers(parsers);
		}
		return new CSVFile(setName, "\t");
	}
	/**
	 * get a group of data summaries for abstract records with timestamp between startTime and endTime
	 */
	public DataBlock getBlock(String setName, double startTime, double endTime)
			throws IllegalArgumentException {
		DataBlock result = new DataBlock();
		int maxSize = 1024;
		result.values = new ArrayList<double[]>();
		int current_item = 0;
		long offset = 0;
		try {
			RecordFile recordFile = getRecordFileForSetName(setName);
			result.columns = recordFile.getColumns();
			while (current_item < maxSize){
				ArrayList<String> tmpBuffer = recordFile.readRecord();
				result.values.add(recordFile.parseRecordText(tmpBuffer,offset));
				offset = recordFile.getFilePointer();
				current_item++;
			}

		}
		catch (FileNotFoundException e){


			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			GWT.log("error" + e.toString());
		}
		GWT.log("sending results" + Arrays.toString(result.columns));
		return result;

	}



	/**
	 * Obtain a summary text description of the Abstract Record designated by Address in set setName
	 */
	public String getText(String setName, long address)
			throws IllegalArgumentException {
		StringBuffer sbOut = new StringBuffer();
		try {
			RecordFile inputFile = getRecordFileForSetName(setName);
			return inputFile.getRecordAsText(address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sbOut.toString();
	}
	@Override
	public ArrayList<String> listAvailableSets() throws IllegalArgumentException {
		ArrayList<String> result = new ArrayList<String>();
		result.add("input.log");
		result.add("input.csv");
		result.add("messages.log");
		return result;
	}




}
