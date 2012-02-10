/*
 * Copyright 2010 Renren, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.renren.android;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.moupress.app.util.Renren.FeedExtractRequestParam;
import com.moupress.app.util.Renren.FeedExtractRequestResponseBean;
import com.moupress.app.util.Renren.FeedHelper;
import com.moupress.app.util.Renren.RenrenUtil;

import android.content.Context;
import android.os.Bundle;



/**
 * 对人人的请求�?装�?异步。注�?：使用该类调用人人接�?�时，�?能在Listener中直接更新UI控件。
 * 
 * @see Renren
 * @see RequestListener
 * 
 * @author yong.li@opi-corp.com
 * 
 */
public class AsyncRenren {

    private RenrenUtil renren;

    private Executor pool;

    public AsyncRenren(RenrenUtil renren) {
        this.renren = renren;
        this.pool = Executors.newFixedThreadPool(2);
    }

    /**
     * 退出登录
     * 
     * @param context
     * @param listener 注�?监�?�器中�?在主线程中执行，所以�?能在监�?�器中直接更新UI代�?。
     */
    public void logout(final Context context, final RequestListener listener) {
        pool.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    String resp = renren.logout(context);
                    RenrenError rrError = Util.parseRenrenError(resp, RenrenUtil.RESPONSE_FORMAT_JSON);
                    if (rrError != null) listener.onRenrenError(rrError);
                    else listener.onComplete(resp);
                } catch (Throwable e) {
                    listener.onFault(e);
                }
            }
        });
    }

    /**
     * 调用 人人 APIs，�?务器的�?应是JSON串。
     * 
     * 人人 APIs 详细信�?��? http://wiki.dev.renren.com/wiki/API
     * 
     * @param parameters 注�?监�?�器中�?在主线程中执行，所以�?能在监�?�器中直接更新UI代�?。
     * @param listener
     */
    public void requestJSON(Bundle parameters, RequestListener listener) {
        request(parameters, listener, RenrenUtil.RESPONSE_FORMAT_JSON);
    }

    /**
     * 调用 人人 APIs �?务器的�?应是XML串。
     * 
     * 人人 APIs 详细信�?��? http://wiki.dev.renren.com/wiki/API
     * 
     * @param parameters 注�?监�?�器中�?在主线程中执行，所以�?能在监�?�器中直接更新代�?。
     * @param listener
     */
    public void requestXML(Bundle parameters, RequestListener listener) {
        request(parameters, listener, RenrenUtil.RESPONSE_FORMAT_XML);
    }

    /**
     * 调用 人人 APIs。
     * 
     * 人人 APIs 详细信�?��? http://wiki.dev.renren.com/wiki/API
     * 
     * @param parameters
     * @param listener 注�?监�?�器中�?在主线程中执行，所以�?能在监�?�器中直接更新UI代�?。
     * @param format return data format (json or xml)
     */
    private void request(final Bundle parameters, final RequestListener listener,
            final String format) {
        pool.execute(new Runnable() {

            @Override
            public void run() {

                try {
                    String resp = "";
                    if ("xml".equalsIgnoreCase(format)) {
                        resp = renren.requestXML(parameters);
                    } else {
                        resp = renren.requestJSON(parameters);
                    }
                    RenrenError rrError = Util.parseRenrenError(resp, format);
                    if (rrError != null) {
                        listener.onRenrenError(rrError);
                    } else {
                        listener.onComplete(resp);
                    }
                } catch (Throwable e) {
                    listener.onFault(e);
                }
            }
        });
    }
    
	/**
	 * 异步方法<br>
	 * 使用用户�??和密�?完�?登陆和授�?� <br>
	 * 
	 * @see Renren.authorize
	 * @param param
	 *            请求对象
	 * @param listener
	 *            登陆状�?的监�?�器
	 */
	public void authorize(PasswordFlowRequestParam param, RenrenAuthListener listener) {
		PasswordFlowHelper passwordFlowHelper = new PasswordFlowHelper();
		passwordFlowHelper.login(pool, param, listener, renren);
	}
    
	/**
	 * 
	 * @see Renren.uploadPhoto
	 */
//    public void publishPhoto(final long albumId, final byte[] photo, final String fileName,
//            final String description, final String format, final RequestListener listener) {
//        pool.execute(new Runnable() {
//
//            @Override
//            public void run() {
//
//                try {
//                    String resp = renren.publishPhoto(albumId, photo, fileName, description, format);
//                    RenrenError rrError = Util.parseRenrenError(resp, format);
//                    if (rrError != null) {
//                        listener.onRenrenError(rrError);
//                    } else {
//                        listener.onComplete(resp);
//                    }
//                } catch (Throwable e) {
//                    listener.onFault(e);
//                }
//            }
//        });
//    }
    
	/**
	 * users.getInfo接�?�<br>
	 * http://wiki.dev.renren.com/wiki/Users.getInfo
	 * @see Renren.getUsersInfo
	 * 
	 * @param param
	 *            请求�?�数
	 * @see Renren.getUsersInfo
	 */
//    public void getUsersInfo (UsersGetInfoRequestParam param, AbstractRequestListener<UsersGetInfoResponseBean> listener) {
//        UsersGetInfoHelper helper = new UsersGetInfoHelper(renren);
//        helper.asyncGetUsersInfo(pool, param, listener);
//    }
    
	/**
	 * �?�状�?，异步调用status.set
	 * �?��?http://wiki.dev.renren.com/wiki/Status.set
	 * 
	 * @param status
	 *          �?�?�布的状�?
	 * @param listener
	 *          用以监�?��?�布状�?结果的监�?�器对象
	 * @param truncOption
	 *          若超出了长度，是�?�自动截短至140个字
	 */
//    public void publishStatus(StatusSetRequestParam status,
//    		AbstractRequestListener<StatusSetResponseBean> listener, 
//    		boolean truncOption) {
//    	StatusHelper helper = new StatusHelper(renren);
//    	helper.asyncPublish(pool, status, listener, truncOption);
//    }
    
	/**
	 * �?��?新鲜事<br>
	 * 异步调用feed.publishFeed接�?�
	 * �?��?http://wiki.dev.renren.com/wiki/Feed.publishFeed
	 * 
	 * @param feed
	 *          �?�?�布的新鲜事
	 * @param listener
	 *          用以监�?��?�布新鲜事结果的监�?�器对象
	 * @param truncOption
	 *          若超出了长度，是�?�自动截短至�?制的长度
	 */
//	public void publishFeed(FeedPublishRequestParam feed,
//			AbstractRequestListener<FeedPublishResponseBean> listener,
//			boolean truncOption) {
//		FeedHelper helper = new FeedHelper(renren);
//		helper.asyncPublish(pool, feed, listener, truncOption);
//	}
	
	
	/**
	 * �?��?新鲜事<br>
	 * 异步调用feed.publishFeed接�?�
	 * �?��?http://wiki.dev.renren.com/wiki/Feed.publishFeed
	 * 
	 * @param feed
	 *          �?�?�布的新鲜事
	 * @param listener
	 *          用以监�?��?�布新鲜事结果的监�?�器对象
	 * @param truncOption
	 *          若超出了长度，是�?�自动截短至�?制的长度
	 */
	public void getFeeds(FeedExtractRequestParam feed,
			AbstractRequestListener<FeedExtractRequestResponseBean> listener) {
		FeedHelper helper = new FeedHelper(renren);
		helper.asyncGetFeed(pool, feed, listener);
	}
	
	/**
	 * 创建相册的异步接�?�
	 * 
	 * @param album
	 * 			调用此接�?�需准备的�?�数
	 * @param listener
	 *            开�?�者实现，对返回结果进行�?作
	 *            
	 * 			  <p>详情请�?�考 http://wiki.dev.renren.com/wiki/Photos.createAlbum
	 */
//	public void createAlbum(AlbumCreateRequestParam album,
//			AbstractRequestListener<AlbumCreateResponseBean> listener) {
//		new PhotoHelper(renren).asyncCreateAlbum(album, listener);
//	}
	
	/**
	 * 获�?�相册的异步接�?�
	 * 
	 * @param album	
	 * 			调用此接�?�需准备的�?�数
	 * @param listener
	 *            开�?�者实现，对返回结果进行�?作
	 *            
	 *            <p>详情请�?�考 http://wiki.dev.renren.com/wiki/Photos.getAlbums
	 * 
	 */
//	public void getAlbums(AlbumGetRequestParam album,
//			AbstractRequestListener<AlbumGetResponseBean> listener) {
//		new PhotoHelper(renren).asyncGetAlbums(album, listener);
//	}
	
	/**
	 * 上传照片的异步接�?�
	 * 
	 * @param photo
	 * 			调用此接�?�需准备的�?�数
	 * @param listener
	 *            开�?�者实现，对返回结果进行�?作
	 * 
	 * 			  <p>详情请�?�考 http://wiki.dev.renren.com/wiki/Photos.upload
	 */
//	public void publishPhoto(PhotoUploadRequestParam photo,
//			AbstractRequestListener<PhotoUploadResponseBean> listener) {
//		new PhotoHelper(renren).asyncUploadPhoto(photo, listener);
//	}

	/**
	 * friends.get接�?� 得到当�?登录用户的好�?�列表，得到的�?�是�?�有好�?�id的列表<br>
	 * http://wiki.dev.renren.com/wiki/Friends.get
	 * 
	 * @see Renren.getFriends
	 * @param param
	 * 			调用接�?�需�?的�?�数
	 * @param listener
	 * 			接�?�调用的监�?�器
	 */
//	public void getFriends (FriendsGetRequestParam param, AbstractRequestListener<FriendsGetResponseBean> listener) {
//		new FriendsHelper(renren).asyncGetFriends(pool, param, listener);
//	}
	
	/**
	 * friends.getFriends接�?�  得到当�?登录用户的好�?�列表<br>
	 * http://wiki.dev.renren.com/wiki/Friends.getFriends
	 * 
	 * @see Renren.getFriends
	 * @param param
	 * 			调用接�?�需�?的�?�数
	 * @param listener
	 * 			接�?�调用的监�?�器
	 */
//	public void getFriends (FriendsGetFriendsRequestParam param, AbstractRequestListener<FriendsGetFriendsResponseBean> listener) {
//		new FriendsHelper(renren).asyncGetFriends(pool, param, listener);
//	}
}
