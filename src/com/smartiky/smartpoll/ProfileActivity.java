package com.smartiky.smartpoll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.smartiky.smartpoll.misc.ImageLoader;
import com.smartiky.smartpoll.misc.Log;
import com.smartiky.smartpoll.system.SmartPollSystem;
import com.smartiky.smartpoll.system.User;

public class ProfileActivity extends FragmentActivity {

	private static final String TAG = "ProfileActivity";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SmartPollSystem smartPoll;
	ImageLoader imageLoader;
	User profile;
	SectionsPagerAdapter mSectionsPagerAdapter;
	EditText searchText;
	Button searchButton, backButton, followButton;
	// ImageView[] imagePreview;
	ImageView profilePicture;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	TextView profileName, followers_following, votes_polls, votes, polls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userprofile);
		overridePendingTransition(R.anim.transition_right_to_left,
				R.anim.transition_right_to_left_out);

		smartPoll = SmartPollSystem.getInstance(getApplicationContext());
		imageLoader = new ImageLoader(smartPoll, false);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		PagerTabStrip PTS = (PagerTabStrip) findViewById(R.id.pager_title_strip);

		Typeface myTypefacebold = Typeface.createFromAsset(this.getAssets(),
				"Roboto-BoldCondensed.ttf");
		Typeface myTypefacebolditalic = Typeface.createFromAsset(
				this.getAssets(), "Roboto-BoldCondensedItalic.ttf");

		profilePicture = (ImageView) findViewById(R.id.profilepicture);
		followButton = (Button) findViewById(R.id.followButton);
		followButton.setTypeface(myTypefacebold);
		backButton = (Button) findViewById(R.id.backButton);
		followers_following = (TextView) findViewById(R.id.followers_following);
		followers_following.setTypeface(myTypefacebolditalic);
		votes_polls = (TextView) findViewById(R.id.votes_polls);
		votes_polls.setTypeface(myTypefacebolditalic);
		profileName = (TextView) findViewById(R.id.profilename);
		profileName.setTypeface(myTypefacebold);

		for (int i = 0; i < PTS.getChildCount(); i++) {
			if (PTS.getChildAt(i) instanceof TextView) {
				((TextView) PTS.getChildAt(i)).setTypeface(myTypefacebold);
				((TextView) PTS.getChildAt(i)).setTextSize(19);
			}
		}

		loadProfile();

		backButton.setTypeface(myTypefacebold);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		followButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncTask<String, Void, User>() {
					@Override
					protected User doInBackground(String... params) {
						if (params[1].equalsIgnoreCase("false"))
							return smartPoll.followUser(params[0]);
						else
							return smartPoll.unfollowUser(params[0]);
					}

					@Override
					protected void onPostExecute(User result) {
						if (result != null) {
							profile = result;
							updateProfileView();
						} else {
							showErrorToast("Unable to follow user");
						}
					}
				}.execute(profile.getId(), "" + profile.isFollowing());
			}
		});

	}

	void loadProfile() {
		try {
			if (!getIntent().hasExtra("profile")) {
				showErrorToast("Unable to load profile.");
				finish();
				return;
			}
			profile = new User(new JSONTokener(getIntent().getStringExtra(
					"profile")));
			updateProfileView();
		} catch (JSONException e) {
			Log.e(TAG, "Unable to parse profile: " + e.getMessage());
			finish();
			return;
		}

		new AsyncTask<String, Void, User>() {
			@Override
			protected User doInBackground(String... params) {
				return smartPoll.getUserProfile(params[0]);
			}

			@Override
			protected void onPostExecute(User result) {
				if (result != null) {
					profile = result;
					updateProfileView();
				} else {
					showErrorToast("Unable to load user profile.");
				}
			}
		}.execute(profile.getId());

		new AsyncTask<String, Void, Drawable>() {
			@Override
			protected Drawable doInBackground(String... params) {
				return smartPoll.getPicture(params[0]);
			}

			protected void onPostExecute(Drawable result) {
				if (result != null) {
					setProfilePicture(result);
				}
			}
		}.execute(profile.getFacebookProfilePicUrl("width=140&height=140"));
	}

	void updateProfileView() {
		profileName.setText(profile.getName());
		followers_following.setText("Followers (" + profile.getNumFollowers()
				+ ") / Following (" + profile.getNumFollowings() + ")");
		votes_polls.setText("Votes (" + profile.getNumVotes() + ") / Polls ("
				+ profile.getNumPolls() + ")");
		if (profile.isFollowing()) {
			followButton.setText("Unfollow");
		} else {
			followButton.setText("Follow");
		}
		
		double latitude = profile.getLatitude();
		double longitude = profile.getLongitude();
		if (latitude != 0 && longitude != 0) {
			Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
			try {
			   List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
	
			   String address = "";
			   if (addresses.size() > 0) 
			   {
				   String city = addresses.get(0).getLocality();
				   String country = addresses.get(0).getCountryName();
				   if (city != null && !city.isEmpty() && country != null && !country.isEmpty())
					   address = city + ", " + country;
				   else if (country != null && !country.isEmpty())
					   address = country;
			   }
	
			   if (!address.isEmpty())
				   followers_following.setText(address);
			}
			catch (IOException e1) {
				Log.d(TAG, "Unable to get user location name!");
			}   
		}
		
		UsersFragment followers = (UsersFragment) mSectionsPagerAdapter.getItem(1);
		UsersFragment followings = (UsersFragment) mSectionsPagerAdapter.getItem(2);
		followers.loadUsers();
		followings.loadUsers();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		Fragment[] fragments;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new Fragment[4];
		}

		@Override
		public Fragment getItem(int position) {
			if (fragments[position] == null)
				fragments[position] = getNewItem(position);
			return fragments[position];
		}
		
		public Fragment getNewItem(int position) {
			switch (position) {
			case 0:
				return PollsFragment.newInstance(PollsFragment.USER_POLLS, profile);
			case 1:
				return new UsersFragment(profile, UsersFragment.FOLLOWERS);
			case 2:
				return new UsersFragment(profile, UsersFragment.FOLLOWING);
			case 3:
				return PollsFragment.newInstance(PollsFragment.USER_FAVORITE_POLLS, profile);
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "POLLS";
			case 1:
				return "FOLLOWERS";
			case 2:
				return "FOLLOWING";
			case 3:
				return "FAVORITES";
			}
			return null;
		}
	}

	/*public class PollsFragment extends Fragment {

		public static final int USER_POLLS = 1;
		public static final int FAVORITE_POLLS = 2;

		ListView view;
		PollListAdapter adapter;
		User profile;
		List<Poll> polls;
		int pollSelection;

		PollsFragment(User profile, int pollSelection) {
			this.profile = profile;
			this.pollSelection = pollSelection;
			polls = new ArrayList<Poll>();
			loadPolls();
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			view = new ListView(ProfileActivity.this);
			view.setDividerHeight(0);
			view.setBackgroundColor(Color.parseColor("#a4a4a4"));
			adapter = new PollListAdapter(ProfileActivity.this, polls);
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
								updatePoll(result);
							} else {
								showErrorToast("Unable to submit your vote!");
								adapter.notifyDataSetChanged();
							}
						}

					}.execute(r.getTag().toString());
				}
			};

			OnClickListener pollDetailListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					Poll poll = ((PollViewHolder) v.getTag()).poll;

					Intent intent = new Intent(ProfileActivity.this,
							PollResultsActivity.class);

					intent.putExtra("poll", poll.toString());
					ProfileActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.transition_left_to_right,
							R.anim.transition_left_to_right_out);
				}
			};
			
			OnClickListener profileListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v.getTag() == null)
						return;
					User creator = (User) v.getTag();
					if (creator.getId().equals(profile.getId()))
						return;  // Do not reopen the same profile
					Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
					intent.putExtra("profile", creator.toString());
					ProfileActivity.this.startActivity(intent);
				}
			};

			adapter.setVoteListener(voteListener);
			adapter.setPollDetailListener(pollDetailListener);
			adapter.setProfileListener(profileListener);
			adapter.setImageLoader(new ImageLoader(smartPoll, true));
			view.setAdapter(adapter);
			return view;
		}

		void loadPolls() {
			new AsyncTask<String, Void, List<Poll>>() {

				@Override
				protected List<Poll> doInBackground(String... params) {
					if (pollSelection == USER_POLLS)
						return smartPoll.getUserPolls(params[0]);
					else if (pollSelection == FAVORITE_POLLS)
						return smartPoll.getUserFavoritePolls(params[0]);
					else
						return null;
				}

				@Override
				protected void onPostExecute(List<Poll> result) {
					if (result != null) {
						polls.clear();
						polls.addAll(result);
						if (adapter != null)
							adapter.notifyDataSetChanged();
					} else {
						showErrorToast("Unable to load user polls.");
					}
				}

			}.execute(profile.getId());
		}

		void updatePoll(Poll poll) {
			for (int i = 0; i < polls.size(); i++)
				if (polls.get(i).getId().equals(poll.getId())) {
					poll.setJustVoted(true);
					polls.set(i, poll);
					break;
				}
			adapter.notifyDataSetChanged();
		}

	}*/

	public class UsersFragment extends Fragment {

		public static final int FOLLOWERS = 1;
		public static final int FOLLOWING = 2;

		ListView view;
		UserListAdapter adapter;
		User profile;
		List<User> users;
		int pollSelection;
		
		UsersFragment(User profile, int pollSelection) {
			this.profile = profile;
			this.pollSelection = pollSelection;
			users = new ArrayList<User>();
			loadUsers();
			setRetainInstance(true);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.v(TAG, "UsersFragment.onCreateView");
			view = new ListView(ProfileActivity.this);
			view.setDividerHeight(0);
			view.setBackgroundColor(Color.parseColor("#a4a4a4"));
			return view;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			Log.v(TAG, "UsersFragment.onActivityCreated");
			super.onActivityCreated(savedInstanceState);
			adapter = new UserListAdapter(ProfileActivity.this, users, R.layout.follow_item);
			adapter.setImageLoader(imageLoader);
			adapter.setUserClickListener(new OnClickListener() {
				public void onClick(View v) {
					UserListAdapter.ViewHolder holder = (UserListAdapter.ViewHolder) v.getTag();
					if (holder.user.getId().equals(profile.getId()))
						return;  // Do not reopen the same profile
					Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
					intent.putExtra("profile", holder.user.toString());				
					startActivity(intent);
				}
			});

			view.setAdapter(adapter);
		}

		void loadUsers() {
			new AsyncTask<String, Void, List<User>>() {

				@Override
				protected List<User> doInBackground(String... params) {
					if (pollSelection == FOLLOWERS)
						return smartPoll.getUserFollowers(params[0]);
					else if (pollSelection == FOLLOWING)
						return smartPoll.getUserFollowings(params[0]);
					else
						return null;
				}

				@Override
				protected void onPostExecute(List<User> result) {
					if (result != null) {
						users.clear();
						users.addAll(result);
						if (adapter != null)
							adapter.notifyDataSetChanged();
					} else {
						showErrorToast("Unable to load user polls.");
					}
				}
			}.execute(profile.getId());
		}
	}

	public void setProfilePicture(Drawable picture) {
		int width = profilePicture.getWidth();
		int height = profilePicture.getHeight();

		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		picture.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		picture.draw(canvas);

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(width / 2, height / 2, width / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		profilePicture.setImageBitmap(output);
	}

	private void showErrorToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed() {
		if (getParent() != null)
			getParent().onBackPressed();
		else {
			super.onBackPressed();
			overridePendingTransition(R.anim.transition_left_to_right,
					R.anim.transition_left_to_right_out);
		}
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
