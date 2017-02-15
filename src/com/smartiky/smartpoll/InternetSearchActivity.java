package com.smartiky.smartpoll;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.smartiky.smartpoll.misc.ImageLoader;
import com.smartiky.smartpoll.misc.Log;
import com.smartiky.smartpoll.system.ImageSearch;
import com.smartiky.smartpoll.system.SmartPollSystem;

public class InternetSearchActivity extends Activity {
	
	protected static final String TAG = "InternetSearchActivity";
	
	EditText searchText;
	Button searchButton, backButton;
	ImageSearch imageSearch;
	ImageLoader imageLoader;
	ImageView[] imagePreview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.internetpicture);
		
		imageSearch = new ImageSearch(this);
		imageLoader = new ImageLoader(SmartPollSystem.getInstance(this),true);
		
		Typeface myTypeface = Typeface.createFromAsset(this.getAssets(), "Roboto-Condensed.ttf");
		((TextView) findViewById(R.id.internetpicture)).setTypeface(myTypeface);
		
		Typeface myTypefacebold = Typeface.createFromAsset(this.getAssets(), "Roboto-BoldCondensed.ttf");
		searchText = (EditText) findViewById(R.id.searchText);
		searchButton = (Button) findViewById(R.id.searchButton);
		backButton = (Button) findViewById(R.id.backButton);
		
		searchButton.setTypeface(myTypefacebold);
		backButton.setTypeface(myTypefacebold);
		
		imagePreview = new ImageView[12];
		imagePreview[0] = (ImageView) findViewById(R.id.pic1);
		imagePreview[1] = (ImageView) findViewById(R.id.pic2);
		imagePreview[2] = (ImageView) findViewById(R.id.pic3);
		imagePreview[3] = (ImageView) findViewById(R.id.pic4);
		imagePreview[4] = (ImageView) findViewById(R.id.pic5);
		imagePreview[5] = (ImageView) findViewById(R.id.pic6);
		imagePreview[6] = (ImageView) findViewById(R.id.pic7);
		imagePreview[7] = (ImageView) findViewById(R.id.pic8);
		imagePreview[8] = (ImageView) findViewById(R.id.pic9);
		imagePreview[9] = (ImageView) findViewById(R.id.pic10);
		imagePreview[10] = (ImageView) findViewById(R.id.pic11);
		imagePreview[11] = (ImageView) findViewById(R.id.pic12);
		
		String searchTerm = getIntent().getStringExtra("SearchTerm");
		if (searchTerm != null) {
			searchText.setText(searchTerm);
			doImageSearch();
		}
		
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doImageSearch();
				InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
			    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				
			}			
		});
		
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		OnClickListener imageClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView imageView = (ImageView) v;
				Intent results = new Intent();
				results.putExtra("data", drawableToBitmap(imageView.getDrawable()));
				setResult(RESULT_OK, results);				
				finish();
			}
		};
		
		for (int i=0; i<imagePreview.length; i++)
			imagePreview[i].setOnClickListener(imageClickListener);
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
	protected void doImageSearch() {
    	new AsyncTask<String, Void, ArrayList<String>> () {
			@Override
			protected ArrayList<String> doInBackground(String... params) {
				return imageSearch.performImageSearch(params[0], 12);
			}

			@Override
			protected void onPostExecute(ArrayList<String> result) {
				if (result == null) {
					Toast.makeText(getApplicationContext(), "No result", Toast.LENGTH_LONG).show();
					return;
				}
				for (int i=0; i<result.size(); i++) {
					Log.v(TAG, "URL: " + result.get(i));
					imageLoader.DisplayImage(result.get(i), imagePreview[i]);
				}
				
			}

    	}.execute(searchText.getText().toString());	
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
