package com.moupress.app.friendshost.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.moupress.app.friendshost.ui.DetailView;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;

public class FeedDetailViewActivity extends Activity implements OnDrawerOpenListener, OnDrawerCloseListener {

	//private FeedOrganisor zFeedOrg;
	private FeedEntry feed;
	private DetailView detailView;
	
	private ListView lstView_comments;
	private LstViewCommentAdapter arrAdapterComment;
	private LinearLayout drawer_comments_content;
	
	private int iScreenHeight;
	private int iScreenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.feed_item_detail);
		
		//fInitDetailView();
		
		iScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
		iScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
		
		fInitFeed();
		fInitUI();
	}
	
	private void fInitDetailView() {
		detailView = new DetailView();
		detailView.InitTitle(this, titleBarListener);
		detailView.InitContent(this, contentViewListener);
		
	}
	
	ContentViewListener contentViewListener = new ContentViewListener()
	{
		
	};
	
	TitleBarListener titleBarListener = new TitleBarListener()
	{

		@Override
		public void OnTitleBarButtonClick(String viewName, int BtnId, String snsName) {
			// TODO Auto-generated method stub
			
		}
		
	};

	private void fInitFeed() {
		Intent intent = this.getIntent();
		
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
		
		fInitUIBasic();
		fInitUIWebView();
		fInitUIPhoto();
		fInitUIDescription();
		fInitUIDrawer();
		
		
	}
	
	private void fInitUIDescription() {
		String descriptionDetail = null;
		String sMsgBody = feed.getsMsgBody() ;
		String sStory = feed.getsStory();
		if ( sMsgBody  != null && sStory != null
				//if first 4 chars are the same, means duplicate message display Story Only
				//specially cater for Renren feed structure
				&& sMsgBody.length() >3 && sStory.length() > 3
				&& sMsgBody.substring(0, 4).compareToIgnoreCase(sStory.substring(0, 4)) != 0) {
			descriptionDetail = sMsgBody + "\n" + sStory;
		}  else if ( sStory != null ) {
			descriptionDetail = sStory;
		} else if ( sMsgBody != null ) {
			descriptionDetail = sMsgBody;
		} 
		if (feed.getsPhotoPreviewName() != null) {
			descriptionDetail += "\n" + feed.getsPhotoPreviewName();
		}
		if (feed.getsPhotoPreviewCaption() != null) {
			descriptionDetail += "\n" + feed.getsPhotoPreviewCaption();
		}
		if (feed.getsPhotoPreviewDescription() != null ) {
			descriptionDetail += "\n" + feed.getsPhotoPreviewDescription();
		}
		TextView txv_description_detail = (TextView) findViewById(R.id.txv_description_detail);
		if (webV_detail.getVisibility() == View.VISIBLE){
			txv_description_detail = (TextView) findViewById(R.id.txv_description_detail);
		} else {
			txv_description_detail.setText(descriptionDetail);			
		}
		//Display display = getWindowManager().getDefaultDisplay();
		//FlowTextHelper.tryFlowText(descriptionDetail, img_photo_detail, txv_description_detail, display);		
	}

	private void fInitUIPhoto() {
		WebImageView img_photo_detail = (WebImageView)findViewById(R.id.img_photo_detail);
		
		String sPhotoUrl = feed.getsPhotoPreviewLink();
		if (sPhotoUrl != null && sPhotoUrl.startsWith("http://") && sPhotoUrl.endsWith(".jpg")) {
			//sPhotoUrl = sPhotoUrl.replace("head", "large");
			img_photo_detail.setVisibility(View.VISIBLE);
			img_photo_detail.setImageUrl(sPhotoUrl);
			img_photo_detail.loadImage();
			
			DetailPhotoClickListener listener = new DetailPhotoClickListener();
			img_photo_detail.setOnClickListener(listener);
		} else {
			img_photo_detail.setVisibility(View.GONE);
		}
	}

	private void fInitUIDrawer() {
		SlidingDrawer drawer_comments = (SlidingDrawer) findViewById(R.id.drawer_comments);
		
		drawer_comments.setOnDrawerOpenListener(this);
		drawer_comments.setOnDrawerCloseListener(this);
		
		lstView_comments = (ListView) findViewById(R.id.lstV_detail_comment);
		lstView_comments.setVisibility(View.GONE);
		lstView_comments.setAdapter(arrAdapterComment);
		
		drawer_comments_content = (LinearLayout) findViewById(R.id.content);
		drawer_comments_content.setVisibility(View.GONE);		
	}

	WebView webV_detail = null;
	private void fInitUIWebView() {
		webV_detail = (WebView) findViewById(R.id.webV_detail);
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
	}

	private void fInitUIBasic() {
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
				
	}

	class DetailPhotoClickListener implements OnClickListener {

		@Override
		public void onClick(View viewClicked) {
			RelativeLayout layout_detail_view = (RelativeLayout) findViewById(R.id.RelativeLayout_Content_ItemDetail);
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			View view = inflater.inflate(R.layout.feed_item_detail_photo, layout_detail_view);
			WebView img_large_photo_detail = (WebView)view.findViewById(R.id.img_large_photo_detail);
			if (viewClicked instanceof WebImageView) {
				String sLargeImgUrl = ((WebImageView) viewClicked).getImageUrl().replace("head", "large");
				img_large_photo_detail.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
				img_large_photo_detail.getSettings().setBuiltInZoomControls(true);
				img_large_photo_detail.getSettings().setUseWideViewPort(true);
				img_large_photo_detail.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
				img_large_photo_detail.loadUrl(sLargeImgUrl);
			}
/*
			 * 
			 
			final WebImageView img_large_photo_detail = (WebImageView)view.findViewById(R.id.img_large_photo_detail);
			if (viewClicked instanceof WebImageView) {
				String sLargeImgUrl = ((WebImageView) viewClicked).getImageUrl().replace("head", "large");
				img_large_photo_detail.setImageUrl(sLargeImgUrl);
				img_large_photo_detail.loadImage();
				// set maximum scroll amount (based on center of image)
			    int maxX = (int)((img_large_photo_detail.getWidth() / 2) - (iScreenWidth / 2));
			    int maxY = (int)((img_large_photo_detail.getHeight() / 2) - (iScreenHeight / 2));

			    // set scroll limits
			    final int maxLeft = (maxX * -1);
			    final int maxRight = maxX;
			    final int maxTop = (maxY * -1);
			    final int maxBottom = maxY;
				img_large_photo_detail.setOnTouchListener(new View.OnTouchListener() {

			        float downX, downY;
			        int totalX, totalY;
			        int scrollByX, scrollByY;
			        public boolean onTouch(View view, MotionEvent event)
			        {
			        	float curX, curY;
			        	float mx = 0, my = 0;
			            switch (event.getAction()) {

			                case MotionEvent.ACTION_DOWN:
			                    mx = event.getX();
			                    my = event.getY();
			                    break;
			                case MotionEvent.ACTION_MOVE:
			                    curX = event.getX();
			                    curY = event.getY();
			                    img_large_photo_detail.scrollBy((int) (mx - curX), (int) (my - curY));
			                    mx = curX;
			                    my = curY;
			                    break;
			                case MotionEvent.ACTION_UP:
			                    curX = event.getX();
			                    curY = event.getY();
			                    img_large_photo_detail.scrollBy((int) (mx - curX), (int) (my - curY));
			                    break;
			            }

			            return true;
			        }
			    });
//				img_large_photo_detail.getSettings().setBuiltInZoomControls(true);
//				img_large_photo_detail.getSettings().setUseWideViewPort(true);
//				img_large_photo_detail.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
 * */
				
			else {
				view.setVisibility(View.GONE);
			}
			Toast.makeText(getApplicationContext(), "Image Clicked: " + viewClicked.getId() , Toast.LENGTH_SHORT).show();
//			
		}
		
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
