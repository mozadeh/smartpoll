package com.smartiky.smartpoll.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.graphics.Bitmap;

public class Poll extends NetworkObject {
	
	Bitmap picture;
	boolean justVoted;

	public Poll() {
		super();
	}

	public Poll(JSONObject copyFrom) {
		super(copyFrom);
	}
	
	public Poll(JSONTokener readFrom) throws JSONException {
		super(readFrom);
	}
	
	public int getNumItems() {
		return this.getNumItems();
	}
	
	public int getNumSharesOnFacebook() {
		try {
			return this.getInt("num_shares");
		} catch (JSONException e) {
			return 0;
		}
	}

	public boolean getIsSharedOnFacebook() {
		try {
			return this.getBoolean("is_shared");
		} catch (JSONException e) {
			return false;
		}
	}

	public int getNumComments() {
		try {
			return this.getInt("num_comments");
		} catch (JSONException e) {
			return 0;
		}
	}

	public void setNumComments(int numComments) {
		try {
			this.put("num_comments", numComments);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getNumFavorites() {
		try {
			return this.getInt("num_favorites");
		} catch (JSONException e) {
			return 0;
		}
	}

	public boolean getIsFavorite() {
		try {
			return this.getBoolean("is_favorite");
		} catch (JSONException e) {
			return false;
		}
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
			return new User(getJSONObject("creator"));
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getCreatorId() {
		User creator = getCreator();
		if (creator != null)
			return getCreator().getId();
		return null;
	}
	
	public void setCreatorId(String creatorId) throws JSONException {
		this.put("creator", creatorId);
	}
	
	public String getCategory() {
		try {
			return this.getString("category");
		} catch (JSONException e) {
			return null;
		}
	}
	
	public void setCategory(String category) throws JSONException {
		this.put("category", category);
	}
	
	public void clearResponse() {
		this.remove("response");
	}

	public String getResponse() {
		try {
			return this.getString("response");
		} catch (JSONException e) {
			return null;
		}
	}
	
	public int getNumResponses() {
		Iterator<Choice> it = getChoices().iterator();
		int num = 0;
		while(it.hasNext()) {
			num += it.next().getNumResponses();			
		}
		return num;
	}	
	
	/**
	 * Calculates the 0-based index of the selected response in the choices list.
	 * @return integer
	 */
	public int getResponseIndex() {
		String choiceId = getResponse();
		ArrayList<Choice> choices = getChoices();
		for (int i=0; i<choices.size(); i++)
			if (choices.get(i).getId() == choiceId)
				return i;
		return -1;
	}
	
	public ArrayList<Choice> getChoices() {
		ArrayList<NetworkObject> list = getObjectArray("choices");
		ArrayList<Choice> choiceList = new ArrayList<Choice>();
		for (int i=0; i<list.size(); i++)
			choiceList.add(new Choice(list.get(i)));
		return choiceList;
	}
	
	public ArrayList<String> getChoiceDescriptions() {
		ArrayList<NetworkObject> list = getObjectArray("choices");
		ArrayList<String> choiceList = new ArrayList<String>();
		for (int i=0; i<list.size(); i++)
			choiceList.add(new Choice(list.get(i)).getDescription());
		return choiceList;
	}
	
	public void setChoices(Collection<Choice> choices) throws JSONException {
		JSONArray list = new JSONArray(choices);
		this.put("choices", list);
	}
	
	public String getThumbnailUrl() {
		try {
			return this.getString("thumbnail");
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getPictureUrl() {
		try {
			return this.getString("picture");
		} catch (JSONException e) {
			return null;
		}
	}
	
	public Bitmap getPicture() {
		return this.picture;
	}
	
	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

	public void setJustVoted(boolean justVoted) {
		this.justVoted = justVoted;
	}
	
	public boolean isJustVoted() {
		return justVoted;
	}

	public String getPrivacy() {
		try {
			return this.getString("privacy");
		} catch (JSONException e) {
			return "";
		}
	}

	public void setPrivacy(String privacy) throws JSONException {
		this.put("privacy", privacy);
	}

}
