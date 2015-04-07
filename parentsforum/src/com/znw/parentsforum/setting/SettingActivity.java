package com.znw.parentsforum.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.znw.parentsforum.R;
import com.znw.parentsforum.util.NetWorkUtil;
import com.znw.parentsforum.util.VersionUtil;

public class SettingActivity extends Activity implements OnClickListener {
	private LinearLayout ll_version;
	private TextView tv_islast;
	private VersionUtil vu;
	private Handler handler = new Handler() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if ((boolean) msg.obj) {
					tv_islast.setText("有最新");
					tv_islast.setTextColor(R.color.azure);
					ll_version.setClickable(true);
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		NetWorkUtil nwu = new NetWorkUtil(SettingActivity.this);
		nwu.idOpenNetWorkSetting();
		init();
	}

	private void init() {
		ll_version = (LinearLayout) findViewById(R.id.ll_version);
		tv_islast = (TextView) findViewById(R.id.tv_islast);
		ll_version.setOnClickListener(this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		checkVersion();
	}

	private void checkVersion() {
		String path = "http://192.168.1.101:8080/login/version/version.xml";
		vu = new VersionUtil(SettingActivity.this, handler, path);
		vu.isUpdate();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_version:
			vu.update();
			break;

		default:
			break;
		}

	}
}
