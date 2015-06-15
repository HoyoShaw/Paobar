package com.hoyo.paobar.service.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hoyo.paobar.service.po.BarInfo;
import com.hoyo.paobar.service.po.UserInfo;
import com.hoyo.paobar.service.protocol.PaobarProtocol;
import com.hoyo.paobar.service.protocol.ProtocolResponse;
import com.hoyo.paobar.service.protocol.ProtocolResponse.ResponseVisitor;

public class PaobarResponseHandler<T> implements BaseResponseHandler<T>{

	private String serviceType;
	private List<T> responseList = new ArrayList<T>();

	public PaobarResponseHandler(String serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public List<T> handleResponse(JSONObject response) {

		ProtocolResponse protocolResponse = new ProtocolResponse(response);
		int result = protocolResponse.getResult();
		if ( result == PaobarProtocol.RESULT_OK) {
			protocolResponse.tranverseResponse(new ResponseVisitor() {

				@Override
				public void onVisit(JSONObject oneNode) {
					T content = dispathVisitByType(oneNode);
					responseList.add(content);
				}
			});
		}else {

		}
		return responseList;
	}

	@SuppressWarnings("unchecked")
	private T dispathVisitByType(JSONObject one) {
		if (serviceType.equals(PaobarProtocol.SERVICE_TYPE_GETBARLIST)) {
			return (T) json2BarInfo(one);
		}
		return null;
	}

	private BarInfo json2BarInfo(JSONObject one){
		BarInfo barInfo = new BarInfo();
		barInfo.setId(one.optInt(BarInfo.ID));
		barInfo.setName(one.optString(BarInfo.NAME));
		barInfo.setTpc(one.optString(BarInfo.TPC));
		barInfo.setPicurl(one.optString(BarInfo.PICURL));
		barInfo.setAvgcost(one.optString(BarInfo.AVGCOST));
		barInfo.setOpentime(one.optString(BarInfo.OPENTIME));
		barInfo.setTelnum(one.optString(BarInfo.TELNUM));
		barInfo.setAddress(one.optString(BarInfo.ADDRESS));
		barInfo.setStar(one.optInt(BarInfo.STAR));
		barInfo.setIcnt(one.optInt(BarInfo.ICNT));
		barInfo.setTag(one.optString(BarInfo.TAG));
		barInfo.setCity(one.optString(BarInfo.CITY));
		barInfo.setPcnt(one.optInt(BarInfo.PCNT));
		barInfo.setPicnum(one.optInt(BarInfo.PICNUM));
		barInfo.setLng(one.optString(BarInfo.LNG));
		barInfo.setLat(one.optString(BarInfo.LAT));
		barInfo.setDis(one.optLong(BarInfo.DIS));

		JSONArray userImg = one.optJSONArray(UserInfo.USERIMG);
		if (userImg != null) {
			int len = userImg.length();
			for(int i = 0; i < len; i++){
				UserInfo userInfo = new UserInfo();
				JSONObject oneUser = userImg.optJSONObject(i);
				userInfo.setHpic(oneUser.optString(UserInfo.NICK));
				userInfo.setNick(oneUser.optString(UserInfo.HPIC));
				barInfo.setUser(userInfo);
			}
		}

		return barInfo;
	}

}
