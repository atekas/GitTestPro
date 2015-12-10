package com.sensu.android.zimaogou.utils;

import android.content.Context;
import com.loopj.android.http.*;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.activity.login.RegisterActivity;
import com.sensu.android.zimaogou.encrypt.MD5Utils;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

	//获取到添加签名的url
	public static String getSignUrl(Map<String, String> hashMap) {
		StringBuilder stringBuilder = new StringBuilder();
		if (hashMap != null) {
			stringBuilder.append("?");
			Iterator iterator = hashMap.entrySet().iterator();
			List<String> strings = new ArrayList<String>();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				strings.add(key + "=" + value);
			}

			for (int i = 0; i < strings.size(); i++) {
				stringBuilder.append(strings.get(i));
				if (i < strings.size() - 1) {
					stringBuilder.append("&");
				}
			}
		}
		return stringBuilder.toString();
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

	public static void post(Context context, String url, JSONObject jsonObject, AsyncHttpResponseHandler responseHandler) {
		try {
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			client.post(context, getAbsoluteUrl(url), stringEntity, "application/json", responseHandler);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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

	public static void getWithSign(final String token,final String url, final RequestParams requestParams, final AsyncHttpResponseHandler asyncHttpResponseHandler){
		get(IConstants.sGetTimestamp, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				try {
					JSONObject jsonObject = new JSONObject(content);
					final String timestamp = jsonObject.optJSONObject("data").optString("timestamp");

					String sign = MD5Utils.md5(getAbsoluteUrl(url)+"||"+token+"||"+timestamp);
					requestParams.put("timestamp",timestamp);
					requestParams.put("sign", sign);

					get(IConstants.sLoginOut,requestParams,asyncHttpResponseHandler);



				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static void postWithSign(final Context context,final String uid, final JSONObject jsonObject, final AsyncHttpResponseHandler asyncHttpResponseHandler){
		get(IConstants.sGetTimestamp, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				try {
					JSONObject jsonObject = new JSONObject(content);
					final String timestamp = jsonObject.optJSONObject("data").optString("timestamp");

					String sign = MD5Utils.md5(uid+"||"+timestamp);
					jsonObject.put("uid",timestamp);
					jsonObject.put("timestamp",timestamp);
					jsonObject.put("sign", sign);

					post(context,IConstants.sLoginOut, jsonObject, asyncHttpResponseHandler);



				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
