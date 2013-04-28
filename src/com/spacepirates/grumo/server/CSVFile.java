package com.spacepirates.grumo.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

public class CSVFile extends RecordFile {

	private String mSeparator;
	private Collection<String> mColumns;
	private int mAddressId;
	private ArrayList<SimpleConverter> mParsers;
	
	public CSVFile(String path, String separator) throws FileNotFoundException, IOException {
		super(path);
		mSeparator = separator;
		readColumns();
		mColumns.add("address");
		mAddressId = mColumns.size() - 1;
		mParsers = new ArrayList<SimpleConverter>();
	}

	/**
	 * read the line describing the CSV columns.
	 * @throws IOException
	 */
	private void readColumns() throws IOException {
		mColumns = new ArrayList<String>();
		String line = mRaf.readLine();
		StringTokenizer st = new StringTokenizer(line, mSeparator);
		while (st.hasMoreTokens()){
			mColumns.add(st.nextToken());
		}
	}
	
	public CSVFile setParsers(ArrayList<SimpleConverter> parsers){
		mParsers = parsers;
		return this;
	}

	@Override
	public String[] getColumns() {
		String[] result = new String[mColumns.size()];
		int i = 0;
		for (String col : mColumns){
			result[i] = col;
			i++;
		}
		return result;
	}

	@Override
	public boolean isRecordStart(String line) {
		return true;
	}

	@Override
	public double[] parseRecordText(ArrayList<String> tmpBuffer, long offset) {
		assert tmpBuffer.size() == 1;
		double [] result = new double[mColumns.size()];
		int column = 0;
		StringTokenizer st = new StringTokenizer(tmpBuffer.get(0), mSeparator);

		while (st.hasMoreTokens()){
			String token = st.nextToken();
			SimpleConverter parser = mParsers.get(column); 
			double tmp = parser.parse(token); // TODO handle non float values.
			result[column++] = tmp;
		}
		result[mAddressId] = offset;
		return result;
	}

}
