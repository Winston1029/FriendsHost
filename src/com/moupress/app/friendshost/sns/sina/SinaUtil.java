package com.moupress.app.friendshost.sns.sina;

import java.util.List;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.RequestToken;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.util.Pref;

public class SinaUtil {

	//private static final String APP_ID = "1255140182";
	//private static final String SECRET_KEY = "ace86405a2aea9d30c5986d5465e163f";
	private static final String APP_ID = "1646212960";
    private static final String SECRET_KEY = "94098772160b6f8ffc1315374d8861f9";
    

	private static final String URL_ACTIVITY_CALLBACK = "weiboandroidsdk://TimeLineActivity";
    private static final String FROM = "xweibo";
    
    private static String sTokenKey = "";
    private static String sTokenSecret = "";
	
	private PubSub zPubSub;
	private Context zContext;
	
	private Weibo zSina;
	
	public SinaUtil(PubSub pubSub) {
		zPubSub = pubSub;
		zContext = zPubSub.fGetContext();
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		zSina = OAuthConstant.getInstance().getWeibo();
		fSinaAuth();
	}
	
	private void fSinaAuth() {
		sTokenKey = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENKEY);
		sTokenSecret = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENSECRET);
		
		if ( !sTokenKey.isEmpty() && !sTokenSecret.isEmpty()) {
			return;
		}
		try {
			RequestToken requestToken =zSina.getOAuthRequestToken("weibo4andriod://OAuthActivity");
			Uri uri = Uri.parse(requestToken.getAuthenticationURL()+ "&from=xweibo");
			OAuthConstant.getInstance().setRequestToken(requestToken);
			zPubSub.fGetActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri));
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	
	public void fGetNewsFeed() {
		sTokenKey = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENKEY);
		sTokenSecret = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENSECRET);
		
		zPubSub.fGetActivity().runOnUiThread(new Runnable () {
			@Override
			public void run() {
				//zSina.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
				zSina.setToken(sTokenKey, sTokenSecret);
				try {
					List<Status> friendsTimeline;
					friendsTimeline = zSina.getFriendsTimeline();
					ArrayAdapter<String> sinaFeedAdapter = zPubSub.fGetArrAdapterFeed();
					for (Status status : friendsTimeline) {
						String msg = status.getUser().getScreenName() + " : " + status.getText();
						sinaFeedAdapter.add(msg);
					}
					sinaFeedAdapter.notifyDataSetChanged();
				} catch (WeiboException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
