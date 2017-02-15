package com.smartiky.smartpoll;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.google.analytics.tracking.android.EasyTracker;
import com.smartiky.smartpoll.misc.Log;
import com.smartiky.smartpoll.system.User;

public class LoginActivity extends FacebookActivity {

	protected static final String TAG = "LoginActivity";

	LoginButton loginButton;
	Typeface tf;
	
	GraphUser facebookUser;
	User user;
	String requestId;
	boolean waitForRequestResult = false;
	int requestDataRetry = 0;
	String pollId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.login);
		
	    tf = Typeface.createFromAsset(getAssets(), "Roboto-BoldCondensed.ttf");
	    
	    loginButton = (LoginButton) findViewById(R.id.loginButton);
	    loginButton.setTypeface(tf);
	    
	    // Check for an incoming notification. Save the info
	    Uri intentUri = getIntent().getData();
	    if (intentUri != null) {
	    	Log.i(TAG, "Intent data: " + intentUri.toString());
	        String requestIdParam = intentUri.getQueryParameter("request_ids");
	        if (requestIdParam != null) {
	            String array[] = requestIdParam.split(",");
	            requestId = array[array.length-1];
	            Log.i(TAG, "Request id: " + requestId);
	        }
	        String pollIdParam = intentUri.getQueryParameter("pid");
	        if (pollIdParam != null) {
	        	pollId = pollIdParam;
	        	Log.i(TAG, "Poll id: " + pollId);
	        }
	    }
	    
	    loginButton.setUserInfoChangedCallback(new UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched(GraphUser facebookUser) {
				if (facebookUser != null) {
					LoginActivity.this.facebookUser = facebookUser;
					loginButton.setText("Authenticating " + facebookUser.getFirstName());
		        	loginToSmartPollWithFacebookUser(facebookUser);
		        	
		    		if (requestId != null && !waitForRequestResult) {
		    			Log.i(TAG, "Get request data");
		    	    	getRequestData(requestId);
		    	        waitForRequestResult = true;
		    		}
		        	launchSmartPollIfReady();
				} else {
					loginButton.setText("Login");
				}
			}
		});
	}
	
	@Override
	void onLoginToSmartPollCompleted(User user) {
		Log.i(TAG, "Login completed!");
		this.user = user;
		launchSmartPollIfReady();
	}

	@Override
	void onGetRequestDataCompleted(JSONObject data) {
		waitForRequestResult = false;
		Log.i(TAG, "Get request data completed.");
		if (data == null) {
			Log.e(TAG, "Failed to get request data from Facebook.");
			requestDataRetry ++;
			if (requestDataRetry < 3) {
				Log.i(TAG, "Get request data retry " + requestDataRetry);
				getRequestData(requestId);
				waitForRequestResult = true;
				return;
			} else {
				Toast.makeText(getApplicationContext(), "Unable to retrieve Facebook request info.", Toast.LENGTH_LONG).show();
			}
		}
		Log.i(TAG, "Deleting request..");
		deleteRequest(requestId);
		requestId = null;
		try {
			if (data != null)
				pollId = data.getString("pollId");
		} catch (JSONException e) {
			Log.e(TAG, "Unable to parse facebook data: " + e.getMessage());
		}
		launchSmartPollIfReady();
	}
	
	boolean launchSmartPollIfReady() {
		if (Session.getActiveSession().isClosed()) {
			Log.i(TAG, "Waiting for facebook session to be opened.");
			return false;
		}
		if (user == null) {
			Log.i(TAG, "Waiting for SmartPoll Login.");
			return false;
		}
		if (requestId != null && pollId == null) {
			Log.i(TAG, "Waiting for request information (pollId).");
			return false;
		}
		if (waitForRequestResult) {
			Log.i(TAG, "Waiting for request result.");
			return false;
		}
		
		Intent intent = new Intent(this, SmartPollActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (pollId != null && !pollId.isEmpty())
			intent.putExtra("pollId", pollId);
		startActivity(intent);
		//overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
		finish();
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();

		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}
	
}
