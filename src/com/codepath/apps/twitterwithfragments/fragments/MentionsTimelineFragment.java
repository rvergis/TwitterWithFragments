package com.codepath.apps.twitterwithfragments.fragments;

import com.codepath.apps.twitterwithfragments.tasks.GetMentionsTimelineTweetsTask;

import android.os.AsyncTask;

public class MentionsTimelineFragment extends AbstractTimelineFragment {

	@Override
	protected AsyncTask<Object, Void, Void> createAsyncTask() {
		return new GetMentionsTimelineTweetsTask();
	}

	
}
