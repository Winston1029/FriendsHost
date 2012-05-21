package com.moupress.app.friendshost.uicomponent;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.droidfu.widgets.WebImageView;

public class FeedItemUIComponent {

	private WebImageView img_Head;
	private TextView txv_FeedUser;
	private TextView txv_MsgCreationTime;
	private TextView txv_MsgBody;
	private WebImageView img_PhotoPreview;
	
	private TextView txv_ImgName;
	private TextView txv_ImgCaption;
	private TextView txv_ImgDecription;
	private LinearLayout img_Layout;
	
	public WebImageView getImg_Head() {
		return img_Head;
	}
	public void setImg_Head(WebImageView img_Head) {
		this.img_Head = img_Head;
		this.img_Head.setDrawingCacheEnabled(true);
	}
	public TextView getTxv_FeedUser() {
		return txv_FeedUser;
	}
	public void setTxv_FeedUser(TextView txv_FeedUser) {
		this.txv_FeedUser = txv_FeedUser;
	}
	public TextView getTxv_MsgCreationTime() {
		return txv_MsgCreationTime;
	}
	public void setTxv_MsgCreationTime(TextView txv_MsgCreationTime) {
		this.txv_MsgCreationTime = txv_MsgCreationTime;
	}
	public TextView getTxv_MsgBody() {
		return txv_MsgBody;
	}
	public void setTxv_MsgBody(TextView txv_MsgBody) {
		this.txv_MsgBody = txv_MsgBody;
	}
	public WebImageView getImg_PhotoPreview() {
		return img_PhotoPreview;
	}
	public void setImg_PhotoPreview(WebImageView img_PhotoPreview) {
		this.img_PhotoPreview = img_PhotoPreview;
	}
	public TextView getTxv_ImgName() {
		return txv_ImgName;
	}
	public void setTxv_ImgName(TextView txv_ImgName) {
		this.txv_ImgName = txv_ImgName;
	}
	public TextView getTxv_ImgCaption() {
		return txv_ImgCaption;
	}
	public void setTxv_ImgCaption(TextView txv_ImgCaption) {
		this.txv_ImgCaption = txv_ImgCaption;
	}
	public TextView getTxv_ImgDecription() {
		return txv_ImgDecription;
	}
	public void setTxv_ImgDecription(TextView txv_ImgDecription) {
		this.txv_ImgDecription = txv_ImgDecription;
	}
	
	
	public LinearLayout getImg_Layout() {
		return img_Layout;
	}
	public void setImg_Layout(LinearLayout img_Layout) {
		this.img_Layout = img_Layout;
	}
	
	public void ImgHeadLoad(String sHeadImgSrc)
	{
		if(sHeadImgSrc != null)
		{
			img_Head.setVisibility(View.VISIBLE);
			img_Head.setImageUrl(sHeadImgSrc);
			img_Head.loadImage();
		}
	}
	
	public void FeedUserLoad(String txtFeedUser)
	{
		this.txv_FeedUser.setText(txtFeedUser);
	}
	
	public void TxtCreationTimeLoad(String txv_MsgCreationTime)
	{
		this.txv_MsgCreationTime.setText(txv_MsgCreationTime);
	}
	
	public void TxtMsgBodyLoad(String sMsgBody,String sStory )
	{
		txv_MsgBody.setVisibility(View.VISIBLE);
		if ( sMsgBody != null && sStory != null ) {
			txv_MsgBody.setText(sMsgBody + "\n" + sStory);
		} else if ( sMsgBody != null ) {
			txv_MsgBody.setText(sMsgBody);
		} else if ( sStory != null ) {
			txv_MsgBody.setText(sStory);
		} else {
			txv_MsgBody.setVisibility(View.GONE);
		}
	}
	
	public void ImgPhotoPreviewLoad(String sImgSrc)
	{
		
		if (sImgSrc != null && sImgSrc.startsWith("http://") && sImgSrc.endsWith(".jpg")) {
			img_PhotoPreview.setVisibility(View.VISIBLE);
			img_PhotoPreview.setImageUrl(sImgSrc);
			//img_PhotoPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			img_PhotoPreview.loadImage();
		} else {
			img_PhotoPreview.setVisibility(View.GONE);
		}
	}
	
	public void TxtImgNameLoad(String sImgName)
	{
		if (sImgName != null) {
			txv_ImgName.setVisibility(View.VISIBLE);
			txv_ImgName.setText(sImgName);
		} else {
			txv_ImgName.setVisibility(View.GONE);
		}
	}
	
	public void TxvImgCaptionLoad(String sImgCaption)
	{
		if (sImgCaption != null) {
			txv_ImgCaption.setVisibility(View.VISIBLE);
			txv_ImgCaption.setText(sImgCaption);
		} else {
			txv_ImgCaption.setVisibility(View.GONE);
		}
	}
	
	public void TxvImgDecription(String sImgDescription)
	{
		if (sImgDescription != null) {
			txv_ImgDecription.setVisibility(View.VISIBLE);
			txv_ImgDecription.setText(sImgDescription);
//		} else if (sMsgBody != null && sStory != null ) {
//			txv_ImgDecription.setText(sStory);
		} else {
			txv_ImgDecription.setVisibility(View.GONE);
		}
	}
}
