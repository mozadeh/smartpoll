package com.smartiky.smartpoll.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class NetworkObject extends JSONObject {
	
	public NetworkObject() {
	}

	public NetworkObject(JSONObject copyFrom) {
		try {
			@SuppressWarnings("unchecked")
			Iterator<String> it = copyFrom.keys();
			while (it.hasNext()) {
				String name = it.next();
				this.put(name, copyFrom.get(name));
			}
		} catch (JSONException e) {
		}
	}

	public NetworkObject(JSONTokener readFrom) throws JSONException {
		super(readFrom);
	}
	
	public Date getDate(String name) {
		try {
			long timestamp = this.getLong(name);
			return new Date(timestamp * 1000);			
		} catch (JSONException e) {
			return null;
		}
	}
	
	public Date getCreationTime() {
		return getDate("date_created");
	}
	
	public String getId() {
		try {
			return this.getString("id");
		} catch (JSONException e) {
			return null;
		}
	}
	
	public ArrayList<NetworkObject> getObjectArray(String name) {
		try {
			ArrayList<NetworkObject> list = new ArrayList<NetworkObject> ();
			JSONArray array = getJSONArray(name);
			for (int i=0; i<array.length(); i++)
				list.add(new NetworkObject(array.getJSONObject(i)));
			return list;
		} catch (JSONException e) {			
		}
		return null;
	}
}
