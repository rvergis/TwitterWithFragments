package com.codepath.apps.twitterwithfragments;

import java.util.List;

import com.codepath.apps.twitterwithfragments.models.ITweetModel;
import com.codepath.apps.twitterwithfragments.models.UserModel;
import com.codepath.apps.twitterwithfragments.tasks.GetHomeTimelineTweetsTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetsAdapter extends ArrayAdapter<ITweetModel> {

	public static long REFRESH_COUNT = 25L;

	public TweetsAdapter(Context context, List<ITweetModel> tweets) {
		super(context, 0, tweets);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		
		ITweetModel tweet = getItem(position);
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
	
	public void clearView() {
		Context context = getContext();
		if (context instanceof Activity) {
			Activity activity = (Activity) getContext();
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					clear();
					notifyDataSetChanged();
				}
				
			});
		}
	}
	
	public void refreshTweetsView() {
		Context context = getContext();
		if (context instanceof Activity) {
			Activity activity = (Activity) getContext();
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					clear();
					new GetHomeTimelineTweetsTask().execute(TweetsAdapter.this);			
				}
				
			});
		}
	}
	
	public void updateTweetsView(final AsyncTask<Object, Void, Void> asyncTask) {
		Context context = getContext();
		if (context instanceof Activity) {
			Activity activity = (Activity) getContext();
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					asyncTask.execute(TweetsAdapter.this);			
				}
				
			});
		}
	}
	
	public void addTweetsToView(final List<ITweetModel> listItems) {	
		Context context = getContext();
		if (context instanceof Activity) {
			Activity activity = (Activity) getContext();
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					addAll(listItems);
					notifyDataSetChanged();			
				}
				
			});
		}
	}
	
}
