package com.moupress.app.friendshost;

public class Const {

	//==============DBHelper================================
	public static final String TAG = "FriendsHost";
	public static final String SNS_FACEBOOK = "Facebook";
	public static final String SNS_RENREN = "Renren";
	public static final String SNS_SINA = "Sina";
	public static final String SNS_TWITTER="Twitter";
	
	public static final String USER_IMG_URL_FB = "https://graph.facebook.com/%s/picture";
	public static final String LOGIN_ID_FACEBOOK = "Login_ID_Facebook";
	public static final String LOGIN_HEAD_RENREN = "Login_Head_Renren";
	public static final String LOGIN_HEAD_SINA = "Login_Head_Sina";
	public static final String LOGIN_HEAD_TWITTER = "Login_Head_Twitter";
	
	public static final String SNS = "sns";
	
	public static final String SNS_SIGN_ON = "SnsSignOn";
	
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
	
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
	
	//=============Activity Request Code===========================
	public static final int FEED_RESEND_REQ_CD = 3;
	public static final int FEED_PUBLISH = 2;
	public static final int FEED_DISPLAY_DETAIL = 1;
	public static final int FEED_DISPLAY_PREVIEW = 0;
	public static final int FEED_DETAIL_RETURN_MAIN = 2000;
	
	//==============Feed Item Attributes========================
	public static final String FEED_ITEM = "feeditem";
	public static final String SFEEDID = "feedid";
	public static final String SHEADIMG = "sHeadImg";
	public static final String SNAME = "sName";
	public static final String SOWNERID = "sOwnerID";
	public static final String SCREATEDTIME = "sCreatedTime";
	public static final String SFEEDTYPE = "sFeedType";
	public static final String SMSGBODY = "sMsgBody";
	public static final String SLINK = "sLink";
	public static final String SRESOURCEID = "sResource_id";
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
	
	//=============Comment Item Attributes==========================
	public static final String COMMENTED_FEEDID = "commetedfeedID";
	public static final String COMMENTED_ID = "commentedID";
	public static final String COMMENTED_TIME = "commentedTime";
	public static final String COMMENTED_MSG = "commentedMsg";
	public static final String COMMENTED_USERID = "commentedUserID";
	public static final String COMMENTED_NAME = "commentedName";
	public static final String COMMENTED_HEADURL = "commentedHeadUrl";
	public static final String COMMENTS = "comments";
	public static final String FRIEND = "friend";
	
	//=============Login Setting======================================
	public static final String SETTING_REQ_KEY = "SettingReqKey";
	public static final String SETTING_ACNT = "SignOn";
	public static final String[] SNSGROUPS = new String[] { SNS_FACEBOOK, SNS_RENREN, SNS_SINA, SNS_TWITTER};
	
	public static final String SETTING_BASIC = "Settings";
	public static final String[] SETTING_BASIC_GROUPS = {"Schedule"};
	//public static final String[][] SETTING_UPT_FREQ = {{"Never","0"},{"30mins","1800"},{"2hours","7200"},{"6hours","21600"},{"1day","86400"}};
	public static final String[] SETTING_UPT_FREQ_BTN_TEXT = {"Never","15mins","1hour","4hours", "12hours"};
	public static final int[] SETTING_UPT_FREQ_BTN_ID = {R.id.duration1,R.id.duration2, R.id.duration3, R.id.duration4, R.id.duration5};
	public static final int[] SETTING_UPT_FREQ_BTN_TXT_ID = {R.id.radtioTxt1,R.id.radtioTxt2,R.id.radtioTxt3, R.id.radtioTxt4,R.id.radtioTxt5};
	public static final int[] SETTING_UPT_FREQ_DURATION = {0,900,3600,14400,57600,172800};
	
	public static final String SETTING_FEEDBACKS = "FeedBacks";
	public static final String[] SETTING_FEEDBACKS_GROUPS = { "FeedBacks", "Rate It","Help"};
	
	
	//============Views Name =======================================
	public static final String VIEW_MAIN = "MainUIView";
	public static final String HTTP_METHOD_POST = "POST";
	public static final String HTTP_METHOD_DELETE = "DELETE";

	  //=============Dialog View======================================
	public static final String DIALOG_VIEW_ID = "DialogViewId";
	public static final int CD_REQ_DIALOG = 1;
	public static final String DIALOG_THEME_ID = "DialogThemeId";  
	
	
	//============Service Message===================================
	public static final int SERVICE_REFRESH_FEED = 1;
	public static final int SERVICE_UPDATE_FREQ = 2;

	
	//============Initial Guide=====================================
	public static final String VIEW_GUIDE = "ViewGuide"; 
}
