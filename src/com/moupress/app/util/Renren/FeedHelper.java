/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.moupress.app.util.Renren;

import java.util.concurrent.Executor;

import org.json.JSONException;
import org.json.JSONObject;

import com.renren.android.AbstractRequestListener;
import com.renren.android.RenrenError;
import com.renren.android.RenrenException;
import com.renren.android.RenrenWidgetListener;
import com.renren.android.Util;

import android.app.Activity;
import android.os.Bundle;


/**
 * �?装根�?�人人开放平�?�新鲜事的相关�?作
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com) 
 *
 */
public class FeedHelper {

	/**
	 * �?务器的�?应字段
	 */
	private static final String RESPONSE = "actor_id";
	
	/**
	 * 用�?��?��?请求的{@link Renren}对象
	 */
	private RenrenUtil renren;
	
	/**
	 * 用�?�在bundle中传递的是�?�进行验�?的boolean值的key
	 */
	public static final String AUTH_OPTION = "auth_option";

	public FeedHelper(RenrenUtil renren) {
		this.renren = renren;
	}
	
	public FeedExtractRequestResponseBean getFeed(FeedExtractRequestParam feed) throws Throwable 
	{
		if (!renren.isSessionKeyValid()) {
			String errorMsg = "Session key is not valid.";
			throw new RenrenException(RenrenError.ERROR_CODE_TOKEN_ERROR,
					errorMsg, errorMsg);
		}
		
		
		if(feed == null) {
			String errorMsg = "The parameter is null.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg, errorMsg);
		}
				
		// �?��?�?�布状�?请求
		String response;
		try {
			Bundle params = feed.getParams();
			//response = renren.requestJSON(params);
			response = renren.requestXML(params);
			System.out.println("Feeds Response " + response);
		} catch (RenrenException rre) {
			
			Util.logger(rre.getMessage());
			throw rre;
		} catch (RuntimeException re) {
			Util.logger(re.getMessage());
			throw new Throwable(re);
		}
		
		RenrenError rrError = Util.parseRenrenError(response, RenrenUtil.RESPONSE_FORMAT_XML);
		if (rrError != null) {
			Util.logger(rrError.getMessage());
			throw new RenrenException(rrError);
		} else {
		
			return new FeedExtractRequestResponseBean(response);
		}
	}
	
	
	/**
	 * �?�步调用feed.publishFeed�?�布新鲜事
	 * 
	 * @param feed 
	 * 			�?�?��?的新鲜事
	 * @return 
	 * 			若�?��?�?功，返回已�?��?的新鲜事，�?�则返回null
	 * @throws RenrenException 
	 * @throws Throwable
	 */
	public FeedPublishResponseBean publish(FeedPublishRequestParam feed) throws RenrenException, Throwable {

		if (!renren.isSessionKeyValid()) {
			String errorMsg = "Session key is not valid.";
			throw new RenrenException(RenrenError.ERROR_CODE_TOKEN_ERROR,
					errorMsg, errorMsg);
		}
		//�?�数�?能为空
		if(feed == null) {
			String errorMsg = "The parameter is null.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg, errorMsg);
		}
				
		// �?��?�?�布状�?请求
		String response;
		try {
			Bundle params = feed.getParams();
			response = renren.requestJSON(params);
		} catch (RenrenException rre) {
			Util.logger(rre.getMessage());
			throw rre;
		} catch (RuntimeException re) {
			Util.logger(re.getMessage());
			throw new Throwable(re);
		}

		RenrenError rrError = Util.parseRenrenError(response, RenrenUtil.RESPONSE_FORMAT_JSON);
		if (rrError != null) {
			Util.logger(rrError.getMessage());
			throw new RenrenException(rrError);
		} else {
			try {
				JSONObject json = new JSONObject(response);
				int postId = json.optInt(RESPONSE);
				if (postId > 0) {
					return new FeedPublishResponseBean(response);
				}
				String errorMsg = "Cannot parse the response.";
				throw new RenrenException(
						RenrenError.ERROR_CODE_UNABLE_PARSE_RESPONSE,
						errorMsg, errorMsg);
			} catch (JSONException je) {
				Util.logger(je.getMessage());
				throw new RenrenException(
						RenrenError.ERROR_CODE_UNABLE_PARSE_RESPONSE, je.getMessage(),
						je.getMessage());
			}
		}		
	}
	
	/**
	 * 异步调用feed.publishFeed�?�布新鲜事
	 * 
	 * @param pool
	 * 			执行�?��?新鲜事�?作的线程池
	 * @param feed
	 *      	�?�?�布的新鲜事
	 * @param listener
	 *      	用以监�?��?�布新鲜事结果的监�?�器对象
	 * @param truncOption
	 *          若超出了长度，是�?�自动截短至�?制的长度
	 */
	public void asyncPublish(Executor pool, 
			final FeedPublishRequestParam feed, 
			final AbstractRequestListener<FeedPublishResponseBean> listener, 
			final boolean truncOption) {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					FeedPublishResponseBean bean = publish(feed);
					if (listener != null) {
						listener.onComplete(bean);
					}
				} catch (RenrenException rre) { // �?�数�?�?务器等错误或异常
					Util.logger(rre.getMessage());
					if (listener != null) {
						listener.onRenrenError(new RenrenError(rre
								.getErrorCode(), rre.getMessage(), rre
								.getOrgResponse()));
					}
				} catch (Throwable t) { // 一般为网络异常
					Util.logger(t.getMessage());
					if (listener != null) {
						listener.onFault(t);
					}
				}
			}
		});
	}
	
	/**
	 * 异步调用feed.publishFeed�?�布新鲜事
	 * 
	 * @param pool
	 * 			执行�?��?新鲜事�?作的线程池
	 * @param feed
	 *      	�?�?�布的新鲜事
	 * @param listener
	 *      	用以监�?��?�布新鲜事结果的监�?�器对象
	 * @param truncOption
	 *          若超出了长度，是�?�自动截短至�?制的长度
	 */
	public void asyncGetFeed(Executor pool, 
			final FeedExtractRequestParam feed, 
			final AbstractRequestListener<FeedExtractRequestResponseBean> listener) {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					FeedExtractRequestResponseBean bean = getFeed(feed);
					if (listener != null) {
						listener.onComplete(bean);
					}
				} catch (RenrenException rre) { // �?�数�?�?务器等错误或异常
					System.out.println("RRE Message "+rre.getMessage());
					Util.logger(rre.getMessage());
					if (listener != null) {
						listener.onRenrenError(new RenrenError(rre
								.getErrorCode(), rre.getMessage(), rre
								.getOrgResponse()));
					}
				} catch (Throwable t) { // 一般为网络异常
					Util.logger(t.getMessage());
					if (listener != null) {
						listener.onFault(t);
					}
				}
			}
		});
		
		
	}
	
	/**
	 * 使用Widget窗�?��?�新鲜事
	 * 
	 * @param appID 
	 * 			应用的app ID
	 * @param activity 
	 * 			显示此widget的activity
	 * @param listener 
	 * 			监�?��?��?新鲜事结果的listener
	 * @throws RenrenException 
	 */
	public void startFeedPublishWidget(String appID, Activity activity, FeedPublishRequestParam feed, 
			final AbstractRequestListener<FeedPublishResponseBean> listener) throws RenrenException {
		if (!renren.isSessionKeyValid()) {
			String errorMsg = "Session key is not valid.";
			throw new RenrenException(RenrenError.ERROR_CODE_TOKEN_ERROR,
					errorMsg, errorMsg);
		}
		
		Bundle params = feed.getWidgetParams();
		params.putString("app_id", appID);
		
		renren.widgetDialog(activity, params, new RenrenWidgetListener() {

			@Override
			public void onError(Bundle retParams) {
				if(listener != null) {
					listener.onRenrenError(
							new RenrenError(retParams.getString("error") 
									+ retParams.getString("error_description")));
				}
			}

			@Override
			public void onComplete(Bundle retParams) {
				if(listener != null) {
					if(retParams.containsKey(FeedPublishResponseBean.RESPONSE)) {
						listener.onComplete(new FeedPublishResponseBean(
								retParams.getString(FeedPublishResponseBean.RESPONSE)));
					} else {
						listener.onComplete(null);
					}
				}
			}

			@Override
			public void onCancel(Bundle retParams) {
				if(listener != null) {
					String errorMsg = "operation cancelled.";
					RenrenError error = 
							new RenrenError(RenrenError.ERROR_CODE_OPERATION_CANCELLED, 
									errorMsg, errorMsg);
					listener.onRenrenError(error);
				}
			}
		}, "http://widget.renren.com/dialog/feed");
	}
}
