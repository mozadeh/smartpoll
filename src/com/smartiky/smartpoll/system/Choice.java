package com.smartiky.smartpoll.system;

import org.json.JSONException;
import org.json.JSONObject;


public class Choice extends NetworkObject {

	public Choice() {
		super();
	}

	public Choice(String desc) throws JSONException {
		super();
		this.setDescription(desc);
	}

	public Choice(JSONObject copyFrom) {
		super(copyFrom);
	}
	
	public String getDescription() {
		try {
			return this.getString("desc");
		} catch (JSONException e) {
			return null;
		}
	}
	
	public void setDescription(String desc) throws JSONException {
		this.put("desc", desc);
	}
	
	public int getNumResponses() {
		try {
			return this.getInt("count");
		} catch (JSONException e) {
			return 0;
		}
	}
	
}
