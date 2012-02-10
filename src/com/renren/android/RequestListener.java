/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;


/**
 * 对人人 API请求结果监�?�。
 * 
 * �?�?在该接�?�的方法中更新UI，一般�?�说这些代�?�?是在UI线程（主线程）中�?行的。
 * 
 * @author �?�勇(yong.li@opi-corp.com) 2010-7-15
 */
public interface RequestListener {

    /**
     * 当请求完�?�?�调用
     * 
     * @param response �?务器返回的结果，一般是JSON或XML串
     *        (根�?�你调用的AsyncRenren.requestJSON还是AsyncRenren .requestXML)。
     */
    public void onComplete(String response);

    /**
     * �?务器返回了错误结果，已�?正确的链接上了�?务器但有错误如：缺少�?�数�?sessionKey过期等。
     * 
     * @param renrenError
     */
    public void onRenrenError(RenrenError renrenError);

    /**
     * 在请求期间�?�生了严�?问题（如：网络故障�?访问的地�?��?存在等）
     * 
     * @param fault
     */
    public void onFault(Throwable fault);

}
