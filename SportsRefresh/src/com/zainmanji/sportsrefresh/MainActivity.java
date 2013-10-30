package com.zainmanji.sportsrefresh;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zainmanji.sportsrefresh.HeadlineItem;
import com.zainmanji.sportsrefresh.HeadlineItemSeeker;

//MainActivity class or the Homepage of the app
public class MainActivity extends Activity {

	private static final int ITEM_ENTER_QUERY = 0;
    private Button searchButton;
    private HeadlineItemSeeker headlineSeeker = new HeadlineItemSeeker();
    private ProgressDialog progressDialog;
 
    //onCreate called when Activity first launches
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.findAllViewsById();
        
        searchButton.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                performSearch("");
            }
        });
        
    }
    
    //Create menu items for activity
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, ITEM_ENTER_QUERY, 0, 
				getString(R.string.enter_query)).setIcon(android.R.drawable.ic_menu_search);
		return super.onCreateOptionsMenu(menu);
	}
	
    //If one of the menu items is touched/clicked, act accordingly
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case ITEM_ENTER_QUERY:
				visitSearchPage();
				return true;
		}
		return false;
	}
	
	//Start FilterActivity
	private void visitSearchPage() {
		Intent intent = new Intent(	MainActivity.this, FilterActivity.class);
        startActivity(intent);
	}
    
    
    //Find views according to their respective ID
    private void findAllViewsById() {
        searchButton = (Button) findViewById(R.id.search_button);
    }
    
    //Search for latest headlines
    private void performSearch(String query) {
        
        progressDialog = ProgressDialog.show(MainActivity.this,
                "Please wait...", "Retrieving headlines...", true, true);
        
        PerformHeadlineSearchTask task = new PerformHeadlineSearchTask();
        task.execute(query);
        progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
        
    }
    
    //Search for headlines as an asynchronous task so it doesn't interfere with the UI
    private class PerformHeadlineSearchTask extends AsyncTask<String, Void, ArrayList<HeadlineItem>> {
    		
    	   //Obtain the headlines results
		   @Override
		   protected ArrayList<HeadlineItem> doInBackground(String... params) {
		      String query = params[0];
		      return headlineSeeker.find(query);
		   }
    	   
		   //Once execution is finished and headlines result list is obtained, pass it to new Activity
    	   @Override
    	   protected void onPostExecute(final ArrayList<HeadlineItem> result) {         
    		  runOnUiThread(new Runnable() {
    			  @Override
    			  public void run() {
    				 if (progressDialog!=null) {
    					 progressDialog.dismiss();
    					 progressDialog = null;
    				 }
    	         
    				 //Launch the HeadlinesListActivity to display the results
	    	         Intent intent = new Intent(MainActivity.this, HeadlinesListActivity.class);
	    	         intent.putExtra("headlines", result);
			         startActivity(intent);
    			  }
    	     });
    	  }      
    }
}
