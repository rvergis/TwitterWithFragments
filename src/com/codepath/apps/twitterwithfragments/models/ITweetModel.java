package com.codepath.apps.twitterwithfragments.models;

public interface ITweetModel {

	public long getUid();
	
	public long getUserUid();
	
	public long getCreatedAt();
	
	public String getBody();
}
