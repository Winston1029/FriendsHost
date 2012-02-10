/*
 * Copyright 2011-2012 Renren Inc.
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.moupress.app.util.Renren.RenrenUtil;

/**
 *����֧����
 * 
 * @author yong.li@opi-corp.com
 * 
 */
public final class Util {

    public static final String LOG_TAG = "Renren-SDK";

    public static void logger(String message) {
        Log.i(LOG_TAG, message);
    }

    /**
     * ��Key-valueת������&�����ӵ�URL��ѯ������ʽ��
     * 
     * @param parameters
     * @return
     */
    public static String encodeUrl(Bundle parameters) {
        if (parameters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : parameters.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            
            if(key.toString().equals("page"))
            {
            	sb.append(key + "=" + URLEncoder.encode(parameters.getInt(key)+""));
            }else
            {
              sb.append(key + "=" + URLEncoder.encode(parameters.getString(key)));
            }
            //sb.append(key + "=" + parameters.getString(key));
        }
        return sb.toString();
    }

    /**
     * ����&�����ӵ�URL����ת����key-value��ʽ��
     * 
     * @param s
     * @return
     */
    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            params.putString("url", s);
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                if (v.length > 1) {
                    params.putString(v[0], URLDecoder.decode(v[1]));
                }
            }
        }
        return params;
    }

    /**
     * ����URL�еĲ�ѯ��ת����key-value
     * 
     * @param url
     * @return
     */
    public static Bundle parseUrl(String url) {
        url = url.replace("rrconnect", "http");
        url = url.replace("#", "?");
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }

    /**
     * ����http����
     * 
     * @param url
     * @param method GET �� POST
     * @param params
     * @return
     */
    public static String openUrl(String url, String method, Bundle params) {
        if (method.equals("GET")) {
            url = url + "?" + encodeUrl(params);
        }
        String response = "";
        try {
            Log.d(LOG_TAG, method + " URL: " + url);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent")
                    + " Renren_Android_SDK_v2.0");
            if (!method.equals("GET")) {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                //encodeUrl(params);
                conn.getOutputStream().write(encodeUrl(params).getBytes("UTF-8"));
            }

            InputStream is = null;
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                is = conn.getInputStream();
            } else {
            	is = conn.getErrorStream();
            }
            response = read(is);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return response;
    }

    private static HttpURLConnection openConn(String url, String method, Bundle params) {
        if (method.equals("GET")) {
            url = url + "?" + encodeUrl(params);
        }
        try {
            Log.d(LOG_TAG, method + " URL: " + url);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent")
                    + " Renren_Android_SDK_v2.0");
            if (!method.equals("GET")) {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.getOutputStream().write(encodeUrl(params).getBytes("UTF-8"));
            }
            return conn;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    public static byte[] getBytes(String url, Bundle params) {
        try {
            HttpURLConnection conn = openConn(url, "post", params);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            InputStream is = conn.getInputStream();
            for (int i = 0; (i = is.read(buf)) > 0;) {
                os.write(buf, 0, i);
            }
            is.close();
            os.close();
            return os.toByteArray();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String uploadFile(String reqUrl, Bundle parameters, String fileParamName,
            String filename, String contentType, byte[] data) {
        HttpURLConnection urlConn = null;
        try {
            urlConn = sendFormdata(reqUrl, parameters, fileParamName, filename, contentType, data);
            String responseContent = read(urlConn.getInputStream());
            return responseContent.trim();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
    }

    private static HttpURLConnection sendFormdata(String reqUrl, Bundle parameters,
            String fileParamName, String filename, String contentType, byte[] data) {
        HttpURLConnection urlConn = null;
        try {
            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(5000);// ����λ�����룩jdk
            urlConn.setReadTimeout(5000);// ����λ�����룩jdk 1.5�������,��������ʱ
            urlConn.setDoOutput(true);

            urlConn.setRequestProperty("connection", "keep-alive");

            String boundary = "-----------------------------114975832116442893661388290519"; // �ָ���
            urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            boundary = "--" + boundary;
            StringBuffer params = new StringBuffer();
            if (parameters != null) {
                for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
                    String name = iter.next();
                    String value = parameters.getString(name);
                    params.append(boundary + "\r\n");
                    params.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
                    // params.append(URLEncoder.encode(value, "UTF-8"));
                    params.append(value);
                    params.append("\r\n");
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append(boundary);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + fileParamName + "\"; filename=\""
                    + filename + "\"\r\n");
            sb.append("Content-Type: " + contentType + "\r\n\r\n");
            byte[] fileDiv = sb.toString().getBytes();
            byte[] endData = ("\r\n" + boundary + "--\r\n").getBytes();
            byte[] ps = params.toString().getBytes();

            OutputStream os = urlConn.getOutputStream();
            os.write(ps);
            os.write(fileDiv);
            os.write(data);
            os.write(endData);

            os.flush();
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return urlConn;
    }

    public static void clearCookies(Context context) {
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    /**
     * �����������صĴ���JSON����ת����RenrenError.
     * 
     * @param JSON��
     * @return
     */
    private static RenrenError parseJson(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            
        	int errorCode = json.getInt("error_code");
        	String errorMessage = json.getString("error_msg");
        	errorMessage = RenrenError.interpretErrorMessage(errorCode, errorMessage);
        	
            return new RenrenError(errorCode, errorMessage, jsonResponse);
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * �����������صĴ���XML����ת����RenrenError.
     * 
     * @param JSON��
     * @return
     */
    private static RenrenError parseXml(String xmlResponse) {
        XmlPullParser parser = Xml.newPullParser();
        RenrenError error = null;
        try {
            parser.setInput(new StringReader(xmlResponse));
            int evtCode = parser.getEventType();
            int errorCode = -1;
            String errorMsg = null;
            while (evtCode != XmlPullParser.END_DOCUMENT) {
                switch (evtCode) {
                    case XmlPullParser.START_TAG:
                        if ("error_code".equals(parser.getName())) {
                            errorCode = Integer.parseInt(parser.nextText());
                        }
                        if ("error_msg".equals(parser.getName())) {
                            errorMsg = parser.nextText();
                        }
                        break;
                }
                if (errorCode > -1 && errorMsg != null) {
                    error = new RenrenError(errorCode, errorMsg, xmlResponse);
                    break;
                }
                evtCode = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return error;
    }

    /**
     * ��Ӧ�������Ǵ�����Ϣʱ����ת����RenrenError�����򷵻�NULL
     * 
     * @param response
     * @param responseFormat
     * @return
     */
    public static RenrenError parseRenrenError(String response, String responseFormat) {
        if (response.indexOf("error_code") < 0) return null;
        if (RenrenUtil.RESPONSE_FORMAT_JSON.equalsIgnoreCase(responseFormat)) return parseJson(response);
        return parseXml(response);
    }
    
    /**
     * 
     * ���ӿڷ��ص����ݣ��Ƿ�������Ϣ��������׳�{@link RenrenException}
     * 
     * @param response
     * @param responseFormat
     * @throws RenrenException 
     */
    public static void checkResponse (String response, String responseFormat) throws RenrenException {
        if (response != null) {
            if (response.indexOf("error_code") < 0) {
                return;
            }
            RenrenError error = null;
            if (RenrenUtil.RESPONSE_FORMAT_JSON.equalsIgnoreCase(responseFormat)) {
                error = parseJson(response);
            } else {
                error = parseXml(response);
            }
            if (error != null) {
                throw new RenrenException(error);
            }
        }
    }

    /**
     * ��ʾһ���򵥵ĶԻ���ֻ����UI�߳��е��á�
     * 
     * @param context
     * @param title
     * @param text
     */
    public static void showAlert(Context context, String title, String text, boolean showOk) {
        AlertDialog alertDialog = new Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        if (showOk) {
            OnClickListener listener = null;
            alertDialog.setButton2("ȷ��", listener);
        }
        alertDialog.show();
    }

    /**
     * ��ʾһ���򵥵ĶԻ���ֻ����UI�߳��е��á�
     * 
     * @param context
     * @param title
     * @param text
     */
    public static void showAlert(Context context, String title, String text) {
        showAlert(context, title, text, true);
    }

    public static String md5(String string) {
        if (string == null || string.trim().length() < 1) {
            return null;
        }
        try {
            return getMD5(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String getMD5(byte[] source) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            StringBuffer result = new StringBuffer();
            for (byte b : md5.digest(source)) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

	/**
	 * ��鵱ǰ��������״̬
	 * @param
	 * 		���ô˷�����Context
	 * @return 
	 * 		true - �п��õ��������ӣ�3G/GSM��wifi�ȣ�
	 * 		false - û�п��õ��������ӣ������contextΪnull
	 */
	public static boolean isNetworkConnected(Context context) {
		if(context == null){
			return false;
		}
		ConnectivityManager connManager = 
				(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobileState = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		State wifiState = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if(mobileState == State.DISCONNECTED && wifiState == State.DISCONNECTED) {
			return false;
		}
		return true;
	}
	
	/**
	 * ��ʾ��ѯ��ȷ�ϡ��Լ���ȡ��ĶԻ���
	 * 
	 * @param activity ��ʾ�˶Ի����Activity����
	 * @param title	�Ի���ı���
	 * @param text	�Ի�����ʾ������
	 * @param listener �û�ѡ��ļ�����
	 */
	public static void showOptionWindow(Activity activity, String title, 
			String text, OnOptionListener listener) {
		AlertDialog dialog = new AlertDialog.Builder(activity).create();
		if(title != null) {
			dialog.setTitle(title);
		}
		
		if(text != null) {
			dialog.setMessage(text);
		}
		
		final OnOptionListener oListener = listener;
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, 
				"Submit",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						oListener.onOK();
					}
				});
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, 
				"Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						oListener.onCancel();
					}
				});
		dialog.show();
	}
	
	/**
	 * ��Ԫѡ��ļ�����
	 * 
	 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
	 *
	 */
	public static interface OnOptionListener {
		/**
		 * ��ȷ��ѡ�����Ӧ
		 */
		public void onOK();
		
		/**
		 * ��ȡ��ѡ�����Ӧ
		 */
		public void onCancel();
	}
	
	/**
	 * �����࣬��ȡ�ļ����������
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] fileToByteArray(File file) {
		try {
			return streamToByteArray(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			logger(e.getMessage());
			return null;
		}
	}

	/**
	 * ���ߣ���������ת�����ֽ�����
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] streamToByteArray(InputStream inputStream) {
		byte[] content = null;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis = new BufferedInputStream(inputStream);

		try {
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = bis.read(buffer)) != -1) {
				baos.write(buffer, 0, length);
			}
			
			content = baos.toByteArray();
			if (content.length == 0) {
				content = null;
			}
			
			baos.close();
			bis.close();
		} catch (IOException e) {
			logger(e.getMessage());
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					logger(e.getMessage());
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					logger(e.getMessage());
				}
			}
		}

		return content;
	}
}