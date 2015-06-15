package com.hoyo.paobar.service.task;

import org.json.JSONObject;

import com.android.volley.VolleyError;


/**
 * 业务回调
 * @author lenovo
 *
 */
public abstract class ServiceCallback {


	/***/
	MessageHandler hanlder ;

	public ServiceCallback(MessageHandler hanlder){
		this.hanlder = hanlder ;
	}

	/**
	 * 异常事件回调
	 * @param value
	 */
	public void onStart(Object value){

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
