package com.codepath.apps.twitterwithfragments.fragments;

import android.os.AsyncTask;

import com.codepath.apps.twitterwithfragments.tasks.GetMentionsTimelineTweetsTask;

public class HomeTimelineFragment extends AbstractTimelineFragment {
	
	public AsyncTask<Object, Void, Void> createAsyncTask() {
		return new GetMentionsTimelineTweetsTask();
	}

}
