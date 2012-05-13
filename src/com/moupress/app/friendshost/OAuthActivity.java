package com.moupress.app.friendshost;

import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.moupress.app.friendshost.sns.sina.OAuthConstant;
import com.moupress.app.friendshost.sns.twitter.TwitterUtil;
import com.moupress.app.friendshost.util.Pref;

public class OAuthActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.timeline);
		Uri uri=this.getIntent().getData();
		
		if(uri != null && uri.getScheme().equals(Const.OAUTH_CALLBACK_SCHEME))
		{
			//Twitter Call Back trigger
			//PubSub.zTwitterUtil.CallBackTrigger(uri, 0, 0, null);
			((TwitterUtil)PubSub.zSnsOrg.GetSnsInstance(Const.SNS_TWITTER)).CallBackTrigger(uri, 0, 0, null);
		}
		else if(uri != null && uri.getScheme().equals(Const.SINA_AUTH))
		{
			try {
				RequestToken requestToken= OAuthConstant.getInstance().getRequestToken();
				AccessToken accessToken=requestToken.getAccessToken(uri.getQueryParameter("oauth_verifier"));
				OAuthConstant.getInstance().setAccessToken(accessToken);
				Pref.setMyStringPref(getApplicationContext(), Const.SP_SINA_TOKENKEY, accessToken.getToken());
				Pref.setMyStringPref(getApplicationContext(), Const.SP_SINA_TOKENSECRET, accessToken.getTokenSecret());
				startActivity(new Intent(this, FriendsHostActivity.class));
				//TextView textView = (TextView) findViewById(R.id.TextView01);
				//textView.setText("得到AccessToken的key和Secret,可以使用这两个参数进行授权登录了.\n Access token:\n"+accessToken.getToken()+"\n Access token secret:\n"+accessToken.getTokenSecret());
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		}
		
//		Button button=  (Button) findViewById(R.id.Button01);
//		button.setText("显示FriendTimeline");
//		button.setOnClickListener(new Button.OnClickListener()
//        {
//
//            @Override
//            public void onClick( View v )
//            {
//    				Weibo weibo=OAuthConstant.getInstance().getWeibo();
//    				weibo.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
//    				List<Status> friendsTimeline;
//    					try {
//							friendsTimeline = weibo.getFriendsTimeline();
//							StringBuilder stringBuilder = new StringBuilder("1");
//	    					for (Status status : friendsTimeline) {
//	    						stringBuilder.append(status.getUser().getScreenName() + "说:"
//	    								+ status.getText() + "\n");
//	    					}
//	    					TextView textView = (TextView) findViewById(R.id.TextView01);
//	    					textView.setText(stringBuilder.toString());
//						} catch (WeiboException e) {
//							e.printStackTrace();
//						}
//    					
//            }
//        } );
		
	}
}
