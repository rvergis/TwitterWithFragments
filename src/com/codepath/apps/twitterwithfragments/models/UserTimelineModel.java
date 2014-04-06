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
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterwithfragments.TweetUtils;
import com.codepath.apps.twitterwithfragments.TwitterClient;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "user_timeline")
public class UserTimelineModel extends Model implements ITweetModel {
	// Define table fields
	@Column(name = "body")
	private String body;
	
	@Column(name = "uid", index=true, unique=true, onUniqueConflict=ConflictAction.IGNORE)
	private long uid;
	
	@Column(name = "createdAt")
	private long createdAt;
	
	@Column(name = "favorited")
	private boolean favorited;
	
	@Column(name = "retweeted")
	private boolean retweeted;
	
	@Column(name = "user")
	private long userUid;
	
	public UserTimelineModel() {
		super();
	}
	
	public UserTimelineModel(String body, long uid, long createdAt, boolean favorited,
			boolean retweeted, long userUid) {
		super();
		this.body = body;
		this.uid = uid;
		this.createdAt = createdAt;
		this.favorited = favorited;
		this.retweeted = retweeted;
		this.userUid = userUid;
	}

	@Override
	public String toString() {
		return Long.toString(uid);
	}

	// Getters
	public String getName() {
		return body;
	}
	
	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public boolean isRetweeted() {
		return retweeted;
	}

	public long getUserUid() {
		return userUid;
	}
	
	public static UserTimelineModel fromJSON(JSONObject jsonObject) throws JSONException {
		UserTimelineModel model = new UserTimelineModel(
				jsonObject.getString("text"), 									// body
				jsonObject.getLong("id"), 										// uid
				TweetUtils.parseTweetJSONDate(jsonObject.getString("created_at")), 	// uid
				jsonObject.getBoolean("favorited"),								// favorited
				jsonObject.getBoolean("retweeted"),								// retweeted
				UserModel.fromJSON(jsonObject.getJSONObject("user")).getUid()	// User
				);
        return model;
	}

	public static Map<Long, UserTimelineModel> fromJson(JSONArray jsonTweets) {
		Map<Long, UserTimelineModel> tweets = new HashMap<Long, UserTimelineModel>();
		try {
			int count = jsonTweets.length();
			for (int i = 0; i < count; i++) {
				JSONObject jsonTweet = (JSONObject) jsonTweets.get(i);
				UserTimelineModel tweet = UserTimelineModel.fromJSON(jsonTweet);
				tweets.put(tweet.getUid(), tweet);					
			}
			Log.d("TwitterClient", jsonTweets.toString());					
		} catch(JSONException e) {
			Log.e(TwitterClient.LOG_NAME, "User timeline", e);
		}
		return tweets;
	}
	
	// Record Finders
	public static UserTimelineModel byId(long id) {
	   return new Select().from(UserTimelineModel.class).where("id = ?", id).executeSingle();
	}
	
	public static UserTimelineModel byUid(long uid) {
	   return new Select().from(UserTimelineModel.class).where("uid = ?", uid).executeSingle();
	}
	
	public static Long maxUid() {
		UserTimelineModel model = (UserTimelineModel) new Select().from(UserTimelineModel.class).orderBy("uid ASC").executeSingle();
		if (model != null) {
			return (model.getUid());			
		}
		return null;
	}
	
	public static List<Model> recentItems(Long maxUid, long maxLimit) {
		if (maxUid != null) {
			// using leq to be consistent with Twitter max_id behavior
			return new Select().from(UserTimelineModel.class).where("uid <= ?", maxUid.longValue()).orderBy("createdAt DESC").limit(Long.toString(maxLimit)).execute();
		}
		return new Select().from(UserTimelineModel.class).orderBy("createdAt DESC").limit(Long.toString(maxLimit)).execute();
	}
	
	public static void save(JSONObject jsonObject) {
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(jsonObject);
		save(jsonArray);
	}
	
	public static void save(JSONArray jsonArray) {
		Map<Long, UserTimelineModel> tweets = UserTimelineModel.fromJson(jsonArray);
		if (tweets != null && !tweets.isEmpty()) {
			for (UserTimelineModel tweet : tweets.values()) {
				if (UserTimelineModel.byUid(tweet.getUid()) == null) {
					tweet.save();					
				}
			}	
		}
	}
}
