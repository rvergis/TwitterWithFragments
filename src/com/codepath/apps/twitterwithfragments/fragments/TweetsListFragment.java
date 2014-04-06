package com.codepath.apps.twitterwithfragments.fragments;

import com.codepath.apps.twitterwithfragments.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TweetsListFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent,
			Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_tweets_list, parent, false);
	}
	
}
