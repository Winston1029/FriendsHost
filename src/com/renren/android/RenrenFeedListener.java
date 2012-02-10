/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

import android.os.Bundle;


/**
 * 监�?��?�自定义新鲜事的状�?；以�?��?�能会被widget�?�代。
 * 
 * @author �?�勇(yong.li@opi-corp.com) 2010-7-15
 */
public interface RenrenFeedListener {

    /**
     * 新鲜事�?��?完�?时调用
     * 
     * @param retParams
     */
    public void onComplete(Bundle retParams);

    /**
     * �?务器请求返回错误内容时调用（如�?�数�?正确）
     */
    public void onRenrenError(RenrenError renrenError);

    /**
     * 出现未知错误
     */
    public void onError(Exception exception);

    /**
     * 用户�?�消时调用。
     */
    public void onCancel();
}
