package com.moupress.app.friendshost;

public class Const {

	//==============DBHelper================================
	public static final String TAG = "FriendsHost";
	public static final String SNS_FACEBOOK = "Facebook";
	public static final String SNS_RENREN = "Renren";
	public static final String SNS_SINA = "Sina";
	public static final String SNS_TWITTER="Twitter";
	
	public static final String SNS = "sns";
	
	//==============DBHelper================================
	public static final String SP_SINA_TOKENKEY = "SinaTokeyKey";
	public static final String SP_SINA_TOKENSECRET = "SinaTokeySecret";
	
	//==============Intent===================================
	public static final String ACTION_DISPLAYFEED = "DisplayFeed";

	//==============Notification============================
	public static final int FRIENDSHOST_NOTIFY_ID = 1;
	
	//=============UploadPhoto Method=======================
	public static enum photoLoc {Galary, Shot};
	
	//=============Sina Auth=============================
	public static final String SINA_AUTH = "weibo4andriod";
	
	//=============Twitter Auth ============================
	public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow-twitter";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
	
	public static final String CONSUMER_KEY = "g6dQOeQIPrT7eXpSE7FGQ";
	public static final String CONSUMER_SECRET= "8MUOI1ShvCyj1IlUWQDlFEfFzSOjNQIugClDGLLUop4";
	
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
	
	//=============FeedResendActivity===========================
	public static final int FEED_RESEND_REQ_CD = 1;
	
	//==============Feed Item Attributes========================
	public static final String FEED_ITEM = "feeditem";
	
	public static final String SHEADIMG = "sHeadImg";
	public static final String SNAME = "sName";
	public static final String SOWNERID = "sOwnerID";
	public static final String SCREATEDTIME = "sCreatedTime";
	public static final String SMSGBODY = "sMsgBody";
	public static final String SSTORY = "sStory";
	public static final String SSTORY_TAGS = "sStory_Tags";
	public static final String SPHOTOPREVIEWLINK = "sPhotoPreviewLink";
	public static final String SPHOTOPREVIEWNAME = "sPhotoPreviewName";
	public static final String SPHOTOPREVIEWCAPTION = "sPhotoPreviewCaption";
	public static final String SPHOTOPREVIEWDESCRIPTION = "sPhotoPreviewDescription";

	//=============Friend Item Attributes==========================
	public static final String FID = "fId";
	public static final String FNAME = "fName";
	public static final String FHEADURL = "fHeadurl";
	public static final String FSNS = "fSns";
	

}
