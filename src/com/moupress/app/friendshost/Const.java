package com.moupress.app.friendshost;

public class Const {

	//==============DBHelper================================
	public static final String TAG = "FriendsHost";
	public static final String SNS_FACEBOOK = "Facebook";
	public static final String SNS_RENREN = "Renren";
	public static final String SNS_SINA = "Sina";
	
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
	
	
	//=============Twitter Auth ============================
	public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow-twitter";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
	
	public static final String CONSUMER_KEY = "g6dQOeQIPrT7eXpSE7FGQ";
	public static final String CONSUMER_SECRET= "8MUOI1ShvCyj1IlUWQDlFEfFzSOjNQIugClDGLLUop4";
	
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
}
