package com.codepath.apps.twitterwithfragments.tasks;

import com.codepath.apps.twitterwithfragments.TwitterClientApp;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GetHomeTimelineTweetsTask extends AbstractGetTimelineTweetsTask {

	@Override
	protected IRestClientInvoker createRestClientInvoker() {
		return new IRestClientInvoker() {
			
			@Override
			public void invoke(Long maxId, long refreshCount,
					JsonHttpResponseHandler handler) {
				TwitterClientApp.getRestClient().getHomeTimeline(maxId, refreshCount, handler);
				
			}
		};
	}
	
	
}
