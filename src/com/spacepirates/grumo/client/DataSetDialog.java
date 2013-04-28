package com.spacepirates.grumo.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataSetDialog extends DialogBox{

		public static interface ChoiceListener{
			
			public void signalSelected(ArrayList<String> selectedList);
		}
		
		private HashMap<String, CheckBox> checkBoxes;
		private VerticalPanel mDialogVPanel;
		private ChoiceListener mChoiceListener;
		
		private void addChoice(String label){
			CheckBox newBox = new CheckBox(label);
			mDialogVPanel.add(newBox);
			checkBoxes.put(label, newBox);
		}
		public DataSetDialog(){
			setText("DataSet choice");
			setAnimationEnabled(false);
			final Button okButton = new Button("OK");
			// We can set the id of a widget by accessing its Element
			okButton.getElement().setId("okButton");
			checkBoxes = new HashMap<String, CheckBox>();
			mDialogVPanel = new VerticalPanel();
			mDialogVPanel.addStyleName("dialogVPanel");
			addChoice("input.log");
			addChoice("input.csv");
			mDialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
			mDialogVPanel.add(okButton);
			setWidget(mDialogVPanel);

			// Add a handler to close the DialogBox
			okButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					mChoiceListener.signalSelected(getSelected());
					hide();
				}
			});
		}
		
		/**
		 * return the list of names of the datasets that are currently checked.
		 * @return
		 */
		public ArrayList<String> getSelected(){
			ArrayList<String> result = new ArrayList<String>();
			for (String name : checkBoxes.keySet()){
				if (checkBoxes.get(name).getValue()){
					result.add(name);
				}
			}
			return result;
		}

		public void addChoiceListener(ChoiceListener listener){
			mChoiceListener = listener;
		}
}
