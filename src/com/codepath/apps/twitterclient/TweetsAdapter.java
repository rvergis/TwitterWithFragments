package com.codepath.apps.twitterclient;

import java.util.List;

import com.codepath.apps.twitterclient.models.TweetModel;
import com.codepath.apps.twitterclient.models.UserModel;
import com.codepath.apps.twitterclient.tasks.RefreshTweetsTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetsAdapter extends ArrayAdapter<TweetModel> {

	public static TweetsAdapter instance = null; 
	
	public TweetsAdapter(Context context, List<TweetModel> tweets) {
		super(context, 0, tweets);
		
		if (instance != null) {
			throw new IllegalStateException();
		}
		TweetsAdapter.instance = this;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		
		TweetModel tweet = getItem(position);
		UserModel user = UserModel.byUid(tweet.getUserUid());
		
		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), imageView);
		
		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		String formattedName = "<b>" + user.getName() + "</b>" + " <small><font color='#777777'>@" + 
									user.getScreenName() + "</font></small>";
		nameView.setText(Html.fromHtml(formattedName));
		
		TextView createdAtView = (TextView) view.findViewById(R.id.tvCreatedAt);
		String formattedDate = TweetUtils.getFriendlyDate(tweet.getCreatedAt());
		createdAtView.setText(formattedDate);
		
		TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
		bodyView.setText(Html.fromHtml(tweet.getBody()));
		
		return view;
	}
	
	public void updateView(final Long maxUid) {	
		Context context = getContext();
		if (context instanceof Activity) {
			Activity activity = (Activity) getContext();
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					addAll(TweetModel.recentItems(maxUid, RefreshTweetsTask.REFRESH_COUNT));
					notifyDataSetChanged();			
				}
				
			});
		}
	}
	
}
