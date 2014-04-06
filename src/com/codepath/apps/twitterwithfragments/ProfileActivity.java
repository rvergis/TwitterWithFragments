package com.codepath.apps.twitterwithfragments;

import com.codepath.apps.twitterwithfragments.models.IUserModel;
import com.codepath.apps.twitterwithfragments.tasks.GetUserInfoTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends FragmentActivity implements IUserModelAdapter{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		Long userId = null;
		Intent intent = getIntent();
		if (intent != null) {
			if (intent.hasExtra("user_id")) {
				userId = intent.getLongExtra("user_id", 0L);				
			}
		}
		
		new GetUserInfoTask().execute(this, userId);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public void adapt(IUserModel userModel) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		
		tvName.setText(userModel.getName());
		tvTagline.setText(userModel.getDescription());
		tvFollowers.setText(userModel.getFollowersCount() + " Followers");
		tvFollowing.setText(userModel.getFriendsCount() + " Following");
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(userModel.getProfileImageUrl(), ivProfileImage);
	}

}
