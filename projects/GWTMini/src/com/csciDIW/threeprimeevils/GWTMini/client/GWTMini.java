package com.csciDIW.threeprimeevils.GWTMini.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTMini implements EntryPoint {
	
	private DiggServiceAsync diggServiceSvc = GWT.create(DiggService.class);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Label error_label = new Label();
		RootPanel.get("label").add(error_label);
		error_label.setText("There was an error in retrieving the information.");
		error_label.setStyleName("error");
		error_label.setVisible(false);
		try{
			if(diggServiceSvc == null)
				diggServiceSvc = GWT.create(DiggService.class);
			AsyncCallback<String> callback = new AsyncCallback<String>(){
				public void onFailure(Throwable caught) {
					error_label.setVisible(true);
				}
	
				public void onSuccess(String diggNewsString) {
					try{
						error_label.setVisible(false);
						displayNews(getDiggNews(diggNewsString));
					}catch(Exception e){
						error_label.setVisible(true);
					}
				}
			};
			diggServiceSvc.getNews(callback);
		}catch(Exception e){
			error_label.setVisible(true);
		}
	}
	
	private void displayNews(ArrayList<HashMap<String, String>> topStories) {
		FlexTable table = new FlexTable();
		table.setText(0, 0, "Diggs");
		table.setText(0, 1, "Description");
		for(int i=0; i<topStories.size(); i++){
			table.setText(i+1, 0, topStories.get(i).get("diggs"));
			table.setHTML(i+1, 1, "<a class=\"title\" target=\"_blank\" href=\"" + topStories.get(i).get("url") + "\">" + topStories.get(i).get("title")+"</a><br/>"+topStories.get(i).get("description"));
		}
		table.getRowFormatter().addStyleName(0, "sectionHeading");
		table.setCellPadding(5);
		RootPanel.get("tableid").add(table);
	}
	
	private ArrayList<HashMap<String, String>> getDiggNews(String json){
		JSONObject jsonObj = new JSONObject(toJSO(json));
		JSONArray jsStories = jsonObj.get("stories").isArray();
		ArrayList<HashMap<String,String>> topStories = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < jsStories.size(); ++i) {
	         JSONObject diggStory = (JSONObject)jsStories.get(i);              
	         JSONString title = (JSONString) diggStory.get("title");
	         JSONNumber diggs = (JSONNumber) diggStory.get("diggs");
	         JSONString description = (JSONString) diggStory.get("description");
	         JSONString url = (JSONString) diggStory.get("url");
	         HashMap<String,String> temp = new HashMap<String,String>();
	 		 temp.put("title", title.stringValue());
	 		 temp.put("description", description.stringValue());
	         temp.put("url", url.stringValue());
	         temp.put("diggs", (int)diggs.doubleValue() + "");
	         topStories.add(temp);
	    }
		return topStories;
	}
	
	private final native JavaScriptObject toJSO(String json) /*-{
		return eval(json)[0];
	}-*/;
}
