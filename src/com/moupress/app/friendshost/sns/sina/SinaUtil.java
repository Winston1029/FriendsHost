package com.moupress.app.friendshost.sns.sina;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.moupress.app.friendshost.PubSub;
import com.weibo.net.*;

public class SinaUtil {
//1255140182
	private static final String APP_ID = "1255140182";
	private static final String SECRET_KEY = "ace86405a2aea9d30c5986d5465e163f";
	//private static final String APP_ID = "1646212960";
    //private static final String SECRET_KEY = "94098772160b6f8ffc1315374d8861f9";
	
	private PubSub zPubSub;
	private Context zContext;
	
	private Weibo zSina;
	
	public SinaUtil(PubSub pubSub) {
		zPubSub = pubSub;
		zContext = zPubSub.fGetContext();
		//zSina = Weibo.getInstance();
		//if ( !zSina.isSessionValid()) {
			fSinaWeiboAuth();
		//}
	}

	private void fSinaWeiboAuth() {
		
//		zSina.setupConsumerConfig(APP_ID, SECRET_KEY);
//		Weibo.setSERVER("https://api.weibo.com/2/");
//        try {
//			Oauth2AccessToken at =
//				zSina.getOauth2AccessToken(zContext, Weibo.getAppKey(), Weibo.getAppSecret(), "mondaybird66@sina.cn", "bao1bao1");
//		} catch (WeiboException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        // Oauth2.0 隐式授权认证方式
//		zSina.setRedirectUrl("http://www.sina.com");
//		zSina.authorize(zPubSub.fGetActivity(), new WeiboDialogListener() {
//			@Override
//			public void onComplete(Bundle values) {
//				String token = values.getString("access_token");
//	            String expires_in = values.getString("expires_in");
//	            AccessToken accessToken = new AccessToken(token, SECRET_KEY);
//	            accessToken.setExpiresIn(expires_in);
//	            Weibo.getInstance().setAccessToken(accessToken);
//	            
//	            Toast.makeText(zContext, "Sina Auth Complete", Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onWeiboException(WeiboException e) {
//				Toast.makeText(zContext, "Sina Auth Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onError(DialogError e) {
//				Toast.makeText(zContext, "Sina onError: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onCancel() {
//				Toast.makeText(zContext, "Sina onCancel", Toast.LENGTH_SHORT).show();
//			}
//		});
	}
	
	public void fGetNewsFeed() {
		String requestURL = Weibo.SERVER + "statuses/home_timeline.json";
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Weibo.getAppKey());
		String result = "";
		try {
			result = zSina.request(zContext, requestURL, bundle, "GET", zSina.getAccessToken());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		System.out.println("Sina result = " + result);
	}
	
	public void onDestroy() {
		Utility.clearCookies(zContext);
	}
}
