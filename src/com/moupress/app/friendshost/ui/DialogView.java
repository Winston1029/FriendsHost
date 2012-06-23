package com.moupress.app.friendshost.ui;

import android.view.View.OnClickListener;

public abstract class DialogView extends View{

	public abstract int GetTitleId();
	public abstract int GetSetBtnTxId();
	public abstract OnClickListener GetSetOnClickListener();
}
