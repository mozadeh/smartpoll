package com.smartiky.smartpoll;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.smartiky.smartpoll.misc.ImageLoader;
import com.smartiky.smartpoll.system.Choice;
import com.smartiky.smartpoll.system.Poll;
import com.smartiky.smartpoll.system.User;
import com.smartiky.smartpoll.widgets.ChoiceButton;

public class PollViewHolder {

	protected static final int MAX_CHOICES = 5;

	public String pollId;
	public Poll poll;
	public View questionFrame;
	public TextView questionDesc;
	public ImageView profilePicture;
	public TextView postedBy;
	public TextView numSharesOnFacebook;
	public TextView numFavorites;
	public TextView numComments;
	public ImageView photo;
	public RadioGroup choiceGroup;
	public ChoiceButton[] choices;
	public ImageView rightArrow;
	public ImageView shadow1;
	FrameLayout tabContent;

	public ImageView shadow2;

	static public Typeface myTypeface, myTypefaceItalic, myTypefaceBold;
	ImageLoader imageLoader;

	void findViews(View view) {
		this.questionFrame = view.findViewById(R.id.questionFrame);
		this.questionDesc = (TextView) view.findViewById(R.id.questionDesc);
		this.postedBy = (TextView) view.findViewById(R.id.postedBy);
		this.numSharesOnFacebook = (TextView) view
				.findViewById(R.id.numberofShares);
		this.numFavorites = (TextView) view
				.findViewById(R.id.numberofFavorites);
		this.numComments = (TextView) view.findViewById(R.id.numberofComments);
		this.profilePicture = (ImageView) view.findViewById(R.id.profilePic);
		this.photo = (ImageView) view.findViewById(R.id.imageViewPhoto);
		this.choiceGroup = (RadioGroup) view
				.findViewById(R.id.radioGroupAnswers);
		this.choices = new ChoiceButton[MAX_CHOICES];
		this.choices[0] = (ChoiceButton) view.findViewById(R.id.answerButton1);
		this.choices[1] = (ChoiceButton) view.findViewById(R.id.answerButton2);
		this.choices[2] = (ChoiceButton) view.findViewById(R.id.answerButton3);
		this.choices[3] = (ChoiceButton) view.findViewById(R.id.answerButton4);
		this.choices[4] = (ChoiceButton) view.findViewById(R.id.answerButton5);
		this.rightArrow = (ImageView) view
				.findViewById(R.id.imageViewRightArrow);
		this.shadow1 = (ImageView) view.findViewById(R.id.shadow1);
		this.shadow2 = (ImageView) view.findViewById(R.id.shadow2);
		/*DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		this.postedBy.setMaxWidth((int)(view.getWidth() - 80*(view.getResources().getDisplayMetrics()).density));*/
		
		
		
		choiceGroup.setTag(this);
	}

	private void loadTypefaces(Context context) {
		if (myTypeface == null)
			myTypeface = Typeface.createFromAsset(context.getAssets(),
					"Roboto-Condensed.ttf");
		if (myTypefaceItalic == null)
			myTypefaceItalic = Typeface.createFromAsset(context.getAssets(),
					"Roboto-CondensedItalic.ttf");
		if (myTypefaceBold == null)
			myTypefaceBold = Typeface.createFromAsset(context.getAssets(),
					"Roboto-BoldCondensed.ttf");
	}

	void setUITypefaces(final Context context) {
		loadTypefaces(context);

		questionDesc.setTypeface(myTypefaceItalic);
		postedBy.setTypeface(myTypefaceBold);
		numComments.setTypeface(myTypefaceBold);
		numFavorites.setTypeface(myTypefaceBold);
		if (numSharesOnFacebook != null)
			numSharesOnFacebook.setTypeface(myTypefaceBold);

		for (int i = 0; i < MAX_CHOICES; ++i) {
			choices[i].setTypeFace(myTypeface);
			choices[i].setTypeFaceNumVotes(myTypefaceBold);
		}
	}

	void setImageLoader(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}

	void setVoteListener(OnClickListener voteListener) {
		for (int i = 0; i < this.choices.length; i++)
			choices[i].setOnClickListener(voteListener);
	}

	public void setProfileListener(OnClickListener profileListener) {
		profilePicture.setOnClickListener(profileListener);
		postedBy.setOnClickListener(profileListener);
	}
	
	void updateView(Poll poll, boolean isOwner, boolean hasResponded,
			boolean loadFullSizeImage) {
		String response = poll.getResponse();
		User creator = poll.getCreator();

		boolean showResult = isOwner || hasResponded;

		this.poll = poll;
		this.pollId = poll.getId();
		this.questionDesc.setText(poll.getDescription());
		this.postedBy.setText(creator.getName());
		this.choiceGroup.clearCheck();

		setNumShares(poll.getNumSharesOnFacebook());
		setNumComments(poll.getNumComments());
		setNumFavorites(poll.getNumFavorites());

		if (imageLoader != null) {
			if (loadFullSizeImage)
				imageLoader.DisplayImage(poll.getPictureUrl(), this.photo);
			else
				imageLoader.DisplayImage(poll.getThumbnailUrl(), this.photo);
			// photo.setVisibility(View.GONE);
			imageLoader.DisplayImage(
					creator.getFacebookProfilePicUrl("type=square"),
					this.profilePicture);
		}
		
		profilePicture.setTag(creator);
		postedBy.setTag(creator);

		ArrayList<Choice> choices = poll.getChoices();
		int totalRes = poll.getNumResponses();
		if (shadow1 != null && shadow2 != null) {
			if (poll.getPictureUrl() == null) {
				shadow1.setVisibility(View.GONE);
				shadow2.setVisibility(View.GONE);
			} else {
				if (choices.size() < 4)
					shadow1.setVisibility(View.GONE);
				else
					shadow1.setVisibility(View.VISIBLE);
				if (choices.size() > 2)
					shadow2.setVisibility(View.GONE);
				else
					shadow2.setVisibility(View.VISIBLE);
			}
		}

		for (int i = 0; i < this.choices.length; i++) {
			if (i >= choices.size()) {
				this.choices[i].setVisibility(View.GONE);
				continue;
			}
			Choice choice = choices.get(i);
			ChoiceButton choiceButton = this.choices[i];
			int numRes = choice.getNumResponses();

			choiceButton.setText(choice.getDescription());
			choiceButton.setVisibility(View.VISIBLE);
			choiceButton.setTag(choice.getId());
			// Removed to enable changing votes
			//choiceButton.setEnabled(!hasResponded);
			choiceButton.setVoted(hasResponded);
			choiceButton.setShowResult(showResult);
			choiceButton.setNumResponses(numRes);
			choiceButton
					.setPercentResponses((totalRes != 0) ? (numRes * 100 / totalRes)
							: 0);
			choiceButton.setAnimation(poll.isJustVoted());
			if (response != null && response.equals(choice.getId()))
				choiceButton.setChecked(true);
		}
		poll.setJustVoted(false);
	}

	public void setVotingUI() {
		// Removed to enable changing votes
		/*for (int i = 0; i < MAX_CHOICES; ++i)
			choices[i].setEnabled(false);*/

		photo.setImageBitmap(null);
		photo.setImageResource(R.drawable.spin_animation);
		AnimationDrawable frameAnimation = (AnimationDrawable) photo
				.getDrawable();
		frameAnimation.start();
	}

	public void setNumShares(int num) {
		if (numSharesOnFacebook != null)
			numSharesOnFacebook.setText("" + num);
	}

	public void setNumComments(int num) {
		numComments.setText("" + num);
	}

	public void setNumFavorites(int num) {
		numFavorites.setText("" + num);
	}

}
