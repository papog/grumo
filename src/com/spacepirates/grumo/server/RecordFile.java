package com.spacepirates.grumo.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

public abstract class RecordFile {

	protected RandomAccessFile mRaf;
	public RecordFile(String path) throws FileNotFoundException{
		File inputFile = new File(path);
		mRaf = new RandomAccessFile(inputFile, "r");

	}
	
	public abstract String[] getColumns();
	
	/**
	 * isRecordStart should be redefined/customized depending on the type of file we need to parse
	 * @param line
	 * @return
	 */
	public abstract boolean isRecordStart(String line);


	/**
	 * gotoFirstRecord looks through the data source to find the first record after the startTime
	 * @param setName
	 * @param startTime
	 * @return
	 * @throws IOException 
	 */
	public long gotoFirstRecord(double startTime) throws IOException {
		// find 
		mRaf.seek(0);
		long length = mRaf.length();

		return 0;
	}

	public ArrayList<String> readRecord() throws IOException{
		boolean started = false;
		ArrayList<String> sbOut = new ArrayList<String>();
		while (true){
			long offset = mRaf.getFilePointer();
			String line = mRaf.readLine();
			//TODO handle incomplete records (IO error before end), last record etc.
			if (line == null){
				GWT.log("got no line");
			}
			else{
				if (isRecordStart(line)){

					if (! started) {
						started = true;
						sbOut.add(line + "\n");
					}
					else {
						mRaf.seek(offset);
						return sbOut;
					}
				}
				else {
					sbOut.add(line + "\n");
				}
			}
		}
	}
	
	
	public String getRecordAsText(long address)
			throws IllegalArgumentException {
		StringBuffer sbOut = new StringBuffer();
		try {

			mRaf.seek(address);
			for (String line : readRecord()){
				sbOut.append(line);
			}
			return sbOut.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return sbOut.toString();
	}

	public long getFilePointer() throws IOException {
		return mRaf.getFilePointer();


	}

	public abstract double[] parseRecordText(ArrayList<String> tmpBuffer, long offset);

}
