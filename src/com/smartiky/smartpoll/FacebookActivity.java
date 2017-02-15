package com.smartiky.smartpoll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.smartiky.smartpoll.misc.Log;
import com.smartiky.smartpoll.system.Choice;
import com.smartiky.smartpoll.system.Poll;
import com.smartiky.smartpoll.system.SmartPollSystem;
import com.smartiky.smartpoll.system.User;

public abstract class FacebookActivity extends Activity {

	private static final String TAG = "FacebookEnabledActivity";
	private static final String FACEBOOK_URL = "http://apps.facebook.com/smartpoll_app/";
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	
	SmartPollSystem smartPoll;
	UiLifecycleHelper uiHelper;
	boolean pendingPublishReauthorization = false;
	Poll pendingPublishPoll;
	boolean pendingPublishIsCreator;
	
	public FacebookActivity() {
		smartPoll = SmartPollSystem.getInstance(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.newpoll);
		
		if (savedInstanceState != null) {
		    pendingPublishReauthorization = 
		        savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
		}
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
        Log.i(TAG, "OnResume");
	    super.onResume();

	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    uiHelper.onResume();
	}
	
	@Override
	public void onPause() {
        Log.i(TAG, "OnPause");
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
        Log.i(TAG, "OnDestroy");
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
	    super.onSaveInstanceState(outState);
	    outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
	    uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "OnActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	    
	    if (pendingPublishReauthorization && 
	            state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
	        pendingPublishReauthorization = false;
	        publishFacebookStory(pendingPublishPoll, pendingPublishIsCreator);
	    }
	}
	
	boolean requestAdditionalPermissions() {
        List<String> permissions = Session.getActiveSession().getPermissions();
        if (!isSubsetOf(PERMISSIONS, permissions)) {
            Session.NewPermissionsRequest newPermissionsRequest = new Session
                    .NewPermissionsRequest(FacebookActivity.this, PERMISSIONS);
            Session.getActiveSession().requestNewPublishPermissions(newPermissionsRequest);
            return true;
        }
        return false;
	}

	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}

	void loginToSmartPollWithFacebookUser(GraphUser facebookUser) {
		new AsyncTask<GraphUser, Void, User>() {

			@Override
			protected User doInBackground(GraphUser... params) {
		        Log.i(TAG, "Logged to SmartPoll...");
				return smartPoll.loginFacebookUser(params[0]);				
			}

			@Override
			protected void onPostExecute(User user) {
		        Log.i(TAG, "Login done.");
				if (user != null) {
					onLoginToSmartPollCompleted(user);
				} else {
			        Log.i(TAG, "No user found!");					
				}
			}
						
		}.execute(facebookUser);
	}
	
	void onLoginToSmartPollCompleted(User user) {}
	
	int publishRetries;
	Poll publishPoll;
	boolean publishIsCreator;
	void publishFacebookStory(Poll poll, boolean is_creator) {
		if (requestAdditionalPermissions()) {
			pendingPublishReauthorization = true;
			pendingPublishPoll = poll;
			pendingPublishIsCreator = is_creator;
			return;
		}
		publishRetries = 0;
		publishPoll = poll;
		publishIsCreator = is_creator;
		publishFacebookStoryWithRetries();
	}
	
	void publishFacebookStoryWithRetries() {
		Poll poll = publishPoll;
		boolean is_creator = publishIsCreator;
		
		Session session = Session.getActiveSession();
		Bundle parameters = new Bundle();
		String pictureUrl = smartPoll.getFullPictureURL(poll.getThumbnailUrl());
		if (pictureUrl != null)
			parameters.putString("picture", pictureUrl);
		parameters.putString("description", " ");
		
		String UserPollId = "pid=" + poll.getId() + "&uid=" + smartPoll.getUserId();
		String fbparameters = "{";
		ArrayList<Choice> choices = poll.getChoices();
		for (int i = 0; i < choices.size(); i++)
			if (choices.get(i).getDescription().isEmpty()) {
				choices.remove(i);
				--i;
			}
		for (int i = 0; i < choices.size(); i++) {
			if(i > 0) { fbparameters += ", "; }
			fbparameters += "'" + (i + 1) + "':'" + choices.get(i).getDescription()	+ "'";
			/*fbparameters += "'" + (i + 1) + 
					"':{ text: '" + choices.get(i).getDescription()	+ 
					"', href: '" + FACEBOOK_URL + "poll?" + UserPollId + "'}";*/
		}
		fbparameters += "}";
		
		if (is_creator)
			parameters.putString("message", "I created this poll on Smart Poll");
		else
			parameters.putString("message", "I shared this poll from Smart Poll");
		parameters.putString("properties", fbparameters);
		parameters.putString("caption", "Click to vote and see the results:");
		parameters.putString("name", poll.getDescription());
		parameters.putString("link", FACEBOOK_URL + "poll?" + UserPollId);
		parameters.putString("actions",
				"[{name: 'Vote', link: '" + FACEBOOK_URL + "poll?" + UserPollId + "'}]");
		
		Request.Callback callback = new Request.Callback() {
			public void onCompleted(Response response) {
				FacebookRequestError error = response.getError();
				String postId = null;
				Log.v(TAG, response.toString());
				try {
					JSONObject graphResponse = response
	                        .getGraphObject()
	                        .getInnerJSONObject();
					postId = graphResponse.getString("id");
				} catch (JSONException e) {
					Log.i(TAG, "JSON error " + e.getMessage());
				} catch (Exception e) {
					Log.i(TAG, "Exception occured: " + e.getMessage());
				}
				if (postId == null && publishRetries ++ < 3) {
					Log.i(TAG, "Retries: " + publishRetries);
					publishFacebookStoryWithRetries();
				} else {
					publishFacebookStoryCompleted(error, postId);
				}
			}
		};
		
		Request request = new Request(session, "me/feed", parameters, HttpMethod.POST, callback);
		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}

	void publishFacebookStoryCompleted(FacebookRequestError error, String postId) {};
	
	void shareWithSelectFriends(Poll poll) {
		try {
			String message = "The smart way to ask questions.";
			JSONObject data = new JSONObject();
			data.put("pollId", poll.getId());
			sendRequestDialog(message, data.toString());
		} catch (JSONException e) {
			onFacebookRequestSent(false, null);
		}
	}

	private void sendRequestDialog(String message, String data) {
	    Bundle params = new Bundle();
	    params.putString("message", message);
	    params.putString("data", data);
	    
	    WebDialog requestsDialog = (
	        new WebDialog.RequestsDialogBuilder(FacebookActivity.this,
	            Session.getActiveSession(),
	            params))
	            .setOnCompleteListener(new OnCompleteListener() {

	                @Override
	                public void onComplete(Bundle values,
	                    FacebookException error) {
	                    if (error != null) {
	                        if (error instanceof FacebookOperationCanceledException) {
	                        	onFacebookRequestSent(false, values);
	                            /*Toast.makeText(getApplicationContext(), 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();*/
	                        } else {
	                        	onFacebookRequestSent(false, values);
	                            Toast.makeText(getApplicationContext(), 
	                                "Network Error", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    } else {
	                        final String requestId = values.getString("request");
	                        if (requestId != null) {
	                        	onFacebookRequestSent(true, values);
	                        } else {
	                        	onFacebookRequestSent(false, values);
	                            /*Toast.makeText(getApplicationContext(), 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();*/
	                        }
	                    }
	                }

	            })
	            .build();
	    requestsDialog.show();
	}
	
	void onFacebookRequestSent(boolean sent, Bundle values) {}

	void getRequestData(final String inRequestId) {
	    // Create a new request for an HTTP GET with the
	    // request ID as the Graph path.
	    Request request = new Request(Session.getActiveSession(), 
	            inRequestId, null, HttpMethod.GET, new Request.Callback() {

	                @Override
	                public void onCompleted(Response response) {
	                    // Process the returned response
	                    GraphObject graphObject = response.getGraphObject();
	                    FacebookRequestError error = response.getError();
	                    if (graphObject != null) {
	                        // Check if there is extra data
	                        if (graphObject.getProperty("data") != null) {
	                            try {
	                                // Get the data, parse info to get the key/value info
	                                JSONObject dataObject = 
	                                	new JSONObject((String)graphObject.getProperty("data"));
	                                onGetRequestDataCompleted(dataObject);
	                            } catch (JSONException e) {
	                            	Log.e(TAG, "Facebook request json exception " + e.getMessage());
	                                onGetRequestDataCompleted(null);
	                            }
	                        } else if (error != null) {
                            	Log.e(TAG, "Facebook request error " + error.getErrorMessage());
                                onGetRequestDataCompleted(null);
	                        }
	                    } else {
                        	Log.e(TAG, "Facebook request no graph object!");
	                    	onGetRequestDataCompleted(null);
	                    }
	                }
	        });
	    // Execute the request asynchronously.
	    Request.executeBatchAsync(request);
	}

	void onGetRequestDataCompleted(JSONObject data) {}
	
	void deleteRequest(String inRequestId) {
	    // Create a new request for an HTTP delete with the
	    // request ID as the Graph path.
	    Request request = new Request(Session.getActiveSession(), 
	        inRequestId, null, HttpMethod.DELETE, new Request.Callback() {

	            @Override
	            public void onCompleted(Response response) {
	            }
	        });
	    // Execute the request asynchronously.
	    Request.executeBatchAsync(request);
	}
	public class myTrackedActivity extends Activity {
		  @Override
		  public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		  }

		  @Override
		  public void onStart() {
		    super.onStart();
		    
		    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
		  }

		  @Override
		  public void onStop() {
		    super.onStop();
		    
		    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
		  }
	}
}
