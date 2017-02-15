package com.smartiky.smartpoll.misc;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.smartiky.smartpoll.R;
import com.smartiky.smartpoll.system.SmartPollSystem;

public class ImageLoader {

	private static final String TAG = "ImageLoader";

	static Map<String, Drawable> memoryCache = Collections
			.synchronizedMap(new LinkedHashMap<String, Drawable>(10, 1.5f, true));

	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	ExecutorService executorService;
	SmartPollSystem smartPoll;
	int cacheLimit = 100;
	final int no_photo_id;
	boolean notinresults;
	
	public ImageLoader(SmartPollSystem smartpoll, boolean notinResults) {
		executorService = Executors.newFixedThreadPool(5);
		this.smartPoll = smartpoll;
		if (notinResults) no_photo_id = R.drawable.no_image_available;
		else no_photo_id = R.drawable.no_image_availableb;
		notinresults=notinResults;
	}

	public void DisplayImage(String url, ImageView imageView) {

		imageViews.put(imageView, url);
		if (url == null) {
			imageView.setImageResource(no_photo_id);
			imageView.setVisibility(View.GONE);
		} else {
			imageView.setVisibility(View.VISIBLE);

			Drawable drawable = memoryCache.get(url);
			if (drawable != null) {
				imageView.setImageDrawable(drawable);
			} else {
				queuePhoto(url, imageView);
				if (imageView != null) {
					if (notinresults)
						imageView.setImageResource(R.drawable.spin_animation);
					else
						imageView.setImageResource(R.drawable.spin_animation_results);
				}
				AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getDrawable();
				frameAnimation.start();
			}
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Drawable getDrawable(String url) {
		return smartPoll.getPicture(url);
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Drawable bmp = getDrawable(photoToLoad.url);
			// TODO(hosseinkb): Calculate drawable's size.
			memoryCache.put(photoToLoad.url, bmp);
			checkSize();
			if (imageViewReused(photoToLoad))
				return;
			DrawableDisplayer bd = new DrawableDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display drawable in the UI thread
	class DrawableDisplayer implements Runnable {
		Drawable drawable;
		PhotoToLoad photoToLoad;
		
		public DrawableDisplayer(Drawable b, PhotoToLoad p) {
			drawable = b;
			photoToLoad = p;
		}

		public void run() {
			try {
				AnimationDrawable frameAnimation = (AnimationDrawable) photoToLoad.imageView
						.getDrawable();
				frameAnimation.stop();
			} catch (Exception e) {
			}
			if (imageViewReused(photoToLoad))
				return;
			if (drawable != null)
				photoToLoad.imageView.setImageDrawable(drawable);
			else
				photoToLoad.imageView.setImageResource(no_photo_id);
		}
	}
	
	private void checkSize() {
        if(memoryCache.size() > cacheLimit){
            Log.i(TAG, "Image cache size: " + memoryCache.size());
            Iterator<Entry<String, Drawable>> iter = memoryCache.entrySet().iterator(); //least recently accessed item will be the first one iterated  
            while(iter.hasNext()) {
                iter.remove();
                if(memoryCache.size() <= cacheLimit)
                    break;
            }
            Log.i(TAG, "Image cache cleaned.");
        }
    }

	public void clearCache() {
		memoryCache.clear();
	}

}