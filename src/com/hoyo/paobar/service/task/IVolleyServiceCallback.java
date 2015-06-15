package com.hoyo.paobar.service.task;

import org.json.JSONObject;

import com.android.volley.VolleyError;

public interface IVolleyServiceCallback {

	void handleFinish(JSONObject response);

	void handleError(VolleyError error);

}
