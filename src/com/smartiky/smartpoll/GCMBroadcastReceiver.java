package com.smartiky.smartpoll;

import org.json.JSONException;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.smartiky.smartpoll.misc.Log;
import com.smartiky.smartpoll.system.BackendResponse;
import com.smartiky.smartpoll.system.Comment;
import com.smartiky.smartpoll.system.Notifications;
import com.smartiky.smartpoll.system.Poll;
import com.smartiky.smartpoll.system.User;

/**
 * Handling of GCM messages.
 */
public class GCMBroadcastReceiver extends BroadcastReceiver {
	
    static final String TAG = "GCMBroadcastReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        //GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
    	Notifications not = Notifications.getInstance(context.getApplicationContext());
        Bundle data = intent.getExtras();
        Log.i(TAG, data.toString());
        String action = data.getString("action");
        if ("test".equalsIgnoreCase(action)) {
	        String message = data.getString("message");
	        not.sendNotification(Notifications.NOTIFICATION_MESSAGE, message, null);
        } else if ("comment".equalsIgnoreCase(action)) {
        	try {
        		Comment comment = new Comment(new BackendResponse(new JSONTokener(data.getString("comment"))));
        		Poll poll = new Poll(new BackendResponse(new JSONTokener(data.getString("poll"))));
            	not.sendCommentNotification(comment, poll);
			} catch (JSONException e) {
				Log.v(TAG, "Invalid comment message received!");
			}
        } else if ("vote".equalsIgnoreCase(action)) {
        	try {
        		User user = new User(new BackendResponse(new JSONTokener(data.getString("user"))));
        		Poll poll = new Poll(new BackendResponse(new JSONTokener(data.getString("poll"))));
            	not.sendVoteNotification(user, poll);
			} catch (JSONException e) {
				Log.v(TAG, "Invalid comment message received!");
			}
        }
        setResultCode(Activity.RESULT_OK);
    }
}