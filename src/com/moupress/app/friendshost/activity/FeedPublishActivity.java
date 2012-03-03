package com.moupress.app.friendshost.activity;

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
	protected Renren renren;
	private ProgressDialog progressDialog;
	private EditText editTextName;
	private EditText editTextDescription;
	private EditText editTextUrl;
	private EditText editTextImageUrl;
	private EditText editTextCaption;
	private EditText editTextMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.feed_publish_layout, null);
		
		this.setContentView(layout);
		
		Intent intent = this.getIntent();
		
		sns = intent.getStringExtra(Const.SNS);
		renren = PubSub.zRenrenUtil.getRenren();
		
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
				if(sns.equals(Const.SNS_RENREN))
				{
					RenrenPublishFeeds();
				}else if(sns.equals(Const.SNS_FACEBOOK))
				{
					FBPublishFeeds();
				}
			 }
			
			}
		);
	}
	
	private void RenrenPublishFeeds() {
		
		if (renren != null) {
			AsyncRenren asyncRenren = new AsyncRenren(renren);
			showProgress();
			String name = editTextName.getText().toString();
			String description = editTextDescription.getText()
					.toString();
			String url = editTextUrl.getText().toString();
			String imageUrl = editTextImageUrl.getText().toString();
			String caption = editTextCaption.getText().toString();
			String message = editTextMessage.getText().toString();
			FeedPublishRequestParam param = new FeedPublishRequestParam(
					name, description, url, imageUrl, caption,
					null, null, message);
			AbstractRequestListener<FeedPublishResponseBean> listener = new AbstractRequestListener<FeedPublishResponseBean>() {

				@Override
				public void onComplete(
						final FeedPublishResponseBean bean) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							//editTextLog.setText(bean.toString());
							Log.d(TAG, bean.toString());
							dismissProgress();
						}
					});
				}


				@Override
				public void onFault(final Throwable fault) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							//editTextLog.setText(fault.getMessage());
							Log.d(TAG, fault.getMessage());
							dismissProgress();
						}
					});
				}

				@Override
				public void onRenrenError(final RenrenError renrenError) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							//editTextLog.setText(renrenError.getMessage());
							Log.d(TAG, renrenError.getMessage());
							dismissProgress();
						}
					});
				}
			};
			asyncRenren.publishFeed(param, listener, true);

		}
	}
	
	private void FBPublishFeeds() {
		
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
