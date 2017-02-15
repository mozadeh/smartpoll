package com.smartiky.smartpoll;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.google.analytics.tracking.android.EasyTracker;
import com.smartiky.smartpoll.misc.ImageTools;
import com.smartiky.smartpoll.misc.Log;
import com.smartiky.smartpoll.misc.Utils;
import com.smartiky.smartpoll.system.Choice;
import com.smartiky.smartpoll.system.Poll;
import com.smartiky.smartpoll.system.SmartPollSystem;

public class NewPollActivity extends FacebookActivity {

	private static final String TAG = "NewPollActivity";
	private static final int GALLERY_RESULT = 10;
	private static final int INTERNET_RESULT = 11;
	private static final int CAMERA_RESULT = 12;

	//Spinner spinner;

	Button createButton, backButton, cameraButton, galleryButton,
			internetButton;
	EditText editQuestion;
	Bitmap mImageBitmap;
	ImageView imagePreview;
	ArrayList<EditText> editOptions;
	ImageView moreOptionsImage;
	TextView moreOptionsText;
	ImageView lessOptionsImage, helpButton;
	TextView lessOptionsText, newPollShare;
	RadioGroup sharingRadio;
	CheckBox isPrivateCheckBox;
	Uri selectedImageUri;
	RadioButton R1,R2,R3;
	Button B1,B2,B3;

	int numOptions = 3;
	Bitmap picture;
	SmartPollSystem smartPoll;

	public NewPollActivity() {
		editOptions = new ArrayList<EditText>();
		smartPoll = SmartPollSystem.getInstance(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newpoll);

		//spinner = (Spinner) findViewById(R.id.spinner);

		createButton = (Button) findViewById(R.id.buttonCreate);
		backButton = (Button) findViewById(R.id.buttonBack);
		cameraButton = (Button) findViewById(R.id.sourceCamera);
		galleryButton = (Button) findViewById(R.id.sourceGallery);
		internetButton = (Button) findViewById(R.id.sourceInternet);
		imagePreview = (ImageView) findViewById(R.id.imagePreview);
		sharingRadio = (RadioGroup) findViewById(R.id.shareRadioGroup);
		isPrivateCheckBox = (CheckBox) findViewById(R.id.checkBoxMakePrivate);
		R1 = (RadioButton) findViewById(R.id.radioShareWithSelectFriends);
		R2 = (RadioButton) findViewById(R.id.radioShareOnFacebookTimeline);
		R3 = (RadioButton) findViewById(R.id.radioDoNotShare);
		B1 = (Button) findViewById(R.id.sourceCamera);
		B2 = (Button) findViewById(R.id.sourceGallery);
		B3 = (Button) findViewById(R.id.sourceInternet);
		editQuestion = (EditText) findViewById(R.id.editQuestion);
		editOptions.add((EditText) findViewById(R.id.editOption1));
		editOptions.add((EditText) findViewById(R.id.editOption2));
		editOptions.add((EditText) findViewById(R.id.editOption3));
		editOptions.add((EditText) findViewById(R.id.editOption4));
		editOptions.add((EditText) findViewById(R.id.editOption5));

		moreOptionsImage = (ImageView) findViewById(R.id.imageViewMoreOptions);
		moreOptionsText = (TextView) findViewById(R.id.textViewMoreOptions);
		lessOptionsImage = (ImageView) findViewById(R.id.imageViewLessOptions);
		lessOptionsText = (TextView) findViewById(R.id.textViewLessOptions);
		newPollShare = (TextView) findViewById(R.id.newPollShare);
		helpButton = (ImageView) findViewById(R.id.helpbutton);

		Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),
				"Roboto-Condensed.ttf");
		Typeface myTypefacebold = Typeface.createFromAsset(this.getAssets(),
				"Roboto-BoldCondensed.ttf");

		backButton.setTypeface(myTypefacebold);
		createButton.setTypeface(myTypefacebold);
		R1.setTypeface(myTypeface);
		R2.setTypeface(myTypeface);
		R3.setTypeface(myTypeface);
		B1.setTypeface(myTypeface);
		B2.setTypeface(myTypeface);
		B3.setTypeface(myTypeface);
		((TextView) findViewById(R.id.newPollQuestion)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.newPollSource)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.textThreeOptions))
				.setTypeface(myTypeface);
		//((TextView) findViewById(R.id.newPollCategory)).setTypeface(myTypeface);
		isPrivateCheckBox.setTypeface(myTypeface);
		newPollShare.setTypeface(myTypeface);
		addItemsToSpinner();
		helpButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				View view = getLayoutInflater().inflate(R.layout.instructions,
						null, false);

				AlertDialog.Builder builder = new AlertDialog.Builder(v
						.getContext());
				builder.setTitle("Help on Sharing");
				builder.setView(view);
				builder.create();
				builder.show();
			}
		});

		cameraButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				File file = ImageTools.getOutputMediaFile();
				selectedImageUri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri); // set
																			// the
																			// image
																			// file
																			// name

				startActivityForResult(intent, CAMERA_RESULT);
			}
		});

		galleryButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, GALLERY_RESULT);
			}
		});

		internetButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(NewPollActivity.this,
						InternetSearchActivity.class);
				intent.putExtra("SearchTerm", editQuestion.getText().toString());
				startActivityForResult(intent, INTERNET_RESULT);
			}
		});

		OnClickListener moreOptionsClickListener = new OnClickListener() {
			public void onClick(View v) {
				if (numOptions < editOptions.size())
					numOptions++;
				updateOptions();
			}
		};

		moreOptionsImage.setOnClickListener(moreOptionsClickListener);
		moreOptionsText.setOnClickListener(moreOptionsClickListener);

		OnClickListener lessOptionsClickListener = new OnClickListener() {

			public void onClick(View v) {
				if (numOptions > 2)
					numOptions--;
				updateOptions();
			}

		};
		lessOptionsImage.setOnClickListener(lessOptionsClickListener);
		lessOptionsText.setOnClickListener(lessOptionsClickListener);

		createButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String msg = getFormErrors();

				if (msg != null) {
					showErrorToast(msg);
					return;
				}
				createPoll();
			}
		});

		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		getParent().onBackPressed();
	}

	int getVisibility(boolean visible) {
		if (visible)
			return View.VISIBLE;
		return View.GONE;
	}

	private void addItemsToSpinner() {
		List<String> list = new ArrayList<String>();
		list.add("Other");
		list.add("Politics");
		list.add("Food");
		list.add("Market");
		list.add("Fashion");
		list.add("Sports");
		list.add("Advice");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//spinner.setAdapter(dataAdapter);
	}

	void updateOptions() {
		for (int i = 0; i < editOptions.size(); i++)
			editOptions.get(i).setVisibility(getVisibility(i < numOptions));
		moreOptionsImage.setVisibility(getVisibility(numOptions < 5));
		moreOptionsText.setVisibility(getVisibility(numOptions < 5));
		lessOptionsImage.setVisibility(getVisibility(numOptions > 2));
		lessOptionsText.setVisibility(getVisibility(numOptions > 2));
	}

	ProgressDialog progressBar;

	void showProgressBar() {
		Log.i(TAG, "ShowProgressBar");
		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(false);
		progressBar.setMessage("Creating Poll");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();
	}

	void dismissProgressBar() {
		Log.i(TAG, "DismissProgressBar");
		if (progressBar != null) {
			progressBar.dismiss();
		}
	}

	@SuppressLint("DefaultLocale")
	protected Poll getNewPoll() {
		try {
			String description = editQuestion.getText().toString();
			//String category = String.valueOf(spinner.getSelectedItem());

			ArrayList<Choice> choices = new ArrayList<Choice>();
			for (int i = 0; i < numOptions; i++)
				if (!editOptions.get(i).getText().toString().isEmpty())
					choices.add(new Choice(editOptions.get(i).getText()
							.toString()));

			Poll newPoll = new Poll();
			newPoll.setDescription(description);
			//newPoll.setCategory(category);
			newPoll.setCategory("no category");
			newPoll.setPicture(picture);
			newPoll.setChoices(choices);

			if (isPrivateCheckBox.isChecked()) {
				newPoll.setPrivacy("private");
			} 

			return newPoll;
		} catch (JSONException e) {
		}
		return null;
	}

	protected void createPoll() {
		showProgressBar();
		Poll poll = getNewPoll();
		if (poll == null) {
			showErrorToast("Unable to create poll.");
			return;
		}
		new AsyncTask<Poll, Void, Poll>() {

			@Override
			protected Poll doInBackground(Poll... params) {
				return smartPoll.createPoll(params[0]);
			}

			@Override
			protected void onPostExecute(Poll result) {
				if (result == null) {
					dismissProgressBar();
					showErrorToast("Sorry, poll creation failed.");
				} else {
					Intent data = new Intent();
					data.putExtra("poll", result.toString());
					setResult(Utils.RESULT_CODE_POLL_CREATED, data);
					
					switch (sharingRadio.getCheckedRadioButtonId()) {
					case R.id.radioShareOnFacebookTimeline:
						publishFacebookStory(result, true);
						break;
					case R.id.radioShareWithSelectFriends:
						shareWithSelectFriends(result);
						break;
					case R.id.radioDoNotShare:
						dismissProgressBar();
						returnWithMessage("Poll created.");
						break;
					}
				}
			}

		}.execute(poll);
	}

	protected String getFormErrors() {
		if (editQuestion.getText().toString().isEmpty())
			return "Please provide a description";
		int numOptions = 0;
		String[] options = new String[editOptions.size()];
		for (int i = 0; i < editOptions.size(); i++)
			if (!editOptions.get(i).getText().toString().isEmpty()) {
				options[numOptions] = editOptions.get(i).getText().toString()
						.trim();
				numOptions++;
			}
		// At least 2 options are necessary.
		if (numOptions < 2)
			return "Please provide at least 2 options";
		// Options should be distinct.
		for (int i = 0; i < numOptions; i++)
			for (int j = i + 1; j < numOptions; j++)
				if (options[i].equalsIgnoreCase(options[j]))
					return "Please provide distinct options";
		// Options should contain characters.
		for (int i = 0; i < numOptions; i++) {
			if (options[i].matches("\\p{Punct}+"))
				return options[i] + " is not a valid answer";
			if (options[i].length() > 45)
				return options[i] + " is too long";
		}
		if (isPrivateCheckBox.isChecked()
				&& sharingRadio.getCheckedRadioButtonId() == R.id.radioDoNotShare) {
			return "Your poll is not visible to anyone. Please review your sharing options";
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "OnActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		// uiHelper.onActivityResult(requestCode, resultCode, data);
		// Session.getActiveSession().onActivityResult(this, requestCode,
		// resultCode, data);

		Intent imageReturnedIntent = data;
		switch (requestCode) {
		case GALLERY_RESULT:
			if (resultCode == RESULT_OK) {
				if (imageReturnedIntent == null)
					return;

				selectedImageUri = imageReturnedIntent.getData();
				try {
					Bitmap b = ImageTools.getCorrectlyOrientedImage(this,
							selectedImageUri);
					picture = b;
				} catch (IOException e) {
					showErrorToast("Cannot load the picture: " + e.getMessage());
				}
				imagePreview.setImageBitmap(picture);
			}
			break;
		case INTERNET_RESULT:
			if (resultCode == RESULT_OK) {
				if (imageReturnedIntent == null)
					return;

				Bundle extras = imageReturnedIntent.getExtras();
				mImageBitmap = (Bitmap) extras.get("data");
				imagePreview.setImageBitmap(mImageBitmap);
				picture = mImageBitmap;
			}
			break;
		case CAMERA_RESULT:
			if (resultCode == RESULT_OK) {
				try {
					Bitmap b = ImageTools.getCorrectlyOrientedImage(this,
							selectedImageUri);
					picture = b;
				} catch (Exception e) {
					showErrorToast("Cannot load the picture.");
					e.printStackTrace();
				}
				imagePreview.setImageBitmap(picture);
			}
			break;
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public void doNotShare(View button) {
		finish();
	}

	void publishFacebookStoryCompleted(FacebookRequestError error, String postId) {
		dismissProgressBar();
		if (error != null) {
			Log.i(TAG, "Facebook Error Message: " + error.getErrorMessage());
			showErrorToast("Poll created, but was not published to your facebook timeline: "
					+ error.getErrorMessage());
			onBackPressed();
		} else {
			Log.i(TAG, "Facebook Message ID: " + postId);
			returnWithMessage("Poll created & published to your facebook timeline.");
		}
	}

	@Override
	void onFacebookRequestSent(boolean sent, Bundle values) {
		dismissProgressBar();
		if (sent) {
			returnWithMessage("Poll created & shared with your friends.");
		} else {
			showErrorToast("Poll created, but was not shared with your friends.");
			onBackPressed();
		}
	}

	private void returnWithMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		onBackPressed();
	}

	private void showErrorToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	

		  @Override
		  public void onStart() {
		    super.onStart();
		    
		    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
		  }

		  @Override
		  public void onStop() {
		    super.onStop();
		    
		    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
		  }
	
}
