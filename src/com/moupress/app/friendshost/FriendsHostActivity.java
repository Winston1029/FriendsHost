package com.moupress.app.friendshost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FriendsHostActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        fInit();
        
        fAnalyseIntent();
    }
    
    public static PubSub zPubsub;
    private void fInit() {
    	zPubsub = new PubSub(this);
	}
    
    private void fAnalyseIntent() {
    	Bundle extras = getIntent().getExtras();
        if (extras == null) {
        	this.startService(new Intent(this, FeedRetrievalService.class));
        } else {
        	String action = extras.getString(Const.ACTION_DISPLAYFEED);
        	if (action != null && action.isEmpty() == false ) {
        		if (action.equals(Const.SNS_FACEBOOK)) {
        			zPubsub.fInitFeedUIPreview();
        			zPubsub.fGetFacebookUtil().fDisplayFeed();
        		} else if (action.equals(Const.SNS_RENREN)) {
        			zPubsub.fInitFeedUI();
        			zPubsub.fGetRenrenUtil().fDisplayRenrenFeed();
        		}
        	}
        }
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		zPubsub.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		fAnalyseIntent();
	}
	
}