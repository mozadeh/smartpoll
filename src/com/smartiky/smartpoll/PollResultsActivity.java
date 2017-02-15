package com.smartiky.smartpoll;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.google.analytics.tracking.android.EasyTracker;
import com.smartiky.smartpoll.R.drawable;
import com.smartiky.smartpoll.misc.ImageLoader;
import com.smartiky.smartpoll.misc.Log;
import com.smartiky.smartpoll.misc.Utils;
import com.smartiky.smartpoll.system.Comment;
import com.smartiky.smartpoll.system.Poll;
import com.smartiky.smartpoll.system.SmartPollSystem;
import com.smartiky.smartpoll.system.User;

public class PollResultsActivity extends FacebookActivity {

	private static final String TAG = "PollResultsActivity";

	SmartPollSystem smartPoll;
	ImageLoader imageLoader;
	Poll poll;
	List<Comment> comments;
	CommentListAdapter commentsAdapter;
	boolean isFromNotification;
	static String replyTo = "";

	Button deleteButton, backButton, commentButton, shareButton,
			favoriteButton, detailsButton;
	PollViewHolder holder;
	static EditText commentText;
	TextView commentDesc;
	TextView commentReply;
	ListView commentList;
	LinearLayout commentsLayout;
	ProgressDialog progressBar;
	FrameLayout framelayout;
	ImageView imageview;
	DisplayMetrics metrics;
	int screenwidth;

	public PollResultsActivity() {
		smartPoll = SmartPollSystem.getInstance(this);
		imageLoader = new ImageLoader(smartPoll, false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.transition_right_to_left,
				R.anim.transition_right_to_left_out);
		setContentView(R.layout.pollresults);
		metrics = this.getResources().getDisplayMetrics();
		screenwidth = metrics.widthPixels;

		try {
			poll = new Poll(new JSONTokener(getIntent().getStringExtra("poll")));
			isFromNotification = getIntent().getBooleanExtra("notification",
					false);
			if (isFromNotification) {
				reloadPoll();
			}
		} catch (JSONException e) {
			Log.e(TAG, "Unable to parse poll: " + e.getMessage());
			showErrorToast("An internal error occured.");
			finish();
			return;
		}

		holder = new PollViewHolder();
		holder.setImageLoader(imageLoader);
		imageview = (ImageView) findViewById(R.id.imageViewPhoto);

		holder.findViews(findViewById(android.R.id.content));
		holder.setUITypefaces(this);
		holder.setVoteListener(voteListener);
		holder.setProfileListener(profileListener);
		holder.postedBy.setMaxWidth((int)(screenwidth - 210*(this.getResources().getDisplayMetrics()).density));
		

		shareButton = (Button) findViewById(R.id.buttonshareonfb);
		favoriteButton = (Button) findViewById(R.id.buttonfavorite);
		detailsButton = (Button) findViewById(R.id.polldetails);
		commentsLayout = (LinearLayout) findViewById(R.id.linearLayoutCommentsScroll);
		backButton = (Button) findViewById(R.id.buttonBack);
		deleteButton = (Button) findViewById(R.id.buttonDelete);
		commentDesc = (TextView) findViewById(R.id.commentDesc);
		commentText = (EditText) findViewById(R.id.editTextComment);
		commentList = (ListView) findViewById(R.id.listViewComments);
		commentButton = (Button) findViewById(R.id.buttonSubmitComment);
		framelayout = (FrameLayout) findViewById(R.id.frameLayout1);
		comments = new ArrayList<Comment>();
		commentsAdapter = new CommentListAdapter(this, comments);
		commentsAdapter.setImageLoader(imageLoader);
		commentList.setAdapter(commentsAdapter);

		updateView();

		if (imageview.getVisibility() == View.GONE)
			framelayout.setVisibility(View.GONE);
		else
			framelayout.setVisibility(View.VISIBLE);

		loadPollComments();

		final ScrollView SV = (ScrollView) findViewById(R.id.scrollView);

		SV.post(new Runnable() {

			public void run() {

				SV.scrollTo(0, 0);
			}
		});

		if (poll.getCreatorId().equals(smartPoll.getUserId()))
			deleteButton.setVisibility(View.VISIBLE);
		else
			deleteButton.setVisibility(View.GONE);

		commentButton.setTypeface(PollViewHolder.myTypefaceBold);
		detailsButton.setTypeface(PollViewHolder.myTypefaceBold);
		commentDesc.setTypeface(PollViewHolder.myTypeface);
		shareButton.setTypeface(PollViewHolder.myTypefaceBold);
		favoriteButton.setTypeface(PollViewHolder.myTypefaceBold);
		backButton.setTypeface(PollViewHolder.myTypefaceBold);
		deleteButton.setTypeface(PollViewHolder.myTypefaceBold);

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		detailsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PollResultsActivity.this,
						PollAnalyticsActivity.class);
				//*************** new to add stuff here ***************

				//intent.putExtra("poll", result.toString());
				startActivity(intent);
				
			}
		});

		deleteButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						v.getContext());

				// set title
				// alertDialogBuilder.setTitle("Your Title");

				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Are you sure you want to delete this poll?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity

										new AsyncTask<String, Void, Boolean>() {
											@Override
											protected Boolean doInBackground(
													String... params) {
												return smartPoll
														.deletePoll(params[0]);
											}

											@Override
											protected void onPostExecute(
													Boolean result) {
												if (result) {
													Intent data = new Intent();
													data.putExtra("pollId", poll.getId());
													setResult(Utils.RESULT_CODE_POLL_DELETED, data);
													onBackPressed();

													/*QuestionsActivity
															.customToast("Poll deleted");*/
												} else
													showErrorToast("Unable to delete poll.");
											}
										}.execute(poll.getId());

										// PollResultsActivity.this.finish();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}

		});

		commentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String commentDesc = commentText.getText().toString();
				commentDesc = commentDesc.replace(
						System.getProperty("line.separator"), "");

				if (commentDesc.isEmpty())
					return;

				Log.i(TAG, "Posting comment: " + commentDesc);
				progressBar = new ProgressDialog(PollResultsActivity.this);
				progressBar.setCancelable(false);
				progressBar.setMessage("Posting comment...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressBar.show();

				new AsyncTask<String, Void, Comment>() {
					@Override
					protected Comment doInBackground(String... params) {
						return smartPoll.submitComment(params[0], params[1]);
					}

					@Override
					protected void onPostExecute(Comment result) {
						progressBar.dismiss();
						InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(
								getCurrentFocus().getWindowToken(), 0);
						if (result != null) {
							comments.add(result);
							updateCommentUI();
							commentText.setText("");
							poll.setNumComments(poll.getNumComments() + 1);
							refreshPollOnReturn();
						} else
							showErrorToast("Unable to post comment.");
					}
				}.execute(poll.getId(), commentDesc);
			}

		});

		favoriteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Mark as favorite!");
				showProgressBar("Marking as favorite...");

				new AsyncTask<String, Void, Poll>() {
					@Override
					protected Poll doInBackground(String... params) {
						return smartPoll.markAsFavorite(params[0]);
					}

					@Override
					protected void onPostExecute(Poll result) {
						dismissProgressBar();
						if (result != null) {
							favoriteButton.setText("Favorite");
							favoriteButton
									.setBackgroundResource(drawable.selectedfavoritebutton);
							favoriteButton.setEnabled(false);
							holder.numFavorites.setText(""
									+ result.getNumFavorites());
							poll = result;
							updateView();
							refreshPollOnReturn();
						} else
							showErrorToast("Unable to mark as favorite.");
					}
				}.execute(poll.getId());
			}
		});

		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showProgressBar("Publishing to Facebook...");
				publishFacebookStory(poll, false);
			}
		});
	}

	OnClickListener voteListener = new OnClickListener() {
		public void onClick(View v) {
			RadioButton r = (RadioButton) v;
			RadioGroup group = (RadioGroup) r.getParent();

			PollViewHolder holder = (PollViewHolder) group.getTag();
			holder.setVotingUI();

			new AsyncTask<String, Void, Poll>() {

				@Override
				protected Poll doInBackground(String... params) {
					return smartPoll.submitVote(params[0]);
				}

				@Override
				protected void onPostExecute(Poll result) {
					if (result != null) {
						poll = result;
						poll.setJustVoted(true);
						updateView();
						refreshPollOnReturn();
					} else {
						showErrorToast("Unable to submit your vote!");
					}
				}

			}.execute(r.getTag().toString());
		}
	};

	OnClickListener profileListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getTag() == null)
				return;
			User creator = (User) v.getTag();
			Intent intent = new Intent(PollResultsActivity.this, ProfileActivity.class);
			intent.putExtra("profile", creator.toString());
			PollResultsActivity.this.startActivity(intent);
		}
	};
	

	void reloadPoll() {
		new AsyncTask<String, Void, Poll>() {

			@Override
			protected Poll doInBackground(String... params) {
				return smartPoll.fetchPoll(params[0]);
			}

			@Override
			protected void onPostExecute(Poll result) {
				poll = result;
				updateView();
			}

		}.execute(poll.getId());
	}

	void updateView() {
		boolean isOwner = (poll.getCreatorId().equals(SmartPollSystem
				.getInstance(this).getUserId()));
		boolean hasResponded = (poll.getResponse() != null);

		holder.updateView(poll, isOwner, hasResponded, true);

		if (poll.getIsFavorite()) {
			favoriteButton.setText("Favorite");
			favoriteButton
					.setBackgroundResource(drawable.selectedfavoritebutton);
			favoriteButton.setEnabled(false);
		}
		if (poll.getIsSharedOnFacebook()) {
			shareButton.setText("Shared");
			shareButton.setBackgroundResource(drawable.selectedsharebutton);
			shareButton.setEnabled(false);
		}
	}

	void updateCommentUI() {
		int height = 0;

		int desiredWidth = MeasureSpec.makeMeasureSpec(commentList.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < commentsAdapter.getCount(); i++) {
			View listItem = commentsAdapter.getView(i, null, commentList);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			height += listItem.getMeasuredHeight();
		}

		height = height
				+ (commentList.getDividerHeight() * (commentsAdapter.getCount() - 1));

		commentsAdapter.setUserClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getTag() == null)
					return;
				CommentListAdapter.ViewHolder holder = (CommentListAdapter.ViewHolder) v.getTag();
				Intent intent = new Intent(PollResultsActivity.this, ProfileActivity.class);
				intent.putExtra("profile", holder.comment.getCreator().toString());
				PollResultsActivity.this.startActivity(intent);
			}
			
		});
		commentsAdapter.notifyDataSetChanged();
		commentsLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, height));
		commentsLayout.requestLayout();
		holder.setNumComments(comments.size());
		commentDesc.setText("Comments (" + comments.size() + ")");
	}

	void loadPollComments() {
		new AsyncTask<String, Void, ArrayList<Comment>>() {
			@Override
			protected ArrayList<Comment> doInBackground(String... params) {
				return smartPoll.fetchPollComments(params[0]);
			}

			@Override
			protected void onPostExecute(ArrayList<Comment> result) {
				if (result != null) {
					comments.clear();
					comments.addAll(result);
					updateCommentUI();
				} else
					showErrorToast("Unable to delete poll.");
			}
		}.execute(poll.getId());
	}

	void showProgressBar(String message) {
		progressBar = new ProgressDialog(PollResultsActivity.this);
		progressBar.setCancelable(false);
		progressBar.setMessage(message);
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();
	}

	void dismissProgressBar() {
		if (progressBar != null)
			progressBar.dismiss();
	}

	static void addReply() {
		commentText.setText(replyTo);
		commentText.requestFocus();
		commentText.setSelection(replyTo.length());

		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

	}

	void publishFacebookStoryCompleted(FacebookRequestError error, String postId) {
		dismissProgressBar();
		if (error != null) {
			Log.i(TAG, "Facebook Error Message: " + error.getErrorMessage());
			showErrorToast("Unable to publish to your facebook timeline: "
					+ error.getErrorMessage());
		} else {
			Log.i(TAG, "Facebook Message ID: " + postId);
			new AsyncTask<String, Void, Poll>() {
				@Override
				protected Poll doInBackground(String... params) {
					return smartPoll.markAsShared(params[0]);
				}

				@Override
				protected void onPostExecute(Poll result) {
					dismissProgressBar();
					shareButton.setText("Shared");
					shareButton
							.setBackgroundResource(drawable.selectedsharebutton);
					shareButton.setEnabled(false);
					showErrorToast("Poll was published to your facebook timeline.");
					refreshPollOnReturn();
				}
			}.execute(poll.getId());
		}
	}

	private void showErrorToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private void refreshPollOnReturn() {
		Intent data = new Intent();
		data.putExtra("poll", poll.toString());
		setResult(Utils.RESULT_CODE_POLL_UPDATED, data);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.transition_left_to_right,
				R.anim.transition_left_to_right_out);
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
