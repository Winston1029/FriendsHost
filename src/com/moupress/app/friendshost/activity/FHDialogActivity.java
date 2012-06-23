package com.moupress.app.friendshost.activity;

import java.util.ArrayList;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.ui.DialogView;
import com.moupress.app.friendshost.ui.FeedBackView;
import com.moupress.app.friendshost.ui.UptFreqView;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.util.Pref;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class FHDialogActivity extends Activity{
	
	private final String TAG = "FHDialogActivity";
	
	//Activity Params
	private String reqId;
	private Context ctx;
	private Intent intentBack;
	
	//Content ViewId
	private int viewId;
	
	//Theme
	private int themeId;
	
	//Header TextView
	private TextView hdrTv;
	
	//Footer Buttons
	private Button setBtn;
	private Button cancelBtn;
	
	private DialogView diaLogView;
	
	//Button Listnener;
	private OnClickListener cancelBtnListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0) {
			
			if(intentBack != null)
				((Activity)ctx).setResult(RESULT_CANCELED, intentBack);
			
			((Activity)ctx).finish();
		}
	};

	private ContentViewListener contentViewListener = new ContentViewListener(){};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoadParams();
		
		super.onCreate(savedInstanceState);
		
		LoadView();
		initControls();
	}
	
	private void LoadView()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(viewId);
	}

	private void LoadParams() {
		ctx = this;
		Intent intent = this.getIntent();
		viewId = intent.getIntExtra(Const.DIALOG_VIEW_ID, -1);
		reqId = intent.getStringExtra(Const.SETTING_REQ_KEY);
		themeId = intent.getIntExtra(Const.DIALOG_THEME_ID, -1);
		if(themeId < 0 ) 
		{
			//super.setTheme(android.R.style.Theme_Dialog);
			//setTheme(android.R.style.Theme_Translucent);
			setTheme(android.R.style.Theme);
		}
	}
	
	private void initControls() {
		
		initContent();
		InitHeader();
		InitFooter();
	}

	
	private void initContent()
	{
		intentBack = new Intent();
		intentBack.putExtra(Const.DIALOG_VIEW_ID, this.viewId);
		
		if(reqId.contains(Const.SETTING_BASIC_GROUPS[0]))
		{
			InitUpdateFrequencyUI();
		}
		else if(reqId.equals(Const.SETTING_FEEDBACKS_GROUPS[1]))
		{
			InitRateAppUI();
		}
		else if(reqId.equals(Const.SETTING_FEEDBACKS_GROUPS[0]))
		{
			InitFeedBackUI();
		}
		else if(reqId.equals(Const.SETTING_FEEDBACKS_GROUPS[2]))
		{
			InitHelpUI();
		}
	}

	private void InitHeader() {
		
		hdrTv = (TextView) this.findViewById(R.id.titleTv);
		
		if(this.diaLogView != null)
		{
			int titleId = this.diaLogView.GetTitleId();
		
			if(hdrTv != null && titleId > 0)
				hdrTv.setText(titleId);
		}
		
	}

	private void InitFooter() {
		
		setBtn = (Button) this.findViewById(R.id.setBtn);
		cancelBtn = (Button) this.findViewById(R.id.canBtn);
	    
		if(this.diaLogView != null)
		{
			OnClickListener setBtnListener = this.diaLogView.GetSetOnClickListener();
		
			if(setBtnListener != null && setBtn != null)
				setBtn.setOnClickListener(setBtnListener);
		}
		
		if(cancelBtnListener != null && cancelBtn != null)
		cancelBtn.setOnClickListener(cancelBtnListener);
	}
	
//===== START Update Frequency =================
	private void InitUpdateFrequencyUI()
	{
		this.diaLogView = new UptFreqView(this,intentBack);
		((UptFreqView)diaLogView).InitContent(this, contentViewListener);
		((UptFreqView)diaLogView).LoadView(null);
	}
	
//===== END Update Frequency ===================

//===== START Feedbacks =========================

	private void InitFeedBackUI() {
		
		//this.titleId = R.string.feedback_app_title;
		//this.setBtnMsgId = R.string.feedback_app_btn_txt;
		this.diaLogView = new FeedBackView(this, intentBack);
		((FeedBackView)diaLogView).InitContent(this, contentViewListener);
		((FeedBackView)diaLogView).LoadView(null);
		//Button sendBtn = (Button) this.findViewById(R.id.thirdbtn);
		
	}
	
//===== END Feedbacks ===========================
	
	
//===== START Help Screen =======================
	private void InitHelpUI()
	{
		Button sendBtn = (Button) this.findViewById(R.id.thirdbtn);
		sendBtn.setVisibility(View.INVISIBLE);
		
		Button closeBtn = (Button) this.findViewById(R.id.CancelBtn);
		closeBtn.setOnClickListener(this.cancelBtnListener);
	}
//===== END Help Screen =======================
	
//===== START Rate App ===========================
	private void InitRateAppUI() {
		//this.titleId = R.string.rate_app_title;
		//this.setBtnMsgId = R.string.rate_app_btn_txt;
	}
	
//======END Rate App===============================
	
}
