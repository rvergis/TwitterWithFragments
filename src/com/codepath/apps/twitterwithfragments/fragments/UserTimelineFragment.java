package com.codepath.apps.twitterwithfragments.fragments;

import android.os.AsyncTask;

import com.codepath.apps.twitterwithfragments.tasks.GetUserTimelineTweetsTask;

public class UserTimelineFragment extends AbstractTimelineFragment {
	
	public AsyncTask<Object, Void, Void> createAsyncTask() {
		return new GetUserTimelineTweetsTask();
	}

}
