package com.zainmanji.sportsrefresh;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import com.zainmanji.sportsrefresh.HeadlineItem;
import com.zainmanji.sportsrefresh.HeadlineItemSeeker;


//Activity for user inputting a query to retrieve specific headline results
public class FilterActivity extends Activity {
    
	//Declare and initialize variables
    private static final String EMPTY_STRING = "";
	public static String query = "";
    private EditText searchEditText;
    private Button searchButton;
    private HeadlineItemSeeker headlineSeeker = new HeadlineItemSeeker();
    private ProgressDialog progressDialog;

    
    //onCreate called when Activity is launched
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
        
        //Obtain all views
        this.findAllViewsById();
        
        //Get query from EditText view and store it. 
        //Call perform search to get latest headlines.
        searchButton.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                query = searchEditText.getText().toString();
                performSearch("");
            }
        });
        
        //Set default text in EditText to search string in resources when EditText does not have focus
        searchEditText.setOnFocusChangeListener(new TextOnFocusListener(getString(R.string.search)));
    }
    

    //Obtain all views by their respective IDs from XML resource
    private void findAllViewsById() {
        searchEditText = (EditText) findViewById(R.id.filter_search_edit_text);
        searchButton = (Button) findViewById(R.id.filter_search_button);
    }
    

    //Handle situation when EditText view has/does not have focus (Help from javacodegeeks.com - open source)
    private class TextOnFocusListener implements OnFocusChangeListener {
        
        private String defaultText;

        public TextOnFocusListener(String defaultText) {
            this.defaultText = defaultText;
        }

        //Handle obtaining/losing focus if View is an EditText
        public void onFocusChange(View v, boolean hasFocus) {
        	
            if (v instanceof EditText) {
            	
                EditText focusedEditText = (EditText) v;
                
                //Handle obtaining focus (Set text to nothing)
                if (hasFocus) {
                    if (focusedEditText.getText().toString().equals(defaultText)) {
                        focusedEditText.setText(EMPTY_STRING);
                    }
                }
                //Handle losing focus (Set text to default string)
                else {
                    if (focusedEditText.getText().toString().equals(EMPTY_STRING)) {
                        focusedEditText.setText(defaultText);
                    }
                }
            }
        }
        
    }
    
    
    //Search for latest headlines
    private void performSearch(String query) {
        
        progressDialog = ProgressDialog.show(FilterActivity.this, "Please wait...", "Retrieving headlines...", true, true);
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
		           if (progressDialog != null) {
		              progressDialog.dismiss();
		              progressDialog = null;
		           }
		         
		           //Launch the HeadlinesListActivity to display the results
		           Intent intent = new Intent(FilterActivity.this, HeadlinesListActivity.class);
			       intent.putExtra("headlines", result);
			       startActivity(intent);
		        }
	        });
	    }      
    }
}
