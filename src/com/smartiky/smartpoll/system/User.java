package com.smartiky.smartpoll.system;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class User extends NetworkObject {

	public User() {
	}
	
	public User(JSONObject copyFrom) {
		super(copyFrom);
	}
	
	public User(JSONTokener readFrom) throws JSONException {
		super(readFrom);
	}	

	public String getName() {
		try {
			String name = this.getString("name");
			if (name.equals("null"))
				return "Anonymous";
			return name;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getEmail() {
		try {
			return this.getString("email");
		} catch (JSONException e) {
			return null;
		}
	}

	public String getFacebookUsername() {
		try {
			return this.getString("FacebookUsername");
		} catch (JSONException e) {
			return null;
		}
	}

	public Date getLastLoginTime() {
		return getDate("last_login");
	}

	public String getFacebookProfilePicUrl(String size) {
		if (getFacebookUsername() == null)
			return null;
		return "http://graph.facebook.com/" + getFacebookUsername() + "/picture?" + size;
	}

	public int getNumVotes() {
		try {
			return this.getInt("num_votes");
		} catch (JSONException e) {
			return 0;
		}
	}

	public int getNumPolls() {
		try {
			return this.getInt("num_polls");
		} catch (JSONException e) {
			return 0;
		}
	}

	public int getNumFavoritePolls() {
		try {
			return this.getInt("num_favorites");
		} catch (JSONException e) {
			return 0;
		}
	}

	public int getNumFollowers() {
		try {
			return this.getInt("num_followers");
		} catch (JSONException e) {
			return 0;
		}
	}

	public int getNumFollowings() {
		try {
			return this.getInt("num_followings");
		} catch (JSONException e) {
			return 0;
		}
	}
	
	public boolean isFollowing() {
		try {
			return this.getBoolean("is_following");
		} catch (JSONException e) {
			return false;
		}
	}

	public double getLatitude() {
		try {
			return this.getDouble("latitude");
		} catch (JSONException e) {
			return 0;
		}
	}

	public double getLongitude() {
		try {
			return this.getDouble("longitude");
		} catch (JSONException e) {
			return 0;
		}
	}

}
