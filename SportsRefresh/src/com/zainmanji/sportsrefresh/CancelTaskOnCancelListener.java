package com.zainmanji.sportsrefresh;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

//Cancels process dialog when asynchronous task is completed
public class CancelTaskOnCancelListener implements OnCancelListener {
	
	private AsyncTask<?, ?, ?> task;
	
	//Constructor, assigns async task to object
	public CancelTaskOnCancelListener(AsyncTask<?, ?, ?> task) {
		this.task = task;
	}
	
	//Cancel process dialog when task is completed
	@Override
	public void onCancel(DialogInterface dialog) {
		if (task != null) {
    		task.cancel(true);
    	}
	}
}