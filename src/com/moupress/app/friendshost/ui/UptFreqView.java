package com.moupress.app.friendshost.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.util.FlurryUtil;
import com.moupress.app.friendshost.util.Pref;

public class UptFreqView extends DialogView {

	private Activity zActivity;
	private Intent intentBack;
	
	public static final String TAG = "UptFreqView";
	
	//Radio Buttons Index
	private int prevInd = -1;
	private int curInd = -1;
	private int tempInd = -1;
	
	private ArrayList<RadioBtn> radioBtns;
	private OnCheckedChangeListener onCheckedChangeListener;
	private OnClickListener selBtnOnClickListener;
	
	public UptFreqView(Activity zActivity, Intent intentBack) {
		 
		// TODO Auto-generated constructor stub
		this.zActivity = zActivity;
		this.intentBack = intentBack;
	}


	@Override
	public void LoadView(Bundle loadData) {
		// TODO Auto-generated method stub
		prevInd = tempInd = curInd = LoadRadioBtns(radioBtns,onCheckedChangeListener);
	}

	@Override
	protected void RefreshView() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void InitContent(Activity activity, ContentViewListener contentViewListener){
		
	    
		//this.titleId = R.string.upt_frequency_title;
		//this.setBtnMsgId = R.string.upt_frequency_btn_txt;
		
		this.radioBtns = new ArrayList<RadioBtn>();
		
		this.onCheckedChangeListener = new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton btn, boolean value) {
				
				if(value == true)
				{
					RadioBtn radioBtn =  (RadioBtn) btn.getTag();
					curInd = radioBtns.indexOf(radioBtn);
					
					if(tempInd != curInd && tempInd >= 0)
					{
						((RadioButton)zActivity.findViewById(radioBtns.get(tempInd).radioBtnId)).setChecked(false);
					}
					
					tempInd = curInd;
				}
			}
		};
		
		this.selBtnOnClickListener =  new OnClickListener()
		{


			@Override
			public void onClick(android.view.View view) {
				// TODO Auto-generated method stub
				 if(prevInd != curInd )
				 {
					 Pref.setMyIntPref((zActivity).getApplicationContext(), Const.SETTING_BASIC+"_UPT_FREQ", curInd);
					 FlurryUtil.logEvent(TAG+":selBtnOnClickListener", Const.SETTING_UPT_FREQ_BTN_TEXT[curInd]);  
				 }
				 
				 if(intentBack != null )
					 intentBack.putExtra(Const.SETTING_BASIC_GROUPS[0]+"_SET", curInd);
				 
				 (zActivity).setResult(android.app.Activity.RESULT_OK, intentBack);
				 (zActivity).finish();
			}
		};
		
		
	}
	
	
	private class RadioBtn
	{
	   private int radioBtnId;
	   private int radioBtnTxtId;
	   private String radioBtnTxt;
	   private boolean isChked = false;
	   private int uptDur;
	}
	
	
	
 private int LoadRadioBtns(ArrayList<RadioBtn> radioBtns, OnCheckedChangeListener onCheckedChangeListener) 
 	{
	    int chkInd = Pref.getMyIntPref(zActivity.getApplicationContext(), Const.SETTING_BASIC+"_UPT_FREQ");;
	    if(chkInd < 0) chkInd = 1;
	    
	 	for(int i=0 ; i < Const.SETTING_UPT_FREQ_BTN_TEXT.length; i++)
	 	{
	 		//Radio Button Structure
	 		RadioBtn radioBtn = new RadioBtn();
	 		radioBtn.radioBtnId = Const.SETTING_UPT_FREQ_BTN_ID[i];
	 		radioBtn.radioBtnTxtId = Const.SETTING_UPT_FREQ_BTN_TXT_ID[i];
	 		radioBtn.radioBtnTxt = Const.SETTING_UPT_FREQ_BTN_TEXT[i];
	 		radioBtn.uptDur = Const.SETTING_UPT_FREQ_DURATION[i];
	 		
	 		//TextView
	 		TextView radioTv = (TextView) zActivity.findViewById(radioBtn.radioBtnTxtId);
	 		radioTv.setText(radioBtn.radioBtnTxt);
	 		
	 		//RadioButton
	 		RadioButton rBtn = ((RadioButton)zActivity.findViewById(radioBtn.radioBtnId));
	 		rBtn.setTag(radioBtn);
	 		rBtn.setOnCheckedChangeListener(onCheckedChangeListener);
	 		
	 		if( chkInd == i && rBtn != null) 
	 		{
	 			//chkInd = i; 
	 			radioBtn.isChked = true;
	 			rBtn.setChecked(radioBtn.isChked);
	 		}
	 		
	 		radioBtns.add(radioBtn);
	 	}
	 	return chkInd;
	}

 
	@Override
	public int GetSetBtnTxId() {
		// TODO Auto-generated method stub
		return R.string.upt_frequency_btn_txt;
	}
	
	@Override
	public OnClickListener GetSetOnClickListener() {
		// TODO Auto-generated method stub
		return  this.selBtnOnClickListener;
	}
	
	@Override
	public int GetTitleId() {
		// TODO Auto-generated method stub
		return R.string.upt_frequency_title;
	}

}
