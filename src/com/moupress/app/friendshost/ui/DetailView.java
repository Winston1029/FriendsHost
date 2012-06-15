package com.moupress.app.friendshost.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.moupress.app.friendshost.activity.LstViewCommentAdapter;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;
import com.moupress.app.friendshost.util.Pref;

public class DetailView extends View implements OnDrawerOpenListener, OnDrawerCloseListener{
	
	private FeedEntry feed;
	private ListView lstView_comments;
	private LstViewCommentAdapter arrAdapterComment;
	private LinearLayout drawer_comments_content;
	private InputMethodManager imm;
	
	private Activity zActivity;
	
	public  DetailView()
	{
		this.TitleLayoutId = R.layout.fh_title_bar;
		this.ContentLayoutId = R.layout.feed_item_detail;
	}
	
	@Override
	public void InitTitle(Activity activity, TitleBarListener titleBarListener) {
		super.InitTitle(activity, titleBarListener);
	    zActivity = activity;
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	public void InitContent(Activity activity, ContentViewListener contentViewListener) {
		super.InitContent(activity, contentViewListener);
		if(ContentLayoutId != -1) {
			activity.setContentView(ContentLayoutId);
		}
		InitTitleButtons(activity);
	}
	
	private void InitTitleButtons(final Activity activity) {
    	Button btnReturnMain = (Button) activity.findViewById(R.id.leftpanelbtn);
    	btnReturnMain.setBackgroundResource(android.R.drawable.ic_menu_revert);
    	btnReturnMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(android.view.View arg0) {
				activity.finish();
			}
		});
    	
    	Button btnLikes = (Button) activity.findViewById(R.id.writefeedbtn);
    	btnLikes.setBackgroundResource(android.R.drawable.btn_star_big_off);
    	btnLikes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(android.view.View v) {
				Toast.makeText(activity, "Like Button Clicked", Toast.LENGTH_SHORT).show();
				v.setBackgroundResource(android.R.drawable.btn_star_big_on);
				
				PubSub.zSnsOrg.GetSnsInstance(feed.getsFeedType()).fLikeFeeds(new Bundle());
				//PubSub.zSnsOrg.GetSnsInstance(feed.getsFeedType()).fUnLikeFeeds(new Bundle());
			}
    		
    	});
    	
    	Button btnShare = (Button) activity.findViewById(R.id.refreshbtn);
    	btnShare.setBackgroundResource(android.R.drawable.ic_menu_share);
    	btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(android.view.View v) {
				Toast.makeText(activity, "Share Button Clicked", Toast.LENGTH_SHORT).show();//fResend
				
				PubSub.zSnsOrg.GetSnsInstance(feed.getsFeedType()).fShareFeeds(new Bundle());
			}
		});	
    }

	@Override
	public void LoadView(Bundle loadData) {
		fInitFeed(loadData);
		fInitUI();
	}
	
	private void fInitFeed(Bundle loadData) {
		//Intent intent = this.getIntent();
		
		String displayedSns = loadData.getString(Const.SNS);
		String feed_id = loadData.getString(Const.FID);
		
		feed = PubSub.zFeedOrg.fGetFeedByID( displayedSns, feed_id );
		feed.setsFeedType(displayedSns);
		
		arrAdapterComment = new LstViewCommentAdapter(zActivity, R.layout.feed_item_detail_comment);
		if (feed.getzComments() != null ) {
			int size = feed.getzComments().size();
			for (int i = 0; i< size; i++) {
				arrAdapterComment.addItem(feed.getzComments().get(i));
			}
		}
		
	}

	@Override
	protected void RefreshView() {}
	
	private void fInitUI() {
		
		fInitUIBasic();
		fInitUIWebView();
		//fInitUIPhoto();
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
		TextView txv_description_detail = (TextView) zActivity.findViewById(R.id.txv_description_detail);
		txv_description_detail.setText(descriptionDetail);			
		//Display display = getWindowManager().getDefaultDisplay();
		//FlowTextHelper.tryFlowText(descriptionDetail, img_photo_detail, txv_description_detail, display);		
	}

	private void fInitUIDrawer() {
		SlidingDrawer drawer_comments = (SlidingDrawer) zActivity.findViewById(R.id.drawer_comments);
		
		drawer_comments.setOnDrawerOpenListener(this);
		drawer_comments.setOnDrawerCloseListener(this);
		
		lstView_comments = (ListView) zActivity.findViewById(R.id.lstV_detail_comment);
		lstView_comments.setVisibility(android.view.View.GONE);
		lstView_comments.setAdapter(arrAdapterComment);
		
		drawer_comments_content = (LinearLayout) zActivity.findViewById(R.id.content);
		drawer_comments_content.setVisibility(android.view.View.GONE);		
	}

	/*
	 * Use WebView to load large image
	 * Trying to make use of its default zooming and scrolling function
	 * Need to set background to be transparent 
	 * so that additional white space associated with WebView can be hidden from user
	 */
	private void fInitUIWebView() {
		String sPhotoUrl = feed.getsPhotoPreviewLink();
		String sFeedType = feed.getsFeedType();
		WebView webV_detail = (WebView) zActivity.findViewById(R.id.webV_detail);
		webV_detail.setVisibility(android.view.View.VISIBLE);
		webV_detail.setWebViewClient(new MyWebViewClient());
		if (sFeedType != null && (sFeedType.equals("blog") || sFeedType.equals("21")) ) {
			webV_detail.loadUrl(feed.getsLink());
		} else if ((sPhotoUrl != null && sPhotoUrl.startsWith("http://") && sPhotoUrl.endsWith(".jpg"))) {
			//String sLargeImgUrl = fRetrieveLargeImgUrl(feed.getsFeedType(), sPhotoUrl);
			String sLargeImgUrl = feed.getsPhotoLargeLink();
			webV_detail.getSettings().setBuiltInZoomControls(true);
			webV_detail.getSettings().setUseWideViewPort(true);
			webV_detail.getSettings().setLoadWithOverviewMode(true);
			webV_detail.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
			webV_detail.setInitialScale(1);
			webV_detail.setBackgroundColor(Color.TRANSPARENT);
			//img_large_photo_detail.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			String HTML_FORMAT = 
					"<html><head>" +
						"<style>{margin:0;padding:0;}</style>" +
						"<meta name=viewport />" +
					"</head>" +
					"<body " +
						"style=\"background-color: whilte;\">" +
						"<img src = \"%s\" />" +
					"</body></html>";
			String html = String.format(HTML_FORMAT, sLargeImgUrl);
			webV_detail.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
		} else {
			webV_detail.setVisibility(android.view.View.GONE);
		}		
	}
	
	private void fInitUIBasic() {
		imm = (InputMethodManager)zActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		WebImageView img_feeduserhead_detail = (WebImageView)zActivity.findViewById(R.id.img_userhead_detail);
		img_feeduserhead_detail.setImageUrl(feed.getzFriend().getHeadurl());
		img_feeduserhead_detail.loadImage();
		//img_feeduserhead_detail.set
		TextView txv_feedusername_detail = (TextView) zActivity.findViewById(R.id.txv_username_detail);
		txv_feedusername_detail.setText(feed.getzFriend().getName());
		
		TextView txv_createdtime_detail = (TextView)zActivity.findViewById(R.id.txv_createdtime_detail);
		txv_createdtime_detail.setText(feed.getsCreatedTime());
		
		ImageButton img_share_detail = (ImageButton) zActivity.findViewById(R.id.img_share_detail);
		img_share_detail.setVisibility(android.view.View.INVISIBLE);
//		img_share_detail.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				Toast.makeText(getBaseContext(), "Share Button Touched", Toast.LENGTH_SHORT).show();
//				return false;
//			}
//		});
//		img_share_detail.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(getBaseContext(), "Share Button Clicked", Toast.LENGTH_SHORT).show();
//				String sFeedType = feed.getsFeedType();
//				//PubSub.zSnsOrg.GetSnsInstance(sFeedType).fResend(feed);
//			}
//		});
				
	}
	
	class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	view.loadUrl(url);
	        return true;
	    }
	    
	    //not working yet !!!
	    @Override
	    public void onReceivedError  (WebView view, int errorCode, String description, String failingUrl) {
	    	if (errorCode == ERROR_FILE_NOT_FOUND) {
	    		String sPhotoUrl = feed.getsPhotoPreviewLink();
	    		String sLargeImgUrl = sPhotoUrl.replace("large", "original");
	    		String HTML_FORMAT = 
					"<html><head>" +
						"<style>{margin:0;padding:0;}</style>" +
						"<meta name=viewport />" +
					"</head>" +
					"<body " +
						"style=\"background-color: whilte;\">" +
						"<img src = \"%s\" />" +
					"</body></html>";
				String html = String.format(HTML_FORMAT, sLargeImgUrl);
				view.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
	    	}
	    }
	}

	@Override
	public void onDrawerClosed() {
		drawer_comments_content.setVisibility(android.view.View.GONE);
		lstView_comments.setVisibility(android.view.View.GONE);
	}

	@Override
	public void onDrawerOpened() {
		drawer_comments_content.setVisibility(android.view.View.VISIBLE);
		lstView_comments.setVisibility(android.view.View.VISIBLE);
		
		arrAdapterComment.notifyDataSetChanged();
		fInitMyCommentUI();
	}

	private void fInitMyCommentUI() {
		WebImageView img_selfhead_detail_comment = (WebImageView) zActivity.findViewById(R.id.img_selfhead_detail_comment);
		String selfHeadUrl = fRetrieveProfileHeadImgUrl(feed.getsFeedType());
		img_selfhead_detail_comment.setImageUrl(selfHeadUrl);
		img_selfhead_detail_comment.loadImage();
		final EditText etx_commentmsg_detail_comment = (EditText) zActivity.findViewById(R.id.etx_commentmsg_detail_comment);
		Button btn_send_detail_comment = (Button) zActivity.findViewById(R.id.btn_send_detail_comment);
		btn_send_detail_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(android.view.View v) {
				String myCommentMsg = etx_commentmsg_detail_comment.getText().toString();
				Toast.makeText(zActivity, myCommentMsg, Toast.LENGTH_SHORT).show();
				if (feed.getsFeedType().equals(Const.SNS_TWITTER)) {
					//twitter is reply 
					//not comment
					myCommentMsg = "@" + feed.getsName() + " " + myCommentMsg;
				} else if (feed.getsFeedType().equals(Const.SNS_RENREN)) {
					myCommentMsg = feed.getsOwnerID() + "%" + myCommentMsg;
				}
				PubSub.zSnsOrg.GetSnsInstance(feed.getsFeedType()).fPostComments(feed.getsID(), myCommentMsg);
				etx_commentmsg_detail_comment.setText("");
				imm.hideSoftInputFromWindow(etx_commentmsg_detail_comment.getWindowToken(), 0);
			}
		});
	}
	
	private String fRetrieveProfileHeadImgUrl(String snsName) {
		String headUrl = "";
		if (snsName.equals(Const.SNS_FACEBOOK)) {
			String login_id = Pref.getMyStringPref(zActivity.getApplicationContext(), Const.LOGIN_ID_FACEBOOK);
			headUrl = String.format(Const.USER_IMG_URL_FB, login_id);
		} else if (snsName.equals(Const.SNS_RENREN)) {
			headUrl = Pref.getMyStringPref(zActivity.getApplicationContext(), Const.LOGIN_HEAD_RENREN);
		} else if (snsName.equals(Const.SNS_SINA)) {
			headUrl = Pref.getMyStringPref(zActivity.getApplicationContext(), Const.LOGIN_HEAD_SINA);
		} else if (snsName.equals(Const.SNS_TWITTER)) {
			headUrl = Pref.getMyStringPref(zActivity.getApplicationContext(), Const.LOGIN_HEAD_TWITTER);
		}
		return headUrl;
	}
	
/*
 * Below code is to be removed if the WebView( fInitUIWebView ) is stablized
 * Below code is to use WebImageView from droid-fu to load large image and allow it to be scrollable
 * 
	private void fInitUIPhoto() {
		WebImageView img_photo_detail = (WebImageView)zActivity.findViewById(R.id.img_photo_detail);
		
		String sPhotoUrl = feed.getsPhotoPreviewLink();
		if (sPhotoUrl != null && sPhotoUrl.startsWith("http://") && sPhotoUrl.endsWith(".jpg")) {
			//sPhotoUrl = sPhotoUrl.replace("head", "large");
			img_photo_detail.setVisibility(View.VISIBLE);
			img_photo_detail.setScrollContainer(true);
			img_photo_detail.setImageUrl(sPhotoUrl);
			img_photo_detail.loadImage();
			
			DetailPhotoClickListener listener = new DetailPhotoClickListener();
			img_photo_detail.setOnClickListener(listener);
		} else {
			img_photo_detail.setVisibility(View.GONE);
		}
	}

	class DetailPhotoClickListener implements OnClickListener {

		@Override
		public void onClick(View viewClicked) {
			RelativeLayout layout_detail_view = (RelativeLayout) zActivity.findViewById(R.id.RelativeLayout_Content_ItemDetail);
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			View view = inflater.inflate(R.layout.feed_item_detail_photo, layout_detail_view);
			WebView img_large_photo_detail = (WebView)view.zActivity.findViewById(R.id.img_large_photo_detail);
			if (viewClicked instanceof WebImageView) {
				String sLargeImgUrl = ((WebImageView) viewClicked).getImageUrl().replace("head", "large");
				img_large_photo_detail.getSettings().setBuiltInZoomControls(true);
				img_large_photo_detail.getSettings().setUseWideViewPort(true);
				img_large_photo_detail.getSettings().setLoadWithOverviewMode(true);
				img_large_photo_detail.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
				img_large_photo_detail.setInitialScale(1);
				img_large_photo_detail.setBackgroundColor(Color.TRANSPARENT);
				//img_large_photo_detail.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
				String HTML_FORMAT = "<html><head><style>{margin:0;padding:0;}</style>" +
						"<meta name=viewport /></head>" +
						"<body style=\"background-color: whilte;\">" +
						"<img src = \"%s\" /></body></html>";
				String html = String.format(HTML_FORMAT, sLargeImgUrl);
				img_large_photo_detail.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
				//img_large_photo_detail.loadUrl(sLargeImgUrl);
			}
			 
			WebImageView img_large_photo_detail = (WebImageView)view.zActivity.findViewById(R.id.img_large_photo_detail);
			if (viewClicked instanceof WebImageView) {
				String sLargeImgUrl = ((WebImageView) viewClicked).getImageUrl().replace("head", "large");
				img_large_photo_detail.setImageUrl(sLargeImgUrl);
				img_large_photo_detail.loadImage();
			} 
			else {
				view.setVisibility(View.GONE);
			}
			Toast.makeText(getApplicationContext(), "Image Clicked: " + viewClicked.getId() , Toast.LENGTH_SHORT).show();
		}
		
	}
	
	class MyTouchListener implements OnTouchListener {

		float downX, downY;
        int totalX, totalY;
        int scrollByX, scrollByY;
        
		@Override
		public boolean onTouch(View v, MotionEvent event) {
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
                    v.scrollBy((int) (mx - curX), (int) (my - curY));
                    mx = curX;
                    my = curY;
                    break;
                case MotionEvent.ACTION_UP:
                    curX = event.getX();
                    curY = event.getY();
                    v.scrollBy((int) (mx - curX), (int) (my - curY));
                    break;
            }

            return true;
		}
		
	}

*/
}
