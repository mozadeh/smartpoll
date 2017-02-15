package com.smartiky.smartpoll.system;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.smartiky.smartpoll.PollResultsActivity;
import com.smartiky.smartpoll.R;
import com.smartiky.smartpoll.SmartPollActivity;
import com.smartiky.smartpoll.misc.Log;

public class Notifications {
	
	static Notifications instance;

    static final String TAG = "Notifications";

    public static final int NOTIFICATION_MESSAGE 	= 1000;
    public static final int NOTIFICATION_VOTE 		= 2000;
    public static final int NOTIFICATION_COMMENT 	= 3000;
    public static final int NOTIFICATION_HASH_MAX 	= 1000;

    SmartPollSystem smartPoll;
    Context context;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

	public static Notifications getInstance(Context context) {
		if (instance == null) {
			instance = new Notifications(context);
		}
		return instance;
	}
	
	private Notifications(Context context) {
		this.context = context;
		smartPoll = SmartPollSystem.getInstance(context);
	}

    public void sendCommentNotification(Comment comment, Poll poll) {
    	int notificationId = NOTIFICATION_COMMENT;
    	if (poll.getCreator().getId() == smartPoll.getUserId()) {
    		if (poll.getNumComments() == 1)
	    		sendNotification(notificationId, comment.getCreator().getName() + " commented on your poll.", poll);
    		else if (poll.getNumComments() == 2)
	    		sendNotification(notificationId, comment.getCreator().getName() + " and one other commented on your poll.", poll);
    		else
    			sendNotification(notificationId, comment.getCreator().getName() + " and " + (poll.getNumComments()-1) + " others commented on your poll.", poll);
    	}
    	else
    		sendNotification(notificationId, comment.getCreator().getName() + " commented on your comment.", poll);
	}

    public void sendVoteNotification(User user, Poll poll) {
    	//  + poll.getId().hashCode() % NOTIFICATION_HASH_MAX
    	int notificationId = NOTIFICATION_VOTE;
    	if (poll.getNumResponses() == 1)
    		sendNotification(notificationId, user.getName() + " voted on your poll.", poll);
    	else if (poll.getNumResponses() == 2)
    		sendNotification(notificationId, user.getName() + " and one other voted on your poll.", poll);
    	else 
    		sendNotification(notificationId, user.getName() + " and " + (poll.getNumResponses()-1) + " others voted on your poll.", poll);
	}

	// Put the message into a notification and post it.
    public void sendNotification(int notificationId, String msg, Poll poll) {
    	Log.i(TAG, msg);
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent;
        if (poll == null) {
        	intent = new Intent(context, SmartPollActivity.class);
        } else {
        	intent = new Intent(context, PollResultsActivity.class);
			intent.putExtra("poll", poll.toString());
			intent.putExtra("notification", true);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.ic_stat_notificationlogo)
        .setAutoCancel(true)
        .setContentTitle("Smart Poll")
        .setLights(0xFF0000FF, 1000, 5000)
        .setOnlyAlertOnce(true)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
        .setContentText(msg);        

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
