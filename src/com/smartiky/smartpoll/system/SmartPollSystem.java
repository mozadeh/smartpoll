package com.smartiky.smartpoll.system;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Pair;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.smartiky.smartpoll.misc.ImageTools;
import com.smartiky.smartpoll.misc.Log;

public class SmartPollSystem {

	private static final String TAG = "SmartPollSystem";
	
	public static final String TREND_HOT = "hot";
	public static final String TREND_TRENDING = "trending";
	public static final String TREND_NEW = "new";
	
	public static final int maxImageWidth = ImageTools.MAX_IMAGE_DIMENSION;
	public static final int maxImageHeight = ImageTools.MAX_IMAGE_DIMENSION;
	
	User profile;
	BackendConnection connection;
	Context context;
	static Location currentLocation;
	static SmartPollSystem currentInstance;
	
	private SmartPollSystem(Context context) {
		this.context = context;
		connection = new BackendConnection(context);		
	}
	
	static public SmartPollSystem getInstance(Context context) {
		if (currentInstance == null)
			currentInstance = new SmartPollSystem(context);
		return currentInstance;
	}
	
	static public Location getCurrentLocation() {
		return currentLocation;
	}

	static public void setCurrentLocation(Location currentLocation) {
		SmartPollSystem.currentLocation = currentLocation;
	}

	private void addLocationIfAvailable(Map<String, String> params) {
		if (currentLocation != null) {
			params.put("latitude", "" + currentLocation.getLatitude());
			params.put("longitude", "" + currentLocation.getLongitude());
			params.put("accuracy", "" + currentLocation.getAccuracy());
		}
	}
	
	private User authRegister() {		
    	HashMap<String, String> params = new HashMap<String, String>();
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("auth", "register", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				User user = new User(result.getJSONObject("user"));
				storeUserId(user.getId());
				return user;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	private User authRegisterWithFacebook(GraphUser facebookUser) {
		HashMap<String, String> params = new HashMap<String, String> ();
		params.put("fb_id", facebookUser.getId());
		params.put("name", facebookUser.getName());
		params.put("fb_link", facebookUser.getLink());
		params.put("fb_username", facebookUser.getUsername());
		params.put("fb_access_token", Session.getActiveSession().getAccessToken());
		addLocationIfAvailable(params);
		BackendResponse result = connection.sendRequest("auth", "registerwithfacebook", params);
		
    	try {
			if (result != null && result.checkSuccess()) {
				User user = new User(result.getJSONObject("user"));
				return user;
			}
			if (result != null && result.checkFailed()) {
				storeUserId("");
				return null;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	private User authGetUser(String userId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", userId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("auth", "get", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				User user = new User(result.getJSONObject("user"));
				return user;
			}
			if (result != null && result.checkFailed()) {
				storeUserId("");
				return authRegister();
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	private void storeUserId(String userId) {
		SharedPreferences settings = context.getSharedPreferences(TAG, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("user", userId);
		editor.commit();
	}
	
	public String getUserId() {
		SharedPreferences settings = context.getSharedPreferences(TAG, 0);
		String userId = settings.getString("user", "");
		Log.v(TAG, "User: " + userId);
		if (userId.equals(""))
			return fetchUser().getId();
		return userId;
	}
	
	public User getProfile() {
		return profile;
	}
	
	public User loginFacebookUser(GraphUser facebookUser) {
		User user = authRegisterWithFacebook(facebookUser);
		if (user != null) {
			profile = user;
			storeUserId(user.getId());
		}
		return user;
	}
	
	public User fetchUser() {
		SharedPreferences settings = context.getSharedPreferences(TAG, 0);
		String userId = settings.getString("user", "");
		
		Log.v(TAG, "User: " + userId);
		if (userId.equals("")) { 
			User user = authRegister();
			if (user == null) {
				User temp = new User();
				try { temp.put("id", "0"); } catch (JSONException e) {}
				return temp;
			}
			storeUserId(user.getId());
			return user;
		} else {
			return authGetUser(userId);
		}
	}
	
	public boolean registerWithGCM(String registrationId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("regid", "" + registrationId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("auth", "registergcm", params);
    	
    	try {
			if (result != null && result.checkFailed()) {
				Log.w(TAG, "Unable to register device on server.");
			}
			if (result != null && result.checkSuccess()) {				
				return true;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return false;
	}
	
	public ArrayList<Poll> fetchPollList(String category, String trend, int offset, int limit) {
		//Log.v(TAG, "fetchPollList: " + category + "/" + trend);
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("category", category);
    	params.put("trend", trend);
    	params.put("offset", "" + offset);
    	params.put("limit", "" + limit);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "list", params);
    	
    	try {
			if (result != null && result.checkFailed()) {
				storeUserId("");
		    	params.put("user", getUserId());
		    	result = connection.sendRequest("poll", "list", params);
				Log.w(TAG, "Unable to fetch polls.");
			}
			if (result != null && result.checkSuccess()) {				
				ArrayList<NetworkObject> list = result.getObjectArray("items");
				ArrayList<Poll> pollList = new ArrayList<Poll>();
				for (int i=0; i<list.size(); i++)
					pollList.add(new Poll(list.get(i)));
				Log.v(TAG, "Fetched " + list.size() + " polls.");
				return pollList;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public Poll submitVote(String choiceId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("choice", choiceId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "vote", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				Poll poll = new Poll(result.getJSONObject("poll"));
				return poll;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public Poll fetchPoll(String pollId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("poll", pollId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "get", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				Poll poll = new Poll(result.getJSONObject("poll"));
				return poll;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public ArrayList<Comment> fetchPollComments(String pollId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user", getUserId());
		params.put("poll", pollId);
		BackendResponse result = connection.sendRequest("poll", "listpollcomments", params);

    	try {
			if (result != null && result.checkSuccess()) {
				ArrayList<NetworkObject> list = result.getObjectArray("items");
				ArrayList<Comment> commentList = new ArrayList<Comment>();
				for (int i=0; i<list.size(); i++)
					commentList.add(new Comment(list.get(i)));
				Log.v(TAG, "Fetched " + list.size() + " comments.");
				return commentList;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public Comment submitComment(String pollId, String desc) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("poll", pollId);
    	params.put("desc", desc);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "comment", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				Comment comment = new Comment(result.getJSONObject("comment"));
				// Poll is unused!
				return comment;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public Poll markAsFavorite(String pollId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("poll", pollId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "favorite", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				Poll poll = new Poll(result.getJSONObject("poll"));
				return poll;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public Poll markAsShared(String pollId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("poll", pollId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "share", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				Poll poll = new Poll(result.getJSONObject("poll"));
				return poll;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public User followUser(String userId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("profile", userId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "follow", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				User profile = new User(result.getJSONObject("profile"));
				return profile;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}	
	
	public User unfollowUser(String userId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("profile", userId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "unfollow", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				User profile = new User(result.getJSONObject("profile"));
				return profile;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}	
	
	public User getUserProfile(String userId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("profile", userId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("profile", "get", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				User profile = new User(result.getJSONObject("profile"));
				return profile;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public ArrayList<Poll> getUserPolls(String userId, int offset, int limit) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("profile", userId);
    	params.put("offset", "" + offset);
    	params.put("limit", "" + limit);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("profile", "polls", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {				
				ArrayList<NetworkObject> list = result.getObjectArray("items");
				ArrayList<Poll> pollList = new ArrayList<Poll>();
				for (int i=0; i<list.size(); i++)
					pollList.add(new Poll(list.get(i)));
				Log.v(TAG, "Fetched " + list.size() + " polls.");
				return pollList;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public ArrayList<Poll> getUserFavoritePolls(String userId, int offset, int limit) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("profile", userId);
    	params.put("offset", "" + offset);
    	params.put("limit", "" + limit);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("profile", "favorites", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {				
				ArrayList<NetworkObject> list = result.getObjectArray("items");
				ArrayList<Poll> pollList = new ArrayList<Poll>();
				for (int i=0; i<list.size(); i++)
					pollList.add(new Poll(list.get(i)));
				Log.v(TAG, "Fetched " + list.size() + " polls.");
				return pollList;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public ArrayList<User> getUserFollowers(String userId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("profile", userId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("profile", "followers", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {				
				ArrayList<NetworkObject> list = result.getObjectArray("items");
				ArrayList<User> userList = new ArrayList<User>();
				for (int i=0; i<list.size(); i++)
					userList.add(new User(list.get(i)));
				Log.v(TAG, "Fetched " + list.size() + " followers.");
				return userList;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public ArrayList<User> getUserFollowings(String userId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("profile", userId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("profile", "followings", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {				
				ArrayList<NetworkObject> list = result.getObjectArray("items");
				ArrayList<User> userList = new ArrayList<User>();
				for (int i=0; i<list.size(); i++)
					userList.add(new User(list.get(i)));
				Log.v(TAG, "Fetched " + list.size() + " followers.");
				return userList;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public Poll createPoll(String description, String category, ArrayList<String> choices, String privacy, Bitmap picture) {
    	
		HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("desc", description);
    	params.put("category", category);
   		params.put("privacy", privacy);
    	addLocationIfAvailable(params);
    	for (int i=0; i<choices.size(); i++)
    		params.put("choice"+(i+1), choices.get(i));
    	
    	HashMap<String, Pair<String, byte[]>> binaries = new HashMap<String, Pair<String, byte[]>>();
		if (picture != null) {
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	if (picture.getWidth() > maxImageWidth || picture.getHeight() > maxImageHeight) {	    		
	    		int w = maxImageWidth;
	    		int h = picture.getHeight() * w / picture.getWidth();
	    		if (h < maxImageHeight) {
	    			h = maxImageHeight;
	    			w = picture.getWidth() * h / picture.getHeight();
	    		}
	    		picture = Bitmap.createScaledBitmap(picture, w, h, true);
	    	}
	    	picture.compress(CompressFormat.JPEG, 75, out);
	    	binaries.put("picture", new Pair<String, byte[]>("picture.jpg", out.toByteArray()));
	    	Log.v(TAG, "Image size: " + picture.getWidth() + "x" + picture.getHeight());
		}

    	BackendResponse result = connection.sendRequestWithBinary("poll", "create", params, binaries);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				Poll poll= new Poll(result.getJSONObject("poll"));
				return poll;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public Poll createPoll(Poll poll) {
		return createPoll(
				poll.getDescription(),
				poll.getCategory(),
				poll.getChoiceDescriptions(),
				poll.getPrivacy(),
				poll.getPicture());		
	}
	
	public ArrayList<Poll> fetchMyPolls(int offset, int limit) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("offset", "" + offset);
    	params.put("limit", "" + limit);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "mypolls", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				ArrayList<NetworkObject> list = result.getObjectArray("items");
				ArrayList<Poll> pollList = new ArrayList<Poll>();
				for (int i=0; i<list.size(); i++)
					pollList.add(new Poll(list.get(i)));
				return pollList;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public ArrayList<Poll> fetchFavoritePolls(int offset, int limit) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("offset", "" + offset);
    	params.put("limit", "" + limit);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "favoritepolls", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				ArrayList<NetworkObject> list = result.getObjectArray("items");
				ArrayList<Poll> pollList = new ArrayList<Poll>();
				for (int i=0; i<list.size(); i++)
					pollList.add(new Poll(list.get(i)));
				return pollList;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public ArrayList<Poll> fetchMyVotes(int offset, int limit) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("offset", "" + offset);
    	params.put("limit", "" + limit);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "myvotes", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				ArrayList<NetworkObject> list = result.getObjectArray("items");
				ArrayList<Poll> pollList = new ArrayList<Poll>();
				for (int i=0; i<list.size(); i++)
					pollList.add(new Poll(list.get(i)));
				return pollList;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public ArrayList<Poll> fetchFollowingPolls(int offset, int limit) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("offset", "" + offset);
    	params.put("limit", "" + limit);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "followingpolls", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				ArrayList<NetworkObject> list = result.getObjectArray("items");
				ArrayList<Poll> pollList = new ArrayList<Poll>();
				for (int i=0; i<list.size(); i++)
					pollList.add(new Poll(list.get(i)));
				return pollList;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}

	public boolean deletePoll(String pollId) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	params.put("poll", pollId);
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "delete", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				return true;
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return false;
	}	
	
	public Pair<List<User>, List<User>> fetchLeaderboard() {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("user", getUserId());
    	addLocationIfAvailable(params);
    	BackendResponse result = connection.sendRequest("poll", "leaderboard", params);
    	
    	try {
			if (result != null && result.checkSuccess()) {
				ArrayList<NetworkObject> list;
				List<User> insightfulUsers = new ArrayList<User> ();
				List<User> bestpollsUsers = new ArrayList<User> ();
				
				list = result.getObjectArray("insightful");
				for (int i=0; i<list.size(); i++)
					insightfulUsers.add(new User(list.get(i)));
				
				list = result.getObjectArray("bestpolls");
				for (int i=0; i<list.size(); i++)
					bestpollsUsers.add(new User(list.get(i)));
				
				return new Pair<List<User>, List<User>>(insightfulUsers, bestpollsUsers);
			}
		} catch (JSONException e) {
	        Log.v(TAG, "Error Parsing :\n" + e.getMessage());
		}
    	
		return null;
	}
	
	public String getFullPictureURL(String pictureUrl) {
		if (pictureUrl == null)
			return null;
		return this.connection.getServerAddress() + pictureUrl; 
	}
	
	public String getFacebookPictureUrl(String pictureUrl) throws JSONException {
		if (pictureUrl.contains("?"))
			pictureUrl += "&redirect=false";
		else
			pictureUrl += "?redirect=false";
		String response = connection.fetchURLAsString(pictureUrl);
		JSONObject obj = new JSONObject(new JSONTokener(response));
		return obj.getJSONObject("data").getString("url");
	}
	
	public Drawable getPicture(String pictureUrl) {
		Log.v(TAG, "GetPicture: " + pictureUrl);
		try {
			if (pictureUrl.contains("graph.facebook.com")) {
				pictureUrl = getFacebookPictureUrl(pictureUrl);
			}
			InputStream is = connection.fetchURLAsStream(pictureUrl);
			Drawable drawable = Drawable.createFromStream(is, "src");
            return drawable;
        } catch (Exception e) {
	        Log.v(TAG, "Error downloading picture:\n" + e.getMessage());
        }
		return null;
	}

}
