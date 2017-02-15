package com.smartiky.smartpoll.misc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.smartiky.smartpoll.system.SmartPollSystem;

public class ImageTools {
	
	public static final int MAX_IMAGE_DIMENSION = 512;

	/** Create a File for saving an image or video */
	public static File getOutputMediaFile() {
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "SmartPollApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
        "IMG_"+ timeStamp + ".jpg");

	    return mediaFile;
	}	

	 public static int getCameraPhotoOrientation(Context context, String imagePath){
	     int rotate = 0;
	     try {
	         //context.getContentResolver().notifyChange(new Uri(imagePath), null);
	         File imageFile = new File(imagePath);
	         ExifInterface exif = new ExifInterface(
	                 imageFile.getAbsolutePath());
	         int orientation = exif.getAttributeInt(
	                 ExifInterface.TAG_ORIENTATION,
	                 ExifInterface.ORIENTATION_NORMAL);

	         switch (orientation) {
	         case ExifInterface.ORIENTATION_ROTATE_270:
	             rotate = 270;
	             break;
	         case ExifInterface.ORIENTATION_ROTATE_180:
	             rotate = 180;
	             break;
	         case ExifInterface.ORIENTATION_ROTATE_90:
	             rotate = 90;
	             break;
	         }

	     } catch (Exception e) {
	         e.printStackTrace();
	     }
	     return rotate;
	 }
	 
	 public static int getOrientation(Context context, Uri photoUri) {
	    /* it's on the external media. */
	    Cursor cursor = context.getContentResolver().query(photoUri,
	            new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

	    if (cursor == null) {
	    	return getCameraPhotoOrientation(context, photoUri.getPath());
	    }
	    
	    if (cursor.getCount() != 1) {
	        return -1;
	    }

	    cursor.moveToFirst();
	    return cursor.getInt(0);
	}

	public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
	    InputStream is = context.getContentResolver().openInputStream(photoUri);
	    BitmapFactory.Options dbo = new BitmapFactory.Options();
	    dbo.inJustDecodeBounds = true;
	    BitmapFactory.decodeStream(is, null, dbo);
	    is.close();

	    int rotatedWidth, rotatedHeight;
	    int orientation = getOrientation(context, photoUri);

	    if (orientation == 90 || orientation == 270) {
	        rotatedWidth = dbo.outHeight;
	        rotatedHeight = dbo.outWidth;
	    } else {
	        rotatedWidth = dbo.outWidth;
	        rotatedHeight = dbo.outHeight;
	    }

	    Bitmap srcBitmap;
	    is = context.getContentResolver().openInputStream(photoUri);
	    if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
	        float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
	        float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
	        float maxRatio = Math.max(widthRatio, heightRatio);

	        // Create the bitmap from file
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize = (int) maxRatio;
	        srcBitmap = BitmapFactory.decodeStream(is, null, options);
	    } else {
	        srcBitmap = BitmapFactory.decodeStream(is);
	    }
	    is.close();

	    /*
	     * if the orientation is not 0 (or -1, which means we don't know), we
	     * have to do a rotation.
	     */
	    if (orientation > 0) {
	        Matrix matrix = new Matrix();
	        matrix.postRotate(orientation);

	        srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
	                srcBitmap.getHeight(), matrix, true);
	    }

	    return srcBitmap;
	}
	
	public static Bitmap getResizedPictureBitmap(String absolutePath) {
	    // Get the dimensions of the View
	    int targetW = SmartPollSystem.maxImageWidth;
	    int targetH = SmartPollSystem.maxImageHeight;
	  
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(absolutePath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	  
	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
	  
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;
	  
	    Bitmap bitmap = BitmapFactory.decodeFile(absolutePath, bmOptions);
	    return bitmap;
	}
}
