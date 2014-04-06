package com.codepath.apps.twitterwithfragments.fragments;

import java.util.ArrayList;
import java.util.List;

import com.codepath.apps.twitterwithfragments.R;
import com.codepath.apps.twitterwithfragments.TweetsAdapter;
import com.codepath.apps.twitterwithfragments.models.TweetModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;

public abstract class AbstractTimelineFragment extends TweetsListFragment {
	
	TweetsAdapter adapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);		
		List<TweetModel> tweets = new ArrayList<TweetModel>();
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
					adapter.updateTweetsView(createAsyncTask());						
				}
			}
		});	
		
	}
	
	protected abstract AsyncTask<Object, Void, Void> createAsyncTask(); 
	
	public TweetsAdapter getAdapter() {
		return adapter;
	}

}