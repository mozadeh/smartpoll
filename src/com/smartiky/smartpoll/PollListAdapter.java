package com.smartiky.smartpoll;

import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.smartiky.smartpoll.misc.ImageLoader;
import com.smartiky.smartpoll.system.Poll;
import com.smartiky.smartpoll.system.SmartPollSystem;

public class PollListAdapter extends ArrayAdapter<Poll> {

	protected static final String TAG = "PollListAdapter";  

	Context context;
	OnClickListener voteListener;
	OnClickListener pollDetailListener;
	OnClickListener profileListener;
	ImageLoader imageLoader;
	LayoutInflater mInflater;
	DisplayMetrics metrics;
	int screenwidth;
	
	public PollListAdapter(Context context, List<Poll> data) {
		super(context, R.layout.question, data);
		this.context = context;
		mInflater = LayoutInflater.from(context);
		metrics = context.getResources().getDisplayMetrics();
		screenwidth = metrics.widthPixels;
	}

	public void setVoteListener(OnClickListener voteListener) {
		this.voteListener = voteListener;
	}
	
	public void setPollDetailListener(OnClickListener pollDetailListener) {
		this.pollDetailListener = pollDetailListener;
	}

	public void setProfileListener(OnClickListener profileListener) {
		this.profileListener = profileListener;
	}
	
	public void updateList() {
		this.notifyDataSetChanged();
	}

	public void setImageLoader(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// Create a ViewHolder if the view is not recycled.
		
		PollViewHolder holder;
		if (view == null) {
			view = mInflater.inflate(R.layout.question, null);
			holder = new PollViewHolder();
			holder.setImageLoader(imageLoader);
			holder.findViews(view);
			holder.postedBy.setMaxWidth((int)(screenwidth - 200*(view.getResources().getDisplayMetrics()).density));
			// Manual customization of the layout
			holder.setUITypefaces(context);
			holder.setVoteListener(voteListener);

			holder.setProfileListener(profileListener);
			
			
			view.setTag(holder);
			
			
		} else {
			holder = (PollViewHolder) view.getTag();
		}
		
		view.setOnClickListener(pollDetailListener);

		Poll poll = getItem(position);
		boolean isOwner = (poll.getCreatorId().equals(SmartPollSystem.getInstance(context).getUserId()));
		boolean hasResponded = (poll.getResponse() != null); 
		
		holder.updateView(poll, isOwner, hasResponded, false);

		if (hasResponded) {
			holder.rightArrow.setImageResource(R.drawable.expandbuttons);
			holder.rightArrow.setBackgroundResource(R.drawable.buttonexpandgradient);
		} else {
			holder.rightArrow.setImageResource(R.drawable.expandbutton);
			holder.rightArrow.setBackgroundResource(R.drawable.buttonexpandgradient1);
		}

		return view;
	}
}
