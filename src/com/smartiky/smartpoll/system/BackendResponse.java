package com.smartiky.smartpoll.system;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class BackendResponse extends NetworkObject {

	public BackendResponse() {
	}

	public BackendResponse(JSONObject copyFrom) {
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

	public BackendResponse(JSONTokener readFrom) throws JSONException {
		super(readFrom);
	}
	
	public boolean checkSuccess() throws JSONException {
		if (has("result") && getString("result").equals("ok"))
			return true;
		if (has("meta") && getJSONObject("meta").getString("result").equals("ok"))
			return true;
		return false;
	}
	
	public boolean checkFailed() throws JSONException {
		if (has("result") && getString("result").equals("failed"))
			return true;
		if (has("meta") && getJSONObject("meta").getString("result").equals("failed"))
			return true;
		return false;
	}
	
}
