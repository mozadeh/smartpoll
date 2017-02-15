package com.smartiky.smartpoll;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.smartiky.smartpoll.misc.ImageLoader;
import com.smartiky.smartpoll.misc.Log;
import com.smartiky.smartpoll.misc.Utils;
import com.smartiky.smartpoll.system.Poll;
import com.smartiky.smartpoll.system.SmartPollSystem;
import com.smartiky.smartpoll.system.User;

public class PollsFragment extends ListFragment {

	protected static final String TAG = "PollsFragment";

	public static final int USER_POLLS = 1;
	public static final int USER_FAVORITE_POLLS = 2;
	public static final int MY_POLLS = 3;
	public static final int MY_VOTES = 4;
	public static final int FAVORITES = 5;
	public static final int FOLLOWING = 6;
	public static final int TRENDING = 7;
	public static final int POPULAR = 8;
	public static final int RECENT = 9;

	public static final int REQUEST_CODE_PROFILE = 1001;
	public static final int REQUEST_CODE_POLL_DETAILS = 1002;
	
	public static final String POLL_SELECTION = "PollSelection";
	public static final String PROFILE = "Profile";

	Activity activity;
	SmartPollSystem smartPoll;

	int pollSelection;
	User profile;
	List<Poll> polls;
	PollListAdapter adapter;

	PullAndLoadListView listView;
	boolean refreshing = false;
	boolean loadmore = false;
	ImageView progressBar;
	AnimationDrawable frameAnimation;
	LayoutAnimationController controller;

	boolean loadMoreItems = true;
	boolean loadingItems = false;
	
	public static PollsFragment newInstance(int pollSelection) {
		PollsFragment f = new PollsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(POLL_SELECTION, pollSelection);
		f.setArguments(bundle);
		return f;
	}

	public static PollsFragment newInstance(int pollSelection, User profile) {
		PollsFragment f = new PollsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(POLL_SELECTION, pollSelection);
		bundle.putString(PROFILE, profile.toString());
		f.setArguments(bundle);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		activity = getActivity();
		smartPoll = SmartPollSystem.getInstance(getActivity());
		pollSelection = getArguments().getInt(POLL_SELECTION);
		try {
			String profileStr = getArguments().getString(PROFILE);
			if (profileStr != null)
				profile = new User(new JSONTokener(profileStr));
		} catch (JSONException e) {
		}
		polls = new ArrayList<Poll>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.polls_fragment, container, false);

		controller = AnimationUtils.loadLayoutAnimation(activity,
				R.anim.list_layout_controller);

		listView = (PullAndLoadListView) view.findViewById(android.R.id.list);
		listView.setDividerHeight(0);
		listView.setBackgroundColor(Color.parseColor("#a4a4a4"));
		listView.setLayoutAnimation(controller);
		// listView.setLockScrollWhileRefreshing(true);
		// listView.setRefreshing();

		progressBar = (ImageView) view.findViewById(android.R.id.empty);
		progressBar.setImageResource(R.drawable.spin_animation_small);
		frameAnimation = (AnimationDrawable) progressBar.getDrawable();
		frameAnimation.start();

		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshing = true;
				refresh();
			}
		});

		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				if (loadMoreItems && !loadingItems) {
					loadmore = true;
					loadPolls();
				}
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new PollListAdapter(activity, polls);

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
							Utils.showErrorToast(activity,
									"Unable to submit your vote!");
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

				Intent intent = new Intent(activity, PollResultsActivity.class);

				intent.putExtra("poll", poll.toString());
				startActivityForResult(intent, REQUEST_CODE_POLL_DETAILS);
				activity.overridePendingTransition(
						R.anim.transition_left_to_right,
						R.anim.transition_left_to_right_out);
			}
		};

		OnClickListener profileListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getTag() == null)
					return;
				User creator = (User) v.getTag();
				if (profile != null && creator.getId().equals(profile.getId()))
					return; // Do not reopen the same profile
				Intent intent = new Intent(activity, ProfileActivity.class);
				intent.putExtra("profile", creator.toString());
				startActivityForResult(intent, REQUEST_CODE_PROFILE);
			}
		};

		adapter.setVoteListener(voteListener);
		adapter.setPollDetailListener(pollDetailListener);
		adapter.setProfileListener(profileListener);
		adapter.setImageLoader(new ImageLoader(smartPoll, true));
		listView.setAdapter(adapter);
		loadPolls();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "onActivityResult");
		if (resultCode == Activity.RESULT_CANCELED)
			return;

		switch (resultCode) {
		case Utils.RESULT_CODE_POLL_UPDATED:
			try {
				Poll poll = new Poll(new JSONTokener(data.getExtras()
						.getString("poll")));
				updatePoll(poll);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case Utils.RESULT_CODE_POLL_DELETED:
			String pollId = data.getExtras().getString("pollId");
			removePoll(pollId);
			break;
		}
	}

	void loadPolls() {
		if (loadingItems)
			return;
		loadingItems = true;

		String pollSelection = "" + this.pollSelection;
		String userId = profile == null ? "" : profile.getId();
		String offset = "" + polls.size();
		Log.i(TAG, "LoadMoreItems: " + offset);
		new AsyncTask<String, Void, List<Poll>>() {

			@Override
			protected List<Poll> doInBackground(String... params) {
				int pollSelection = Integer.parseInt(params[0]);
				String userId = params[1];
				int offset = Integer.parseInt(params[2]);
				int limit = 10;

				switch (pollSelection) {
				case MY_POLLS:
					return smartPoll.fetchMyPolls(offset, limit);
				case MY_VOTES:
					return smartPoll.fetchMyVotes(offset, limit);
				case FAVORITES:
					return smartPoll.fetchFavoritePolls(offset, limit);
				case FOLLOWING:
					return smartPoll.fetchFollowingPolls(offset, limit);
				case TRENDING:
					return smartPoll.fetchPollList("all polls", "trending",
							offset, limit);
				case POPULAR:
					return smartPoll.fetchPollList("all polls", "popular",
							offset, limit);
				case RECENT:
					return smartPoll.fetchPollList("all polls", "recent",
							offset, limit);
				case USER_POLLS:
					if (userId == null)
						return null;
					return smartPoll.getUserPolls(userId, offset, limit);
				case USER_FAVORITE_POLLS:
					if (userId == null)
						return null;
					return smartPoll
							.getUserFavoritePolls(userId, offset, limit);
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<Poll> result) {
				if (result != null) {
					// polls.clear();
					polls.addAll(result);
					if (adapter != null)
						adapter.notifyDataSetChanged();

					progressBar.setVisibility(View.GONE);
					frameAnimation.stop();

					if (result.size() == 0)
						loadMoreItems = false;

					if (polls.size() <= 10 && loadMoreItems)
						listView.startLayoutAnimation();
				} else {
					loadMoreItems = false;
					Utils.showErrorToast(activity, "Unable to load user polls.");
				}

				if (refreshing) {
					listView.onRefreshComplete();
					refreshing = false;
				} 
				if (loadmore){
					listView.onLoadMoreComplete();
					loadmore = false;
				}
				loadingItems = false;
			}

		}.execute(pollSelection, userId, offset);
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

	void removePoll(String pollId) {
		for (int i = 0; i < polls.size(); i++)
			if (polls.get(i).getId().equals(pollId)) {
				polls.remove(i);
				break;
			}
		adapter.notifyDataSetChanged();
	}
	
	void refresh() {
		refreshing = true;
		listView.prepareForRefresh();
		loadMoreItems = true;
		progressBar.setVisibility(View.VISIBLE);
		frameAnimation.start();
		polls.clear();
		loadPolls();
		adapter.notifyDataSetChanged();
	}
}
