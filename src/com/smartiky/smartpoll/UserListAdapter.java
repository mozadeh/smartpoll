package com.smartiky.smartpoll;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartiky.smartpoll.misc.ImageLoader;
import com.smartiky.smartpoll.system.User;

public class UserListAdapter extends ArrayAdapter<User> {

	Context context;
	ImageLoader imageLoader;
	LayoutInflater mInflater;
	OnClickListener userClickListener;
	int layoutResource;
	Typeface myTypeface;

	public UserListAdapter(Context context, List<User> objects, int resource) {
		super(context, R.id.leaderboardItemName, objects);
		this.context = context;
		this.layoutResource = resource;
		mInflater = LayoutInflater.from(context);
		myTypeface = Typeface.createFromAsset(context.getAssets(),
				"Roboto-Condensed.ttf");
	}

	void setImageLoader(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}

	public void setUserClickListener(OnClickListener userClickListener) {
		this.userClickListener = userClickListener;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			
			view = mInflater.inflate(layoutResource, null);

			holder = new ViewHolder();
			holder.name = (TextView) view
					.findViewById(R.id.leaderboardItemName);
			holder.name.setTypeface(myTypeface);
			holder.rank = (TextView) view
					.findViewById(R.id.leaderboardItemRank);
			holder.photo = (ImageView) view
					.findViewById(R.id.leaderboardItemPhoto);
			holder.llayout = (LinearLayout) view.findViewById(R.id.leaderboardLL);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		User user = getItem(position);
		holder.user = user;
		holder.name.setText(user.getName());
		holder.rank.setText((position + 1) + ".");

		if (imageLoader != null) {
			imageLoader.DisplayImage(
					user.getFacebookProfilePicUrl("type=square"), holder.photo);
		}
		
		view.setOnClickListener(userClickListener);

		return view;
	}

	static class ViewHolder {
		User user;
		TextView rank, name;
		ImageView photo;
		LinearLayout llayout;
	}

}
