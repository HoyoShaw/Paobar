package com.hoyo.paobar.service.task;

import org.json.JSONObject;

/**
 * 业务层
 * @author Hoyo
 *
 */
public interface IService {

	public void doAction(IServiceCallback callback,JSONObject request);


}
