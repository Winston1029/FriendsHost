package com.moupress.app.friendshost.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.droidfu.widgets.WebImageView;
import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.FeedEntry;

public class FeedDetailViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.feed_item_detail);
		
		fInitUI();
	}

	private void fInitUI() {
		Intent intent = this.getIntent();
		final FeedEntry feed = intent.getParcelableExtra(Const.FEED_ITEM);
		final String displayedSns = intent.getStringExtra(Const.SNS);
		
		WebImageView img_feeduserhead_detail = (WebImageView)findViewById(R.id.img_userhead_detail);
		img_feeduserhead_detail.setImageUrl(feed.getzFriend().getHeadurl());
		img_feeduserhead_detail.loadImage();
		
		TextView txv_feedusername_detail = (TextView) findViewById(R.id.txv_username_detail);
		txv_feedusername_detail.setText(feed.getzFriend().getName());
		
		TextView txv_createdtime_detail = (TextView)findViewById(R.id.txv_createdtime_detail);
		txv_createdtime_detail.setText(feed.getsCreatedTime());
		
		ImageButton img_share_detail = (ImageButton) findViewById(R.id.img_share_detail);
		img_share_detail.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Toast.makeText(getBaseContext(), "Share Button Touched", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		img_share_detail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Share Button Clicked", Toast.LENGTH_SHORT).show();
			}
		});
		
		WebView webV_detail = (WebView) findViewById(R.id.webV_detail);
		if (feed.getsFeedType() != null 
				&& (feed.getsFeedType().equals("blog") || feed.getsFeedType().equals("21")) 
				&& displayedSns.equals(Const.SNS_RENREN)) {
			webV_detail.setVisibility(View.VISIBLE);
			webV_detail.setWebViewClient(new MyWebViewClient());
			webV_detail.loadUrl(feed.getsLink());
		} else {
			webV_detail.setVisibility(View.GONE);
		}
		
		WebImageView img_photo_detail = (WebImageView)findViewById(R.id.img_photo_detail);
		TextView txv_description_detail = (TextView) findViewById(R.id.txv_description_detail);
		if (webV_detail.getVisibility() == View.VISIBLE) {
			img_photo_detail.setVisibility(View.GONE);
			txv_description_detail.setVisibility(View.GONE);
		} else {
			String photoUrl = feed.getsPhotoPreviewLink();
			if (photoUrl != null && photoUrl.length() > 0) {
				img_photo_detail.setVisibility(View.VISIBLE);
				img_photo_detail.setImageUrl(photoUrl);
				img_photo_detail.loadImage();
			} else {
				img_photo_detail.setVisibility(View.GONE);
			}
			
			String descriptionDetail = feed.getsMsgBody() + "\n" + feed.getsStory() + "\n";
			if (photoUrl != null && photoUrl.length() > 0) {
				descriptionDetail += "\n" + feed.getsPhotoPreviewName();
				descriptionDetail += "\n" + feed.getsPhotoPreviewCaption();
				descriptionDetail += "\n" + feed.getsPhotoPreviewDescription();
			}
			txv_description_detail.setText(descriptionDetail);
			//Display display = getWindowManager().getDefaultDisplay();
			//FlowTextHelper.tryFlowText(descriptionDetail, img_photo_detail, txv_description_detail, display);
		}
	}
	
	class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}
}
