package com.moupress.app.friendshost.activity;

import com.github.droidfu.widgets.WebImageView;
import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.FeedItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FeedResendActivity extends Activity{
	
	private final String TAG = "FeedResendActivity";
	private Activity zActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.feed_resend_layout);
		zActivity = this;
		initUI();
	}

	private void initUI() {
		Intent intent = this.getIntent();
		final FeedItem feed = intent.getParcelableExtra(Const.FEED_ITEM);
		final String displayedSns = intent.getStringExtra(Const.SNS);
		loadFeed(feed);
		Log.i(TAG, " Name: "+feed.getsName()+" Body: "+ feed.getsMsgBody()+" Friend "+feed.getzFriend().getHeadurl());
		
		final Intent intentBack = new Intent();
		
		Button resendBtn = (Button) this.findViewById(R.id.btn_resend_asis);
		resendBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
				feed.setsMsgBody("From "+feed.getsName()+" : "+feed.getsMsgBody());
				intentBack.putExtra(Const.SNS, displayedSns);
				intentBack.putExtra(Const.FEED_ITEM, feed);
				zActivity.setResult(RESULT_OK, intentBack);
				zActivity.finish();
			}});
		
		Button cmtAsIsBtn = (Button) this.findViewById(R.id.btn_resend_cmtadd);
		cmtAsIsBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
			}});
		
		Button cancelBtn = (Button) this.findViewById(R.id.btn_resend_cancel);
		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Cancel Btn is clicked!");
				//zActivity.finishActivity(Const.FEED_RESEND_REQ_CD);
				zActivity.setResult(RESULT_CANCELED, intentBack);
				zActivity.finish();
			}});
	}

	private void loadFeed(FeedItem feed) {
		WebImageView img_Head = (WebImageView) this.findViewById(R.id.img_feeduser);
		String sHeadImgSrc = feed.getzFriend().getHeadurl();
		
		if(sHeadImgSrc != null)
		{
			img_Head.setImageUrl(sHeadImgSrc);
			img_Head.loadImage();
		}
		
		TextView txv_FeedUser = (TextView) this.findViewById(R.id.txt_name);
		txv_FeedUser.setText(feed.getsName());
		TextView txv_MsgCreationTime = (TextView) this.findViewById(R.id.txt_msgcreatedtime);
		String sCreateTime = feed.getsCreatedTime();
		txv_MsgCreationTime.setText(sCreateTime);
		
		String sMsgBody = feed.getsMsgBody();
		String sStory = feed.getsStory();
		TextView txv_MsgBody = (TextView) this.findViewById(R.id.txt_msgbody);
		
		if ( sMsgBody != null && sStory != null ) {
			txv_MsgBody.setText(sMsgBody + "\n" + sStory);
		} else if ( sMsgBody != null ) {
			txv_MsgBody.setText(sMsgBody);
		} else if ( sStory != null ) {
			txv_MsgBody.setText(sStory);
		} else {
			txv_MsgBody.setVisibility(View.GONE);
		}
		
		WebImageView img_PhotoPreview = (WebImageView) this.findViewById(R.id.img_photopreview);
		String sImgSrc = feed.getsPhotoPreviewLink();
		
		if (sImgSrc != null && sImgSrc.startsWith("http://") && sImgSrc.endsWith(".jpg")) {
			img_PhotoPreview.setImageUrl(sImgSrc);
			//img_PhotoPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			img_PhotoPreview.loadImage();
		} else {
			img_PhotoPreview.setVisibility(View.GONE);
		}
		
		String sImgName = feed.getsPhotoPreviewName();
		String sImgCaption = feed.getsPhotoPreviewCaption();
		String sImgDescription = feed.getsPhotoPreviewDescription();
		
		TextView txv_ImgName = (TextView) this.findViewById(R.id.txv_imgName);
		TextView txv_ImgCaption = (TextView) this.findViewById(R.id.txv_imgCaption);
		TextView txv_ImgDecription = (TextView) this.findViewById(R.id.txv_imgdescription);
		
		if (sImgName != null) {
			txv_ImgName.setText(sImgName);
		} else {
			txv_ImgName.setVisibility(View.GONE);
		}
		if (sImgCaption != null) {
			txv_ImgCaption.setText(sImgCaption);
		} else {
			txv_ImgCaption.setVisibility(View.GONE);
		}
		if (sImgDescription != null) {
			txv_ImgDecription.setText(sImgDescription);
//		} else if (sMsgBody != null && sStory != null ) {
//			txv_ImgDecription.setText(sStory);
		} else {
			txv_ImgDecription.setVisibility(View.GONE);
		}
	}
}
