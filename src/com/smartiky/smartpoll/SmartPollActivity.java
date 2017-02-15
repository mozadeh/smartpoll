package com.smartiky.smartpoll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.smartiky.smartpoll.R.drawable;
import com.smartiky.smartpoll.misc.GCMUtils;
import com.smartiky.smartpoll.misc.ImageLoader;
import com.smartiky.smartpoll.misc.Log;
import com.smartiky.smartpoll.misc.Utils;
import com.smartiky.smartpoll.system.Poll;
import com.smartiky.smartpoll.system.SmartPollSystem;
import com.smartiky.smartpoll.system.User;

@SuppressWarnings("deprecation")
public class SmartPollActivity extends ActivityGroup {

	protected static final String TAG = "SmartPollActivity";

	protected static final int MYPOLLS_ACTIVITY = 1000;
	protected static boolean leaderboardOpen = false;
	
	protected static int initialHeight;
	protected static int targetHeight;

	protected static final int MENUITEM_NORMAL = 0;
	protected static final int MENUITEM_SELECTED = 1;

	// boolean dontSlide = false;

	static String CATEGORY_AFTER_CREATE_POLL = "all polls";
	static boolean newPollCreated = false;

	SmartPollSystem smartPoll;
	ImageLoader imageLoader;

	Button createButton, pollButton, profileButton;
	FrameLayout tabContent;
	ImageView topArrow;
	TextView leaderBoard, insightfuluserstv, bestpollstv;
	GCMUtils gcmUtils;
	LinearLayout leaderboardLL;
	int width, height;
	float scale;
	DisplayMetrics metrics;
	Display display;

	final String PREFS_NAME = "MyPrefsFile";

	ArrayList<View> pollButtons;
	ArrayList<Button> profileButtons;
	ArrayList<String> categories, trends;
	Vector<Map<String, Integer>> popmenuIcons;

	ArrayList<User> insightfulUsers, bestpollsUsers;
	UserListAdapter insightfulUsersAdapter, bestpollsUsersAdapter;
	ListView insightfulUsersList, bestpollsUsersList;

	public SmartPollActivity() {
		smartPoll = SmartPollSystem.getInstance(this);
		imageLoader = new ImageLoader(smartPoll, false);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		registerGCM();
		findUserLocation();

		metrics = getResources().getDisplayMetrics();
		display = getWindowManager().getDefaultDisplay();
		scale = getResources().getDisplayMetrics().density;

		tabContent = (FrameLayout) findViewById(android.R.id.tabcontent);

		createButton = (Button) findViewById(R.id.createButton);
		topArrow = (ImageView) findViewById(R.id.menuItemImage);
		leaderBoard = (TextView) findViewById(R.id.leaderboardbutton);
		pollButton = (Button) findViewById(R.id.pollButton);
		profileButton = (Button) findViewById(R.id.profileButton);
		leaderboardLL = (LinearLayout) findViewById(R.id.leaderboardLL);
		insightfuluserstv = (TextView) findViewById(R.id.insightfuluserstv);
		bestpollstv = (TextView) findViewById(R.id.bestpollstv);
		//leaderBoard.setShadowLayer(6 * metrics.density, 0, 0, 0xFF000000);
		//createMenuButtons();
		
		Typeface myTypeface = Typeface.createFromAsset(getAssets(),
				"Roboto-BoldCondensed.ttf");
		createButton.setTypeface(myTypeface);
		profileButton.setTypeface(myTypeface);
		pollButton.setTypeface(myTypeface);
		leaderBoard.setTypeface(myTypeface);
		insightfuluserstv.setTypeface(myTypeface);
		bestpollstv.setTypeface(myTypeface);

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		if (settings.getBoolean("my_first_time", true)) {
			// The app is being launched for first time.
			Log.d(TAG, "First time");

			addShortcut();

			// record the fact that the app has been started at least once
			settings.edit().putBoolean("my_first_time", false).commit();
			createButton.setSelected(true);
		} else {
			pollButton.setSelected(true);
		}

		initLeaderboard();
		updateActivities();

		createButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(v.getContext(),
						drawable.anim_click));
				
				createButton.setSelected(true);
				pollButton.setSelected(false);
				profileButton.setSelected(false);
				
				leaderboardOpen=false;
				updateActivities();
			}
		});

		pollButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(v.getContext(),
						drawable.anim_click));
				pollButton.setSelected(true);
				createButton.setSelected(false);
				profileButton.setSelected(false);
				
				updateActivities();

				//categoryMenu.showAsDropDown(pollButton,(int) (-1 * metrics.density), 0);

			}
		});

		profileButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(v.getContext(),
						drawable.anim_click));
				profileButton.setSelected(true);
				pollButton.setSelected(false);
				createButton.setSelected(false);
				
				updateActivities();
				//trendMenu.showAsDropDown(profileButton,(int) (-1 * metrics.density), 0);

			}
		});

		leaderboardLL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (!leaderboardOpen) {

					loadLeaderboard();

					targetHeight = (int) (display.getHeight() - 42 * scale);
					initialHeight = (int) (42 * scale);

					Animation a = new Animation() {
						@Override
						protected void applyTransformation(
								float interpolatedTime, Transformation t) {
							leaderboardLL.getLayoutParams().height = interpolatedTime == 1 ? (int) (targetHeight)
									: (int) (targetHeight * interpolatedTime)
											+ initialHeight;
							leaderboardLL.requestLayout();
						}

						@Override
						public boolean willChangeBounds() {
							return true;
						}
					};

					a.setDuration(500);
					v.startAnimation(a);

					leaderboardOpen = true;
				} else {
					closeLeaderboard();
				}
			}
		});

		String pollId = getIntent().getStringExtra("pollId");
		if (pollId != null && !pollId.isEmpty()) {
			new AsyncTask<String, Void, Poll>() {

				@Override
				protected Poll doInBackground(String... params) {
					return SmartPollSystem.getInstance(getApplicationContext())
							.fetchPoll(params[0]);
				}

				@Override
				protected void onPostExecute(Poll result) {
					Intent intent = new Intent(SmartPollActivity.this,
							PollResultsActivity.class);

					intent.putExtra("poll", result.toString());
					startActivity(intent);
				};

			}.execute(pollId);
		}

	}

	void registerGCM() {
		gcmUtils = new GCMUtils();
		gcmUtils.register(getApplicationContext(),
				gcmUtils.new RegistrationIdListener() {

					@Override
					public void onRegistrationIdReceived(String registrationId) {
						if (registrationId == null || registrationId.isEmpty())
							return;
						Log.i(TAG, "Registering GCM ID with server: "
								+ registrationId);
						new AsyncTask<String, Void, Boolean>() {

							@Override
							protected Boolean doInBackground(String... params) {
								String registrationId = params[0];
								return SmartPollSystem.getInstance(
										getApplicationContext())
										.registerWithGCM(registrationId);
							}

							@Override
							protected void onPostExecute(Boolean result) {
								if (!result) {
									// TODO: retry registration!
									Log.i(TAG, "Unable to register the device!");
								} else {
									Log.i(TAG, "Device registered!");
								}
							}

						}.execute(registrationId);

					}

				});
	
	}
	
	void findUserLocation() {
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		String provider = locationManager.getBestProvider(criteria, true);

		if (provider == null) {
			Log.v(TAG, "No location provider found!");
		} else {
			SmartPollSystem.setCurrentLocation(locationManager
					.getLastKnownLocation(provider));

			// Define a listener that responds to location updates
			LocationListener locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					// Called when a new location is found by the network
					// location provider.
					SmartPollSystem.setCurrentLocation(location);
				}

				public void onStatusChanged(String provider, int status,
						Bundle extras) {
				}

				public void onProviderEnabled(String provider) {
				}

				public void onProviderDisabled(String provider) {
				}
			};

			// Register the listener with the Location Manager to receive
			// location updates
			locationManager.requestLocationUpdates(provider, 0, 0,
					locationListener);
		}
		
	}

	void updateActivities() {

		//saveStateToPref();
		//getLocalActivityManager().removeAllActivities();

		if (createButton.isSelected()) {
			addNewPollActivity();
			leaderboardLL.setVisibility(View.GONE);
			createButton.setTextColor(Color.parseColor("#000000"));
			createButton.setShadowLayer(0, 0, 0, 0xFF000000);
			createButton.setBackgroundResource(R.drawable.topmenugradientselected);
		} else {
			createButton.setTextColor(Color.WHITE);
			createButton.setShadowLayer(4 * metrics.density, 0, 0, 0xFF000000);
			createButton.setBackgroundResource(R.drawable.topmenugradient);
		}

		if (profileButton.isSelected()) {
			addProfileActivity();
			leaderboardLL.setVisibility(View.GONE);
			profileButton.setTextColor(Color.parseColor("#000000"));
			profileButton.setShadowLayer(0, 0, 0, 0xFF000000);
			profileButton.setBackgroundResource(R.drawable.topmenugradientselected);
		} else {
			profileButton.setTextColor(Color.WHITE);
			profileButton.setShadowLayer(4 * metrics.density, 0, 0, 0xFF000000);
			profileButton.setBackgroundResource(R.drawable.topmenugradient);
		}
		
		if (pollButton.isSelected()) {
			addQuestionsActivity();
			leaderboardLL.setVisibility(View.VISIBLE);
			pollButton.setTextColor(Color.parseColor("#000000"));
			pollButton.setShadowLayer(0, 0, 0, 0xFF000000);
			pollButton.setBackgroundResource(R.drawable.topmenugradientselected);
		} else {
			pollButton.setTextColor(Color.WHITE);
			pollButton.setShadowLayer(4 * metrics.density, 0, 0, 0xFF000000);
			pollButton.setBackgroundResource(R.drawable.topmenugradient);
		}
		
	}

	public void addActivity(String id, Intent intent) {
		Window window = getLocalActivityManager().startActivity(id, intent);

		tabContent.removeAllViews();
		tabContent.addView(window.getDecorView(), new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

	void addQuestionsActivity() {
		Intent intent = new Intent(this, PollsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		addActivity("QuestionsActivity", intent);
	}

	void addNewPollActivity() {
		Intent intent = new Intent(this, NewPollActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		addActivity("NewPollActivity", intent);
	}
	
	void addProfileActivity() {
		Intent intent = new Intent(this, ProfileActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("profile", smartPoll.getProfile().toString());
		addActivity("ProfileActivity", intent);
	}

	void returnFromNewPoll() {
		//getLocalActivityManager().removeAllActivities();
		createButton.setSelected(false);
		pollButton.setSelected(true);
		
		updateActivities();
		PollsActivity activity = (PollsActivity) getLocalActivityManager().getActivity("QuestionsActivity");
		activity.setSelectedTab(1);
	}

	void returnFromProfile() {
		//getLocalActivityManager().removeAllActivities();
		profileButton.setSelected(false);
		pollButton.setSelected(true);
		
		updateActivities();
	}

	@Override
	public void onBackPressed() {
		if (createButton.isSelected())
			returnFromNewPoll();
		else if (profileButton.isSelected())
			returnFromProfile();
		else if (leaderboardOpen)
			closeLeaderboard();
		else
			super.onBackPressed();
	}
	
 	public void closeLeaderboard(){
		initialHeight = (int) (display.getHeight() - 42 * scale);
		targetHeight = (int) (42 * scale);

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				leaderboardLL.getLayoutParams().height = initialHeight
						- (int) ((initialHeight - targetHeight) * interpolatedTime);
				leaderboardLL.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		a.setDuration(500);
		//getCurrentFocus().startAnimation(a);
		this.getWindow().getDecorView().findViewById(android.R.id.content).startAnimation(a);

		leaderboardOpen = false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case MYPOLLS_ACTIVITY:
			if (resultCode == RESULT_FIRST_USER) {
				createButton.performClick();
			}
			break;
		}
	}

	/**
	 * Creates and Handles Option Menu Items
	 */
	private static final int MENU_REFRESH_POLLS = Menu.FIRST;
	private static final int MENU_ABOUT = Menu.FIRST + 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuRefresh = menu.add(0, MENU_REFRESH_POLLS, 0,
				R.string.refresh_polls);
		menuRefresh.setIcon(R.drawable.ic_menu_refresh);
		MenuItem menuAbout = menu.add(0, MENU_ABOUT, Menu.NONE,
				R.string.menu_about);
		menuAbout.setIcon(R.drawable.ic_menu_info_details);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_REFRESH_POLLS:
			getLocalActivityManager().removeAllActivities();
			updateActivities();
			break;
		case MENU_ABOUT:
			Utils.showAboutDialog(this);
			break;
		default:
			break;
		}
		return true;
	}

	private void addShortcut() {
		// Adding shortcut for MainActivity on Home screen
		Intent shortcutIntent = new Intent(getApplicationContext(),
				LoginActivity.class);

		shortcutIntent.setAction(Intent.ACTION_MAIN);

		Intent addIntent = new Intent();
		Intent removeIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Smart Poll");
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(
						getApplicationContext(), R.drawable.ic_launcher));

		removeIntent
				.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
		getApplicationContext().sendBroadcast(removeIntent);

		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		getApplicationContext().sendBroadcast(addIntent);
	}

	private void initLeaderboard() {
		OnClickListener profileListener = new OnClickListener() {
			public void onClick(View v) {
				UserListAdapter.ViewHolder holder = (UserListAdapter.ViewHolder) v.getTag();
				Intent intent = new Intent(SmartPollActivity.this, ProfileActivity.class);
				intent.putExtra("profile", holder.user.toString());				
				startActivity(intent);
			}
		};

		insightfulUsers = new ArrayList<User>();
		insightfulUsersAdapter = new UserListAdapter(this, insightfulUsers, R.layout.leaderboard_item);
		insightfulUsersAdapter.setImageLoader(imageLoader);
		insightfulUsersAdapter.setUserClickListener(profileListener);
		insightfulUsersList = (ListView) findViewById(R.id.insightfuluserslist);
		insightfulUsersList.setAdapter(insightfulUsersAdapter);

		bestpollsUsers = new ArrayList<User>();
		bestpollsUsersAdapter = new UserListAdapter(this, bestpollsUsers, R.layout.leaderboard_item);
		bestpollsUsersAdapter.setImageLoader(imageLoader);
		bestpollsUsersAdapter.setUserClickListener(profileListener);
		bestpollsUsersList = (ListView) findViewById(R.id.bestpollslist);
		bestpollsUsersList.setAdapter(bestpollsUsersAdapter);
	}

	private void loadLeaderboard() {
		new AsyncTask<Void, Void, Pair<List<User>, List<User>>>() {

			@Override
			protected Pair<List<User>, List<User>> doInBackground(
					Void... params) {
				return smartPoll.fetchLeaderboard();
			}

			@Override
			protected void onPostExecute(Pair<List<User>, List<User>> result) {
				if (result != null && result.first != null
						&& result.second != null) {
					insightfulUsers.clear();
					for (int i = 0; i < result.first.size(); i++)
						insightfulUsers.add(result.first.get(i));
					insightfulUsersAdapter.notifyDataSetChanged();
					bestpollsUsers.clear();
					for (int i = 0; i < result.second.size(); i++)
						bestpollsUsers.add(result.second.get(i));
					bestpollsUsersAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getApplicationContext(),
							"Unable to fetch leaderboard.", Toast.LENGTH_LONG)
							.show();
				}
			}

		}.execute();
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
