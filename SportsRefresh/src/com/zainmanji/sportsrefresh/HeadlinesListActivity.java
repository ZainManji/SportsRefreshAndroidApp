package com.zainmanji.sportsrefresh;

import java.util.ArrayList;
import java.util.Iterator;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.zainmanji.sportsrefresh.HeadlineItem;
import com.zainmanji.sportsrefresh.HeadlinesAdapter;


//Activity where headline results are listed
public class HeadlinesListActivity extends ListActivity {
	
	private static final int ITEM_ENTER_QUERY = 0;
	private ArrayList<HeadlineItem> headlinesList = new ArrayList<HeadlineItem>();
	private HeadlinesAdapter headlinesAdapter;
	
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.headlines_layout);

        headlinesAdapter = new HeadlinesAdapter(this, R.layout.headline_data_row, headlinesList);
        headlinesList = (ArrayList<HeadlineItem>) getIntent().getSerializableExtra("headlines");
        
        //Filter results from headlinesList according to specified query from user
        if (FilterActivity.query != "") {
        	filterHeadlines(headlinesList);
        }
        
        setListAdapter(headlinesAdapter);
        
        if (headlinesList!=null && !headlinesList.isEmpty()) {
        	headlinesAdapter.notifyDataSetChanged();
        	headlinesAdapter.clear();
        	
        	//Add headlines to headlinesAdapter
    		for (int i = 0; i < headlinesList.size(); i++) {
    			headlinesAdapter.add(headlinesList.get(i));
    		}
        }
        
        //Notify headlinesAdapter that its content has changed
        headlinesAdapter.notifyDataSetChanged();
    }
	
	
	//onCreate to create menu on activity page
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(Menu.NONE, ITEM_ENTER_QUERY, 0, 
				getString(R.string.enter_query)).setIcon(android.R.drawable.ic_menu_search);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	//If user selects search item from menu, move to that activity's page (FilterActivity)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		//Switch statement in case more menu items need to be added
		switch (item.getItemId()) {
			case ITEM_ENTER_QUERY:
				visitSearchPage();
				return true;
		}
		return false;
	}
	
	
	//Reacts to when certain headline in the list is touched/clicked. 
	//When headline is clicked, that headline's mobile URL will be launched in a browser.
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		HeadlineItem headlineItem = headlinesAdapter.getItem(position);
		String headlineLink = headlineItem.mobileLink; 
		
		if (headlineLink==null || headlineLink.length()==0) {
			longToast(getString(R.string.no_headline_link_found));
			return;
		}
		
		Intent headlineIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(headlineLink));				
		startActivity(headlineIntent);
	}
	
	
	//Visit FilterActivity page
	private void visitSearchPage() {
		Intent intent = new Intent(	HeadlinesListActivity.this, FilterActivity.class);
        startActivity(intent);
	}
	
	//Filter headlinesList with query specified by user
	private void filterHeadlines(ArrayList<HeadlineItem> headlinesList) {
		
		Iterator<HeadlineItem> iter = headlinesList.iterator();
		
		while(iter.hasNext()) {
			if (!iter.next().story.contains(FilterActivity.query)) {
				iter.remove();
			}
		}
		
	}
	
	
	//Display toast if mobile link URL for story does not exist
	public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}
