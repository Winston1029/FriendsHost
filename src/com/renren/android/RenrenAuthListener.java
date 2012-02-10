/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

import android.os.Bundle;


/**
 * çå?¬è®¤è¯?ï¼åææ?å¨ä½ã
 * 
 * @author æ?å(yong.li@opi-corp.com) 2011-2-25
 */
public interface RenrenAuthListener {

    /**
     * ç»å½åææ?å®æ?å?è°ç¨ã
     * 
     * @param values key:ææ?æ?å¡å¨è¿åçå?æ°å??ï¼value:æ¯å?æ°å¼ã
     */
    public void onComplete(Bundle values);

    /**
     * æ?å¡å¨è¿åéè¯¯
     * 
     * @param renrenAuthError
     */
    public void onRenrenAuthError(RenrenAuthError renrenAuthError);

    /**
     * ç¨æ·å?æ¶ç»å½ã
     */
    public void onCancelLogin();

    /**
     * ç¨æ·å?æ¶ææ?ã
     * 
     * @param values key:ææ?æ?å¡å¨è¿åçå?æ°å??ï¼value:æ¯å?æ°å¼ã
     */
    public void onCancelAuth(Bundle values);

}
