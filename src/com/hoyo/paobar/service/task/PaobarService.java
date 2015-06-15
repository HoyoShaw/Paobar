package com.hoyo.paobar.service.task;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hoyo.paobar.service.protocol.PaobarProtocol;
import com.hoyo.paobar.volley.VolleyManager;

public class PaobarService implements IService{

	private static PaobarService sService;
	private static Context mContext;

	private PaobarService(){

	}
	public static PaobarService getInstance(Context context){
		mContext = context;
		if (sService == null) {
			synchronized (PaobarService.class) {
				if (sService == null) {
					sService = new PaobarService();
					VolleyManager.init(mContext);
				}
			}
		}
		return sService;
	}

	@Override
	public void doAction(final IServiceCallback callback,JSONObject request){

        if (callback != null && !callback.onStart(mContext)) {
            callback.onError(new VolleyError(VolleyError.ERROR_NETWORK));
        }

		RequestQueue queue = VolleyManager.getRequestQueue();
		JsonObjectRequest volleyRequest = new JsonObjectRequest(PaobarProtocol.BASE_URL, request, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				if (callback != null) {
					callback.onFinish(response);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (callback != null) {
					callback.onError(error);
				}
			}
		});

		queue.add(volleyRequest);
	}


    public static void destory() {
        if (sService != null) {
            sService = null;
        }
    }

}
