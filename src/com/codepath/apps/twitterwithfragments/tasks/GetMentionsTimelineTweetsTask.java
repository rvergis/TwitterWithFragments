package com.codepath.apps.twitterwithfragments.tasks;

import com.codepath.apps.twitterwithfragments.TwitterClientApp;
import com.loopj.android.http.JsonHttpResponseHandler;


public class GetMentionsTimelineTweetsTask extends AbstractGetTimelineTweetsTask {

	@Override
	protected IRestClientInvoker createRestClientInvoker() {
		return new IRestClientInvoker() {
			
			@Override
			public void invoke(Long maxId, long refreshCount,
					JsonHttpResponseHandler handler) {
				TwitterClientApp.getRestClient().getMentionsTimeline(maxId, refreshCount, handler);				
			}
		};
	}
	
}
