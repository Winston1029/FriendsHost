package com.moupress.app.friendshost.ui.listeners;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class TextLinkClickListenerImpl implements TextLinkClickListener {
	private Activity activity;
	
	public TextLinkClickListenerImpl(Activity act) {
		activity = act;
	}
	
	@Override
	public void onTextLinkClick(View textView, String clickedString) {
		if (clickedString.trim().startsWith("http://")) {
			
		}
		else if (clickedString.trim().startsWith("@")) {
			clickedString = clickedString.trim().replace("@", "https://twitter.com/");
		} else if (clickedString.trim().startsWith("#")) {
			clickedString = clickedString.trim().replace("#", "https://twitter.com/#!/search/%23");
		} 
		
		Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(clickedString));
		activity.startActivity(viewIntent);			
	}
}
