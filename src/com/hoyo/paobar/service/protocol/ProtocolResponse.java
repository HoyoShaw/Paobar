package com.hoyo.paobar.service.protocol;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * ������Ӧ
 * @author hoyo
 *
 */
public class ProtocolResponse extends PaobarProtocol{
	
	private JSONObject response;
	
	public ProtocolResponse(JSONObject response) {
		this.response = response;
	}
	
	public int getResult(){
		int result = RESULT_OK;
		if (response != null) {
			JSONObject resultJson = response.optJSONObject(KEY_RESULT);
			result = resultJson.optInt(KEY_RESULT);
		}
		return result;
	}
	
	public void tranverseResponse(ResponseVisitor visitor){
		if (response != null) {
			JSONArray data = response.optJSONArray(KEY_DATA);
			if (null != data) {
				int len = data.length();
				for(int i = 0; i < len; i++){
					JSONObject oneNode = data.optJSONObject(i);
					if (null != oneNode) {
						visitor.onVisit(oneNode);
					}
				}
			}
		}
	}
	
	public static interface ResponseVisitor{
		void onVisit(JSONObject oneNode);
	}

}
