package com.moupress.app.friendshost;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LstViewFeedAdapter extends BaseAdapter{
	
	private ArrayList<FeedListItem> feedArrayList;
	
	private Activity zActivity;
	private int iLayoutResId;
	private LayoutInflater viewInflator;

	public LstViewFeedAdapter(Activity activity, int layoutResId) {
		this.zActivity = activity;
		this.iLayoutResId = layoutResId;
		viewInflator = zActivity.getLayoutInflater();
	}
	
	public void clear() {
		feedArrayList = new ArrayList<FeedListItem>();
	}

	@Override
	public int getCount() {

		if (feedArrayList == null ) {
			return 0;
		}
		else {
			return feedArrayList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return feedArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = viewInflator.inflate(iLayoutResId, null);
		}

		//compulsory field
		TextView txv_FeedUser = (TextView) convertView.findViewById(R.id.txt_name);
		txv_FeedUser.setText(feedArrayList.get(position).getsName());
		TextView txv_MsgCreationTime = (TextView) convertView.findViewById(R.id.txt_msgcreatedtime);
		String sCreateTime = feedArrayList.get(position).getsCreatedTime();
		txv_MsgCreationTime.setText(sCreateTime);

		// optional field
		String sMsgBody = feedArrayList.get(position).getsMsgBody();
		TextView txv_MsgBody = (TextView) convertView.findViewById(R.id.txt_msgbody);
		if ( sMsgBody != null ) {
			txv_MsgBody.setText(sMsgBody);
		} else {
			txv_MsgBody.setVisibility(View.GONE);
		}
		
		ImageView img_PhotoPreview = (ImageView) convertView.findViewById(R.id.img_photopreview);
		String sImgSrc = feedArrayList.get(position).getsPhotoPreviewLink();
		//String sImgSrc = "http://photos-g.ak.fbcdn.net/hphotos-ak-ash4/431333_10150739807624187_554329186_11304408_1165959123_s.jpg";
		Bitmap img = ImageOperations(sImgSrc);
		if (img != null) {
			img_PhotoPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			img_PhotoPreview.setImageBitmap(img);
		} else {
			img_PhotoPreview.setVisibility(View.GONE);
		}
		
		String sImgDescription = feedArrayList.get(position).getsPhotoPreviewDescription();
		TextView txv_ImgDecription = (TextView) convertView.findViewById(R.id.txv_imgdescription);
		if (sImgDescription != null) {
			txv_ImgDecription.setText(sImgDescription);
		} else {
			txv_ImgDecription.setVisibility(View.GONE);
		}
		
		
		return convertView;
	}

	public void addItem(String[] feedMsg) {
		FeedListItem item = new FeedListItem();
		item.setsName(feedMsg[0]);
		item.setsCreatedTime(feedMsg[1]);
		item.setsMsgBody(feedMsg[2]);
		item.setsPhotoPreviewLink(feedMsg[3]);
		item.setsPhotoPreviewDescription(feedMsg[4]);
		feedArrayList.add(item);
	}
	
	private Bitmap ImageOperations(String url) {
		HttpURLConnection con = null;
		Bitmap bmp = null;
		if (url != null) {
			try {
				URL ulrn = new URL(url);
				con = (HttpURLConnection)ulrn.openConnection();
			    InputStream is = con.getInputStream();
			    bmp = BitmapFactory.decodeStream(is);
		    } catch(Exception e) {
		    	
		    } finally { 
		    	if (con != null) { con.disconnect(); } 
	    	}
		}
		return bmp;
	}

//	public Object fetch(String address) throws MalformedURLException,IOException {
//		URL url = new URL(address);
//		Object content = url.getContent();
//		return content;
//	}
	
}
