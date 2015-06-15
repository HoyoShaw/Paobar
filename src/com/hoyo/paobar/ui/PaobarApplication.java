package com.hoyo.paobar.ui;

import android.app.Application;

import com.hoyo.paobar.util.ContextUtil;
import com.hoyo.paobar.volley.VolleyManager;

public class PaobarApplication extends Application{
	

	@Override
	public void onCreate() {
		super.onCreate();
		ContextUtil.init(this);
		
	}


	@Override
	public void onTerminate() {
		super.onTerminate();
		VolleyManager.destory();
	}
	

}
