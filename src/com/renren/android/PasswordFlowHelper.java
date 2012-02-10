/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

import java.util.concurrent.Executor;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.moupress.app.friendshost.R;
import com.moupress.app.util.Renren.RenrenUtil;

/**
 * ���ڷ���������֤����
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 */
public class PasswordFlowHelper {

	private static final String PASSWORD_FLOW_URL = "https://graph.renren.com/oauth/token";

	/**
	 * ͬ��������ʹ���û����������½
	 * 
	 * @param param
	 *            ����Ĳ���
	 * @return ����{@link PasswordFlowResponseBean}����
	 * @throws RenrenException
	 * @throws Throwable
	 */
	public PasswordFlowResponseBean login(PasswordFlowRequestParam param)
			throws RenrenException, Throwable {
		String url = PASSWORD_FLOW_URL;
		String method = "POST";
		Bundle bundle = param.getParams();
		try {
			String response = Util.openUrl(url, method, bundle);
			if (response != null) {
				JSONObject obj = new JSONObject(response);
				if (obj != null) {
					if (!TextUtils.isEmpty(obj.optString("error"))) {
						String message = obj.optString("error_description");
						throw new RenrenException(RenrenError.ERROR_CODE_AUTH_FAILED, message, message);
					}
				}
				PasswordFlowResponseBean passwordFlow = new PasswordFlowResponseBean(response);
				return passwordFlow;
			} else {
				Util.logger("null response");
				throw new RenrenException(RenrenError.ERROR_CODE_UNKNOWN_ERROR, "null response", "null response");
			}
		} catch (RuntimeException e) {
			Util.logger("runtime exception" + e.getMessage());
			throw new Throwable(e);
		}
	}

	/**
	 * �첽������ʹ���û����������½
	 * 
	 * @param param
	 *            ����Ĳ���
	 * @param listener
	 *            ��½״̬�ļ�����
	 * @param renren
	 *            Renren����
	 */
	public void login(Executor pool, final PasswordFlowRequestParam param,
			final RenrenAuthListener listener, final RenrenUtil renren) {

		pool.execute(new Runnable() {

			@Override
			public void run() {

				try {
					PasswordFlowResponseBean passwordFlow = login(param);
					if (passwordFlow != null) {
						Bundle values = new Bundle();
						values.putString(PasswordFlowResponseBean.KEY_ACCESS_TOKEN,	passwordFlow.getAccessToken());
						values.putLong(PasswordFlowResponseBean.KEY_EXPIRES_IN,	passwordFlow.getExpire());
						values.putString(PasswordFlowResponseBean.KEY_REFRESH_TOKEN, passwordFlow.getRefreshToken());
						values.putString(PasswordFlowResponseBean.KEY_SCOPE, passwordFlow.getScope());
						renren.updateAccessToken(passwordFlow.getAccessToken());
						if (listener != null) {
							listener.onComplete(values);
						}
					} else {
						Util.logger("null response");
						throw new RenrenException(RenrenError.ERROR_CODE_UNKNOWN_ERROR, "null response", "null response");
					}
				} catch (RenrenException e) {
					Util.logger("renren exception " + e.getMessage());
					if (listener != null) {
						listener.onRenrenAuthError(new RenrenAuthError(String.valueOf(e.getErrorCode()), 
																		e.getOrgResponse(),
																		PASSWORD_FLOW_URL));
					}
					e.printStackTrace();
				} catch (Throwable e) {
					Util.logger("on renren fault " + e.getMessage());
					if (listener != null) {
						listener.onRenrenAuthError(new RenrenAuthError("on falut", e.getMessage(), PASSWORD_FLOW_URL));
					}
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * ���������ʹ���û����������½
	 * 
	 * @param activity
	 *            ��ǰ����ʾ��Activity
	 * @param param
	 *            ����Ĳ���
	 * @param listener
	 *            ��½״̬�ļ�����
	 * @param renren
	 *            Renren����
	 */
	public void login(final Activity activity, PasswordFlowRequestParam param,
			final RenrenAuthListener listener, final RenrenUtil renren) {

		LoginEntryDialog dialog = new LoginEntryDialog(activity, param,	listener, this, renren);
		dialog.show();

	}

	/**
	 * ��½�ĵ���
	 * 
	 * @author hecao (he.cao@renren-inc.com)
	 * 
	 */
	class LoginEntryDialog extends Dialog {

		private PasswordFlowRequestParam param;
		private RenrenAuthListener listener;
		private ProgressDialog dialog;
		private Handler handler;
		private RenrenUtil renren;

		public LoginEntryDialog(Context context) {
			super(context);
		}

		public LoginEntryDialog(Activity activity,
				PasswordFlowRequestParam param, RenrenAuthListener listener,
				PasswordFlowHelper helper, RenrenUtil renren) {
			super(activity);
			this.param = param;
			this.listener = listener;
			this.handler = new Handler();
			this.renren = renren;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			final LinearLayout loginEntryView = (LinearLayout) getLayoutInflater().inflate(R.layout.renren_sdk_login_entry, null);
			loginEntryView.setOnClickListener(null);
			loginEntryView.setOnTouchListener(null);
			loginEntryView.setOnLongClickListener(null);
			loginEntryView.setOnKeyListener(null);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
																		ViewGroup.LayoutParams.WRAP_CONTENT);
			addContentView(loginEntryView, params);
			initViews(loginEntryView);
		}

		private void initViews(LinearLayout loginEntryView) {
			final EditText userNameEditText = (EditText) loginEntryView.findViewById(R.id.renren_sdk_login_entry_username);
			final EditText passwordEditText = (EditText) loginEntryView.findViewById(R.id.renren_sdk_login_entry_password);
			Button button = (Button) loginEntryView.findViewById(R.id.renren_sdk_login_confirm_button);
			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String userName = userNameEditText.getText().toString();
					String password = passwordEditText.getText().toString();

					if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
						param.setUserName(userName);
						param.setPassword(password);
						login();
					}
				}
			});
			this.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					if (listener != null) {
						listener.onCancelLogin();
					}
				}
			});
		}

		private void login() {
			dialog = new ProgressDialog(getContext());
			dialog.setMessage("���ڵ�½");
			dialog.show();
			AsyncRenren asyncRenren = new AsyncRenren(renren);
			RenrenAuthListener authListener = new RenrenAuthListener() {
				
				@Override
				public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
					if (listener != null) {
						listener.onRenrenAuthError(renrenAuthError);
					}
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							dialog.dismiss();
						}
					});
				}
				
				@Override
				public void onComplete(Bundle values) {
					if (listener != null) {
						listener.onComplete(values);
					}
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							dialog.dismiss();
							LoginEntryDialog.this.dismiss();
						}
					});
				}
				
				@Override
				public void onCancelLogin() {
					if (listener != null) {
						listener.onCancelLogin();
					}
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							dialog.dismiss();
						}
					});
				}
				
				@Override
				public void onCancelAuth(Bundle values) {
					if (listener != null) {
						listener.onCancelAuth(values);
					}
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							dialog.dismiss();
						}
					});
				}
			};
			asyncRenren.authorize(param, authListener);
		}

	}

}
