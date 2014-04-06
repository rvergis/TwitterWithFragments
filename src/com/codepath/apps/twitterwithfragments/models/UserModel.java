package com.codepath.apps.twitterwithfragments.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterwithfragments.TwitterClient;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "users")
public class UserModel extends Model {
	// Define table fields
	@Column(name = "name")
	private String name;
	
	@Column(name = "uid", index=true, unique=true, onUniqueConflict=ConflictAction.IGNORE)
	private long uid;
	
	@Column(name = "screenName")
	private String screenName;
	
	@Column(name = "profileBgImageUrl")
	private String profileBgImageUrl;
	
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	
	@Column(name = "numTweets")
	private int numTweets;
	
	@Column(name = "followersCount")
	private int followersCount;
	
	@Column(name = "friendsCount")
	private int friendsCount;
	
	public UserModel() {
		super();
	}

	public UserModel(String name, long uid, String screenName,
			String profileBgImageUrl, String profileImageUrl, int numTweets, int followersCount,
			int friendsCount) {
		super();
		this.name = name;
		this.uid = uid;
		this.screenName = screenName;
		this.profileBgImageUrl = profileBgImageUrl;
		this.profileImageUrl = profileImageUrl;
		this.numTweets = numTweets;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
	}

	

	@Override
	public String toString() {
		return Long.toString(uid);
	}

	// Getters
	public String getName() {
		return name;
	}
	
	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileBgImageUrl() {
		return profileBgImageUrl;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public int getNumTweets() {
		return numTweets;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	// Record Finders
	public static UserModel byId(long id) {
	   return new Select().from(UserModel.class).where("id = ?", id).executeSingle();
	}
	
	public static UserModel byUid(long uid) {
	   return new Select().from(UserModel.class).where("uid = ?", uid).executeSingle();
	}
	
	public static List<UserModel> recentItems() {
      return new Select().from(UserModel.class).orderBy("id DESC").limit("300").execute();
	}
	
	public static UserModel fromJSON(JSONObject jsonObject) throws JSONException {
		UserModel model = new UserModel(
					jsonObject.getString("name"), 							// name
					jsonObject.getLong("id"), 								// uid
					jsonObject.getString("screen_name"), 					// screenName
					jsonObject.getString("profile_background_image_url"), 	// profileBgImageUrl
					jsonObject.getString("profile_image_url"), 				// profileImageUrl
					jsonObject.getInt("statuses_count"), 					// num_tweets
					jsonObject.getInt("followers_count"), 					// followersCount
					jsonObject.getInt("friends_count") 						// friendsCount
					);			
        return model;
	}

	public static Map<Long, UserModel> fromJson(JSONArray jsonArray) {
		Map<Long, UserModel> tweets = new HashMap<Long, UserModel>();
		try {
			int count = jsonArray.length();
			for (int i = 0; i < count; i++) {
				JSONObject parentJsonObject = (JSONObject) jsonArray.get(i);
				JSONObject jsonObject = parentJsonObject.getJSONObject("user");
				UserModel tweet = UserModel.fromJSON(jsonObject);
				tweets.put(tweet.getUid(), tweet);
			}
			Log.d(TwitterClient.LOG_NAME, jsonArray.toString());					
		} catch(JSONException e) {
			Log.e(TwitterClient.LOG_NAME, "Home timeline", e);
		}
		return tweets;
	}
	
	public static void save(JSONObject jsonObject) {
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(jsonObject);
		save(jsonArray);
	}
	
	public static void save(JSONArray jsonArray) {
		Map<Long, UserModel> users = UserModel.fromJson(jsonArray);
		if (users != null && !users.isEmpty()) {
			for (UserModel user : users.values()) {
				if (UserModel.byUid(user.getUid()) == null) {
					user.save();
				}
			}
		}

	}
	
}
