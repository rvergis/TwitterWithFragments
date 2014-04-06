package com.codepath.apps.twitterwithfragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;

import android.util.Log;

public class TweetUtils {
	
	public static final String JSON_DATE_FORMAT_TWITTER ="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
	
	private static final long MILLISECS_IN_1_SEC = 1000L;
	
	private static final long SECS_IN_1_MIN = 60;
	
	private static final long SECS_IN_1_HOUR = SECS_IN_1_MIN * 60;
	
	private static final long SECS_IN_1_DAY = SECS_IN_1_HOUR * 24;
	
	private static final long SECS_IN_1_WEEK = SECS_IN_1_DAY * 7;
	
	private static final long SECS_IN_1_MONTH = SECS_IN_1_DAY * 30;
	
	private static final long SECS_IN_1_YEAR = SECS_IN_1_DAY * 365;
	
	private static final String LOG_NAME = TweetUtils.class.getSimpleName();

	public static long parseTweetJSONDate(String jsonTweetDateStr) throws JSONException {
		try {
			SimpleDateFormat sf = new SimpleDateFormat(JSON_DATE_FORMAT_TWITTER, Locale.getDefault());
			sf.setLenient(true);
			Date date = sf.parse(jsonTweetDateStr);
			return date.getTime();
		} catch(ParseException e) {
			Log.e(LOG_NAME, "Unable to parse " + jsonTweetDateStr + " using " + JSON_DATE_FORMAT_TWITTER, e);
			throw new JSONException(e.getMessage());
		}
	}
	
	public static String getFriendlyDate(long dateTime) {
		String friendlyName = new Date(dateTime).toString();
		
		Date now = new Date();
		Date then = new Date(dateTime);
		Date diff = new Date(now.getTime() - then.getTime());
		
		// handle any slight discrepancy between Twitter time servers and this client
		long secs = Math.max(diff.getTime() / MILLISECS_IN_1_SEC, 0);
		if (secs < SECS_IN_1_MIN) {
			friendlyName = "around " + pluralize(secs, "second");
		} else if (secs < SECS_IN_1_HOUR) {
			friendlyName = "around " + pluralize(secs / SECS_IN_1_MIN, "minute");
		} else if (secs < SECS_IN_1_DAY) {
			friendlyName = "around " + pluralize(secs / SECS_IN_1_HOUR, "hour");			
		} else if (secs < SECS_IN_1_WEEK) {
			friendlyName = "around " + pluralize(secs / SECS_IN_1_DAY, "day");						
		} else if (secs < SECS_IN_1_MONTH) {
			friendlyName = "around " + pluralize(secs / SECS_IN_1_WEEK, "week");									
		} else if (secs < SECS_IN_1_YEAR) {
			friendlyName = "around " + pluralize(secs / SECS_IN_1_MONTH, "month");												
		} else {
			friendlyName = "around " + pluralize(secs / SECS_IN_1_YEAR, "year");															
		}
	
		return friendlyName;
	}
	
	public static String pluralize(long value, String singular) {
		if (value == 1) {
			return "a " + singular + " ago";
		}
		return + value + " " + singular + "s" + " ago";
	}
}
