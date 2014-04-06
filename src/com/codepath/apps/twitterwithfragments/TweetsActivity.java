package com.codepath.apps.twitterwithfragments;

import com.codepath.apps.twitterwithfragments.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterwithfragments.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterwithfragments.tasks.PostTweetTask;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TweetsActivity extends FragmentActivity implements TabListener {

	private static final int REQUEST_CODE = 1;
	
	private HomeTimelineFragment homeTimelineFragment;
	 
	private MentionsTimelineFragment mentionsTimelineFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupNavigationTabs();
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	public void onComposeNewPressed(MenuItem mi) {
		Intent intent = new Intent(this, ComposeActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				postTweet(requestCode, resultCode, data.getStringExtra("tweet"));
			}
		}
	}
	
	private void postTweet(int requestCode, int resultCode, final String tweet) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Twitter bird dispatched", Toast.LENGTH_SHORT).show();
				postToTwitter(tweet);
			} else {
				Toast.makeText(this, "Twitter bird shot down", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void postToTwitter(String tweet) {
		if (homeTimelineFragment != null) {
			new PostTweetTask().execute(tweet, this, homeTimelineFragment.getAdapter());			
		}
		if (mentionsTimelineFragment != null) {
			new PostTweetTask().execute(tweet, this, mentionsTimelineFragment.getAdapter());						
		}
	}
	
	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tabHome = actionBar.newTab()
								.setText("Home")
								.setTag("HomeTimelineFragment")
								.setTabListener(this);
		
		Tab tabMentions = actionBar.newTab()
									.setText("Mentions")
									.setTag("MentionsTimelineFragment")
									.setTabListener(this);
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		
		actionBar.selectTab(tabHome);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if (tab.getTag().equals("HomeTimelineFragment")) {
			homeTimelineFragment = new HomeTimelineFragment();
			fts.replace(R.id.frame_container, homeTimelineFragment);
		} else if (tab.getTag().equals("MentionsTimelineFragment")) {
			mentionsTimelineFragment = new MentionsTimelineFragment();
			fts.replace(R.id.frame_container, mentionsTimelineFragment);
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
}
