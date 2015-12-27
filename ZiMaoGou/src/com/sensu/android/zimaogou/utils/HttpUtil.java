package com.sensu.android.zimaogou.utils;

import com.loopj.android.http.*;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.encrypt.MD5Utils;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.security.NoSuchAlgorithmException;

public class HttpUtil {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static final String HOST = "http://139.196.108.137:80/v1/";

    static {
        client.setTimeout(30000);//设置超时
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

    //获取完整的url
    private static String getAbsoluteUrl(String relativeUrl) {
        return HOST + relativeUrl;
    }

    /**
     * 带参数获取json对象
     *
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
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
        if(urlString.indexOf("about_us") >= 0){
            client.get(urlString,res);
        }else {
            client.get(getAbsoluteUrl(urlString), res);
        }
    }

    /**
     * 下载数据使用，会返回byte数据
     *
     * @param uString
     * @param bHandler
     */
    public static void get(String uString, BinaryHttpResponseHandler bHandler) {

        client.get(getAbsoluteUrl(uString), bHandler);

    }

    public static void post(String url, RequestParams params,
                            JsonHttpResponseHandler responseHandler) {

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

    public static void getWithSign(final String token, final String url, final RequestParams requestParams, final JsonHttpResponseHandler asyncHttpResponseHandler) {
        get(IConstants.sGetTimestamp, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseString) {
                super.onSuccess(statusCode, headers, responseString);
                try {
                    final String timestamp = responseString.optJSONObject("data").optString("timestamp");

                    String sign = MD5Utils.md5(getAbsoluteUrl(url) + "||" + token + "||" + timestamp);
                    requestParams.put("timestamp", timestamp);
                    requestParams.put("sign", sign);

                    get(url, requestParams, asyncHttpResponseHandler);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void postWithSign(final String token, final String url, final RequestParams requestParams, final JsonHttpResponseHandler asyncHttpResponseHandler) {
        get(IConstants.sGetTimestamp, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseString) {
                super.onSuccess(statusCode, headers, responseString);
                try {
                    final String timestamp = responseString.optJSONObject("data").optString("timestamp");
                    String sign = MD5Utils.md5(getAbsoluteUrl(url) + "||" + token + "||" + timestamp);
                    requestParams.put("sign", sign);
                    requestParams.put("timestamp", timestamp);

                    LogUtils.i("info", requestParams.toString());

                    post(url, requestParams, asyncHttpResponseHandler);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void postImage(String uid,String token,String imageUrl,JsonHttpResponseHandler jsonHttpResponseHandler){
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",uid);
        try {

            String path = BitmapUtils.getThumbUploadPath(imageUrl, 480);

            requestParams.put("body",new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        postWithSign(token,IConstants.sImageUpload,requestParams,jsonHttpResponseHandler);
    }
}
