package com.znw.parentsforum.register;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.znw.parentsforum.R;
import com.znw.parentsforum.login.LoginActivity;
import com.znw.parentsforum.util.NetWorkUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "65c9c289aee8";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "d0a7a22287b310b80a21af2d943ac743";

	private ImageView iv_back_register;
	private Button btn_get_identifyingcode, btn_register;
	private EditText ed_regeister_phone, ed_identifyingcode, ed_regeister_pwd;
	private Context context = RegisterActivity.this;
	
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		// 判断网络是否可用，不可用则打开设置网络
		NetWorkUtil nwu = new NetWorkUtil(RegisterActivity.this);
		nwu.idOpenNetWorkSetting();
		init();
	}

	private void init() {
		iv_back_register=(ImageView) findViewById(R.id.iv_back_register);
		ed_regeister_phone = (EditText) findViewById(R.id.ed_regeister_phone);
		ed_identifyingcode = (EditText) findViewById(R.id.ed_identifyingcode);
		ed_regeister_pwd = (EditText) findViewById(R.id.ed_regeister_pwd);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_get_identifyingcode = (Button) findViewById(R.id.btn_get_identifyingcode);
		btn_get_identifyingcode.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		iv_back_register.setOnClickListener(this);

		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		EventHandler eh = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {

				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}

		};
		SMSSDK.registerEventHandler(eh);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back_register:
			finish();
			break;
		case R.id.btn_get_identifyingcode:
			if (!TextUtils.isEmpty(ed_regeister_phone.getText().toString())) {
				SMSSDK.getVerificationCode("86", ed_regeister_phone.getText()
						.toString());
				phone=ed_regeister_phone.getText().toString();
			} else {
				Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.btn_register:
			String phone = ed_regeister_phone.getText().toString();
			String pwd = ed_regeister_pwd.getText().toString();
			if (!TextUtils.isEmpty(ed_regeister_phone.getText().toString())) {
				SMSSDK.getVerificationCode("86", ed_regeister_phone.getText()
						.toString());
			} else {
				Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
			}
			if (!TextUtils.isEmpty(ed_regeister_pwd.getText().toString())) {
			} else {
				Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			}
			if(!TextUtils.isEmpty(ed_identifyingcode.getText().toString())){
				SMSSDK.submitVerificationCode("86", phone, ed_identifyingcode.getText().toString());
			}else {
				Toast.makeText(this, "验证码不能为空", 1).show();
			}
			if ("15612345678".equals(phone)) {
				Toast.makeText(this, "该手机号已注册！", Toast.LENGTH_SHORT).show();
				ed_regeister_phone.setText("");
				ed_identifyingcode.setText("");
			}
			if ("15712345678".equals(phone) && "123456".equals(pwd)) {
				Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, LoginActivity.class);
				startActivity(intent);
			}
			break;
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			if (result == SMSSDK.RESULT_COMPLETE) {
				// 短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					//Toast.makeText(getApplicationContext(), "提交验证码成功",
					//		Toast.LENGTH_SHORT).show();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					btn_get_identifyingcode.setText("验证码已发送");
					ed_identifyingcode.setText(data.toString());
				}
			} else {
				((Throwable) data).printStackTrace();
			}

		}

	};

	protected void onDestroy() {
		SMSSDK.unregisterAllEventHandler();
	};
}
