package com.codepath.apps.twitterclient.models;

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
import com.codepath.apps.twitterclient.TweetUtils;
import com.codepath.apps.twitterclient.TwitterClient;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "tweets")
public class TweetModel extends Model implements IUid {
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
	
	public TweetModel() {
		super();
	}
	
	public TweetModel(String body, long uid, long createdAt, boolean favorited,
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
	
	public static TweetModel fromJSON(JSONObject jsonObject) throws JSONException {
		TweetModel model = new TweetModel(
				jsonObject.getString("text"), 									// body
				jsonObject.getLong("id"), 										// uid
				TweetUtils.parseTweetJSONDate(jsonObject.getString("created_at")), 	// uid
				jsonObject.getBoolean("favorited"),								// favorited
				jsonObject.getBoolean("retweeted"),								// retweeted
				UserModel.fromJSON(jsonObject.getJSONObject("user")).getUid()	// User
				);
        return model;
	}

	public static Map<Long, TweetModel> fromJson(JSONArray jsonTweets) {
		Map<Long, TweetModel> tweets = new HashMap<Long, TweetModel>();
		try {
			int count = jsonTweets.length();
			for (int i = 0; i < count; i++) {
				JSONObject jsonTweet = (JSONObject) jsonTweets.get(i);
				TweetModel tweet = TweetModel.fromJSON(jsonTweet);
				tweets.put(tweet.getUid(), tweet);					
			}
			Log.d("TwitterClient", jsonTweets.toString());					
		} catch(JSONException e) {
			Log.e(TwitterClient.LOG_NAME, "Home timeline", e);
		}
		return tweets;
	}
	
	// Record Finders
	public static TweetModel byId(long id) {
	   return new Select().from(TweetModel.class).where("id = ?", id).executeSingle();
	}
	
	public static TweetModel byUid(long uid) {
	   return new Select().from(TweetModel.class).where("uid = ?", uid).executeSingle();
	}
	
	public static Long maxUid() {
		TweetModel model = (TweetModel) new Select().from(TweetModel.class).orderBy("uid ASC").executeSingle();
		if (model != null) {
			return (model.getUid() - 1L);			
		}
		return null;
	}
	
	public static List<TweetModel> recentItems(Long maxUid, long maxLimit) {
		if (maxUid != null) {
			return new Select().from(TweetModel.class).where("uid < ?", maxUid.longValue()).orderBy("createdAt DESC").limit(Long.toString(maxLimit)).execute();
		}
		return new Select().from(TweetModel.class).orderBy("createdAt DESC").limit(Long.toString(maxLimit)).execute();
	}
}
