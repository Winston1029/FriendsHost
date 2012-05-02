package com.moupress.app.friendshost;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.droidfu.widgets.WebImageView;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.uicomponent.FeedItemUIComponent;

public class LstViewFeedAdapter extends BaseAdapter{
	
	private ArrayList<FeedEntry> feedArrayList;
	
	private Activity zActivity;
	private int iLayoutResId;
	private LayoutInflater viewInflator;

	public LstViewFeedAdapter(Activity activity, int layoutResId) {
		this.zActivity = activity;
		this.iLayoutResId = layoutResId;
		viewInflator = zActivity.getLayoutInflater();
	}
	
	public void clear() {
		feedArrayList = new ArrayList<FeedEntry>();
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
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		FeedItemUIComponent feedItemUIComponent;
		if(convertView == null)
		{
			convertView = viewInflator.inflate(iLayoutResId, null);
			feedItemUIComponent = new FeedItemUIComponent();
			
			//Img_Head
			feedItemUIComponent.setImg_Head((WebImageView) convertView.findViewById(R.id.img_feeduser));
			
			//txv_FeedUser
			feedItemUIComponent.setTxv_FeedUser((TextView) convertView.findViewById(R.id.txt_name));
			
			//layout_img
			feedItemUIComponent.setImg_Layout((LinearLayout)convertView.findViewById(R.id.layout_img));
			
			//txt_msgcreatedtime
			feedItemUIComponent.setTxv_MsgCreationTime((TextView)convertView.findViewById(R.id.txt_msgcreatedtime));
			
			//txt_msgbody
			feedItemUIComponent.setTxv_MsgBody((TextView)convertView.findViewById(R.id.txt_msgbody));
			
			//img_photopreview
			feedItemUIComponent.setImg_PhotoPreview((WebImageView) convertView.findViewById(R.id.img_photopreview));
			
			//txv_imgName
			feedItemUIComponent.setTxv_ImgName((TextView) convertView.findViewById(R.id.txv_imgName));
			
			//txv_ImgCaption
			feedItemUIComponent.setTxv_ImgCaption((TextView) convertView.findViewById(R.id.txv_imgCaption));
			
			//txv_ImgDecription
			feedItemUIComponent.setTxv_ImgDecription((TextView) convertView.findViewById(R.id.txv_imgdescription));
			
			convertView.setTag(feedItemUIComponent);
		}
			else
		{
			feedItemUIComponent = (FeedItemUIComponent) convertView.getTag();
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedEntry feed = (FeedEntry) feedArrayList.get(position);
				FriendsHostActivity.zPubsub.fFeedDisplayDetailUI(feed);
			}
		});
		
		feedItemUIComponent.ImgHeadLoad(feedArrayList.get(position).getzFriend().getHeadurl());
		if (feedArrayList == null || feedArrayList.size() < 2) {
			
			feedItemUIComponent.getTxv_FeedUser().setText("Feed Not available yet. \nPlease refresh later");
			feedItemUIComponent.getImg_Head().setVisibility(View.GONE);
			feedItemUIComponent.getTxv_MsgCreationTime().setVisibility(View.GONE);
			feedItemUIComponent.getTxv_MsgBody().setVisibility(View.GONE);
			
		}else {
			
			feedItemUIComponent.getTxv_FeedUser().setText(feedArrayList.get(position).getsName());
			feedItemUIComponent.getTxv_MsgCreationTime().setText(feedArrayList.get(position).getsCreatedTime());
			feedItemUIComponent.TxtMsgBodyLoad(feedArrayList.get(position).getsMsgBody(), feedArrayList.get(position).getsStory());
			
			feedItemUIComponent.ImgPhotoPreviewLoad(feedArrayList.get(position).getsPhotoPreviewLink());
			
			feedItemUIComponent.TxtImgNameLoad(feedArrayList.get(position).getsPhotoPreviewName());
			feedItemUIComponent.TxvImgCaptionLoad( feedArrayList.get(position).getsPhotoPreviewCaption());
			feedItemUIComponent.TxvImgDecription(feedArrayList.get(position).getsPhotoPreviewDescription());
			
		}
		return convertView;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			convertView = viewInflator.inflate(iLayoutResId, null);
//		}
//		
//		WebImageView img_Head = (WebImageView) convertView.findViewById(R.id.img_feeduser);
//		String sHeadImgSrc = feedArrayList.get(position).getzFriend().getHeadurl();
//		if(sHeadImgSrc != null)
//		{
//			img_Head.setImageUrl(sHeadImgSrc);
//			img_Head.loadImage();
//		}
////		Bitmap imgHead = null; 
////		if (sHeadImgSrc != null) {
////			imgHead = ImageOperations(sHeadImgSrc);
////		}
////		if (imgHead != null) {
////			img_Head.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
////			img_Head.setImageBitmap(imgHead);
////		}
//		
//		TextView txv_FeedUser = (TextView) convertView.findViewById(R.id.txt_name);
//		
//		if (feedArrayList == null || feedArrayList.size() < 2) {
//			txv_FeedUser.setText("Feed Not available yet. \nPlease refresh later");
//			
//			//disable other views
//			convertView.findViewById(R.id.img_feeduser).setVisibility(View.GONE);
//			((LinearLayout)convertView.findViewById(R.id.layout_img)).setVisibility(View.GONE);
//			convertView.findViewById(R.id.txt_msgcreatedtime).setVisibility(View.GONE);
//			convertView.findViewById(R.id.txt_msgbody).setVisibility(View.GONE);
//		} else {
//
//			//compulsory field
//			//TextView txv_FeedUser = (TextView) convertView.findViewById(R.id.txt_name);
//			txv_FeedUser.setText(feedArrayList.get(position).getsName());
//			TextView txv_MsgCreationTime = (TextView) convertView.findViewById(R.id.txt_msgcreatedtime);
//			String sCreateTime = feedArrayList.get(position).getsCreatedTime();
//			txv_MsgCreationTime.setText(sCreateTime);
//	
//			// optional field
//			// message or tagged story
//			String sMsgBody = feedArrayList.get(position).getsMsgBody();
//			String sStory = feedArrayList.get(position).getsStory();
//			TextView txv_MsgBody = (TextView) convertView.findViewById(R.id.txt_msgbody);
//
//			if ( sMsgBody != null && sStory != null ) {
//				txv_MsgBody.setText(sMsgBody + "\n" + sStory);
//			} else if ( sMsgBody != null ) {
//				txv_MsgBody.setText(sMsgBody);
//			} else if ( sStory != null ) {
//				txv_MsgBody.setText(sStory);
//			} else {
//				txv_MsgBody.setVisibility(View.GONE);
//			}
//			String username = feedArrayList.get(position).getsName();
////			if (username.equals("梁玉萍")) {
////				System.out.println("");
////			}
//			WebImageView img_PhotoPreview = (WebImageView) convertView.findViewById(R.id.img_photopreview);
//			String sImgSrc = feedArrayList.get(position).getsPhotoPreviewLink();
//			//img_PhotoPreview.setImageUrl(sHeadImgSrc);
//			//img_PhotoPreview.
//			//img_PhotoPreview.loadImage();
//			if (sImgSrc != null && sImgSrc.startsWith("http://") && sImgSrc.endsWith(".jpg")) {
//				img_PhotoPreview.setImageUrl(sImgSrc);
//				//img_PhotoPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//				img_PhotoPreview.loadImage();
//			} else {
//				img_PhotoPreview.setVisibility(View.GONE);
//			}
//			//String sImgSrc = "http://photos-g.ak.fbcdn.net/hphotos-ak-ash4/431333_10150739807624187_554329186_11304408_1165959123_s.jpg";
////			Bitmap imgPhoto = ImageOperations(sImgSrc);
////			if (imgPhoto != null) {
////				img_PhotoPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
////				img_PhotoPreview.setImageBitmap(imgPhoto);
////			} else {
////				img_PhotoPreview.setVisibility(View.GONE);
////			}
//			
//			// Image related text
//			String sImgName = feedArrayList.get(position).getsPhotoPreviewName();
//			String sImgCaption = feedArrayList.get(position).getsPhotoPreviewCaption();
//			String sImgDescription = feedArrayList.get(position).getsPhotoPreviewDescription();
//			
//			TextView txv_ImgName = (TextView) convertView.findViewById(R.id.txv_imgName);
//			TextView txv_ImgCaption = (TextView) convertView.findViewById(R.id.txv_imgCaption);
//			TextView txv_ImgDecription = (TextView) convertView.findViewById(R.id.txv_imgdescription);
//			if (sImgName != null) {
//				txv_ImgName.setText(sImgName);
//			} else {
//				txv_ImgName.setVisibility(View.GONE);
//			}
//			if (sImgCaption != null) {
//				txv_ImgCaption.setText(sImgCaption);
//			} else {
//				txv_ImgCaption.setVisibility(View.GONE);
//			}
//			if (sImgDescription != null) {
//				txv_ImgDecription.setText(sImgDescription);
////			} else if (sMsgBody != null && sStory != null ) {
////				txv_ImgDecription.setText(sStory);
//			} else {
//				txv_ImgDecription.setVisibility(View.GONE);
//			}
//		}
//		return convertView;
//	}

	public void addItem(String[] feedMsg) {
		FeedEntry item = new FeedEntry();
		if ( feedMsg.length > 2 ) {								
			item.setsName(feedMsg[0]);							//name
			item.setsOwnerID(feedMsg[1]);						//feed owner id
			item.setsCreatedTime(feedMsg[2]);					//created time
			item.setsMsgBody(feedMsg[3]);						//message
			item.setsStory(feedMsg[4]);							//story
			//item.setsStory_tags(feedMsg[4]);							//story_tags
			item.setsPhotoPreviewLink(feedMsg[5]);				//pic url
			item.setsPhotoPreviewName(feedMsg[6]);				//pic/album name
			item.setsPhotoPreviewCaption(feedMsg[7]);			//pic/album caption
			item.setsPhotoPreviewDescription(feedMsg[8]);		//pic/album description
		} else {												//No local feed available
			item.setsName(feedMsg[0]);
		}
		
		feedArrayList.add(item);
		
	}
	
	public void addItem(FeedEntry item) {
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
