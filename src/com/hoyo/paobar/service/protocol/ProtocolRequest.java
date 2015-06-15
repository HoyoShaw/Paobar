package com.hoyo.paobar.service.protocol;

import org.json.JSONObject;

import android.text.TextUtils;

public class ProtocolRequest extends PaobarProtocol implements IRequest{
	
	private static final String DEFAULT_SORT_TYPE = "2,0";//����������
	
	JSONObject root = new JSONObject();
	
	private String serviceType;
	
	public ProtocolRequest(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public String getServiceType(){
		return serviceType;
	}
	
	
	@Override
	public JSONObject buildRequest() {
		return null;
	}
	
	/**
	 * ��ȡ�ư��б�
	 * @param key
	 * @param city
	 * @param gps
	 * @param dis
	 * @param sortType
	 * @param psize
	 * @param pindex
	 */
	public JSONObject buildGetBarListRequest(
							String key,
							String city,
							String gps,
							String dis,
							String sortType,
							int psize,
							int pindex)
	{
		try {
			root.put(KEY_UDID, "");
			root.put(KEY_TYP, SERVICE_TYPE_GETBARLIST);
			root.put(KEY_KEY, key);
			root.put(BarList.KEY_CITY,city);
			root.put(BarList.KEY_GPS, gps);
			root.put(BarList.KEY_DIS, dis);
			root.put(BarList.KEY_SORTTYPE, TextUtils.isEmpty(sortType) ? DEFAULT_SORT_TYPE : sortType);
			root.put(BarList.KEY_PSIZE, psize);
			root.put(BarList.KEY_PINDEX, pindex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}

	
}
