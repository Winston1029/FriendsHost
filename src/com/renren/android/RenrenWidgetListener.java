/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

import android.os.Bundle;

/**
 * widget相关�?作监�?�。
 * 
 * @author �?�勇(yong.li@opi-corp.com) 2011-4-7
 */
public interface RenrenWidgetListener {

    /**
     * 完�?时调用
     * 
     * @param retParams
     */
    public void onComplete(Bundle retParams);

    /**
     * 出现未知错误
     */
    public void onError(Bundle retParams);

    /**
     * 用户�?�消时调用。
     */
    public void onCancel(Bundle retParams);
}
