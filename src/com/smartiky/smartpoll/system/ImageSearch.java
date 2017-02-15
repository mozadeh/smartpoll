package com.smartiky.smartpoll.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.smartiky.smartpoll.misc.Log;


public class ImageSearch extends BackendConnection {
	
	/**
	 * Reference: https://developers.google.com/image-search/v1/jsondevguide
	 */
	
	

    private static final String TAG = "ImageSearch";
    
	public ImageSearch(Context context) {
		super(context);
		serverAddress = "http://ajax.googleapis.com/ajax/services/search/images";
	}
	
	public NetworkObject sendQuery(Map<String, String> params) {
        try {
        	String URI = serverAddress;
        	boolean first = true;
        	
            Iterator<Entry<String, String>> it = params.entrySet().iterator();
            while(it.hasNext()) {
            	Entry<String, String> e = it.next();
            	if (first)
            		URI += "?";
            	else
            		URI += "&";
            	URI += URLEncoder.encode(e.getKey()) + "=" + URLEncoder.encode(e.getValue());
            	first = false;
            }
            
        	/* Open HTTP Connection */
            HttpGet httpget = new HttpGet(URI);
            
            Log.v(TAG, "Request: " + httpget.getRequestLine());
           
            
            HttpResponse httpResponse = client.execute(httpget);
            
            /* Read and return Output */
            String res = EntityUtils.toString(httpResponse.getEntity());
            
            Log.v(TAG, "Response: " + res);
            NetworkObject object = new NetworkObject(new JSONTokener(res));

            return object;
	    }
	    catch(ClientProtocolException e) {
	        Log.v(TAG, "Client Request Error :\n" + e.getMessage());
	    }
        catch(JSONException e) {
	        Log.v(TAG, "Error Parsing the Output :\n" + e.getMessage());
        }
	    catch(IOException e){
	        Log.v(TAG, "Input/Output Error :\n" + e.getClass().getName() + " - " + e.getMessage());
	    }
	    catch(Exception e){
	    	//e.printStackTrace();
	    	Log.v(TAG, "Unknown Error :\n" + e.getMessage());
	    }
	    
    	return null;		
	}
	
	public ArrayList<String> performImageSearch(String query, int size) {		
		ArrayList<String> list = null;
		Map<String, String> params = new HashMap<String, String> ();
		String ipAddress="192.0.0.1";

		try
		{
			ipAddress = getIp();
		}catch(Exception e)
		{
		   e.printStackTrace();
		}
		
		params.put("q", query);
		params.put("v", "1.0");
		params.put("imgsz", "small|medium");
		params.put("userip", ipAddress);
		
			try {
			int start = 0;
			int nextpage = 0;
			do {
				start = nextpage;
				params.put("start", "" + start);
				NetworkObject response = sendQuery(params);
				if (response == null)
					return list;
				ImageSearchResults result = new ImageSearchResults(sendQuery(params));
				if (list == null)
					list = result.getResults();
				else
					list.addAll(result.getResults());
				nextpage = result.getNextPage(start);
			} while(list.size() < size && nextpage != start);
			
		} catch(JSONException e) {
		        Log.v(TAG, "Error Parsing the Output :\n" + e.getMessage());
		} catch(Exception e) {
	    	Log.v(TAG, "Unknown Error :\n" + e.getMessage());
		}
		return list;
	}
	
	class ImageSearchResults extends NetworkObject {
		
		public ImageSearchResults() {
			super();
		}

		public ImageSearchResults(JSONObject copyFrom) {
			super(copyFrom);
		}
		
		private JSONObject getResponse() throws JSONException {
			return getJSONObject("responseData");
		}
		
		public int getNextPage(int start) throws JSONException {
			// TODO Auto-generated method stub
			JSONObject cursor = getResponse().getJSONObject("cursor");
			JSONArray pages = cursor.getJSONArray("pages");
			int currentPageIndex = cursor.getInt("currentPageIndex");
			JSONObject nextPage = pages.getJSONObject(currentPageIndex + 1);
			return nextPage.getInt("start");
		}

		public ArrayList<Integer> getPages() {
			// TODO Auto-generated method stub
			return null;
		}

		public ArrayList<String> getResults() throws JSONException {
			ArrayList<String> ret = new ArrayList<String> ();
			JSONArray result = getResponse().getJSONArray("results");
			for (int i=0; i<result.length(); i++){
				ret.add(result.getJSONObject(i).getString("url"));}
			return ret;
		}

	}

	
	
	public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	
	
}
