package com.moupress.app.friendshost.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.droidfu.widgets.WebImageView;
import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.util.FeedOrganisor;

public class FeedDetailViewActivity extends Activity implements OnDrawerOpenListener, OnDrawerCloseListener {

	//private FeedOrganisor zFeedOrg;
	private FeedEntry feed;
	
	private ListView lstView_comments;
	private LstViewCommentAdapter arrAdapterComment;
	private LinearLayout drawer_comments_content;
	
	private int iScreenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.feed_item_detail);
		
		iScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
		
		fInitFeed();
		fInitUI();
	}
	
	private void fInitFeed() {
		Intent intent = this.getIntent();
//		final FeedEntry feed = intent.getParcelableExtra(Const.FEED_ITEM);
//		final ArrayList<Parcelable> comments = intent.getParcelableArrayListExtra(Const.COMMENTS);
//		if (comments != null) {
//			System.out.println();
//		}
		//zFeedOrg = new FeedOrganisor(this);
		//zFeedOrg = PubSub.zFeedOrg;
		
		String displayedSns = intent.getStringExtra(Const.SNS);
		String feed_id = intent.getStringExtra(Const.FID);
		
		feed = PubSub.zFeedOrg.fGetFeedByID( displayedSns, feed_id );
		
		arrAdapterComment = new LstViewCommentAdapter(this, R.layout.feed_item_detail_comment);
//		for (FeedEntryComment comment : feed.getzComments()) {
//			arrAdapterComment.addItem(comment);
//		}
		if (feed.getzComments() != null ) {
			int size = feed.getzComments().size();
			for (int i = 0; i< size; i++) {
				arrAdapterComment.addItem(feed.getzComments().get(i));
			}
		}
		
	}

	private void fInitUI() {
		
		WebImageView img_feeduserhead_detail = (WebImageView)findViewById(R.id.img_userhead_detail);
		img_feeduserhead_detail.setImageUrl(feed.getzFriend().getHeadurl());
		img_feeduserhead_detail.loadImage();
		//img_feeduserhead_detail.set
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
		if (
				feed.getsFeedType() != null 
				&& (feed.getsFeedType().equals("blog") || feed.getsFeedType().equals("21")) 
			) {
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
		
		SlidingDrawer drawer_comments = (SlidingDrawer) findViewById(R.id.drawer_comments);
		
		drawer_comments.setOnDrawerOpenListener(this);
		drawer_comments.setOnDrawerCloseListener(this);
		
		lstView_comments = (ListView) findViewById(R.id.lstV_detail_comment);
		lstView_comments.setVisibility(View.GONE);
		lstView_comments.setAdapter(arrAdapterComment);
		
		drawer_comments_content = (LinearLayout) findViewById(R.id.content);
		drawer_comments_content.setVisibility(View.GONE);
	}
	
	class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}

	@Override
	public void onDrawerClosed() {
		drawer_comments_content.setVisibility(View.GONE);
		lstView_comments.setVisibility(View.GONE);
	}

	@Override
	public void onDrawerOpened() {
		drawer_comments_content.setVisibility(View.VISIBLE);
		lstView_comments.setVisibility(View.VISIBLE);
		
		arrAdapterComment.notifyDataSetChanged();
		fInitMyCommentUI();
	}

	private void fInitMyCommentUI() {
		WebImageView img_selfhead_detail_comment = (WebImageView) findViewById(R.id.img_selfhead_detail_comment);
		final EditText etx_commentmsg_detail_comment = (EditText) findViewById(R.id.etx_commentmsg_detail_comment);
		Button btn_send_detail_comment = (Button) findViewById(R.id.btn_send_detail_comment);
		btn_send_detail_comment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String myCommentMsg = etx_commentmsg_detail_comment.getText().toString();
				Toast.makeText(getApplicationContext(), myCommentMsg, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
