package com.smartiky.smartpoll.system;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment extends NetworkObject {
	
	

	public Comment() {
		super();
	}

	public Comment(String desc) throws JSONException {
		super();
		this.setDescription(desc);
	}

	public Comment(JSONObject copyFrom) {
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
	
	public User getCreator() {
		try {
			return new User(this.getJSONObject("creator"));
		} catch (JSONException e) {
			return null;
		}
	}
	
	public void setCreator(User creator) {
		try {
			this.put("creator", creator.getId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public int getDateCreated() {
		try {
			return this.getInt("date_created");
		} catch (JSONException e) {
			return 0;
		}
	}
	
	public int getNumComments() {
		try {
			return this.getInt("num_comments");
		} catch (JSONException e) {
			return 0;
		}
	}	
}
