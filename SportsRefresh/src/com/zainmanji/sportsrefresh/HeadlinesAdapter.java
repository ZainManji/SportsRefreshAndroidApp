package com.zainmanji.sportsrefresh;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zainmanji.sportsrefresh.R;
import com.zainmanji.sportsrefresh.FlushedInputStream;
import com.zainmanji.sportsrefresh.HeadlineItem;
import com.zainmanji.sportsrefresh.HttpRetriever;

//This class is a customized ArrayAdapter that displays the headlines in the headlines list on the list view
public class HeadlinesAdapter extends ArrayAdapter<HeadlineItem> {
	
	private HttpRetriever httpRetriever = new HttpRetriever();
	private ArrayList<HeadlineItem> headlineDataItems;
	private Activity context;
	
	//Constructor for HeadlinesAdapter
	public HeadlinesAdapter(Activity context, int textViewResourceId, ArrayList<HeadlineItem> headlineDataItems) {
        super(context, textViewResourceId, headlineDataItems);
        this.context = context;
        this.headlineDataItems = headlineDataItems;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.headline_data_row, null);
		}
		
		//Gets the headline from headlinesList at position specified
		HeadlineItem headlineItem = headlineDataItems.get(position);
		
		//If the headline item is not null, fill the textviews from xml layout resource with the headline's data
		if (headlineItem != null) {
			
			// headline
            TextView headlineTextView = (TextView) view.findViewById(R.id.headline_text_view);
            headlineTextView.setText(headlineItem.headline);
            
            // source
            TextView sourceTextView = (TextView) view.findViewById(R.id.source_text_view);
            sourceTextView.setText(Html.fromHtml("<b>Source:</b> " + headlineItem.source));
            
            // last modified
            TextView lastModifiedTextView = (TextView) view.findViewById(R.id.last_modified_text_view);
            lastModifiedTextView.setText(Html.fromHtml("<b>Date:</b> " + headlineItem.lastModified));
            
            // description
            TextView descriptionTextView = (TextView) view.findViewById(R.id.description_text_view);
            descriptionTextView.setText(Html.fromHtml("<b>Description:</b> " + headlineItem.description));
            
            // headline image
            ImageView imageView = (ImageView) view.findViewById(R.id.headline_thumb_icon);
            String url = headlineItem.retrieveThumbnail();
			
			//If url to download image from is not null, fetch Bitmap from url. If no url exists to download
            //image, display no image for that headline
			if (url != null) {
				
				Bitmap bitmap = fetchBitmapFromCache(url);
				
				//If bitmap does not exist in cache, download it from url, otherwise set it to the image view
				if (bitmap == null) {				
					new BitmapDownloaderTask(imageView).execute(url);
				}
				else {
					imageView.setImageBitmap(bitmap);
				}
			}
			else {
				imageView.setImageBitmap(null);
			}
		}
		return view;
	}
	
	//Bitmap Cache
	private LinkedHashMap<String, Bitmap> bitmapCache = new LinkedHashMap<String, Bitmap>();
	
	//Adds bitmap from url to Cache
	private void addBitmapToCache(String url, Bitmap bitmap) {
		
		//If bitmap exists, add to cache
        if (bitmap != null) {
            synchronized (bitmapCache) {
                bitmapCache.put(url, bitmap);
            }
        }
    }
	
	//Gets bitmap from cache with respective download url
	private Bitmap fetchBitmapFromCache(String url) {
		
		synchronized (bitmapCache) {
	
            final Bitmap bitmap = bitmapCache.get(url);
            
            //If bitmap exists in cache, move element to first position so it is removed last
            if (bitmap != null) {
                bitmapCache.remove(url);
                bitmapCache.put(url, bitmap);
                return bitmap;
            }
        }
        return null;
	}
	
	
	//Asynchronous task to download bitmap (help from javacodegeeks.com - open source)
	private class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		
        private String url;
		private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }
        
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            InputStream is = httpRetriever.retrieveStream(url);
		if (is==null) {
				return null;
			}
		return BitmapFactory.decodeStream(new FlushedInputStream(is));
        }
        
        @Override
        protected void onPostExecute(Bitmap bitmap) {        	
            if (isCancelled()) {
                bitmap = null;
            }
            
            addBitmapToCache(url, bitmap);

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
