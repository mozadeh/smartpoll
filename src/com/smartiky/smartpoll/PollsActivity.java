package com.smartiky.smartpoll;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.google.analytics.tracking.android.EasyTracker;
import com.smartiky.smartpoll.misc.Log;
import com.smartiky.smartpoll.system.SmartPollSystem;

public class PollsActivity extends FragmentActivity {

	// Results: delete a poll, update a poll, a new poll was created.

	protected static final String TAG = "PollsActivity";

	SmartPollSystem smartPoll;
	Animation animCustomToastEnter;
	Animation animCustomToastExit;
	ViewPager mViewPager;
	SectionsPagerAdapter mSectionsPagerAdapter;
	PagerSlidingTabStrip PTS;
	
	LinearLayout layoutbackground;

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.polls_activity);
		smartPoll = SmartPollSystem.getInstance(getApplicationContext());
		Typeface myTypefacebold = Typeface.createFromAsset(this.getAssets(),
				"Roboto-BoldCondensed.ttf");
		overridePendingTransition(R.anim.transition_left_to_right, R.anim.transition_left_to_right_out);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		PTS = (PagerSlidingTabStrip) findViewById(R.id.pager_title_strip);
		PTS.setTypeface(myTypefacebold, Typeface.NORMAL);
		PTS.setViewPager(mViewPager);		
		
		
		for (int i = 0; i < PTS.getChildCount(); i++) {
			if (PTS.getChildAt(i) instanceof TextView) {
				((TextView) PTS.getChildAt(i)).setTypeface(myTypefacebold);
				((TextView) PTS.getChildAt(i)).setTextSize(19);
			}
		}
		
		/*controller = AnimationUtils.loadLayoutAnimation(this,
				R.anim.list_layout_controller);
		pollloader = (TextView) findViewById(R.id.pollloader);
		// loadingPolls = (TextView) findViewById(R.id.loadingPolls);
		layoutbackground = (LinearLayout) findViewById(R.id.questionlistbackground);
		animCustomToastEnter = AnimationUtils.loadAnimation(this, R.drawable.anim_alphaenter);
		animCustomToastExit = AnimationUtils.loadAnimation(this, R.drawable.anim_alphaexit);

		Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),
				"Roboto-Condensed.ttf");

		((TextView) findViewById(R.id.textViewStartPoll))
				.setTypeface(myTypeface);
		pollloader.setTypeface(myTypeface);*/

		/* Initialising and restoring state */
		/*Map<String, Object> state = (Map<String, Object>) getLastNonConfigurationInstance();
		if (state != null) {
			smartPoll = (SmartPollSystem) state.get("smartPoll");
		}
		if (smartPoll == null) {
			smartPoll = SmartPollSystem.getInstance(this);

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

		ImageView refreshButton = (ImageView) findViewById(R.id.imageViewRefresh);
		refreshButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				emptyList.setVisibility(View.GONE);
				//loadListItems();
			}

		});

		((TextView) findViewById(R.id.textViewStartPoll))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						getParent().findViewById(R.id.createButton).performClick();
					}
				});*/
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		Log.i(TAG, "onActivityResult");
	}
	
	public void setSelectedTab(int item) {
		mViewPager.setCurrentItem(item);
		PTS.notifyDataSetChanged();
		mSectionsPagerAdapter.notifyDataSetChanged();
		Fragment fragment = mSectionsPagerAdapter.fragments[item];
		if (fragment instanceof PollsFragment) {
			((PollsFragment) fragment).refresh();
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		Fragment[] fragments;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new Fragment[getCount()];
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
				return PollsFragment.newInstance(PollsFragment.POPULAR);
			case 1:
				return PollsFragment.newInstance(PollsFragment.RECENT);
			case 2:
				return PollsFragment.newInstance(PollsFragment.FOLLOWING);
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "POPULAR";
			case 1:
				return "RECENT";
			case 2:
				return "FOLLOWING";
			}
			return null;
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
