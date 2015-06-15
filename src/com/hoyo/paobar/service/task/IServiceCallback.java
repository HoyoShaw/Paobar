package com.hoyo.paobar.service.task;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;
import com.hoyo.paobar.util.Utils;


/**
 * 业务回调
 * @author lenovo
 *
 */
public abstract class IServiceCallback {


	/***/
	MessageHandler hanlder ;

	public IServiceCallback(MessageHandler hanlder){
		this.hanlder = hanlder ;
	}

	    /**
     * 事件开始前准备动作回调
     *
     * @param value
     */
    public boolean onStart(Context context) {
        return Utils.isDataConnected(context);
	}

	/**
	 * 正常事件回调
	 * @param value
	 */
	public abstract void onFinish(JSONObject response);

	/**
	 * 异常事件回调
	 * @param value
	 */
	public abstract void onError(VolleyError error);


}
