package com.smartiky.smartpoll.system;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Pair;

import com.smartiky.smartpoll.misc.Log;

public class BackendConnection {
	
    private static final String TAG = "BackendConnection";

    String serverAddress;
    DefaultHttpClient client;
    Context context;
    Charset UTF8Charset = Charset.forName("UTF-8");
	
	public BackendConnection(Context context) {
		this.context = context;
		serverAddress = "http://smartpoll.smartiky.com";
		//serverAddress = "http://dev.smartiky.com";
		serverAddress = "http://192.168.0.10";
		//serverAddress = "http://hosseinkb.dyndns.org";
    	client = createHttpClient();
	}

    /* Creates a HttpClient that accepts self-signed certificates. */
    protected static DefaultHttpClient createHttpClient() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        // http scheme
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        
        // https scheme
        //schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));

        HttpParams params = new BasicHttpParams();
        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
                        new ConnPerRouteBean(30));
        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
        //HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
                        schemeRegistry);

        return new DefaultHttpClient(cm, params);
    }
    
    public BackendResponse sendRequest(String controller, String action, Map<String, String> params) {
    	return sendRequestWithFile(controller, action, params, null);
    }
    
    public BackendResponse sendRequestWithFile(String controller, String action, Map<String, String> params, Map<String, File> files) {
    	return sendRequestWithFileAndBinaries(controller, action, params, files, null);
    }
    
    public BackendResponse sendRequestWithBinary(String controller, String action, Map<String, String> params, Map<String, Pair<String, byte[]>> binaries) {
    	return sendRequestWithFileAndBinaries(controller, action, params, null, binaries);
    }
    
    public BackendResponse sendRequestWithFileAndBinaries(String controller, String action, Map<String, String> params, Map<String, File> files, Map<String, Pair<String, byte[]>> binaries) {
        try {
        	//Thread.sleep(3000);
        	/* Open HTTP Connection */
            HttpPost httpost = new HttpPost(serverAddress + "/" + controller + "/" + action + "/");
            Log.v(TAG, "Sending a request: " + controller + "/" + action);
            /* Adding POST variables and data */
            if (files == null && binaries == null) {
                List <NameValuePair> nvp = new ArrayList <NameValuePair>();
                Iterator<Entry<String, String>> it = params.entrySet().iterator();
                while(it.hasNext()) {
                	Entry<String, String> e = it.next();
                	nvp.add(new BasicNameValuePair(e.getKey(), e.getValue()));
                }

                httpost.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8));
            }
            else {
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                
                Iterator<Entry<String, String>> it = params.entrySet().iterator();
                while(it.hasNext()) {
                	Entry<String, String> e = it.next();
                	multipartEntity.addPart(e.getKey(), new StringBody(e.getValue(), UTF8Charset));
                }
                if (binaries != null) {
	                Iterator<Entry<String, Pair<String, byte[]>>> itb = binaries.entrySet().iterator();
	                while(itb.hasNext()) {
	                	Entry<String, Pair<String, byte[]>> e = itb.next();
	                	ByteArrayBody body = new ByteArrayBody(e.getValue().second, e.getValue().first);
	                	multipartEntity.addPart(e.getKey(), body);
	                }
                }
                if (files != null) {
	                Iterator<Entry<String, File>> itf = files.entrySet().iterator();
	                while(itf.hasNext()) {
	                	Entry<String, File> e = itf.next();
	                	FileBody body = new FileBody(e.getValue());
	                	multipartEntity.addPart(e.getKey(), body);
	                }
                }
                httpost.setEntity(multipartEntity);                
            }
            
            Log.v(TAG, "Request: " + httpost.getRequestLine());
            HttpResponse httpResponse = client.execute(httpost);
            
            /* Read and return Output */
            String res = EntityUtils.toString(httpResponse.getEntity());
            
            Log.v(TAG, "Response: " + res);

            return new BackendResponse(new JSONTokener(res));
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
	    	Log.v(TAG, "Unknown Error :\n" + e.getMessage() + " " + e.toString());
	    }
	    
    	return null;
    }
    
    public String fetchURLAsString(String url) {
        try {
        	/* Open HTTP Connection */
			if (!url.startsWith("https://") && !url.startsWith("http://"))
				url = serverAddress + url;
            HttpGet request = new HttpGet(url);
            HttpResponse httpResponse = client.execute(request);

            return EntityUtils.toString(httpResponse.getEntity());
	    }
	    catch(ClientProtocolException e) {
	        Log.v(TAG, "Client Request Error :\n" + e.getMessage());
	    }
	    catch(IOException e){
	        Log.v(TAG, "Input/Output Error :\n" + e.getClass().getName() + " - " + e.getMessage());
	    }
	    catch(Exception e){
	    	Log.v(TAG, "Unknown Error :\n" + e.getMessage());
	    }
	    
    	return null;
    }
    
    public InputStream fetchURLAsStream(String url) {
        try {
        	/* Open HTTP Connection */
			if (!url.startsWith("https://") && !url.startsWith("http://"))
				url = serverAddress + url;
            HttpGet request = new HttpGet(url);
            HttpResponse httpResponse = client.execute(request);

            return httpResponse.getEntity().getContent();
	    }
	    catch(ClientProtocolException e) {
	        Log.v(TAG, "Client Request Error :\n" + e.getMessage());
	    }
	    catch(IOException e){
	        Log.v(TAG, "Input/Output Error :\n" + e.getClass().getName() + " - " + e.getMessage());
	    }
	    catch(Exception e){
	    	Log.v(TAG, "Unknown Error :\n" + e.getMessage());
	    }
	    
    	return null;
    }
    
    public String getServerAddress() {
    	return this.serverAddress;
    }

}
