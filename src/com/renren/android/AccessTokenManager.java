/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 用�?�存�?�AccessToken和SesionKey；开�?�者�?会直接使用该类。
 * 
 * @author �?�勇(yong.li@opi-corp.com) 2011-2-24
 */
public class AccessTokenManager implements Parcelable {

	private static final String SESSION_KEY_URL = "http://graph.renren.com/renren_api/session_key";

	private static final String RENREN_SDK_CONFIG = "renren_sdk_config";

	private static final String RENREN_SDK_CONFIG_PROP_ACCESS_TOKEN = "renren_sdk_config_prop_access_token";
	
	private static final String RENREN_SDK_CONFIG_PROP_SESSION_KEY = "renren_sdk_config_prop_session_key";
	
	private static final String RENREN_SDK_CONFIG_PROP_SESSION_SECRET = "renren_sdk_config_prop_session_secret";

	private static final String RENREN_SDK_CONFIG_PROP_CREATE_TIME = "renren_sdk_config_prop_create_time";
	
	private static final String RENREN_SDK_CONFIG_PROP_SESSION_CREATE_TIME = "renren_sdk_config_prop_session_create_time";
	
	private static final String RENREN_SDK_CONFIG_PROP_EXPIRE_SECONDS = "renren_sdk_config_prop_expire_secends";
	
	private static final String RENREN_SDK_CONFIG_PROP_USER_ID = "renren_sdk_config_prop_user_id";

	private static final String KEY_ACCESS_TOKEN = "renren_token_manager_access_token";

	private static final String KEY_SESSION_KEY = "renren_token_manager_session_key";

	private static final String KEY_SESSION_SECRET = "renren_token_manager_session_secret";

	private static final String KEY_UID = "renren_token_manager_user_id";
	
	private static final String KEY_SESSION_KEY_EXPIRE_TIME = "renren_token_manager_session_key_expire_time";

	private static final long ONE_HOUR = 1000 * 60 * 60;

	private Context context;

	private String accessToken = null;

	private String sessionKey;

	private String sessionSecret = null;
	
	/**
	 * SessionKey 过期时间
	 */
	private long expireTime = 0;
	
	/**
	 * �?存当�?登录用户的uid
	 */
	private long uid;

	public AccessTokenManager(Context context) {
		this.context = context;
		compareWithConfig();
	}

	public AccessTokenManager(Parcel in) {
		Bundle bundle = Bundle.CREATOR.createFromParcel(in);
		if (bundle != null) {
			accessToken = bundle.getString(KEY_ACCESS_TOKEN);
			sessionKey = bundle.getString(KEY_SESSION_KEY);
			sessionSecret = bundle.getString(KEY_SESSION_SECRET);
			expireTime = bundle.getLong(KEY_SESSION_KEY_EXPIRE_TIME);
			uid = bundle.getLong(KEY_UID);
		}
		compareWithConfig();
	}

	public String getAccessToken() {
		this.accessToken = this.restoreAccessToken();
		return accessToken;
	}

	public String getSessionKey() {
		if(sessionKey == null || sessionSecret == null) {
			initSessionKey();
		}
		
		if(System.currentTimeMillis() > expireTime) {
			clearSessionParams();
		}
		
		return this.sessionKey;
	}

	public String getSessionSecret() {
		if(sessionKey == null || sessionSecret == null) {
			initSessionKey();
		}
		
		if(System.currentTimeMillis() > expireTime) {
			clearSessionParams();
		}
		return this.sessionSecret;
	}

	public long getUid() {
		return this.uid;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	/**
	 * 返回当�?session key是�?�有效
	 * @return 
	 * 		true - session key有效
	 * 		false - session key无效
	 */
	public boolean isSessionKeyValid() {
		if(sessionKey != null && sessionSecret != null 
				&& System.currentTimeMillis() < expireTime) {
			return true;
		}
		//若当�?sessionKey以�?�sessionSecret�?�为null，则
		//�?试从SharedPreference中对sessionkey进行�?始化
		initSessionKey();
		if(sessionKey != null && sessionSecret != null 
				&& System.currentTimeMillis() < expireTime) {
			return true;
		}
		return false;
	}

	/**
	 * 更新accessToken；如果�?��?�sessionKey出现错误抛出RenrenException。
	 * 
	 * @param accessToken
	 */
	public void updateAccessToken(String accessToken) {
		this.updateAccessToken(accessToken, true);
	}

	public void restoreSessionKey() {
		this.accessToken = this.restoreAccessToken();
		
		//�?试从SharedPreference中�?始化Session key以�?�Session Secret
		if(accessToken != null) {
			initSessionKey();
		}
		try {
			initSessionKey();
			//若没有session Key或者Session key已�?过期
			if(sessionKey == null || sessionSecret == null || expireTime < System.currentTimeMillis()) {
				this.exchangeSessionKey(accessToken);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.clearPersistSession();
		}
	}
	
	/**
	 * 检查当�?的session信�?�与�?置文件是�?�一直，若�?一致，则对当�?的sessionkey等进行�?始化
	 * @return
	 */
	private void compareWithConfig() {
		if(context == null) {
			return;
		}
		// 对�?置的�?�化进行监�?�，使得有多个Renren对象时，�?�以对�?��?信�?��?�?一致
		final SharedPreferences sp = context.getSharedPreferences(
				RENREN_SDK_CONFIG, Context.MODE_PRIVATE);
		sp.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				String sKey = sp.getString(RENREN_SDK_CONFIG_PROP_SESSION_KEY,
						null);
				String secret = sp.getString(
						RENREN_SDK_CONFIG_PROP_SESSION_SECRET, null);
				long userId = sp.getLong(RENREN_SDK_CONFIG_PROP_USER_ID, 0);
				if ((sessionKey != null && !sessionKey.equals(sKey))
						|| (sessionSecret != null && !sessionKey.equals(secret)
						|| (sessionKey == null && sKey != null) 
						|| (sessionSecret == null && secret != null))
						|| (uid != userId)) {
					initSessionKey();
				}
			}
		});
	}

	public void clearPersistSession() {
		Editor editor = context.getSharedPreferences(RENREN_SDK_CONFIG,
				Context.MODE_PRIVATE).edit();
		editor.remove(RENREN_SDK_CONFIG_PROP_ACCESS_TOKEN);
		editor.remove(RENREN_SDK_CONFIG_PROP_CREATE_TIME);
		editor.remove(RENREN_SDK_CONFIG_PROP_SESSION_KEY);
		editor.remove(RENREN_SDK_CONFIG_PROP_SESSION_SECRET);
		editor.remove(RENREN_SDK_CONFIG_PROP_EXPIRE_SECONDS);
		editor.remove(RENREN_SDK_CONFIG_PROP_USER_ID);
		editor.commit();
		this.accessToken = null;
		this.sessionKey = null;
		this.sessionSecret = null;
		this.expireTime = 0;
		this.uid = 0;
	}

	/**
	 * 从SharedPreference中读入SessionKey
	 */
	private synchronized void initSessionKey() {
		SharedPreferences sp = context.getSharedPreferences(RENREN_SDK_CONFIG,
				Context.MODE_PRIVATE);
		sessionKey = sp.getString(RENREN_SDK_CONFIG_PROP_SESSION_KEY, null);
		sessionSecret = sp.getString(RENREN_SDK_CONFIG_PROP_SESSION_SECRET, null);
		uid = sp.getLong(RENREN_SDK_CONFIG_PROP_USER_ID, 0);
		long expires = sp.getLong(RENREN_SDK_CONFIG_PROP_EXPIRE_SECONDS, 0); 
		long createTime = sp.getLong(RENREN_SDK_CONFIG_PROP_SESSION_CREATE_TIME, 0);
		long current = System.currentTimeMillis();
		expireTime = createTime + expires;
		
		//sessioin key已过期，
		if(expireTime < current) {
			clearSessionParams();
			sessionKey = null;
			sessionSecret = null;
			expireTime = 0;
			uid = 0;
		}
	}
	
	private void updateAccessToken(String accessToken, boolean isPersist) {
		if (accessToken == null || accessToken.length() < 1) {
			return;
		}
		this.accessToken = accessToken;
		
		try {
			initSessionKey();
			//若没有session Key或者Session key已�?过期
			if(sessionKey == null || sessionSecret == null || expireTime < System.currentTimeMillis()) {
				this.exchangeSessionKey(accessToken);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.clearPersistSession();
		}

		if (isPersist) {
			this.storeAccessToken(accessToken);
		} else {
			this.clearPersistSession();
		}
	}

	private void exchangeSessionKey(String accessToken) {
		if (accessToken == null || accessToken.length() < 1) {
			return;
		}
		Bundle params = new Bundle();
		params.putString("oauth_token", accessToken);
		String sk = Util.openUrl(SESSION_KEY_URL, "POST", params);
		try {
			JSONObject obj = new JSONObject(sk);
			String error = obj.optString("error", null);
			if (error != null) {
				throw new RenrenError(obj.toString());
			}
			sessionKey = obj.getJSONObject("renren_token").getString(
					"session_key");
			sessionSecret = obj.getJSONObject("renren_token").getString(
					"session_secret");
			// 获�?�当�?登录用户的uid
			uid = obj.getJSONObject("user").getLong("id");
			//�?务器返回的过期时间�?��?为秒，故乘以1000
			long expires = obj.getJSONObject("renren_token").getLong(
					"expires_in") * 1000;
			long current = System.currentTimeMillis();
			expireTime = current + expires;
			// 存储当�?获得的sessionKey， sessionSecret
			storeSessionParams(sessionKey, sessionSecret, current, expires, uid);
			
			Log.i(Util.LOG_TAG, "---login success sessionKey:" + sessionKey
					+ " expires:" + expires + " sessionSecret:" + sessionSecret
					+ " uid:" + uid);
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 存储accessToken，
	 * @param accessToken
	 */
	private void storeAccessToken(String accessToken) {
		Editor editor = context.getSharedPreferences(RENREN_SDK_CONFIG,
				Context.MODE_PRIVATE).edit();
		if (accessToken != null) {
			editor.putString(RENREN_SDK_CONFIG_PROP_ACCESS_TOKEN, accessToken);
			editor.putLong(RENREN_SDK_CONFIG_PROP_CREATE_TIME,
					System.currentTimeMillis());
		} else {
			this.clearPersistSession();
		}
		editor.commit();
	}
	
	/**
	 * 在SharedPreference中存储accessToken，sessionKey, sessionSecret以�?�过期时间
	 * @param accessToken 
	 * @param sessionKey 
	 * @param sessionSecret
	 * @param expireTime Session key 的过期时间，�?��?为秒
	 * @param uid
	 */
	private void storeSessionParams(String sessionKey, String sessionSecret, long sessionCreateTime,
			long expires, long uid) {
		if (sessionKey == null || sessionSecret == null) {
			this.clearPersistSession();
			return;
		}

		Editor editor = context.getSharedPreferences(RENREN_SDK_CONFIG,
				Context.MODE_PRIVATE).edit();
		
		editor.putString(RENREN_SDK_CONFIG_PROP_SESSION_KEY, sessionKey);
		editor.putString(RENREN_SDK_CONFIG_PROP_SESSION_SECRET, sessionSecret);
		editor.putLong(RENREN_SDK_CONFIG_PROP_EXPIRE_SECONDS, expires);
		editor.putLong(RENREN_SDK_CONFIG_PROP_SESSION_CREATE_TIME, sessionCreateTime);
		editor.putLong(RENREN_SDK_CONFIG_PROP_USER_ID, uid);
		editor.commit();
	}

	/**
	 * 删除SharedPreference中存储的SessionKey信�?�
	 */
	private void clearSessionParams() {
		Editor editor = context.getSharedPreferences(RENREN_SDK_CONFIG,
				Context.MODE_PRIVATE).edit();
		editor.remove(RENREN_SDK_CONFIG_PROP_SESSION_KEY);
		editor.remove(RENREN_SDK_CONFIG_PROP_SESSION_SECRET);
		editor.remove(RENREN_SDK_CONFIG_PROP_EXPIRE_SECONDS);
		editor.remove(RENREN_SDK_CONFIG_PROP_SESSION_CREATE_TIME);
		editor.remove(RENREN_SDK_CONFIG_PROP_USER_ID);
		sessionKey = null;
		sessionSecret = null;
		expireTime = 0;
		uid = 0;
		editor.commit();
	}
	
	private String restoreAccessToken() {
		SharedPreferences sp = context.getSharedPreferences(RENREN_SDK_CONFIG,
				Context.MODE_PRIVATE);
		String accessToken = sp.getString(RENREN_SDK_CONFIG_PROP_ACCESS_TOKEN,
				null);
		if (accessToken == null) {
			return null;
		}
		long createTime = sp.getLong(RENREN_SDK_CONFIG_PROP_CREATE_TIME, 0);
		long life = Long.parseLong(accessToken.split("\\.")[2]) * 1000;
		long currenct = System.currentTimeMillis();
		if ((createTime + life) < (currenct - ONE_HOUR)) {
			this.clearPersistSession();
			return null;
		}
		return accessToken;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		 Bundle bundle = new Bundle();
		 if (accessToken != null) {
			 bundle.putString(KEY_ACCESS_TOKEN, accessToken);
		 }
		 if (sessionKey != null) {
			 bundle.putString(KEY_SESSION_KEY, sessionKey);
		 }
		 if (sessionSecret != null) {
			 bundle.putString(KEY_SESSION_SECRET, sessionSecret);
		 }
		 if (expireTime != 0) {
			 bundle.putLong(KEY_SESSION_KEY_EXPIRE_TIME, expireTime);
		 }
		 if (uid != 0) {
			 bundle.putLong(KEY_UID, uid);
		 }
		 bundle.writeToParcel(dest, flags);
	}

	public static final Parcelable.Creator<AccessTokenManager> CREATOR = new Parcelable.Creator<AccessTokenManager>() {
		public AccessTokenManager createFromParcel(Parcel in) {
			return new AccessTokenManager(in);
		}

		public AccessTokenManager[] newArray(int size) {
			return new AccessTokenManager[size];
		}
	};
}
