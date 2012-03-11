package com.moupress.app.friendshost;

import java.util.ArrayList;

import android.app.Activity;
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
	private int iLayoutRef;
	private LayoutInflater viewInflator;

	public LstViewFeedAdapter(Activity activity, int layoutRef) {
		this.zActivity = activity;
		this.iLayoutRef = layoutRef;
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
			convertView = viewInflator.inflate(R.layout.feed_item_preview, null);
		}
		
		TextView txv_FeedUser = (TextView) convertView.findViewById(R.id.txt_name);
		txv_FeedUser.setText(feedArrayList.get(position).getsName());
		TextView txv_MsgCreationTime = (TextView) convertView.findViewById(R.id.txt_msgcreatedtime);
		txv_MsgCreationTime.setText(feedArrayList.get(position).getsCreatedTime());
		TextView txv_MsgBody = (TextView) convertView.findViewById(R.id.txt_msgbody);
		txv_MsgBody.setText(feedArrayList.get(position).getsMsgBody());
		ImageView img_PhotoPreview = (ImageView) convertView.findViewById(R.id.img_photopreview);
		Uri imgSource = Uri.parse(feedArrayList.get(position).getsPhotoPreviewLink());
		img_PhotoPreview.setImageURI(imgSource);
		
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
	
}
