/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

/**
 * ç›‘å?¬RenrenDialogï¼Œå¼€å?‘è€…ä¸?ä¼šç›´æŽ¥ä½¿ç”¨è¯¥ç±»ã€‚
 * 
 * @author æ?Žå‹‡(yong.li@opi-corp.com) 2011-2-25
 */
public interface RenrenDialogListener {

    /**
     * æœªå¤„ç?†
     */
    public final static int ACTION_UNPROCCESS = 0;

    /**
     * å·²å¤„ç?†
     */
    public final static int ACTION_PROCCESSED = 1;

    /**
     * ç”±Dialogå¤„ç?†
     */
    public final static int ACTION_DIALOG_PROCCESS = 2;

    /**
     * é¡µé?¢åŠ è½½ä¹‹å‰?è°ƒç”¨ã€‚
     * 
     * @param url
     * @return 0:æœªå¤„ç?†ï¼Œ1:å·²ç»?å¤„ç?†ï¼Œ2:ç”±Dialogå¤„ç?†
     */
    public int onPageBegin(String url);

    /**
     * é¡µé?¢å¼€å§‹åŠ è½½æ—¶è°ƒç”¨ã€‚
     * 
     * @param url
     * @return
     */
    public boolean onPageStart(String url);

    /**
     * é¡µé?¢åŠ è½½ç»“æ?Ÿè°ƒç”¨ã€‚
     * 
     * @param url
     */
    public void onPageFinished(String url);

    /**
     * å‡ºçŽ°é”™è¯¯è°ƒç”¨ã€‚
     * 
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    public void onReceivedError(int errorCode, String description, String failingUrl);
}
