package com.sensu.android.zimaogou.utils;

import com.loopj.android.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class HttpUtil {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static final String HOST = "http://139.196.108.137:80/v1/";
	static {
		client.setTimeout(5000);//设置超时
		client.addHeader("Content-type", "application/json");
	}

	//获取完整的url
	private static String getAbsoluteUrl(String relativeUrl) {
		return HOST + relativeUrl;
	}

	/**
	 * 用一个完整URL获取一个String对象
	 * 
	 * @param url
	 * @param responseHandler
	 */
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		LogUtils.d("HttpUtil", "get url:" + getAbsoluteUrl(url));
		get(url, null, responseHandler);
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		LogUtils.d("HttpUtil", "get url:" + getAbsoluteUrl(url) + "||参数:"
				+ (params != null ? params.toString() : "params is null"));
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	/**
	 * 不带参数，获取json对象或者数组
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, JsonHttpResponseHandler res) {
		client.get(getAbsoluteUrl(urlString), res);

	}

	/**
	 * 带参数，获取json对象或者数组
	 * 
	 * @param urlString
	 * @param params
	 * @param res
	 */
	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) {

		client.get(getAbsoluteUrl(urlString), params, res);

	}

	/**
	 * 下载数据使用，会返回byte数据
	 * @param uString
	 * @param bHandler
	 */
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {

		client.get(getAbsoluteUrl(uString), bHandler);

	}

	public static AsyncHttpClient getClient() {

		return client;
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
//		params = addKey(url, params);
		LogUtils.d("HttpUtil", "post url:" + getAbsoluteUrl(url) + "||参数:"
				+ (params != null ? params.toString() : "params is null"));
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public static JSONArray getJsonArrayFromResponse(JSONObject response,
			String dataFieldStr) {
		JSONArray data = null;
		if (response != null) {
			try {
				JSONObject body = response.getJSONObject("data");
				if (body != null) {
					data = body.getJSONArray(dataFieldStr);
				}
			} catch (Exception e) {
			}
		}
		return data;
	}
}
