package com.codepath.apps.twitterwithfragments.models;

public interface IUserModel {

	public int getFollowersCount();

	public int getFriendsCount();
	
	public String getName();
	
	public long getUid();

	public String getScreenName();

	public String getProfileBgImageUrl();

	public String getProfileImageUrl();
	
	public String getDescription();
	

}
