package com.moupress.app.util.Renren;

import java.util.ArrayList;

import com.moupress.app.friendshost.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class FeedListAdapter extends BaseAdapter
{
	private ArrayList<FeedElement> feedList;
	private LayoutInflater viewInflator;
	
	public FeedListAdapter(ArrayList<FeedElement> feedList,Context context)
	{
		this.feedList = feedList;
		viewInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("Size of List " + feedList.size());
		return feedList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return feedList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("Title "+feedList.get(position).getTitle());
		
		if (convertView == null) {
			convertView = viewInflator.inflate(R.layout.feed_item,null);
		}
		
		TextView tv = (TextView) convertView.findViewById(R.id.msgtxt);
		tv.setText(feedList.get(position).getName()+" : "+feedList.get(position).getTitle());
		return convertView;
	}
	
}
