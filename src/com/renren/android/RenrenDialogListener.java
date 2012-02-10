/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

/**
 * 监�?�RenrenDialog，开�?�者�?会直接使用该类。
 * 
 * @author �?�勇(yong.li@opi-corp.com) 2011-2-25
 */
public interface RenrenDialogListener {

    /**
     * 未处�?�
     */
    public final static int ACTION_UNPROCCESS = 0;

    /**
     * 已处�?�
     */
    public final static int ACTION_PROCCESSED = 1;

    /**
     * 由Dialog处�?�
     */
    public final static int ACTION_DIALOG_PROCCESS = 2;

    /**
     * 页�?�加载之�?调用。
     * 
     * @param url
     * @return 0:未处�?�，1:已�?处�?�，2:由Dialog处�?�
     */
    public int onPageBegin(String url);

    /**
     * 页�?�开始加载时调用。
     * 
     * @param url
     * @return
     */
    public boolean onPageStart(String url);

    /**
     * 页�?�加载结�?�调用。
     * 
     * @param url
     */
    public void onPageFinished(String url);

    /**
     * 出现错误调用。
     * 
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    public void onReceivedError(int errorCode, String description, String failingUrl);
}
