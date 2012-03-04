package com.moupress.app.friendshost.activity;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.feed.FeedPublishRequestParam;
import com.renren.api.connect.android.feed.FeedPublishResponseBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

/**
 * @author Li Ji
 *
 */
public class FeedPublishActivity extends Activity{
	
	public static final String TAG = "FeedPublishActivity";
	private String sns;
	//private Renren renren;
	//private Facebook facebook;
	private ProgressDialog progressDialog;
	
	//Controls Capture user's input info
	private EditText editTextName;
	private EditText editTextDescription;
	private EditText editTextUrl;
	private EditText editTextImageUrl;
	private EditText editTextCaption;
	private EditText editTextMessage;
	
	//Parameters Caputure user's input info
	private String name;
	private String description;
	private String url;
	private String imageUrl;
	private String caption;
	private String message;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.feed_publish_layout, null);
		
		this.setContentView(layout);
		
		Intent intent = this.getIntent();
		
		sns = intent.getStringExtra(Const.SNS);
		//renren = PubSub.zRenrenUtil.GetRenren();
		//facebook = PubSub.zFacebook.GetFBObject();
		
		editTextName = (EditText) layout.findViewById(R.id.name);
		editTextDescription = (EditText) layout.findViewById(R.id.description);
		editTextUrl = (EditText) layout.findViewById(R.id.link);
		editTextImageUrl = (EditText) layout.findViewById(R.id.image);
		editTextCaption = (EditText) layout.findViewById(R.id.caption);
		editTextMessage = (EditText) layout.findViewById(R.id.message);
		
		Button publishButton = (Button) layout.findViewById(R.id.publish);
		publishButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				name = editTextName.getText().toString();
				description = editTextDescription.getText().toString();
				url = editTextUrl.getText().toString();
				imageUrl = editTextImageUrl.getText().toString();
				caption = editTextCaption.getText().toString();
				message = editTextMessage.getText().toString();
				
				if(sns.equals(Const.SNS_RENREN))
				{
					PubSub.zRenrenUtil.fPublishFeeds(name, description, url, imageUrl, caption, message);
					
				}
					else if(sns.equals(Const.SNS_FACEBOOK))
				{
					FBPublishFeeds();
				}
			 }
			
			}
		);
	}
	
	
	
	private void FBPublishFeeds() {
		
//		if(facebook != null)
//		{
//			AsyncFacebookRunner asyncFB = new AsyncFacebookRunner(facebook);
//			showProgress();
//			
//		}
	}
	

	protected void showProgress() {
		showProgress("Please wait", "progressing");
	}

	
	protected void showProgress(String title, String message) {
		progressDialog = ProgressDialog.show(this, title, message);
	}

	protected void dismissProgress() {
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {

			}
		}
	}

}
