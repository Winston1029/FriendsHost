package com.moupress.app.friendshost;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
		if ( sMsgBody == null || sMsgBody.isEmpty() ) {
			txv_MsgBody.setVisibility(View.INVISIBLE);
			System.out.println("Pos = " + position + " MsgBody is Empty");
		} else {
			txv_MsgBody.setText(sMsgBody);
		}
		
		ImageView img_PhotoPreview = (ImageView) convertView.findViewById(R.id.img_photopreview);
		String sImgSrc = feedArrayList.get(position).getsPhotoPreviewLink();
		if (sImgSrc == null || sImgSrc.isEmpty() ) {
			img_PhotoPreview.setVisibility(View.INVISIBLE);
			System.out.println("Pos = " + position + " ImgSrc is Empty");
		} else {
			//Uri imgSource = Uri.parse(sImgSrc);
			//img_PhotoPreview.setImageURI(imgSource);
			Drawable image = ImageOperations(sImgSrc,"image.jpg");
			img_PhotoPreview.setImageDrawable(image);
		}
		
		return convertView;
	}

	public void addItem(String[] feedMsg) {
		FeedListItem item = new FeedListItem();
		item.setsName(feedMsg[0]);
		item.setsCreatedTime(feedMsg[1]);
		item.setsMsgBody(feedMsg[2]);
		item.setsPhotoPreviewLink(feedMsg[3]);
		feedArrayList.add(item);
	}
	
	private Drawable ImageOperations(String url, String saveFilename) {
		try {
			//InputStream is = (InputStream) this.fetch(url);
			InputStream is = (InputStream) (new URL(url)).getContent(); 
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

//	public Object fetch(String address) throws MalformedURLException,IOException {
//		URL url = new URL(address);
//		Object content = url.getContent();
//		return content;
//	}
	
}
