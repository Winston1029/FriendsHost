package com.moupress.app.friendshost.activity;

import java.io.File;

import org.apache.commons.httpclient.cookie.IgnoreCookiesSpec;

import twitter4j.TwitterException;

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
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * @author Li Ji
 *
 */
public class FeedPublishActivity extends Activity{
	
	public static final String TAG = "FeedPublishActivity";
	private String sns;
	private Activity activity;
	private ProgressDialog progressDialog;
	
	//Sns Boolean Selected Indicator
	private boolean FBSelected = false;
	private boolean RRSelected = false;
	private boolean TWSelected = false;
	
	//Controls & variables to publish Photos
	private Button btnUploadPic;
	private Button btnTakePic;

	private static final int SELECT_PICTURE = 1;
	private static final int CAMERA_PIC_REQUEST = 2;
	private String selectedImagePath;
	
	//Message to publish
	private String message;
	private String title;
	private String bodyText;
	
	//Button to publish msg
	private Button publishButton;
	
	//Uri of captured Image
	private Uri mCapturedImageURI;
	
	//Notification & Handler;
	private Handler mNotificationHandler = new Handler();
	private Runnable mNotification; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.feed_publish_layout, null);
		this.setContentView(layout);

		selectedImagePath = "";
		activity= this;
		
	
		
		fGetIntent();
		fInitFieldUI();
		fInitPicButtons();
		fInitPubButtons();
	}
	
	private void fGetIntent() {
		
		Intent intent = activity.getIntent();
		
		title = null;
		bodyText = null;
		
		Bundle extras = intent.getExtras();
		title = extras.getString(Intent.EXTRA_SUBJECT);
		bodyText = extras.getString(Intent.EXTRA_TEXT);
		
	}

	private void fInitFieldUI() {
		//Controls Capture user's input info
		//ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.feed_publish_layout, null);
		final EditText editTextMessage = (EditText) this.findViewById(R.id.message);
		
		if(title != null||bodyText != null)
		{
			editTextMessage.setText(title+" - "+ bodyText);
		}
		
		publishButton = (Button) this.findViewById(R.id.publish);
		publishButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.i(TAG, "Publish Button is Clicked!");
				message = editTextMessage.getText().toString();	
				if(selectedImagePath != null && selectedImagePath.length() > 0)
				{
					uploadPhoto();
					selectedImagePath="";
				}
				else 
				{
					publishFeed();
				}
				activity.finish();
			}
		});
	}

	//Select or Unselect Sns Btn
	private void toggleBtnSelected(ImageButton btn)
	{
		if (btn.isSelected()){
			btn.setSelected(false);
	       } else {
	    	  btn.setSelected(true);
	       }
	}
	
	//Pop up Toast 
	
	private void popUpToast(String snsSelected, boolean Selected)
	{
		final String text = snsSelected + " is "+ (Selected?"selected":"unselected");
		mNotification = new Runnable(){
			@Override
			public void run() {
				 Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
				}
			};
			
		this.mNotificationHandler.post(mNotification);
	}
	
	//Initialize Buttons on UI
	public void fInitPubButtons() {
		//Facebook
		final ImageButton imgBtn_Facebook = (ImageButton) this.findViewById(R.id.imgBtn_pubFacebook);
		imgBtn_Facebook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleBtnSelected(imgBtn_Facebook);
				FBSelected = imgBtn_Facebook.isSelected();
				popUpToast("Facebook",FBSelected);
			}
		});
		
		//Renren
		final ImageButton imgBtn_Renren = (ImageButton) this.findViewById(R.id.imgBtn_pubRenren);
		imgBtn_Renren.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleBtnSelected(imgBtn_Renren);
				RRSelected = imgBtn_Renren.isSelected();
				popUpToast("RenRen",RRSelected);
			}
		});
		
		//Twitter
		final ImageButton imgBtn_Twitter = (ImageButton) this.findViewById(R.id.imgBtn_pubTwitter);
		imgBtn_Twitter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleBtnSelected(imgBtn_Twitter);
				TWSelected = imgBtn_Twitter.isSelected();
				popUpToast("Twitter",TWSelected);
			}
		});
	}
	
	public void fInitPicButtons() {
		ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.feed_publish_layout, null);
		
		//Functions of Photos
		btnUploadPic = (Button) this.findViewById(R.id.updPic);
		btnUploadPic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG, "Upload Pictures");
				Intent intent = new Intent();
	            intent.setType("image/*");
	            intent.setAction(Intent.ACTION_GET_CONTENT);
	            startActivityForResult(Intent.createChooser(intent,
	                    "Select Picture"), SELECT_PICTURE);
			}
		});
		
		btnTakePic = (Button) this.findViewById(R.id.tkePic);
		btnTakePic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Log.i(TAG, "Take Photo Button is Clicked! ");
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
	            
	        }
	        	else if(requestCode == CAMERA_PIC_REQUEST)
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
	
	
	private void publishFeed()
	{
		if(this.RRSelected)
		{
			PubSub.zRenrenUtil.fPublishFeeds(" ", " ", " ", " ", " ", message);
			
		}
	    if(this.FBSelected)
		{
			PubSub.zFacebook.fPublishFeeds(null, null, null, null, null, message);
		}
	    if(this.TWSelected)
	    {
	    	PubSub.zTwitterUtil.SendFeed(message);
	    }
	}
	private void uploadPhoto() {
		
		if(this.RRSelected)
		{
		   PubSub.zRenrenUtil.fUploadPic(message,selectedImagePath);
		}
		
		if(this.FBSelected)
		{
			PubSub.zFacebook.fUploadPic(message,selectedImagePath);
		}
		
		if(this.TWSelected)
		{
			PubSub.zTwitterUtil.fUploadPic(message, selectedImagePath);
		}
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
