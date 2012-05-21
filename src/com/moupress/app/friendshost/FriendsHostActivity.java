package com.moupress.app.friendshost;

import com.moupress.app.friendshost.util.Mail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class FriendsHostActivity extends FragmentActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        fInit();
        fAnalyseIntent();
    }
    
    public static PubSub zPubsub;
    private void fInit() {
    	if (zPubsub == null ) {
    		zPubsub = new PubSub(this);
    	}
	}
    
    private void fAnalyseIntent() {
    	Bundle extras = getIntent().getExtras();
        if (extras == null) {
        	this.startService(new Intent(this, FeedRetrievalService.class));
        } else {
        	String action = extras.getString(Const.ACTION_DISPLAYFEED);
        	if (action != null && action.length() > 0 ) {
//        		//zPubsub.fInitFeedUIPreview();
//        		if (action.equals(Const.SNS_FACEBOOK)) {
//        			//zPubsub.fInitFeedUIPreview();
//        			zPubsub.fGetFacebookUtil().fDisplayFeed();
//        		} else if (action.equals(Const.SNS_RENREN)) {
//        			//zPubsub.fInitFeedUI();
//        			zPubsub.fGetRenrenUtil().fDisplayRenrenFeed();
//        		} else if (action.equals(Const.SNS_SINA)) {
//        			//zPubsub.fInitFeedUI();
//        			zPubsub.fGetSinaUtil().fDisplaySinaFeed();
//        		}
        		zPubsub.zSnsOrg.GetSnsInstance(action).fDisplayFeed();
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
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.feed_item_ctx_menu, menu);

	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.option_menu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
        case R.id.menu_item_about:
        	Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
        	break;
        case R.id.menu_item_help:
        	Toast.makeText(getApplicationContext(), "Help", Toast.LENGTH_SHORT).show();
        	break;
        case R.id.menu_item_feedback:
        	Toast.makeText(getApplicationContext(), "FeedBack", Toast.LENGTH_SHORT).show();
        	Mail.sendFeedbackEmail(this);
        	break;
        case R.id.menu_item_rate:
        	Toast.makeText(getApplicationContext(), "Rate", Toast.LENGTH_SHORT).show();
        	break;
        default:
        	super.onOptionsItemSelected(item);
        }		
		return true;
	}
}