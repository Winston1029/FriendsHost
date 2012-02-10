/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

import android.os.Bundle;


/**
 * ç›‘å?¬è®¤è¯?ï¼Œå’ŒæŽˆæ?ƒåŠ¨ä½œã€‚
 * 
 * @author æ?Žå‹‡(yong.li@opi-corp.com) 2011-2-25
 */
public interface RenrenAuthListener {

    /**
     * ç™»å½•å’ŒæŽˆæ?ƒå®Œæˆ?å?Žè°ƒç”¨ã€‚
     * 
     * @param values key:æŽˆæ?ƒæœ?åŠ¡å™¨è¿”å›žçš„å?‚æ•°å??ï¼Œvalue:æ˜¯å?‚æ•°å€¼ã€‚
     */
    public void onComplete(Bundle values);

    /**
     * æœ?åŠ¡å™¨è¿”å›žé”™è¯¯
     * 
     * @param renrenAuthError
     */
    public void onRenrenAuthError(RenrenAuthError renrenAuthError);

    /**
     * ç”¨æˆ·å?–æ¶ˆç™»å½•ã€‚
     */
    public void onCancelLogin();

    /**
     * ç”¨æˆ·å?–æ¶ˆæŽˆæ?ƒã€‚
     * 
     * @param values key:æŽˆæ?ƒæœ?åŠ¡å™¨è¿”å›žçš„å?‚æ•°å??ï¼Œvalue:æ˜¯å?‚æ•°å€¼ã€‚
     */
    public void onCancelAuth(Bundle values);

}
