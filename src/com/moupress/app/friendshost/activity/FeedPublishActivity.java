package com.moupress.app.friendshost.activity;

import java.io.File;

import org.apache.commons.httpclient.cookie.IgnoreCookiesSpec;

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
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

/**
 * @author Li Ji
 *
 */
public class FeedPublishActivity extends Activity{
	
	public static final String TAG = "FeedPublishActivity";
	private String sns;
	private Activity activity;
	//private Renren renren;
	//private Facebook facebook;
	private ProgressDialog progressDialog;
	

	
	//Controls & variables to publish Photos
	private Button btnUploadPic;
	private Button btnTakePic;

	private static final int SELECT_PICTURE = 1;
	private static final int CAMERA_PIC_REQUEST = 2;
	private String selectedImagePath;
	
	
	//Parameters Caputure user's input info
	private String name;
	private String description;
	private String url;
	private String imageUrl;
	private String caption;
	private String message;
	
	private Uri mCapturedImageURI;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.feed_publish_layout, null);
		
		this.setContentView(layout);
		
		Intent intent = this.getIntent();
		
		sns = intent.getStringExtra(Const.SNS);

		selectedImagePath = "";
		
		activity= this;
		
		fInitFieldUI();
		fInitPicButtons();
		fInitPubButtons();
		
		
	}
	
	private void fInitFieldUI() {
		//Controls Capture user's input info
		ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.feed_publish_layout, null);
		EditText editTextName = (EditText) layout.findViewById(R.id.name);
		EditText editTextDescription = (EditText) layout.findViewById(R.id.description);
		EditText editTextUrl = (EditText) layout.findViewById(R.id.link);
		EditText editTextImageUrl = (EditText) layout.findViewById(R.id.image);
		EditText editTextCaption = (EditText) layout.findViewById(R.id.caption);
		EditText editTextMessage = (EditText) layout.findViewById(R.id.message);
		
		name = editTextName.getText().toString();
		description = editTextDescription.getText().toString();
		url = editTextUrl.getText().toString();
		imageUrl = editTextImageUrl.getText().toString();
		caption = editTextCaption.getText().toString();
		message = editTextMessage.getText().toString();		
	}

	public void fInitPubButtons() {
		//Facebook
		ImageButton imgBtn_Facebook = (ImageButton) this.findViewById(R.id.imgBtn_pubFacebook);
		imgBtn_Facebook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedImagePath != null && selectedImagePath.length() > 0) {
					PubSub.zFacebook.fUploadPic(message,selectedImagePath);
					selectedImagePath="";
				} else {
					PubSub.zFacebook.fPublishFeeds(name, description, url, imageUrl, caption, message);				
				}
			}
		});
		
		//Renren
		ImageButton imgBtn_Renren = (ImageButton) this.findViewById(R.id.imgBtn_pubRenren);
		imgBtn_Renren.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedImagePath != null && selectedImagePath.length() > 0) {
					PubSub.zRenrenUtil.fUploadPic(message,selectedImagePath);
					selectedImagePath="";
				} else {
					PubSub.zRenrenUtil.fPublishFeeds(name, description, url, imageUrl, caption, message);				
				}
			}
		});
		
		//Twitter
		ImageButton imgBtn_Twitter = (ImageButton) this.findViewById(R.id.imgBtn_pubTwitter);
		imgBtn_Twitter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedImagePath != null && selectedImagePath.length() > 0) {
					PubSub.zRenrenUtil.fUploadPic(message,selectedImagePath);
					selectedImagePath="";
				} else {
					PubSub.zRenrenUtil.fPublishFeeds(name, description, url, imageUrl, caption, message);
				}
			}
		});
	}
	
	public void fInitPicButtons() {
		ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.feed_publish_layout, null);
		
//		Button publishButton = (Button) layout.findViewById(R.id.publish);
//		publishButton.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				name = editTextName.getText().toString();
//				description = editTextDescription.getText().toString();
//				url = editTextUrl.getText().toString();
//				imageUrl = editTextImageUrl.getText().toString();
//				caption = editTextCaption.getText().toString();
//				message = editTextMessage.getText().toString();
//				
//				if(selectedImagePath != null && selectedImagePath.length() > 0)
//				{
//					uploadPhoto();
//					selectedImagePath="";
//				}
//				else 
//				{
//					publishFeed();
//				}
//				
//				activity.finish();
//			 }
//			
//			}
//		);
		
		
		//Functions of Photos
		btnUploadPic = (Button) layout.findViewById(R.id.updPic);
		btnUploadPic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
	            intent.setType("image/*");
	            intent.setAction(Intent.ACTION_GET_CONTENT);
	            startActivityForResult(Intent.createChooser(intent,
	                    "Select Picture"), SELECT_PICTURE);
				
			}

		});
		
		btnTakePic = (Button) layout.findViewById(R.id.tkePic);
		btnTakePic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//takePhoto();
				 String fileName = "temp.jpg";  
			     ContentValues values = new ContentValues();  
			     values.put(MediaStore.Images.Media.TITLE, fileName);  
			     mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  

				 Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				 cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
				 startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
			}

		});
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK ) {
	        if (requestCode == SELECT_PICTURE) {
	            Uri selectedImageUri = data.getData();
	            selectedImagePath = getPath(selectedImageUri);
	            Log.d(TAG, "Image Selection Path is " + selectedImagePath);
	            
	        }else if(requestCode == CAMERA_PIC_REQUEST)
	        {
	        	//pic = (Bitmap) data.getExtras().get("data");
	        	String[] projection = { MediaStore.Images.Media.DATA}; 
	            Cursor cursor = managedQuery(mCapturedImageURI, projection, null, null, null); 
	            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
	            cursor.moveToFirst(); 
	             selectedImagePath = cursor.getString(column_index_data);
	        	Log.d(TAG, "Camera is capturing pics!");
	        }
	    }
	}
	
	public String getPath(Uri uri) {
	    String[] projection = { MediaStore.Images.Media.DATA };
	    Cursor cursor = managedQuery(uri, projection, null, null, null);
	    int column_index = cursor
	            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	
	/*
	private void publishFeed()
	{
		if(sns.equals(Const.SNS_RENREN))
		{
			PubSub.zRenrenUtil.fPublishFeeds(name, description, url, imageUrl, caption, message);
			
		}
			else if(sns.equals(Const.SNS_FACEBOOK))
		{
			PubSub.zFacebook.fPublishFeeds(name, description, url, imageUrl, caption, message);
		}
	}
	private void uploadPhoto() {
		
		if(sns.equals(Const.SNS_RENREN))
		{
		   PubSub.zRenrenUtil.fUploadPic(message,selectedImagePath);
			
		}else if(sns.equals(Const.SNS_FACEBOOK))
		{
			PubSub.zFacebook.fUploadPic(message,selectedImagePath);
		}
	}
	
	private void takePhoto() {
		
	}
	 */
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
