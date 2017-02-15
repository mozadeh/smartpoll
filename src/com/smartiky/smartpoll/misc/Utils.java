package com.smartiky.smartpoll.misc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.smartiky.smartpoll.R;

/**
 * This is the utility class with a lot of useful methods to be used all across
 * the application.
 * 
 */
public class Utils {

	public static final int RESULT_CODE_POLL_UPDATED = Activity.RESULT_FIRST_USER + 1;
	public static final int RESULT_CODE_POLL_DELETED = Activity.RESULT_FIRST_USER + 2;
	public static final int RESULT_CODE_POLL_CREATED = Activity.RESULT_FIRST_USER + 3;

	/**
	 * Show an about dialog.
	 * 
	 * @param activity
	 *            the activity asking for the dialog
	 */
	public static void showAboutDialog(final Activity activity) {
		View view = activity.getLayoutInflater().inflate(R.layout.about, null,
				false);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getString(R.string.app_name));
		builder.setView(view);
		builder.setPositiveButton("Rate App",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						String myUrl = "market://details?id=com.smartiky.smartpoll";
						Intent i = new Intent(Intent.ACTION_VIEW, Uri
								.parse(myUrl));
						activity.startActivity(i);
					}
				});
		builder.setNegativeButton("Feedback",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("message/rfc822"); // use from live device
						i.putExtra(Intent.EXTRA_EMAIL,
								new String[] { "admin@smartiky.com" });
						i.putExtra(Intent.EXTRA_SUBJECT,
								"SmartTransit Feedback");
						i.putExtra(Intent.EXTRA_TEXT, "feedback:");
						activity.startActivity(Intent.createChooser(i,
								"Select email application."));
					}

				});
		builder.setCancelable(true);

		builder.create();
		builder.show();
	}
	
	public static void showErrorToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT);
	}

}
