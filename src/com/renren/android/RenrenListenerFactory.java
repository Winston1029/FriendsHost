/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

import com.moupress.app.util.Renren.RenrenUtil;

import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;



/**
 * ����RenrenDialogListener�Ĺ����������߲���ֱ��ʹ�ø��ࡣ
 * 
 * @author ����(yong.li@opi-corp.com) 2011-2-25
 */
public class RenrenListenerFactory {

    static RenrenDialogListener genFeedRenrenDialogListener(final RenrenUtil renren,
            final RenrenFeedListener listener) {
        return new AbstractRenrenDialogListener() {

            @Override
            public void onReceivedError(int errorCode, String description, String failingUrl) {
                listener.onError(new RenrenAuthError(String.valueOf(errorCode), description,
                        failingUrl));
            }

            @Override
            public int onPageBegin(String url) {
                if (url.startsWith(RenrenUtil.SUCCESS_URI)) {
                    Bundle values = Util.parseUrl(url);
                    String error = values.getString("error_reason");
                    if (error == null) {
                        listener.onComplete(values);
                    } else {
                        listener.onRenrenError(new RenrenError(error));
                    }
                    return RenrenDialogListener.ACTION_PROCCESSED;
                } else if (url.startsWith(RenrenUtil.CANCEL_URI)) {
                    Bundle values = Util.parseUrl(url);
                    String errMsg = values.getString("errMsg");
                    if (errMsg != null) {
                        listener.onRenrenError(new RenrenError(errMsg));
                    } else {
                        listener.onCancel();
                    }
                    return RenrenDialogListener.ACTION_PROCCESSED;
                }
                return RenrenDialogListener.ACTION_UNPROCCESS;
            }
        };
    }

    public static RenrenDialogListener genRenrenWidgetDialogListener(final RenrenUtil renren,
            final RenrenWidgetListener listener) {
        return new AbstractRenrenDialogListener() {

            @Override
            public void onReceivedError(int errorCode, String description, String failingUrl) {
                Bundle params = new Bundle();
                params.putInt("code", errorCode);
                params.putString("desc", description);
                params.putString("failUrl", failingUrl);
                listener.onError(params);
            }

            @Override
            public int onPageBegin(String url) {
                if (url.startsWith(RenrenUtil.WIDGET_CALLBACK_URI)) {
                    Bundle values = Util.parseUrl(url);
                    String error = values.getString("error");
                    if (error == null) {
                        listener.onComplete(values);
                    } else if ("access_denied".equals(error)) {
                        listener.onCancel(values);
                    } else {
                        listener.onError(values);
                    }
                    return RenrenDialogListener.ACTION_PROCCESSED;
                }
                return RenrenDialogListener.ACTION_DIALOG_PROCCESS;
            }
        };
    }

    /**
     * User-Agent Flow
     * 
     * @param renren
     * @param listener
     * @return
     */
    public static RenrenDialogListener genUserAgentFlowRenrenDialogListener(final RenrenUtil renren,
            final RenrenAuthListener listener, final String redirectUrl) {
        return new AbstractRenrenDialogListener() {

            @Override
            public void onReceivedError(int errorCode, String description, String failingUrl) {
                listener.onRenrenAuthError(new RenrenAuthError(String.valueOf(errorCode),
                        description, failingUrl));
            }

            @Override
            public int onPageBegin(String url) {
                int i = super.onPageBegin(url);
                if (i == RenrenDialogListener.ACTION_UNPROCCESS) {
                    if (url.startsWith("http://graph.renren.com/login_deny/")) {
                        return RenrenDialogListener.ACTION_DIALOG_PROCCESS;
                    } else {
                        i = this.checkUrl(url);
                    }
                }
                return i;
            }

            @Override
            public boolean onPageStart(String url) {
                boolean b = false;
                //����Ȩ��ɵ�ʱ�򲻵���onPageBegin������ԭ��δ֪����ʱ��װ��ҳ���ʱ����ȵ���onPageStart�ٵ�onPageBegin��ԭ��δ֪�ѵ���BUG��
                if (url.startsWith(redirectUrl)) {
                    Bundle values = Util.parseUrl(url);
                    String accessToken = values.getString("access_token");
                    if (accessToken != null) {
                        this.authComplete(values);
                        b = true;
                    }
                }
                return b;
            }

            private int checkUrl(String url) {
                if (url.startsWith(redirectUrl)) {
                    Bundle values = Util.parseUrl(url);
                    String error = values.getString("error");//OAuth���صĴ������
                    if (error != null) {
                        if ("access_denied".equalsIgnoreCase(error)) {
                            listener.onCancelAuth(values);
                        } else if ("login_denied".equalsIgnoreCase(error)) {
                            listener.onCancelLogin();
                        } else {
                            String desc = values.getString("error_description");
                            String errorUri = values.getString("error_uri");
                            listener.onRenrenAuthError(new RenrenAuthError(error, desc, errorUri));
                        }
                    } else {
                        this.authComplete(values);
                    }
                    return RenrenDialogListener.ACTION_PROCCESSED;
                }
                return RenrenDialogListener.ACTION_UNPROCCESS;
            }

            private void authComplete(Bundle values) {
                CookieSyncManager.getInstance().sync();
                String accessToken = values.getString("access_token");
                if (accessToken != null) {
                    Log.d(Util.LOG_TAG, "Success obtain access_token=" + accessToken);
                    try {
                        renren.updateAccessToken(accessToken);
                        listener.onComplete(values);
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onRenrenAuthError(new RenrenAuthError(e.getClass().getName(), e
                                .getMessage(), e.toString()));
                    }
                }
            }
        };
    }

    /**
     * Web Server Flow
     * 
     * @param renren
     * @param listener
     * @param redirectUrl
     * @return
     */
    public static RenrenDialogListener genWebServerFlowRenrenDialogListener(final RenrenUtil renren,
            final RenrenAuthListener listener, final String redirectUrl) {
        return new AbstractRenrenDialogListener() {

            @Override
            public void onReceivedError(int errorCode, String description, String failingUrl) {
                listener.onRenrenAuthError(new RenrenAuthError(String.valueOf(errorCode),
                        description, failingUrl));
            }

            @Override
            public int onPageBegin(String url) {
                int i = super.onPageBegin(url);
                if (url.startsWith("http://graph.renren.com/login_deny/")) {
                    return RenrenDialogListener.ACTION_DIALOG_PROCCESS;
                }
                if (url.startsWith(redirectUrl)) {
                    return RenrenDialogListener.ACTION_DIALOG_PROCCESS;
                }
                if (this.check(url)) {
                    return RenrenDialogListener.ACTION_PROCCESSED;
                }
                return i;
            }

            @Override
            public boolean onPageStart(String url) {
                return this.check(url);
            }

            private boolean check(String url) {
                if (url.startsWith(RenrenUtil.CANCEL_URI)) {
                    Bundle values = Util.parseUrl(url);
                    String action = values.getString("action");
                    if ("auth".equalsIgnoreCase(action)) {
                        listener.onCancelAuth(values);
                    } else {
                        listener.onCancelLogin();
                    }
                    return true;
                } else if (url.startsWith(RenrenUtil.SUCCESS_URI)) {
                    Bundle values = Util.parseUrl(url);
                    listener.onComplete(values);
                    return true;
                }
                return false;
            }

        };
    }
}

abstract class AbstractRenrenDialogListener implements RenrenDialogListener {

    @Override
    public int onPageBegin(String url) {
        if (url.contains("display")) {//�����д���display������ҳ�棻�ڸ�WebView����ʾ��
            return RenrenDialogListener.ACTION_DIALOG_PROCCESS;
        }
        return 0;
    }

    @Override
    public void onPageFinished(String url) {
    }

    @Override
    public boolean onPageStart(String url) {
        return false;
    }
}
