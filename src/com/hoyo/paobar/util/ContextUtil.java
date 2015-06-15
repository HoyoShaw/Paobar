package com.hoyo.paobar.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * 此类封装全局的context，
 * 在application中调用一次初始化，所有的地方都可以使用getContext
 *
 * @author hegao
 *
 */
public final class ContextUtil {
	private ContextUtil() {
	}

	private static Context app;

	public static synchronized void init(Application application) {
		if (app == null) {
			app = application;
		}
	}

	/**
	 * 此处返回全局的context，没有初始化就可能返回空
	 * @return
	 */
	public static synchronized Context getContext() {
    	if (app != null){
    		return app;
    	} else {
    		Log.d("Integration", "MUST invoke init(application) before this ContextUtil.getContext!!!");
    		assert( app != null );
	    }

		return app;
	}
}
