package com.codepath.apps.twitterwithfragments.fragments;

import java.util.ArrayList;
import java.util.List;

import com.codepath.apps.twitterwithfragments.ProfileActivity;
import com.codepath.apps.twitterwithfragments.R;
import com.codepath.apps.twitterwithfragments.TweetsAdapter;
import com.codepath.apps.twitterwithfragments.models.ITweetModel;
import com.codepath.apps.twitterwithfragments.models.UserTimelineModel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public abstract class AbstractTimelineFragment extends TweetsListFragment {
	
	TweetsAdapter adapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);	
		
		final Long[] userIds = new Long[] { null };
		Intent intent = getActivity().getIntent();
		if (intent != null) {
			if (intent.hasExtra("user_id")) {
				userIds[0] = intent.getLongExtra("user_id", 0L);				
			}
		}
		
		List<ITweetModel> tweets = new ArrayList<ITweetModel>();
		adapter = new TweetsAdapter(getActivity(), tweets);
		ListView lvTweets = (ListView) getActivity().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);		
		lvTweets.setOnScrollListener(new AbsListView.OnScrollListener() {			
			static final int THRESHOLD_COUNT = 3;			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {				
				if (firstVisibleItem + visibleItemCount + THRESHOLD_COUNT >= totalItemCount) {
					adapter.updateTweetsView(createAsyncTask(), userIds[0]);						
				}
			}
		});
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserTimelineModel.clear();
				ITweetModel tweetModel = adapter.getItem(position);				
				Intent intent = new Intent(getActivity(), ProfileActivity.class);
				intent.putExtra("user_id", tweetModel.getUserUid());
				startActivity(intent);
			}			
			
		});
		
	}
	
	protected abstract AsyncTask<Object, Void, Void> createAsyncTask(); 
	
	public TweetsAdapter getAdapter() {
		return adapter;
	}

}
