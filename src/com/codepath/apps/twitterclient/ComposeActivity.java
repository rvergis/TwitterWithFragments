package com.codepath.apps.twitterclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ComposeActivity extends Activity {
	
	private static final int MAX_TWEET_LENGTH = 140;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		final EditText et = (EditText) findViewById(R.id.etComposeView);
		et.setText(null);
		et.setHint(R.string.composeTweetHint);
		
		et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// no op
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// no op
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				updateMaxCharsRemaining();	
				String text = et.getText().toString();
				if (text != null && text.length() > MAX_TWEET_LENGTH) {
					text = text.substring(0, MAX_TWEET_LENGTH);
					et.setText(text);
					et.setSelection(et.length());
				}				
			}
		});
		
		updateMaxCharsRemaining();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void updateMaxCharsRemaining() {
		EditText et = (EditText) findViewById(R.id.etComposeView);
		TextView tv = (TextView) findViewById(R.id.tvMaxChars);
		int rem = Math.max(0, MAX_TWEET_LENGTH - et.length());
		if (rem > 10) {
			tv.setText(rem  + " characters remaining");			
		} else if (rem > 0) {
				tv.setText("'Enough said!'");			
		} else {
			tv.setText("'Calling PETA (757-622-7382)!'");
		}
	}
	
	public void onClickPressed(View b) {
		EditText et = (EditText) findViewById(R.id.etComposeView);
		Intent intent = new Intent();
		intent.putExtra("tweet", et.getText().toString());
		setResult(RESULT_OK, intent);
		finish();
	}

}
