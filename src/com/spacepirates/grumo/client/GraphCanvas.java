package com.spacepirates.grumo.client;


import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalScrollbar;
import com.google.gwt.user.client.ui.NativeHorizontalScrollbar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.spacepirates.grumo.shared.DataBlock;
import com.spacepirates.grumo.shared.DataPoint;
import com.spacepirates.grumo.shared.DataSet;

/**
 * A canvas and control buttons to display multiple overlaid time based graphs.
 *  - zoom in and out (time)
 *  - scroll left/right (time)
 *  - click on glyph, get corresponding datapoint
 *  
 * @author pgaillard
 *
 */
public class GraphCanvas extends Composite  {

	private Canvas mCanvas;
	private Button mDataSetButton;
	private LocalStore mStore;
	private HashMap<String, PlotDefinition> mPlotDefinitionDict;
	private Button mZoomIn;
	private Button mZoomOut;
	private double mStartTime;
	private double mEndTime;
    private Context2d mContext;
	private double mScale;
	private HorizontalScrollbar mScrollBar;
	private MouseKeyHandler mMouseHandler; 
	private TextArea mTextArea;
	private ReverseMap mMap;
	
	public class MouseKeyHandler implements MouseMoveHandler, MouseUpHandler, MouseDownHandler, KeyDownHandler, KeyUpHandler  {

		private double startX;
		private double lastX;
		private boolean mouseDown;
		public MouseKeyHandler(){
			mouseDown = false;
		}
		@Override
		public void onMouseMove(MouseMoveEvent event) {
			GWT.log("mouse moved " + (event.getX() - lastX));
			if (mouseDown){
				if (event.isControlKeyDown()){ // Zoom 

				}
				else { // Pan
					double deltaTime = - (translateX(event.getX()) - translateX(lastX));
					if (Math.abs(event.getX() - lastX) > 3)
					{
						doTimeShift(deltaTime);
						lastX = event.getX();
					}
				}
			}
		}

		@Override
		public void onMouseDown(MouseDownEvent event) {
			startX = event.getX();
			lastX = startX;
	
			mouseDown = true;
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			double deltaTime = -(translateX(event.getX()) - translateX(lastX));

			if (Math.abs(event.getX() - lastX) > 3){
				if (event.isControlKeyDown()){

					setTimeRange(translateX(Math.min(lastX, event.getX())),
							translateX(Math.max(lastX, event.getX())));	
					render();

				}
				else {
					doTimeShift(deltaTime);
				}
			}
			else {
				DataPoint p = mMap.get(event.getX(), event.getY(), 10);
				if (p != null){
		   		    mTextArea.setText("found data\n" + p);
     				GetDataServiceAsync dataService = GWT.create(GetDataService.class);

					dataService.getText(p.getDataSet().getName(), (long) p.getAsDouble("address"), (AsyncCallback<String>) new AsyncCallback<String>() {

						 
						@Override
						public void onSuccess(String result) {
							String tmp = mTextArea.getText();
							mTextArea.setText(tmp + "full text\n" +  result);
						}

						
						@Override
						public void onFailure(Throwable caught) {
							mTextArea.setText("Get data call failed:"+ caught);
						}
					});
				    
				}
				else {
					mTextArea.setText("nothing clicked at:" + event.getX() + " " + event.getY() + " map has " + mMap.size() + "contains:" + mMap);
				}
			}
			mouseDown = false;
		}
	
		@Override
		public void onKeyUp(KeyUpEvent event) {
			double deltaTime = 0.0;
			if (event.isLeftArrow()){
				deltaTime = (mEndTime - mStartTime) / 10.0;
				doTimeShift(deltaTime);
			}
			if (event.isRightArrow()){
				deltaTime = - (mEndTime - mStartTime) / 10.0;
				doTimeShift(deltaTime);
			}
			if (event.isUpArrow()){
				doZoomIn();
				event.stopPropagation();
			}
			if (event.isDownArrow()){
				doZoomOut();
				event.stopPropagation();
			}
			
		}
		@Override
		public void onKeyDown(KeyDownEvent event) {
			
		}
		
	}
	public GraphCanvas(){
		VerticalPanel p = new VerticalPanel();
		mCanvas = Canvas.createIfSupported();
        mCanvas.setWidth(800 + "px");
        mCanvas.setHeight(450 + "px");
	    mCanvas.setCoordinateSpaceWidth(800);
	    mCanvas.setCoordinateSpaceHeight(450);
	    HorizontalPanel canvasAndText = new HorizontalPanel();
	    canvasAndText.add(mCanvas);
	    mTextArea = new TextArea();
	    mTextArea.setWidth(400+ "px");
	    mTextArea.setHeight(450 + "px");
	    canvasAndText.add(mTextArea);
	    mTextArea.setText("All is good");

	    p.add(canvasAndText);
	    mScrollBar = new NativeHorizontalScrollbar();
	    p.add(mScrollBar);
	    HorizontalPanel h = new HorizontalPanel();
	    mZoomIn = new Button("Zoom in");
	    mZoomOut = new Button("Zoom out");
	    mDataSetButton = new Button("Data Sets");
		final DataSetDialog dataSetDialog = new DataSetDialog();
		
		dataSetDialog.addChoiceListener(new DataSetDialog.ChoiceListener(){

			@Override
			public void signalSelected(ArrayList<String> selectedList) {
				loadSelected(selectedList);
			}
			
		});
		
	    mDataSetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dataSetDialog.show();
			}
		});
	    
	    mZoomIn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doZoomIn();
			}
		});
	    mZoomOut.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doZoomOut();
			}
		});
	    mMouseHandler = new MouseKeyHandler();
	    mCanvas.addMouseDownHandler(mMouseHandler);
	    mCanvas.addMouseUpHandler(mMouseHandler);
	    mCanvas.addMouseMoveHandler(mMouseHandler);
	    mCanvas.addKeyUpHandler(mMouseHandler);
	    mCanvas.addKeyDownHandler(mMouseHandler);

	    initWidget(p);
		
	    h.add(mZoomIn);
	    h.add(mZoomOut);
	    h.add(mDataSetButton);
	    p.add(h);
	    setStyleName("GraphCanvas");
	    mStore = new LocalStore();
	    mPlotDefinitionDict = new HashMap<String, PlotDefinition>();
		setupPlots();

	    setTimeRange(-100, 600);
	    mMap = new ReverseMap();

	}
	
	public void doZoomOut() {
		double pad = (mEndTime - mStartTime) / 2.0;
		setTimeRange(mStartTime - pad, mEndTime + pad);
		render();
	}
	
	public void doZoomIn() {
		double skip = (mEndTime - mStartTime) / 3.0; 
		setTimeRange(mStartTime + skip, mEndTime - skip);
		render();
	}
	


	public void setupPlots(){
		DataMapping mapping = new DataMapping("Date", "SQL").add("Category").add("Duration");
		mPlotDefinitionDict.put("input.csv",
								new PlotDefinition(mapping,
												   new CrossGlyph(),
												   new LinearConverter().setRatio(0.1, 0.8)
												                        .setRange(0.0, 200.0),
												   new ObjectKeyer<CssColor>().add(0, CssColor.make(255,255,255))
												       .add(1, CssColor.make("green"))
												       .add(2, CssColor.make("orange"))
												       .add(3, CssColor.make("red"))
												       .add(4, CssColor.make("violet"))
												       .add(5, CssColor.make("blue"))
												       .add(6, CssColor.make("pink")),
												   new LinearConverter().setRatio(0.1, 1.0)
												                        .setRange(0.0,1.0)
												                        .setDimension(15))
														);
		DataMapping mappingForLog = new DataMapping("timestamp", "line_length").add("log_level").add("nb_lines");
		mPlotDefinitionDict.put("input.log",
				new PlotDefinition(mappingForLog,
                new CrossGlyph(),
				   new LinearConverter().setRatio(0.1, 0.8)
				                        .setRange(0.0, 100.0),
				   new ObjectKeyer<CssColor>().add(0, CssColor.make(255,255,255))
				       .add(1, CssColor.make("green"))
				       .add(2, CssColor.make("orange"))
				       .add(3, CssColor.make("red"))
				       .add(4, CssColor.make("violet"))
				       .add(5, CssColor.make("pink")),
				   new LinearConverter().setRatio(0.2, 1.0)
				                        .setRange(0,100)
				                        .setDimension(12))
						);


	}


	public void loadSelected(ArrayList<String> selectedList){

		for (String dataSetName : selectedList){
			mStore.loadDataSet(dataSetName, new LocalStore.DataSetListener(){

				@Override
				public void onSuccess(String dataSetName) {
					mTextArea.setText("Got data:" + dataSetName);
					DataMapping mapping = mPlotDefinitionDict.get(dataSetName).getMapping();					
					setTimeRange(mStore.getMinDataTime(dataSetName, mapping),
						     mStore.getMaxDataTime(dataSetName, mapping));
					render();

					
				}

				@Override
				public void onFailure(String dataSetName, Throwable caught) {
					mTextArea.setText("Get data call failed for :" + dataSetName +
							          " received error:"+ caught);

				}
				
			});
		}
		
		
	}

	
	/**
	 * Set the view to show the data between startTime and endTime
	 * @param startTime
	 * @param endTime
	 */
	public void setTimeRange(double startTime, double endTime){
		mStartTime = startTime;
		mEndTime = endTime;
		mScale = mCanvas.getCoordinateSpaceWidth() / (mEndTime - mStartTime);
	}
	
	double translateTimeStamp(double timeStamp){
		return (timeStamp - mStartTime) * mScale; 
	}
	
	double translateX(double x){
		return mStartTime + (x) / mScale;
	}
	
	public void drawTimeStamps(){ 
		int marginSize = 3;
		int nbTicks = 4;
		float step = mCanvas.getCoordinateSpaceWidth() / nbTicks; 
		for (int i = 0 ; i < nbTicks ; i++ ){
			mContext.fillText("" + (mStartTime + i *(mEndTime - mStartTime)),
					          i * step,
					          mCanvas.getCoordinateSpaceHeight() - marginSize );
		}
	}
	
	public void translateRender(double deltaTime){
		double deltaX = deltaTime * mScale;
		
		// 
		double keptWidth = mCanvas.getCoordinateSpaceWidth() - Math.abs(deltaX);
		if (deltaX > 0){
			mContext.drawImage(mCanvas.getCanvasElement(),
					           deltaX,
					           0.0,
					           keptWidth,
					           (double) mCanvas.getCoordinateSpaceHeight(),
					           0.0,
					           0.0,
					           keptWidth,
					           (double) mCanvas.getCoordinateSpaceHeight() );
			render(mEndTime - deltaTime,mEndTime);
		}
		else {
	
			mContext.drawImage(mCanvas.getCanvasElement(),
			           0.0,
			           0.0,
			           keptWidth,
			           (double) mCanvas.getCoordinateSpaceHeight(),
			           - deltaX,
			           0.0,
			           keptWidth,
			           (double) mCanvas.getCoordinateSpaceHeight() );		
			render(mStartTime, mStartTime - deltaTime);

		}

	}
	
	public void render(){
		render(mStartTime, mEndTime);
	}
	
	public void render(double renderStartTime, double renderEndTime){
		// Render visible data in current window.
		// There are multiple graphs to render.
		// Each graph is characterized by the mapping that extracts the data from the datapoints and the glyph used
		// to represent the data.
		double startTime = System.currentTimeMillis();
		int margin = 11;
		mContext = mCanvas.getContext2d();
		mContext.setFillStyle(CssColor.make(0,0,0));
		mContext.fillRect(translateTimeStamp(renderStartTime),
				          0,
				          translateTimeStamp(renderEndTime),
				          mCanvas.getCoordinateSpaceHeight());
		mContext.fillRect(0,0,mCanvas.getCoordinateSpaceWidth(), margin);
		mContext.fillRect(0, mCanvas.getCoordinateSpaceHeight() - margin , mCanvas.getCoordinateSpaceWidth() , mCanvas.getCoordinateSpaceHeight() );
		double height = mCanvas.getCoordinateSpaceHeight();
		mMap = new ReverseMap();
		int plotIndex = 0;
		for (DataSet dataSet : mStore.getAvailableSets()){
			PlotDefinition plotDefinition = mPlotDefinitionDict.get(dataSet.getName());
			DataMapping mapping = plotDefinition.getMapping();
			Glyph glyph = plotDefinition.getGlyph();
			for (DataPoint dataPoint : dataSet.contents) {
				double timeStamp = mapping.getTimeStamp(dataPoint);
				if (timeStamp > mStartTime && timeStamp < mEndTime) {
					double value = mapping.getValue(dataPoint);
					double x = translateTimeStamp(timeStamp);
					double y = plotDefinition.convert(value, height);
					if (timeStamp > renderStartTime && timeStamp < renderEndTime) {
						double cValue = mapping.getValue(dataPoint, 1);
						double sValue = mapping.getValue(dataPoint, 2);
						glyph.draw(mContext, x , y, plotDefinition.convertSize(sValue), plotDefinition.convertColor(cValue));
					}
					mMap.put((int) x, (int) y, dataPoint);
				}

			}
			plotIndex++; // change plot definition with each DataSet
		}
		mContext.setFillStyle(CssColor.make(255,255,255));
		
		mContext.fillText("duration:" + (System.currentTimeMillis() - startTime), 10,10);
		drawTimeStamps();
	
	}

	public void doTimeShift(double deltaTime) {
		setTimeRange(mStartTime + deltaTime, mEndTime + deltaTime);
		translateRender(deltaTime);
	}
}
