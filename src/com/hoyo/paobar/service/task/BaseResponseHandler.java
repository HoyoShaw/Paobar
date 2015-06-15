package com.hoyo.paobar.service.task;

import java.util.List;

import org.json.JSONObject;

/**
 * 解析响应
 * @author lenovo
 * @param <T>
 *
 */
public interface BaseResponseHandler<T> {

	List<T> handleResponse(JSONObject response);
}
