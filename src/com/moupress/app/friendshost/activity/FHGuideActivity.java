package com.moupress.app.friendshost.activity;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.util.Pref;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class FHGuideActivity extends Activity{

	private Activity zActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		zActivity = this;
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.fh_guide_layout);
		
		//this.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		Button stopBtn = (Button) this.findViewById(R.id.tabStartBtn);
		stopBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Pref.setMyBoolPref(zActivity.getApplicationContext(), Const.VIEW_GUIDE, true);
				zActivity.finish();
			}});
	}
	
}
