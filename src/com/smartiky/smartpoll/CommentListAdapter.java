package com.smartiky.smartpoll;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartiky.smartpoll.misc.ImageLoader;
import com.smartiky.smartpoll.system.Comment;

public class CommentListAdapter extends ArrayAdapter<Comment> {

	Context context;
	ImageLoader imageLoader;
	LayoutInflater mInflater;
	TextView commentReply;
	OnClickListener userClickListener;
	
	public CommentListAdapter(Context context, List<Comment> data) {
		super(context, R.layout.comment, data);
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	
	void setImageLoader(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}

	public void setUserClickListener(OnClickListener userClickListener) {
		this.userClickListener = userClickListener;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			view = mInflater.inflate(R.layout.comment, null);
			
			holder = new ViewHolder();
			holder.creator = (TextView) view.findViewById(R.id.commentCreator);
			holder.date = (TextView) view.findViewById(R.id.commentDate);
			holder.desc = (TextView) view.findViewById(R.id.commentDesc);
			holder.photo = (ImageView) view.findViewById(R.id.commentCreatorPhoto);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		Comment comment = getItem(position);
		holder.comment = comment;		
		holder.desc.setText(comment.getDescription());
		commentReply = (TextView) view.findViewById(R.id.commentReply);
		commentReply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PollResultsActivity.replyTo="@"+((String)(holder.creator.getText())).split(" ")[0]+": ";
				PollResultsActivity.addReply();
				InputMethodManager imm = (InputMethodManager) getContext()
		                .getSystemService(Context.INPUT_METHOD_SERVICE);

		        if (imm != null) {
		            imm.showSoftInput(PollResultsActivity.commentText,0);}
				
			}
		});
		if (comment.getCreator().getName().equals("null"))
			holder.creator.setText("Anonymous");
		else
			holder.creator.setText(comment.getCreator().getName());						
		
		Date commentcreated = new Date(comment.getDateCreated() * 1000L);
		Date today = new Date();
		
		Calendar calendar1 = Calendar.getInstance();
	    Calendar calendar2 = Calendar.getInstance();
	    calendar1.setTime(commentcreated);
	    calendar2.setTime(today);
	    long milliseconds1 = calendar1.getTimeInMillis();
	    long milliseconds2 = calendar2.getTimeInMillis();
	    long diff = milliseconds2 - milliseconds1;
	    //long diffSeconds = diff / 1000;
	    long diffMinutes = diff / (60 * 1000);
	    long diffHours = diff / (60 * 60 * 1000);
	    long diffDays = diff / (24 * 60 * 60 * 1000);

		
		
		if (diffDays>365) 
			holder.date.setText("year ago");
		else if (Math.floor(diffDays)>=60)
			holder.date.setText((int)Math.floor(diffDays/30) + " months ago");
		
		else if (Math.floor(diffDays)>=30)
			holder.date.setText("a month ago");
		
		else if (Math.floor(diffDays)>1)
			holder.date.setText((int)Math.floor(diffDays) + " days ago");
		
		else if (Math.floor(diffDays)==1)
			holder.date.setText("yesterday");
		
		else if (Math.floor(diffHours)>1)
			holder.date.setText((int)Math.floor(diffHours) + " hours ago");
		
		else if (Math.floor(diffHours)==1)
			holder.date.setText("an hour ago");
		
		else if (Math.floor(diffMinutes)>20)
			holder.date.setText("less than an hour ago");
		
		else 
			holder.date.setText("a few minutes ago");
		
		if (imageLoader != null) {
			imageLoader.DisplayImage(comment.getCreator().getFacebookProfilePicUrl("type=square"), holder.photo);
		}
		if (userClickListener != null) {
			view.setOnClickListener(userClickListener);
		}
		
		return view;
	}
	
	static class ViewHolder {
		Comment comment;
		TextView creator, desc, date;
		ImageView photo;
	}

}
