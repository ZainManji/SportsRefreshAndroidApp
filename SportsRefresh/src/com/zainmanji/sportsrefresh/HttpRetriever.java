package com.zainmanji.sportsrefresh;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.zainmanji.sportsrefresh.FlushedInputStream;
import com.zainmanji.sportsrefresh.Utils;

public class HttpRetriever {
   
	//Http client
   private DefaultHttpClient client = new DefaultHttpClient();   
   
   //Get response from URL
   public String retrieve(String url) {
      
      HttpGet getRequest = new HttpGet(url);
        
      try {
         HttpResponse getResponse = client.execute(getRequest);
         final int statusCode = getResponse.getStatusLine().getStatusCode();
         
         if (statusCode != HttpStatus.SC_OK) { 
            Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url); 
            return null;
         }
         
         HttpEntity getResponseEntity = getResponse.getEntity();
         
         if (getResponseEntity != null) {
            return EntityUtils.toString(getResponseEntity);
         }
      } 
      catch (IOException e) {
         getRequest.abort();
         Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
      }
      
      return null;
   }
   
   //Returns input stream when downloading image from the url for a headline
   public InputStream retrieveStream(String url) {
      
      HttpGet getRequest = new HttpGet(url);
        
      try {
         HttpResponse getResponse = client.execute(getRequest);
         final int statusCode = getResponse.getStatusLine().getStatusCode();
         
         if (statusCode != HttpStatus.SC_OK) { 
            Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url); 
            return null;
         }

         HttpEntity getResponseEntity = getResponse.getEntity();
         return getResponseEntity.getContent();
         
      } 
      catch (IOException e) {
         getRequest.abort();
         Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
      }
      
      return null;
   }
   
   
   //Download bitmap from given url
   public Bitmap retrieveBitmap(String url) throws Exception {
      
      InputStream inputStream = null;
      try {
         inputStream = this.retrieveStream(url);
         final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
         return bitmap;
      } 
      finally {
         Utils.closeStream(inputStream);
      }
      
   }

}