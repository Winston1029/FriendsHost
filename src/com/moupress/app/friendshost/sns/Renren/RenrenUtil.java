package com.moupress.app.friendshost.sns.Renren;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.widget.Toast;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.sns.SnsUtil;
import com.moupress.app.friendshost.sns.Listener.SnsEventListener;
import com.moupress.app.friendshost.util.NotificationTask;
import com.moupress.app.friendshost.util.Pref;
import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.feed.FeedPublishRequestParam;
import com.renren.api.connect.android.feed.FeedPublishResponseBean;
import com.renren.api.connect.android.photos.PhotoUploadRequestParam;
import com.renren.api.connect.android.photos.PhotoUploadResponseBean;
import com.renren.api.connect.android.users.UsersGetInfoRequestParam;
import com.renren.api.connect.android.users.UsersGetInfoResponseBean;
import com.renren.api.connect.android.view.RenrenAuthListener;


public class RenrenUtil extends SnsUtil{
	//friendshost
	//private static final String API_KEY = "7872469fdd144ef792233e56dca0eb31";
	//private static final String SECRET_KEY = "938ebb14322d40c89015483b2479d144";
	//private static final String APP_ID = "166341";
	//melonfriends
	private static final String API_KEY = "ff40a6bf3ae2421fab637a16ed990f67";
	private static final String SECRET_KEY = "61e107d1d35f4a41a3df5742df12f8ba";
	private static final String APP_ID = "199556";
	
	private static final String TAG = "RenrenUtil";
	
	private static final String[] PERMISSIONS = new String[] {
		"read_user_feed", "publish_feed", 
		"read_user_share", "publish_share", "read_user_status",
		"publish_comment", 
		"photo_upload", "create_album", "read_user_album",
		"operate_like"};
	
	//private PubSub zPubSub;
	//private Context zContext;
	//private Activity zActivity;
	
	private Renren zRenren;
	private static AsyncRenren asyncRenren;
	private NotificationTask notificationTask;
		
	public RenrenUtil(PubSub pubSub) {
		//zPubSub = pubSub;
		//zContext = zPubSub.fGetContext();
		//zActivity = zPubSub.fGetActivity();
		super(pubSub, Const.SNS_RENREN);
		
		this.zRenren = new Renren(API_KEY, SECRET_KEY, APP_ID, zContext);
		
		this.logImg  = R.drawable.fh_renren_logo;
		//fRenrenAuth();
	}
	
	@Override
	public boolean isSessionValid() {
		if (zRenren != null ) {
			return zRenren.isSessionKeyValid();
		} else {
			return false;
		}
	}

	@Override
	public void fSnsAuth(final SnsEventListener snsEventListener,final boolean uptPref) {
		if ( isSessionValid() ) {
			this.SnsAddEventCallback(snsEventListener,uptPref);
			return;
		}
		final AbstractRequestListener<UsersGetInfoResponseBean> userinfo_listener = new AbstractRequestListener<UsersGetInfoResponseBean>(){
			@Override
			public void onComplete(UsersGetInfoResponseBean bean) {
				String renren_headurl = bean.getUsersInfo().get(0).getTinyurl();
				Pref.setMyStringPref(zContext, Const.LOGIN_HEAD_RENREN, renren_headurl);
			}
			@Override
			public void onRenrenError(RenrenError renrenError) {
			}
			@Override
			public void onFault(Throwable fault) {
			}
		};
		
		final RenrenAuthListener listener = new RenrenAuthListener() {

			@Override
			public void onComplete(Bundle values) {
				//Toast.makeText(zContext, "Renren Auth Complete",Toast.LENGTH_SHORT).show();
				SnsAddEventCallback(snsEventListener,uptPref);
				asyncRenren = fGetAsyncRenren();
				asyncRenren.getUsersInfo(new UsersGetInfoRequestParam(new String[]{zRenren.getCurrentUid()+""}), userinfo_listener);
			}

			@Override
			public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
				Log.d(TAG, renrenAuthError.getMessage());
				Toast.makeText(zContext, "Renren Auth Failed", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCancelLogin() {
			}

			@Override
			public void onCancelAuth(Bundle values) {
			}
		};
		
		zRenren.authorize(zActivity, PERMISSIONS, listener);
	}
	
	public AsyncRenren fGetAsyncRenren()
	{
		if (asyncRenren == null) {
			fSnsAuth(null,false);
			asyncRenren = new AsyncRenren(zRenren);
		}
		return asyncRenren;
	}
	/**
	 * type	 描述
	 *	10	 更新状态的新鲜事。
	 *  11	 page更新状态的新鲜事。
	 *  20	 发表日志的新鲜事。
	 *  21	 分享日志的新鲜事。
	 *  22	 page发表日志的新鲜事。
	 *  23	 page分享日志的新鲜事。
	 *  30	 上传照片的新鲜事。
	 *  31	 page上传照片的新鲜事。
	 *  32	 分享照片的新鲜事。
	 *  33	 分享相册的新鲜事。
	 *  34	 修改头像的新鲜事。
	 *  35	 page修改头像的新鲜事。
	 *  36	 page分享照片的新鲜事。
	 *  40	 成为好友的新鲜事。
	 *  41	 成为page粉丝的新鲜事。
	 *  50	 分享视频的新鲜事。
	 *  51	 分享链接的新鲜事。
	 *  52	 分享音乐的新鲜事。
	 *  53	 page分享视频的新鲜事。
	 *  54	 page分享链接的新鲜事。
	 *  55	 page分享音乐的新鲜事。
	 */
	
	@Override
	public void fGetNewsFeed(final Context context) {
		//AsyncRenren asyncRenren = new AsyncRenren(zRenren);
		asyncRenren = fGetAsyncRenren();
		//FeedExtractRequestParam param = new FeedExtractRequestParam("XML", "10,11,20,21,22,23,30,31,32,33,34", 1);
		FeedExtractRequestParam param = new FeedExtractRequestParam("json", "10,11,20,21,22,23,30,31,32,33,34", 1);
		
		AbstractRequestListener<FeedExtractResponseBean> listener = new AbstractRequestListener<FeedExtractResponseBean>(){
			@Override
			public void onComplete(final FeedExtractResponseBean bean) {
				//System.out.println("Renren news feed get listener on complete");
				Log.i(TAG, "Renren news feed get listener on complete");
				zPubSub.fGetFeedOrganisor().fSaveNewFeeds(bean, context);
			}

			@Override
			public void onRenrenError(RenrenError renrenError) {
				System.out.println(" onRenrenError Extract Request Response Bean !");
			}

			@Override
			public void onFault(Throwable fault) {
				System.out.println(" onFault Extract Request Response Bean !");
			}
		};
		asyncRenren.getFeed(param, listener, false);
		
	}
	
	// no working yet
	public void fPostComments(String feedID, String message) {
		if (zRenren != null) {
			String[] comment = message.split("%");
			String comment_user_id = comment[0];
			String commentMsg = comment[1];
			
			Bundle parameters = new Bundle();
			parameters.putString("method", "status.addComment");
			parameters.putString("owner_id", comment_user_id);
			parameters.putString("content", commentMsg);
			parameters.putString("status_id", feedID);
			String response = zRenren.requestJSON(parameters);
			System.out.println(response);
		}
	}
	
	@Override
	public void fPublishFeeds(Bundle params) {
		//String name, String description,String url, String imageUrl, String caption, String message
		if (zRenren != null) {
			//AsyncRenren asyncRenren = new AsyncRenren(zRenren);
			if (!isSessionValid()) {
				fSnsAuth(null, false);
			}
			asyncRenren = fGetAsyncRenren();
			//showProgress();
			String name = params.getString(Const.SNAME);
			String description = params.getString(Const.SPHOTOPREVIEWDESCRIPTION);
			String url = params.getString(Const.SLINK);
			String imageUrl = params.getString(Const.SHEADIMG);
			String caption = params.getString(Const.SPHOTOPREVIEWCAPTION);
			String message = params.getString(Const.SMSGBODY);
			FeedPublishRequestParam param = new FeedPublishRequestParam(
					(name != null ? name : " "),
					(description != null ? description : " "),
					(url != null ? url : " "),
					(imageUrl != null ? imageUrl : ""),
					(caption != null ? caption : ""),
					null,  null,(message != null ? message : ""));
			
			this.startNotification(4, "Feed");
			AbstractRequestListener<FeedPublishResponseBean> listener = new AbstractRequestListener<FeedPublishResponseBean>() {

				@Override
				public void onComplete(final FeedPublishResponseBean bean) {
					Log.d(TAG, bean.toString());
					stopNotification();
				}

				@Override
				public void onFault(final Throwable fault) {
					Log.d(TAG, fault.getMessage());
					stopNotification();
				}

				@Override
				public void onRenrenError(final RenrenError renrenError) {
					Log.d(TAG, renrenError.getMessage());
					stopNotification();
				}
			};
			asyncRenren.publishFeed(param, listener, true);

		}
	}
	
	//public void fDisplayRenrenFeed() {
//	@Override
//	public void fDisplayFeed(){
//		zActivity.runOnUiThread(new Runnable() {
//			public void run() {
////				ArrayAdapter<String> adapterRenrenResponse = zPubSub.fGetArrAdapterFeed();
////				adapterRenrenResponse.clear();
////				String[] feedMsg = zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeedSummary(Const.SNS_RENREN);
////				for(int i= 0; i<feedMsg.length;i++) {
////					adapterRenrenResponse.add(feedMsg[i]);
////				}
////				adapterRenrenResponse.notifyDataSetChanged();
//				
//				LstViewFeedAdapter feedAdapter = zPubSub.fGetAdapterFeedPreview();
//				feedAdapter.clear();
//				ArrayList<FeedEntry> feeds = zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(Const.SNS_RENREN);
//				for (FeedEntry item : feeds ) {
//					feedAdapter.addItem(item);
//				}
//				feedAdapter.notifyDataSetChanged();
//			}
//		});
//	}
	
	public void onComplete (int requestCode, int resultCode, Intent data) {
		if (zRenren != null ) {
			zRenren.authorizeCallback(requestCode, resultCode, data);
		}
    }

	public void fUploadPic(String message, String selectedImagePath) {
			if (zRenren != null) {
				if (!isSessionValid()) {
					fSnsAuth(null, false);
				}
				//AsyncRenren asyncRenren = new AsyncRenren(zRenren);
				asyncRenren = fGetAsyncRenren();
				PhotoUploadRequestParam photoParam = new PhotoUploadRequestParam();
				
				photoParam.setCaption(message);
				
				photoParam.setFile(new File(selectedImagePath));
				
				startNotification(3,"Picture");
				
				asyncRenren.publishPhoto(photoParam,new AbstractRequestListener<PhotoUploadResponseBean>(){

					@Override
					public void onComplete(PhotoUploadResponseBean bean) {
						Log.d(TAG, bean.toString());
						stopNotification();
					}

					@Override
					public void onFault(Throwable fault) {
						Log.d(TAG, fault.getMessage());
						stopNotification();
					}

					@Override
					public void onRenrenError(RenrenError renrenError) {
						Log.d(TAG, renrenError.getMessage());
						stopNotification();
					}});
			}
		}
	
	private void startNotification(int notificationId,String fileType)
    {
    	notificationTask = new NotificationTask(this.zContext);
		 if(notificationTask != null)
		{
			notificationTask.SetNotificationTask(notificationId, "Renren", fileType);
			notificationTask.execute(0);
		}
    }
    
    private void stopNotification()
    {
    	if(notificationTask != null)
		{
			notificationTask.setTaskDone(true);
		}
    }

    @Override
	public void fResend(FeedEntry feed) {
//		//String name = (feed.getsName() == null?" ":feed.getsName());
//		String name = " ";
//		String description = (feed.getsPhotoPreviewDescription() == null? " ":feed.getsPhotoPreviewDescription());
//		String link = (feed.getsPhotoPreviewLink() == null ? " ":feed.getsPhotoPreviewLink());
//		String imgUrl = (feed.getsPhotoPreviewLink() == null ? " ":feed.getsPhotoPreviewLink());
//		String caption = (feed.getsPhotoPreviewCaption() == null ? " ":feed.getsPhotoPreviewCaption());
//		String msg = (feed.getsMsgBody() == null ? " ":feed.getsMsgBody());
//		String story = (feed.getsStory()==null? " ":feed.getsStory());
//		this.fPublishFeeds(name, description, link, imgUrl, caption, msg+story);
	}
    
    public void fLikeFeeds(Bundle params) {
    	String likeUrl = "http://www.renren.com/g?ownerid=%s&resourceid=%s&type=%s";
    	if (zRenren != null) {
    		Bundle parameters = new Bundle();
			parameters.putString("method", "like.like");
			
			int iResType = Integer.parseInt(params.getString(Const.SFEEDTYPE));
			String sResType = ""; 
			if (iResType >= 20 && iResType < 30 ) {
				sResType = "blog";
			} else if (iResType >= 30 && iResType < 40 && iResType != 33) {
				sResType = "photo";
			} else if (iResType == 33) {
				sResType = "album";
			}
			likeUrl = String.format(likeUrl, params.getString(Const.SOWNERID), params.getString(Const.SRESOURCEID), sResType);
			parameters.putString("url", likeUrl);
			//parameters.putString("url", "http://photo.renren.com/photo/227794402/photo-6166039326");
			String response = zRenren.requestJSON(parameters);
			System.out.println(response);
    	}
	}
	
	public void fUnLikeFeeds(Bundle params) {
		String likeUrl = "http://www.renren.com/g?ownerid=%s&resourceid=%s&type=%s";
    	if (zRenren != null) {
    		Bundle parameters = new Bundle();
			parameters.putString("method", "like.unlike");
			
			int iResType = Integer.parseInt(params.getString(Const.SFEEDTYPE));
			String sResType = ""; 
			if (iResType >= 20 && iResType < 30 ) {
				sResType = "blog";
			} else if (iResType >= 30 && iResType < 40 && iResType != 33) {
				sResType = "photo";
			} else if (iResType == 33) {
				sResType = "album";
			}
			likeUrl = String.format(likeUrl, params.getString(Const.SOWNERID), params.getString(Const.SRESOURCEID), sResType);
			parameters.putString("url", likeUrl);
			String response = zRenren.requestJSON(parameters);
			System.out.println(response);
    	}
	}
	
	public void fLogout(Bundle params) {
		if (zRenren != null) {
			zRenren.logout(zContext);
		}
	}
	
	public void fShareFeeds(Bundle params) {
	}
}
