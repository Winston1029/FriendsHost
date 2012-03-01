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
        
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        	//this.startService(new Intent(this, FeedRetrievalService.class));
        }
    }
    
    public PubSub zPubsub;
    private void fInit() {
    	zPubsub = new PubSub(this);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		zPubsub.onActivityResult(requestCode, resultCode, data);
	}
}